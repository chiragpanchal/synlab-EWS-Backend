package com.ewsv3.ews.team.service;

import com.ewsv3.ews.team.dto.*;
import com.ewsv3.ews.virtual.ExecutorConfig;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.ewsv3.ews.team.service.TeamUtils.*;
import static com.ewsv3.ews.team.service.TeamUtilsPaginationSql.*;


@Service
public class TeamService {

    //    private final ExecutorService executorService;
    private final ExecutorService executorService;
    private final ExecutorConfig executorConfig;

    public TeamService(ExecutorService executorService, ExecutorConfig executorConfig) {
        this.executorService = executorService;
        this.executorConfig = executorConfig;
    }

    public List<TeamMembers> getTeamTimecards(long userId, Long profileId, LocalDate startDate, LocalDate endDate, Integer pageNo, Integer pageSize, JdbcClient jdbcClient) {

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userId", userId);
        objectMap.put("profileId", profileId);
        objectMap.put("startDate", startDate);
        objectMap.put("endDate", endDate);
        objectMap.put("pageNo", pageNo);
        objectMap.put("pageSize", pageSize);

        List<TeamMembers> members = jdbcClient.sql(TeamSql).params(objectMap).query(TeamMembers.class).list();

        for (TeamMembers member : members) {
            Map<String, Object> personDateMap = new HashMap<>();

            personDateMap.put("person_id", member.getPersonId());
            personDateMap.put("start_date", startDate);
            personDateMap.put("end_date", endDate);

            List<TimecardLine> timecardLines = jdbcClient.sql(MemberTimecardLineSql).params(personDateMap).query(TimecardLine.class).list();

            for (TimecardLine line : timecardLines) {

                //System.out.println("getTeamTimecards line:" + line);

                Map<String, Object> tlObject = new HashMap<>();
                tlObject.put("person_id", member.getPersonId());
                tlObject.put("person_roster_id", line.getPersonRosterId());
                tlObject.put("effective_date", line.getEffectiveDate());

                List<TimecardActuals> actuals = null;
                try {
                    actuals = jdbcClient.sql(MemberTimecardActualsSql).params(tlObject).query(TimecardActuals.class).list();
                } catch (Exception e) {
//                    throw new RuntimeException(e);
                }

                line.setTimecardActuals(actuals);

            }
            member.setTimecardLines(timecardLines);
        }
        return members;
    }

    public List<TeamTimecardSimple> getTeamTimecardsSimpleV2(long userId, Long profileId, LocalDate startDate, LocalDate endDate, int page, int size, String text, String filterFlag, JdbcClient jdbcClient) {

        Map<String, Object> objectMap = Map.of(
                "userId", userId,
                "profileId", profileId,
                "startDate", startDate,
                "endDate", endDate,
                "offset", page,
                "pageSize", size,
                "text", "%" + text + "%",
                "pFilterFlag", filterFlag
        );

        //System.out.println("getTeamTimecardsSimpleV2 objectMap:" + objectMap);

        List<TeamTimecardSimple> timecardSimples = jdbcClient.sql(TramSimpleSql).params(objectMap).query(TeamTimecardSimple.class).list();

        for (TeamTimecardSimple timecardSimple : timecardSimples) {

            Map<String, Object> personDateMap = Map.of(
                    "personId", timecardSimple.getPersonId(),
                    "startDate", startDate,
                    "endDate", endDate,
                    "pFilterFlag", filterFlag
            );

            List<TeamTimecardSimpleChild> children = jdbcClient.sql(TeamSimpleChildSql).params(personDateMap).query(TeamTimecardSimpleChild.class).list();


// Ensure at least one record per date between startDate and endDate
            Set<LocalDate> existingDates = children.stream()
                    .map(TeamTimecardSimpleChild::effectiveDate)
                    .collect(Collectors.toSet());

            List<TeamTimecardSimpleChild> filledChildren = new ArrayList<>(children);
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                if (!existingDates.contains(date)) {
                    TeamTimecardSimpleChild emptyChild = new TeamTimecardSimpleChild(
                            date,
                            null,
                            0.0,
                            0.0,
                            0L,
                            0L
                    );
                    emptyChild.setEffectiveDate(date);
                    filledChildren.add(emptyChild);
                }
            }

            filledChildren.sort(Comparator.comparing(TeamTimecardSimpleChild::effectiveDate));

//            System.out.println("getTeamTimecardsSimpleV2 filledChildren:" + filledChildren);
            //System.out.println("============================================================");
            //System.out.println("getTeamTimecardsSimpleV2 timecardSimple.getEmployeeNumber():" + timecardSimple.getEmployeeNumber());
//            for (TeamTimecardSimpleChild child : filledChildren) {
//                System.out.println("getTeamTimecardsSimpleV2 child.child.effectiveDate():" + child.effectiveDate());
//                System.out.println("getTeamTimecardsSimpleV2 child.child.actHrs():" + child.actHrs());
//            }
            timecardSimple.setChildren(filledChildren);

//            System.out.println("getTeamTimecardsSimpleV2 children:" + children);
//            for (TeamTimecardSimpleChild child : children) {
//                System.out.println("getTeamTimecardsSimpleV2 child.child.actHrs():" + child.actHrs());
//            }
//            timecardSimple.setChildren(children);
        }

