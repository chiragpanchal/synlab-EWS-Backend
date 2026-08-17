# OCI Logging — Implementation Notes (EWS Backend)

**Status:** Not implemented. Design + runbook only.
**Blocked on:** IAM access in the Synlab PROD tenancy (see [What to ask the customer](#1-what-to-ask-the-customer)).
**Owner:** Chirag Panchal
**Last updated:** 2026-08-07

---

## Goal

Stop the "log into OCI console → open the CLI → download logs → grep locally" loop. Make PROD logs
searchable from a laptop terminal or browser, and make errors page us instead of waiting to be found.

## Decision log

Picked **OCI Logging** (native service) over the alternatives. Recorded so this isn't relitigated:

| Option | Verdict |
|---|---|
| **OCI Logging + Unified Monitoring Agent** | **Chosen.** Already in OCI, no new vendor, no new PO, PROD data stays in-tenancy. Lowest setup cost. |
| Grafana Loki self-hosted | Nicer query UX, but we'd be operating it. Revisit only if OCI Logging Search proves genuinely blocking. |
| OCI OpenSearch | Overkill for our volume; not cheap. |
| SaaS (Datadog / Better Stack / Grafana Cloud) | Best UX by far, but ships PROD logs containing patient-adjacent staff/roster data to a third party. Needs a data-residency + DPA conversation with Synlab first. Not worth blocking on. |
| OCI Logging Analytics | More capable (dashboards, parsers, long retention), more setup and cost. Possible **phase 2** once basic Logging is proven. |

## Target architecture

```
  EWS Spring Boot app  (OCI Compute, PROD)
        │  writes structured JSON, rotating
        ▼
  /var/log/ews/ews.json
        │  tailed by
        ▼
  Unified Monitoring Agent  (Oracle Cloud Agent plugin, already on most OCI images)
        │  pushes via Agent Configuration
        ▼
  OCI Logging — Custom Log  ("ews-prod-app")  inside Log Group "ews-prod"
        │
        ├──▶ Logging Search        → console UI + `oci logging-search` CLI  ← the main win
        ├──▶ Service Connector Hub → Notifications (email/Slack) on ERROR   ← alerting
        └──▶ Service Connector Hub → Object Storage                          ← cheap long-term archive
```

---

## 1. What to ask the customer

This is the current blocker. Concretely, we need Synlab's tenancy admin to do **one** of:

**Option A (preferred) — they grant us a group with the permissions, we do the setup.**

Ask for a group (e.g. `ews-devops`) containing our users, with these policies in the PROD compartment:

```
Allow group ews-devops to manage log-groups           in compartment <ews-prod-compartment>
Allow group ews-devops to manage log-content          in compartment <ews-prod-compartment>
Allow group ews-devops to manage unified-configurations in compartment <ews-prod-compartment>
Allow group ews-devops to read   instance-agent-plugins in compartment <ews-prod-compartment>
```

Plus the agent's own permission — the instance must be able to push log content. This needs a
**dynamic group** covering the PROD instance:

```
# Dynamic group: ews-prod-instances
ANY { instance.compartment.id = '<ews-prod-compartment-ocid>' }

# Policy
Allow dynamic-group ews-prod-instances to use log-content in compartment <ews-prod-compartment>
```

**Option B (minimum viable) — they do the setup, we only get read access.**

If they won't grant write, this still solves the main pain. We need:

```
Allow group ews-developers to read log-groups  in compartment <ews-prod-compartment>
Allow group ews-developers to read log-content in compartment <ews-prod-compartment>
```

That alone gives us Logging Search from the console and the CLI — no more downloading. Worth taking
Option B if Option A stalls; **do not let the write-access negotiation block read access.**

> Verify resource-type names (`unified-configurations` in particular) against current OCI docs when
> we actually run this — Oracle has renamed logging resource types before.

**Also confirm with them:**
- Which compartment the PROD instance lives in, and its OCID.
- Log retention required — OCI Logging supports 30–180 days. Anything longer needs the Object
  Storage archive step (§7). Ask whether Synlab has a retention *mandate* (likely, given the domain).
- Whether an existing Log Group / observability standard already exists in the tenancy that we
  should slot into rather than inventing our own.
- Who should receive error alerts, and on what channel.

---

## 2. Phase 1 — App-side structured logging

Do this first. It's independent of IAM and can be built and tested locally while access is pending.

We're on **Spring Boot 3.5.3 / Java 21**, which has structured logging built in — **no extra
dependency**, no `logback-spring.xml` needed.

### 2a. Split the config by profile

Current state: a single `src/main/resources/application.properties`, no profiles. Don't turn on JSON
logging locally — it's miserable to read. Create `application-prod.properties` and put the logging
config there only.

```properties
# src/main/resources/application-prod.properties

# Structured JSON on disk; console stays plain for anyone tailing over SSH
logging.structured.format.file=ecs
logging.structured.ecs.service.name=ews
logging.structured.ecs.service.version=@project.version@
logging.structured.ecs.service.environment=prod

logging.file.name=/var/log/ews/ews.json

# Rotation — the agent tails the live file; keep the box from filling up
logging.logback.rollingpolicy.file-name-pattern=/var/log/ews/ews.json.%d{yyyy-MM-dd}.%i.gz
logging.logback.rollingpolicy.max-file-size=100MB
logging.logback.rollingpolicy.max-history=7
logging.logback.rollingpolicy.total-size-cap=2GB
```

Launch PROD with `--spring.profiles.active=prod` (or `SPRING_PROFILES_ACTIVE=prod`).

Format options are `ecs`, `gelf`, `logstash`. **ECS** (Elastic Common Schema) is the right default —
well-documented field names, and it keeps the door open if we ever move to OpenSearch/Loki.

### 2b. Add a correlation ID — this is the part that makes search pay off

Without it, we can search but can't reconstruct a single request. A servlet `Filter` putting values
into SLF4J's **MDC** gets them onto *every* log line automatically, no changes to existing
`logger.info` calls.

Sketch (not implemented — for reference when we build it):

```java
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestCorrelationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String requestId = Optional.ofNullable(req.getHeader("X-Request-Id"))
                                   .orElseGet(() -> UUID.randomUUID().toString());
        MDC.put("requestId", requestId);
        MDC.put("path", req.getRequestURI());
        // userId from SecurityContext once authenticated — see RosterController.getCurrentUserId()
        res.setHeader("X-Request-Id", requestId);
        try {
            chain.doFilter(req, res);
        } finally {
            MDC.clear();   // MUST clear — Tomcat reuses threads, else IDs leak between requests
        }
    }
}
```

With ECS format, MDC entries surface as fields on every line. One failed roster save then becomes a
single query instead of eyeballing timestamps.

**Good news:** the existing logging is already consistent — every endpoint follows the same
`ENTRY` / `EXIT` / `EXCEPTION` shape with `LocalDateTime.now()`. That consistency is the hard part and
it's already done. Note that `logging.structured.*` will emit its own timestamp, so the hand-rolled
`Time: {}` in the message becomes redundant — harmless, but worth cleaning up eventually.

### 2c. ⚠️ Trim what we log before shipping it

**Do not skip this.** Nearly every endpoint logs the full request body (`Request: {}`). Today those
strings die on the box. Once shipped, roster/person payloads — names, employee numbers, schedules —
sit in a retained store for months. For an EU healthcare-adjacent business that is a **GDPR question
to answer before turning on ingestion, not after.**

Recommendation:
- **INFO** — IDs, counts, durations. (`personId=1234, days=21`)
- **DEBUG** — full request bodies, off by default in PROD.

This also happens to be the main driver of ingestion cost, which is billed per GB. Two problems, one fix.

---

## 3. Phase 2 — OCI Logging setup

Once IAM lands. Console path, roughly:

1. **Enable the agent plugin.** Instance → *Oracle Cloud Agent* tab → enable **Custom Logs Monitoring**.
   Usually pre-installed on Oracle Linux images; just needs toggling on. Allow a few minutes.
2. **Create a Log Group.** Observability & Management → Logging → *Log Groups* → e.g. `ews-prod`.
3. **Create a Custom Log.** Logging → *Logs* → Create custom log → name `ews-prod-app`, in group
   `ews-prod`. Set **retention** here (30–180 days).
4. **Agent configuration.** In the same create-flow, choose *Create new configuration*:
   - **Group:** the dynamic group `ews-prod-instances` (or select the instance directly).
   - **Input type:** Log path.
   - **Path:** `/var/log/ews/ews.json`
   - **Parser:** **JSON** ← easy to miss, and without it every line ingests as one opaque string,
     which defeats the whole point.
5. **Wait.** First records typically appear within a few minutes. Not instant.

### File permissions gotcha

The unified agent does **not** run as the app user. It needs:
- **read** on `/var/log/ews/ews.json`
- **execute (`+x`)** on `/var/log` and `/var/log/ews`

Silent-failure #1 is the agent being unable to traverse into the log directory. If nothing arrives,
check this before anything else, then the agent's own logs:

```bash
sudo tail -f /var/log/unified-monitoring-agent/unified-monitoring-agent.log
```

### Rotation gotcha

Point the agent at the **live** file (`ews.json`), not the rotated pattern. Confirm it keeps
following across a rotation — if lines go missing right at a rollover, that's the cause.

---

## 4. Phase 3 — Verify

```bash
# 1. App is producing valid JSON
sudo tail -n 2 /var/log/ews/ews.json | jq .

# 2. Force a known-unique line, then search for it in the console
logger -f /var/log/ews/ews.json "CANARY-$(date +%s)"    # or hit an endpoint
```

Then in the console: Logging → Search → scope to `ews-prod-app` → find the canary.

**Inspect the first ingested record's actual shape before writing any queries.** Custom-log payloads
land nested under `data`, but the exact leaf paths depend on the ECS output and the parser. Expand
one record in the console, note the real field paths, and write queries against *those* — don't
assume the examples below match verbatim.

---

## 5. Phase 4 — Searching (the actual payoff)

**Console:** Logging → Search. Pick log group, time range, filter.

**CLI** (this is the one that replaces the download loop):

```bash
oci logging-search search-logs \
  --search-query "search \"<compartment-ocid>/<log-group-ocid>/<log-ocid>\"
                  | where data.log.level = 'ERROR'
                  | sort by datetime desc" \
  --time-start 2026-08-07T00:00:00Z \
  --time-end   2026-08-07T23:59:59Z \
  --limit 200
```

Trace one request end-to-end once the MDC filter is in:

```bash
--search-query "search \"...\" | where data.requestId = '<uuid>'"
```

Worth wrapping in a small `logs.sh` helper in this repo once the OCIDs are known, so nobody has to
remember the query syntax.

---

## 6. Phase 5 — Alerting

The real upgrade: stop *going to look*.

**Service Connector Hub** — Source: Logging (`ews-prod-app`) → optional log filter on ERROR →
Target: **Notifications** (ONS topic → email, or Slack via webhook subscription).

Start deliberately noisy-but-narrow: alert only on `ERROR`. Tune once we see the real volume.
An alert channel everyone mutes is worse than none.

For rate-based alarms ("more than N errors in 5 min") rather than per-event, route Logging →
Connector Hub → **Monitoring** as a custom metric, then build an OCI Monitoring alarm on it.

---

## 7. Optional — Long-term archive

If Synlab's retention requirement exceeds 180 days: Service Connector Hub → Source: Logging →
Target: **Object Storage**, with a lifecycle policy to Archive tier. Cheap. Not searchable without
rehydrating, but satisfies "we kept it."

---

## 8. Rollback

Low risk, fully reversible:
- **App side:** remove the `logging.*` lines from `application-prod.properties` (or drop the `prod`
  profile) → back to default console logging.
- **OCI side:** disable or delete the Agent Configuration → ingestion stops. Existing logs age out
  per retention.

Nothing here touches application behaviour, DB access, or request handling.

---

## 9. Open questions

- [ ] Is PROD a plain Compute VM, or OKE? *(Notes above assume Compute. If OKE: same destination,
      but use a fluent-bit DaemonSet instead of the Unified Monitoring Agent.)*
- [ ] How is the app currently started in PROD — systemd unit, `nohup`, something else? Determines
      where stdout goes today and how we set `SPRING_PROFILES_ACTIVE=prod`.
- [ ] Current PROD log volume per day? Drives cost and retention choices. Billed per GB ingested —
      confirm current OCI pricing at implementation time, don't assume.
- [ ] Does Synlab already have a tenancy-wide observability standard we should conform to?
- [ ] Retention mandate for logs containing staff/roster data?

## 10. Related

- Unrelated to logging but noticed while scoping: `src/main/resources/application.properties` has the
  DB password, Oracle HCM password, and JWT signing secret in plaintext, committed to git. Worth
  moving to env vars or OCI Vault — the env-var scaffolding is already there, commented out at lines
  8–11. Separate piece of work; flagged so it isn't lost.

---

> **Note on this file's location.** It lives at the repo root to match the existing
> `AUTHENTICATION_README.md` convention. If we later want it loadable as an actual Claude Code skill,
> it moves to `.claude/skills/oci-logging/SKILL.md` and needs YAML frontmatter (`name`, `description`).
