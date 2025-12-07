package com.ewsv3.ews.schedulers.service;

import com.ewsv3.ews.schedulers.dto.BIScheduler;
import com.ewsv3.ews.schedulers.dto.InboundIntegration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.*;

import static com.ewsv3.ews.schedulers.service.SchedulerConstants.*;
import static com.ewsv3.ews.schedulers.service.SchedulerSql.*;

@Service
public class SchedulerService {

    private final ExecutorService virtualThreadExecutor;
    private final ScheduledExecutorService scheduler;
    private final JdbcClient jdbcClient;
    private final RestClient restClient;

    public SchedulerService(@Qualifier("executorService") ExecutorService virtualThreadExecutor,
            @Qualifier("scheduledExecutorService") ScheduledExecutorService scheduler, JdbcClient jdbcClient) {
        this.virtualThreadExecutor = virtualThreadExecutor;
        this.jdbcClient = jdbcClient;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.restClient = RestClient
                .builder()
                .baseUrl(baseUrl)
                .build();
    }

    public void startInboundintegration() {

        long interval = fetchIntervalFromDatabase(jdbcClient);

        scheduler.scheduleWithFixedDelay(() -> {
            // System.out.println("scheduler called....");
            // Call the APIs and store responses
            CompletableFuture.runAsync(this::callApisAndStoreResponses, virtualThreadExecutor).join(); // Block until
                                                                                                       // all tasks
                                                                                                       // complete
        }, 0, interval, TimeUnit.MINUTES);

        // System.out.println("startInboundintegration: interval:" + interval);
        //
        // scheduler.scheduleWithFixedDelay(() -> {
        // CompletableFuture.runAsync(() -> {
        // callApisAndStoreResponses(jdbcClient); // Call the APIs and store responses
        // }, virtualThreadExecutor).join(); // Block until all tasks complete
        //
        // }, 0, interval, TimeUnit.MILLISECONDS); // Use the interval from the database
        // System.out.println("startInboundintegration: ends");
        // this.scheduler.scheduleWithFixedDelay(() -> {
        // callApisAndStoreResponses(jdbcClient);
        // startInboundintegration(jdbcClient);
        // }, 0, interval, TimeUnit.MINUTES);

        // try {
        // CompletableFuture<Void> runAsync = CompletableFuture.runAsync(() -> {
        // this.scheduler.scheduleWithFixedDelay(() -> {
        // callApisAndStoreResponses(jdbcClient);
        // startInboundintegration(jdbcClient);
        // }, 0, interval, TimeUnit.MINUTES);
        // }, scheduler);
        //
        // runAsync.join();
        // } catch (Exception e) {
        // System.out.println("error startInboundintegration:" + e.getMessage());
        // throw new RuntimeException(e);
        // }

    }

    private void callApisAndStoreResponses() {
        // System.out.println("SCHEDULER STARTED at" + LocalTime.now() + " = * = * = * = * = * = * = * = * =");
        List<InboundIntegration> inboundIntegrationList = jdbcClient.sql(getBiApis).query(InboundIntegration.class)
                .list();
        ObjectTransformation objectTransformation = new ObjectTransformation();

        // System.out.println("inboundIntegrationList:" + inboundIntegrationList);
        // System.out.println("inboundIntegrationList.size():" + inboundIntegrationList.size());

        jdbcClient.sql(cleanInterfaceTablesSql).update();// Deleting all rows from inb tables

        String authStr = fusionUserName + ":" + fusionPassword;
        String encodedAuth = Base64.getEncoder().encodeToString(authStr.getBytes());

        // HttpHeaders headers = new HttpHeaders();
        // headers.setContentType(MediaType.APPLICATION_JSON);
        // headers.set("Authorization", "Basic " + encodedAuth);

        // Set batch_id
        LocalDateTime now = LocalDateTime.now();

        // Define the custom format: mmddyyyyhh24miss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyyHHmmss");

        // Format the date and time
        String batchId = now.format(formatter);

        ExecutorService exeService = Executors.newVirtualThreadPerTaskExecutor();

        try {
            // Use CompletableFuture for parallel execution
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (int i = 0; i < inboundIntegrationList.size(); i++) {
                int taskId = i;
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    String endpoint = inboundIntegrationList.get(taskId).endPoint();
                    String endpointName = inboundIntegrationList.get(taskId).name();

                    // System.out.println("endpoint:" + endpoint);

                    try {
                        String body = restClient.get()
                                .uri(endpoint)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Basic " + encodedAuth)
                                .retrieve()
                                .body(String.class);

                        ObjectMapper objectMapper = new ObjectMapper();
                        JsonNode rootNode = objectMapper.readTree(body);
                        JsonNode itemCountNode = rootNode.path("count");

                        // System.out.println("endpointName: " + endpointName + ", counts: " + itemCountNode);

                        // Call the appropriate transformation method in parallel
                        switch (endpointName) {
                            case "1Business Unit":
                                objectTransformation.businessUnitTransform(batchId, rootNode, jdbcClient);
                                break;
                            case "1Departments":
                                objectTransformation.departmentTransform(batchId, endpoint, restClient, encodedAuth,
                                        jdbcClient);
                                break;
                            case "1Jobs":
                                objectTransformation.jobTransform(batchId, endpoint, restClient, encodedAuth,
                                        jdbcClient);
                                break;
                            case "1Job Families":
                                objectTransformation.jobFamiliesTransform(batchId, endpoint, restClient, encodedAuth,
                                        jdbcClient);
                                break;
                            case "Person Data":
                                objectTransformation.workerTransform(batchId, endpoint, restClient, encodedAuth,
                                        jdbcClient);
                                break;
                            default:
                                // System.out.println("Unknown endpoint: " + endpointName);
                        }

                    } catch (Exception e) {
                        // System.out.println("Error for endpoint " + endpointName + ": " + e.getMessage());
                        // TODO: Add proper logging for errors
                    }

                }, exeService);

                futures.add(future); // Add future to the list to ensure parallel execution
            }