        return timecardSimples;
    }

    public List<TeamTimecardSimple> getTeamTimecardsSimple(long userId, Long profileId, LocalDate startDate, LocalDate endDate, JdbcClient jdbcClient) {

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userId", userId);
        objectMap.put("profileId", profileId);
        objectMap.put("startDate", startDate);
        objectMap.put("endDate", endDate);
        objectMap.put("offset", 0);
        objectMap.put("pageSize", 100);


        //System.out.println("getTeamTimecardsSimple objectMap:" + objectMap);

        List<TeamTimecardSimple> timecardSimples = jdbcClient.sql(TramSimpleSql).params(objectMap).query(TeamTimecardSimple.class).list();

        return timecardSimples;

    }


    public CompletableFuture<List<TeamMembers>> getTeamTimecards2(long userId, Long profileId, LocalDate startDate, LocalDate endDate, JdbcClient jdbcClient) {

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userId", userId);
        objectMap.put("profileId", profileId);
        objectMap.put("startDate", startDate);
        objectMap.put("endDate", endDate);

        CompletableFuture<List<TeamMembers>> teamMembersFuture = CompletableFuture.supplyAsync(() ->
                jdbcClient.sql(TeamAllSql).params(objectMap).query(TeamMembers.class).list(), executorConfig.executorService());

//        if (true){
//            return teamMembersFuture;
//        }


        CompletableFuture<List<TimecardLine>> timecardLineFuture = CompletableFuture.supplyAsync(() ->
                jdbcClient.sql(MemberAllTimecardLineSql).params(objectMap).query(TimecardLine.class).list(), executorConfig.executorService());

        CompletableFuture<List<TimecardActuals>> timecardActualsFuture = CompletableFuture.supplyAsync(() ->
                jdbcClient.sql(MemberAllTimecardActualsSql).params(objectMap).query(TimecardActuals.class).list(), executorConfig.executorService());

        // Use thenCombine to aggregate results from futures
        return teamMembersFuture.thenCombine(timecardLineFuture, (members, timecardLines) -> {
            return Map.entry(members, timecardLines);
        }).thenCombine(timecardActualsFuture, (membersAndLines, actuals) -> {
            List<TeamMembers> members = membersAndLines.getKey();
            List<TimecardLine> timecardLines = membersAndLines.getValue();

            // Map timecard actuals by personRosterId and by personId+effectiveDate for quick access
            Map<Long, List<TimecardActuals>> actualsByRosterId = actuals.stream()
                    .filter(actual -> actual.personRosterId() != null)
                    .collect(Collectors.groupingBy(TimecardActuals::personRosterId));

            Map<String, List<TimecardActuals>> actualsByPersonIdAndDate = actuals.stream()
                    .filter(actual -> actual.personRosterId() == null)
                    .collect(Collectors.groupingBy(actual -> actual.personId() + "|" + actual.effectiveDate().toString()));

            // Map timecard lines by personId for quick access
            Map<Long, List<TimecardLine>> linesByPersonId = timecardLines.stream()
                    .peek(line -> {
                        if (line.getPersonRosterId() != null) {
                            line.setTimecardActuals(actualsByRosterId.getOrDefault(line.getPersonRosterId(), new ArrayList<>()));
                        } else {
                            String key = line.getPersonId() + "|" + line.getEffectiveDate().toString();
                            line.setTimecardActuals(actualsByPersonIdAndDate.getOrDefault(key, new ArrayList<>()));
                        }
                    })
                    .collect(Collectors.groupingBy(TimecardLine::getPersonId));

            // Set timecard lines to members
            for (TeamMembers member : members) {
                member.setTimecardLines(linesByPersonId.getOrDefault(member.getPersonId(), new ArrayList<>()));
            }

            return members;
        }).thenApply(members -> {
            // This will execute after all futures are complete
            //System.out.println("getTeamTimecards2 members:" + members);
            return members;
        }).exceptionally(ex -> {
            ex.printStackTrace();
            // Handle exceptions here
            return Collections.emptyList();
        });
    }

    public CompletableFuture<List<TeamMembers>> getTeamTimecards5(
            long userId,
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            JdbcClient jdbcClient
    ) {
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userId", userId);
        objectMap.put("profileId", profileId);
        objectMap.put("startDate", startDate);
        objectMap.put("endDate", endDate);

        CompletableFuture<List<TeamMembers>> teamMembersFuture = CompletableFuture.supplyAsync(() ->
                jdbcClient.sql(TeamAllSql).params(objectMap).query(TeamMembers.class).list(), executorConfig.executorService());

        CompletableFuture<List<TimecardLine>> timecardLineFuture = CompletableFuture.supplyAsync(() ->
                jdbcClient.sql(MemberAllTimecardLineSql).params(objectMap).query(TimecardLine.class).list(), executorConfig.executorService());

        CompletableFuture<List<TimecardActuals>> timecardActualsFuture = CompletableFuture.supplyAsync(() ->
                jdbcClient.sql(MemberAllTimecardActualsSql).params(objectMap).query(TimecardActuals.class).list(), executorConfig.executorService());

        return teamMembersFuture.thenCombine(timecardLineFuture, (members, timecardLines) -> {
            return Map.entry(members, timecardLines);
        }).thenCombine(timecardActualsFuture, (membersAndLines, actuals) -> {
            List<TeamMembers> members = membersAndLines.getKey();
            List<TimecardLine> timecardLines = membersAndLines.getValue();

            // Map timecard actuals by personRosterId and by personId+effectiveDate for quick access
            Map<Long, List<TimecardActuals>> actualsByRosterId = actuals.parallelStream()
                    .filter(actual -> actual.personRosterId() != null)
                    .collect(Collectors.groupingBy(TimecardActuals::personRosterId));

            Map<String, List<TimecardActuals>> actualsByPersonIdAndDate = actuals.parallelStream()
                    .filter(actual -> actual.personRosterId() == null)
                    .collect(Collectors.groupingBy(actual -> actual.personId() + "|" + actual.effectiveDate().toString()));

            // Map timecard lines by personId for quick access
            Map<Long, List<TimecardLine>> linesByPersonId = timecardLines.parallelStream()
                    .peek(line -> {
                        if (line.getPersonRosterId() != null) {
                            line.setTimecardActuals(actualsByRosterId.getOrDefault(line.getPersonRosterId(), new ArrayList<>()));
                        } else {
                            String key = line.getPersonId() + "|" + line.getEffectiveDate().toString();
                            line.setTimecardActuals(actualsByPersonIdAndDate.getOrDefault(key, new ArrayList<>()));
                        }
                    })
                    .collect(Collectors.groupingBy(TimecardLine::getPersonId));

            // Set timecard lines to members
            for (TeamMembers member : members) {
                member.setTimecardLines(linesByPersonId.getOrDefault(member.getPersonId(), new ArrayList<>()));
            }

            return members;
        }).thenApply(members -> {
            // This will execute after all futures are complete
//            System.out.println("getTeamTimecards2 members:" + members);
            return members;
        }).exceptionally(ex -> {
            ex.printStackTrace();
            // Handle exceptions here
            return Collections.emptyList();
        });
    }


    public CompletableFuture<List<TeamMembers>> getTeamTimecards4(long userId, Long profileId, LocalDate startDate, LocalDate endDate, JdbcClient jdbcClient) {

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userId", userId);
        objectMap.put("profileId", profileId);
        objectMap.put("startDate", startDate);
        objectMap.put("endDate", endDate);

//        List<TeamMembers> members = jdbcClient.sql(TeamSql).params(objectMap).query(TeamMembers.class).list();
//        List<TimecardLine> timecardLines = jdbcClient.sql(MemberAllTimecardLineSql).params(objectMap).query(TimecardLine.class).list();
//        List<TimecardActuals> actuals = jdbcClient.sql(MemberAllTimecardActualsSql).params(objectMap).query(TimecardActuals.class).list();

//        using virtual thread

        CompletableFuture<List<TeamMembers>> teamMembersFuture = CompletableFuture.supplyAsync(() ->
                jdbcClient.sql(TeamSql).params(objectMap).query(TeamMembers.class).list(), executorService
        );

        CompletableFuture<List<TimecardLine>> timecardLineFuture = CompletableFuture.supplyAsync(() ->
                jdbcClient.sql(MemberAllTimecardLineSql).params(objectMap).query(TimecardLine.class).list(), executorService);


        CompletableFuture<List<TimecardActuals>> timecardActualsFuture = CompletableFuture.supplyAsync(() ->
                jdbcClient.sql(MemberAllTimecardActualsSql).params(objectMap).query(TimecardActuals.class).list(), executorService);

        CompletableFuture<Void> future = CompletableFuture.allOf(teamMembersFuture, timecardLineFuture, timecardActualsFuture);

        AtomicReference<List<TeamMembers>> finalMembers = new AtomicReference<>(new ArrayList<>());

//        CompletableFuture<List<TeamMembers>> finalMembers = new AtomicReference<>(new CompletableFuture<>());

        future.thenRun(() -> {
            try {
                List<TeamMembers> members = teamMembersFuture.get();
                List<TimecardLine> timecardLines = timecardLineFuture.get();
                List<TimecardActuals> actuals = timecardActualsFuture.get();

                //System.out.println("getTeamTimecards2 timecardLines:" + timecardLines);
                //System.out.println("getTeamTimecards2 actuals:" + actuals);

// Map timecard actuals by personRosterId and by personId+effectiveDate for quick access
                Map<Long, List<TimecardActuals>> actualsByRosterId = actuals.stream()
                        .filter(actual -> actual.personRosterId() != null)
                        .collect(Collectors.groupingBy(TimecardActuals::personRosterId));

                //System.out.println("getTeamTimecards2 actualsByRosterId:" + actualsByRosterId);

                Map<String, List<TimecardActuals>> actualsByPersonIdAndDate = actuals.stream()
                        .filter(actual -> actual.personRosterId() == null)
                        .collect(Collectors.groupingBy(actual -> actual.personId() + "|" + actual.effectiveDate().toString()));

                //System.out.println("getTeamTimecards2 actualsByPersonIdAndDate:" + actualsByPersonIdAndDate);

                // Map timecard lines by personId for quick access
                Map<Long, List<TimecardLine>> linesByPersonId = timecardLines.stream()
                        .peek(line -> {
                            //System.out.println("getTeamTimecards2 line.getPersonId():" + line.getPersonId());
                            //System.out.println("getTeamTimecards2 line.getPersonRosterId():" + line.getPersonRosterId());
                            if (line.getPersonRosterId() != null) {
                                line.setTimecardActuals(actualsByRosterId.getOrDefault(line.getPersonRosterId(), new ArrayList<>()));
                                //System.out.println("getTeamTimecards2 line.setTimecardActuals>>:" + actualsByRosterId.getOrDefault(line.getPersonRosterId(), new ArrayList<>()));
                            } else {
                                String key = line.getPersonId() + "|" + line.getEffectiveDate().toString();
                                line.setTimecardActuals(actualsByPersonIdAndDate.getOrDefault(key, new ArrayList<>()));
                            }
                        })
                        .collect(Collectors.groupingBy(TimecardLine::getPersonId));

                //System.out.println("getTeamTimecards2 linesByPersonId>>:" + linesByPersonId);
                // Set timecard lines to members
                for (TeamMembers member : members) {
                    member.setTimecardLines(linesByPersonId.getOrDefault(member.getPersonId(), new ArrayList<>()));
                }

                finalMembers.set(members);


                //System.out.println("getTeamTimecards2 members:" + members);


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }

        });

        //System.out.println("getTeamTimecards2 finalMembers.get():" + finalMembers.get());
        return CompletableFuture.completedFuture(finalMembers.get());

    }

    public List<TeamTimecardObject> getTeamTimecards3(long userId, Long profileId, LocalDate startDate, LocalDate endDate, JdbcClient jdbcClient) {

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userId", userId);
        objectMap.put("profileId", profileId);
        objectMap.put("startDate", startDate);
        objectMap.put("endDate", endDate);

        List<TeamTimecardObject> timecardObjects = jdbcClient.sql(TeamTimecardOneSql).params(objectMap).query(TeamTimecardObject.class).list();

        return timecardObjects;

    }

    public TeamTimecardKpi getTeamTimecardKpi(long userId, Long profileId, LocalDate startDate, LocalDate endDate, JdbcClient jdbcClient) {

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("userId", userId);
        objectMap.put("profileId", profileId);
        objectMap.put("startDate", startDate);
        objectMap.put("endDate", endDate);

        TeamTimecardKpi teamTimecardKpi = jdbcClient.sql(TeamTimecardKpiSql).params(objectMap).query(TeamTimecardKpi.class).single();

        return teamTimecardKpi;

    }


}