            // Wait for all tasks to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        } finally {
            // System.out.println("SCHEDULER COMPLETED at" + LocalTime.now() + " = * = * = * = * = * = * = * = * =");
            exeService.shutdown();
        }
    }

    // private void callApisAndStoreResponsesOld() {

    // List<InboundIntegration> inboundIntegrationList =
    // jdbcClient.sql(getBiApis).query(InboundIntegration.class)
    // .list();
    // ObjectTransformation objectTransformation = new ObjectTransformation();

    // System.out.println("inboundIntegrationList:" + inboundIntegrationList);
    // System.out.println("inboundIntegrationList.size():" +
    // inboundIntegrationList.size());

    // CompletableFuture<?>[] futures = new
    // CompletableFuture<?>[inboundIntegrationList.size()];

    // String authStr = fusionUserName + ":" + fusionPassword;
    // String encodedAuth = Base64.getEncoder().encodeToString(authStr.getBytes());

    // // Set batch_id
    // LocalDateTime now = LocalDateTime.now();

    // // Define the custom format: mmddyyyyhh24miss
    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyyHHmmss");

    // // Format the date and time
    // String batchId = now.format(formatter);

    // System.out.println("batchId: " + batchId);

    // HttpHeaders headers = new HttpHeaders();
    // headers.setContentType(MediaType.APPLICATION_JSON);
    // headers.set("Authorization", "Basic " + encodedAuth);

    // for (int i = 0; i < inboundIntegrationList.size(); i++) {
    // String endpoint = inboundIntegrationList.get(i).endPoint();
    // String endpointName = inboundIntegrationList.get(i).name();

    // System.out.println("BEGIN == == == == == == == ==");
    // System.out.println("endpoint:" + endpoint);

    // try (var exeService = Executors.newVirtualThreadPerTaskExecutor()) {

    // exeService.submit(() -> {
    // String body = null;
    // try {

    // String serviceUrl = endpoint;

    // body = restClient.get()
    // .uri(serviceUrl)
    // .accept(MediaType.APPLICATION_JSON)
    // .header("Authorization", "Basic " + encodedAuth)
    // .retrieve()
    // .body(String.class);

    // try {
    // ObjectMapper objectMapper = new ObjectMapper();
    // JsonNode rootNode = objectMapper.readTree(body);
    // JsonNode itemNode = rootNode.path("items");
    // JsonNode itemCountNode = rootNode.path("count");

    // System.out.println("endpointName :" + endpointName + ", counts: " +
    // itemCountNode);

    // if (Objects.equals(endpointName, "Business Unit")) {
    // objectTransformation.businessUnitTransform(batchId, rootNode, jdbcClient);
    // } else if (Objects.equals(endpointName, "Departments")) {
    // objectTransformation.departmentTransform(batchId, serviceUrl, restClient,
    // encodedAuth,
    // jdbcClient);

    // } else if (Objects.equals(endpointName, "Jobs")) {
    // objectTransformation.jobTransform(batchId, serviceUrl, restClient,
    // encodedAuth,
    // jdbcClient);
    // } else if (Objects.equals(endpointName, "Job Families")) {
    // objectTransformation.jobFamiliesTransform(batchId, serviceUrl, restClient,
    // encodedAuth,
    // jdbcClient);
    // } else if (Objects.equals(endpointName, "Legal Entities")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Location")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Work Schedules")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Work Schedule Patterns")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Supervisor Hierarchy")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Area Of Responsibilty")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Person Absence")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Assignment Data")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Email Details")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Employee Skills")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "User Vacation And Delegation")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Leave Balances")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Lookups")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Payroll Details")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "People Groups")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Period Of Service")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Person Assignment Details")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Person Citizenships")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Person National Identifier")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Person Public Holiday")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Person Religion")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Person Data")) {
    // objectTransformation.workerTransform(batchId, serviceUrl, restClient,
    // encodedAuth,
    // jdbcClient);
    // System.out.println("Person Data completed");
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Phone Detail")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Planned Schedules")) {
    // System.out.println();
    // } else if (Objects.equals(endpointName, "Position Details")) {
    // System.out.println();
    // }

    // } catch (Exception exception) {
    // // TODO: Add logging service here for body parsing error
    // System.out.println("body parsing error:" + exception.getMessage());
    // }

    // } catch (Exception e) {
    // System.out.println("endpoint response error :" + e.getMessage());
    // // TODO: Add logging service here for endpoint response error
    // throw new RuntimeException(e);
    // }

    // System.out.println("END == == == == == == == ==");
    // });

    // } catch (Exception exception) {

    // }

    // }

    // }

    private long fetchIntervalFromDatabase(JdbcClient jdbcClient) {

        long interval = 60;// in minutes
        try {
            BIScheduler single = jdbcClient.sql(getBISchedulers).query(BIScheduler.class).single();
            interval = single.minutesDelay();
        } catch (Exception e) {
        }

        // System.out.println("interval:" + interval);
        return interval;

    }

}
