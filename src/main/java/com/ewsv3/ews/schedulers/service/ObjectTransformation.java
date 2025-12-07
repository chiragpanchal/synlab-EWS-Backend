package com.ewsv3.ews.schedulers.service;

import com.ewsv3.ews.schedulers.dto.fusionDto.businessUnits.FusionBusinessUnit;
import com.ewsv3.ews.schedulers.dto.fusionDto.businessUnits.FusionBusinessUnitResponse;
import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.ewsv3.ews.schedulers.dto.fusionDto.department.FusionDepartment;
import com.ewsv3.ews.schedulers.dto.fusionDto.department.FusionDepartmentDff;
import com.ewsv3.ews.schedulers.dto.fusionDto.department.FusionDepartmentDffResponse;
import com.ewsv3.ews.schedulers.dto.fusionDto.department.FusionDepartmentResponse;
import com.ewsv3.ews.schedulers.dto.fusionDto.errors.errorLogs;
import com.ewsv3.ews.schedulers.dto.fusionDto.job.FusionJob;
import com.ewsv3.ews.schedulers.dto.fusionDto.job.FusionJobFamily;
import com.ewsv3.ews.schedulers.dto.fusionDto.job.FusionJobFamilyResponse;
import com.ewsv3.ews.schedulers.dto.fusionDto.job.FusionJobResponse;
import com.ewsv3.ews.schedulers.dto.fusionDto.worker.citizenship.FusionWorkerCitizenshipResponse;
import com.ewsv3.ews.schedulers.dto.fusionDto.worker.citizenship.FusionWorkerCitizenships;
import com.ewsv3.ews.schedulers.dto.fusionDto.worker.emails.FusionWorkerEmailResponse;
import com.ewsv3.ews.schedulers.dto.fusionDto.worker.emails.FusionWorkerEmails;
import com.ewsv3.ews.schedulers.dto.fusionDto.worker.names.FusionWorkerNames;
import com.ewsv3.ews.schedulers.dto.fusionDto.worker.names.FusionWorkerNamesResponse;
import com.ewsv3.ews.schedulers.dto.fusionDto.worker.phones.FusionWorkerPhones;
import com.ewsv3.ews.schedulers.dto.fusionDto.worker.phones.FusionWorkerPhonesResponse;
import com.ewsv3.ews.schedulers.dto.fusionDto.worker.worker.FusionWorker;
import com.ewsv3.ews.schedulers.dto.fusionDto.worker.worker.FusionWorkerResponse;
import com.ewsv3.ews.schedulers.service.SchedulerSql;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ObjectTransformation {

    private final int API_LIMIT = 100;

    public void businessUnitTransform(String batchId, JsonNode rootNode, JdbcClient jdbcClient) {
        //System.out.println("businessUnitTransform start:");
        ObjectMapper mapper = new ObjectMapper();
        List<FusionBusinessUnit> fusionBusinessUnitList = new ArrayList<>();

        try {
            FusionBusinessUnitResponse response = mapper.readValue(rootNode.toString(),
                    FusionBusinessUnitResponse.class);

            //System.out.println(Thread.currentThread().getStackTrace()[2]);

            // System.out.println("businessUnitTransform response:" + response);
            if (response != null) {
                fusionBusinessUnitList.addAll(response.items());
            }

            // System.out.println("businessUnitTransform fusionBusinessUnitList:" +
            // fusionBusinessUnitList);

            for (FusionBusinessUnit businessUnit : fusionBusinessUnitList) {

                try {
                    jdbcClient.sql(SchedulerSql.insertBusinessUnitSql)
                            .params(businessUnit.BusinessUnitId())
                            .params(businessUnit.Name())
                            .params(businessUnit.Status())
                            .update();
                } catch (Exception e) {
                    // TODO: Add logging service here to track DB calls
                    // System.out.println("error inserting business unit:" + businessUnit + ",
                    // error:" + e.getMessage());
                }

            }

        } catch (JsonProcessingException e) {
            //System.out.println("error parsing business unit, rootNode:" + rootNode);
            // TODO: Add logging service here for response parsing error
        }
        //System.out.println("businessUnitTransform end:");

    }

    public void departmentTransform(String batchId, String serviceUrl, RestClient restClient, String encodedAuth,
            JdbcClient jdbcClient) throws JsonProcessingException {
        //System.out.println("departmentTransform start:");
        int limit = API_LIMIT;
        int offset = 0;
        boolean hasMore = true;

        while (hasMore) {
            String restUrl = serviceUrl + "?orderBy=OrganizationId&limit=" + limit + "&offset=" + offset;
            //System.out.println("departmentTransform restUrl:" + restUrl);

            String deptBody = restClient.get()
                    .uri(restUrl)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Basic " + encodedAuth)
                    .retrieve()
                    .body(String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(deptBody);

            ObjectMapper mapper = new ObjectMapper();
            List<FusionDepartment> fusionFusionDepartmentList = new ArrayList<>();

            try {
                mapper.registerModule(new JavaTimeModule());
                mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

                FusionDepartmentResponse response = mapper.readValue(rootNode.toString(),
                        FusionDepartmentResponse.class);

                hasMore = response.hasMore();
                offset = response.offset() + limit;

                if (response != null) {
                    fusionFusionDepartmentList.addAll(response.items());
                }

                for (FusionDepartment fusionDepartment : fusionFusionDepartmentList) {

                    try {
                        jdbcClient.sql(SchedulerSql.insertDepartmentSql)
                                .params(fusionDepartment.organizationId())
                                .params(fusionDepartment.name())
                                .params(fusionDepartment.effectiveStartDate())
                                .params(fusionDepartment.effectiveEndDate())
                                .params(fusionDepartment.classificationCode())
                                .params(fusionDepartment.locationId())
                                .params(fusionDepartment.organizationCode())
                                .params(fusionDepartment.status())
                                .update();
                    } catch (Exception e) {
                        // TODO: Add logging service here to track DB calls
                        // System.out.println("error inserting fusionDepartment:" + fusionDepartment +
                        // ", error:" + e.getMessage());
                    }

                    for (LinksObject linksObject : fusionDepartment.links()) {

                        if (Objects.equals(linksObject.name(), "OrganizationDFF")) {
                            try {
                                String orgDffBody = restClient.get()
                                        .uri(linksObject.href())
                                        .accept(MediaType.APPLICATION_JSON)
                                        .header("Authorization", "Basic " + encodedAuth)
                                        .retrieve()
                                        .body(String.class);
                                JsonNode deptDffNode = objectMapper.readTree(orgDffBody);
                                departmentDFFTransform(deptDffNode, jdbcClient);
                            } catch (JsonProcessingException e) {
                                // TODO: Add logging service here to track DB calls
                                throw new RuntimeException(e);
                            }
                        }
                    }

                }

            } catch (JsonProcessingException e) {
                // System.out.println("error parsing fusionDepartment, rootNode:" + rootNode +
                // ", error:" + e.getMessage());
                //System.out.println("error parsing fusionDepartment error:" + e.getMessage());
                // TODO: Add logging service here for response parsing error
            }

        }
        //System.out.println("departmentTransform end:");

    }

    public void departmentDFFTransform(JsonNode rootNode, JdbcClient jdbcClient) {
        //System.out.println("departmentDFFTransform start:");
        ObjectMapper mapper = new ObjectMapper();
        List<FusionDepartmentDff> fusionDepartmentDffList = new ArrayList<>();

        try {
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            FusionDepartmentDffResponse response = mapper.readValue(rootNode.toString(),
                    FusionDepartmentDffResponse.class);

            // System.out.println(Thread.currentThread().getStackTrace()[2]);

            // System.out.println("departmentTransform response:" + response);
            if (response != null) {
                fusionDepartmentDffList.addAll(response.items());
            }

            for (FusionDepartmentDff fusionDepartmentDff : fusionDepartmentDffList) {
                try {
                    jdbcClient.sql(SchedulerSql.insertDepartmentDffSql)
                            .params(fusionDepartmentDff.organizationId())
                            .params(fusionDepartmentDff.attribute1())
                            .params(fusionDepartmentDff.attribute2())
                            .params(fusionDepartmentDff.attribute3())
                            .params(fusionDepartmentDff.attribute4())
                            .params(fusionDepartmentDff.attribute5())
                            .update();
                } catch (Exception e) {
                    // TODO: Add logging service here to track DB calls
                    // System.out.println("error inserting fusionDepartmentDff:" +
                    // fusionDepartmentDff + ", error:" + e.getMessage());
                }
            }

        } catch (JsonProcessingException e) {
            //System.out.println("error parsing fusionDepartmentDff, rootNode:" + rootNode + ", error:" + e.getMessage());
            // TODO: Add logging service here for response parsing error
        }
        //System.out.println("departmentDFFTransform end:");

    }

    public void jobTransform(String batchId, String serviceUrl, RestClient restClient, String encodedAuth,
            JdbcClient jdbcClient) throws JsonProcessingException {
        //System.out.println("jobTransform start:");
        int limit = API_LIMIT;
        int offset = 0;
        boolean hasMore = true;

        while (hasMore) {

            String restUrl = serviceUrl + "?orderBy=JobId&limit=" + limit + "&offset=" + offset;
            //System.out.println("jobTransform restUrl:" + restUrl);
            String restBody = restClient.get()
                    .uri(restUrl)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Basic " + encodedAuth)
                    .retrieve()
                    .body(String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(restBody);

            ObjectMapper mapper = new ObjectMapper();
            List<FusionJob> fusionJobList = new ArrayList<>();
            try {
                mapper.registerModule(new JavaTimeModule());
                mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

                FusionJobResponse response = mapper.readValue(rootNode.toString(), FusionJobResponse.class);

                hasMore = response.hasMore();
                offset = response.offset() + limit;

                // System.out.println(Thread.currentThread().getStackTrace()[2]);

                // System.out.println("departmentTransform response:" + response);
                if (response != null) {
                    fusionJobList.addAll(response.items());
                }

                // System.out.println("departmentTransform fusionFusionDepartmentList:" +
                // fusionFusionDepartmentList);

                for (FusionJob fusionJob : fusionJobList) {
                    // System.out.println("fusionJob:>>" + fusionJob);

                    try {
                        jdbcClient.sql(SchedulerSql.insertJobSql)
                                .params(fusionJob.jobId())
                                .params(fusionJob.jobCode())
                                .params(fusionJob.jobFamilyId())
                                .params(fusionJob.activeStatus())
                                .params(fusionJob.fullPartTime())
                                .params(fusionJob.jobFunctionCode())
                                .params(fusionJob.managerLevel())
                                .params(fusionJob.medicalCheckupRequired())
                                .params(fusionJob.standardWorkingHours())
                                .params(fusionJob.standardWorkingFrequency())
                                .params(fusionJob.standardAnnualWorkingDuration())
                                .params(fusionJob.annualWorkingDurationUnits())
                                .params(fusionJob.regularTemporary())
                                .params(fusionJob.setId())
                                .params(fusionJob.effectiveStartDate())
                                .params(fusionJob.effectiveEndDate())
                                .params(fusionJob.name())
                                .params(fusionJob.approvalAuthority())
                                .params(fusionJob.schedulingGroup())
                                .params(fusionJob.gradeLadderId())
                                .params(fusionJob.creationDate())
                                .params(fusionJob.lastUpdateDate())
                                .update();
                    } catch (Exception e) {
                        // TODO: Add logging service here to track DB calls
                        //System.out.println("error inserting fusionJob:" + fusionJob + ", error:" + e.getMessage());
                    }

                }

            } catch (JsonProcessingException e) {
                // System.out.println("error parsing fusionDepartment, rootNode:" + rootNode +
                // ", error:" + e.getMessage());
                //System.out.println("error parsing FusionJob error:" + e.getMessage());
                // TODO: Add logging service here for response parsing error
            }

        }
        //System.out.println("jobTransform end:");

        // System.out.println("departmentTransform
        // fusionFusionDepartmentList:"+fusionFusionDepartmentList);

    }

    public void jobFamiliesTransform(String batchId, String serviceUrl, RestClient restClient, String encodedAuth,
            JdbcClient jdbcClient) throws JsonProcessingException {
        //System.out.println("jobFamiliesTransform start:");
        int limit = API_LIMIT;
        int offset = 0;
        boolean hasMore = true;

        while (hasMore) {
            String restUrl = serviceUrl + "?orderBy=JobFamilyId&limit=" + limit + "&offset=" + offset;
            //System.out.println("jobFamiliesTransform restUrl:" + restUrl);
            String restBody = restClient.get()
                    .uri(restUrl)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Basic " + encodedAuth)
                    .retrieve()
                    .body(String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(restBody);

            ObjectMapper mapper = new ObjectMapper();
            List<FusionJobFamily> jobFamilyList = new ArrayList<>();
            //System.out.println("jobFamiliesTransform rootNode:" + rootNode);

            try {
                mapper.registerModule(new JavaTimeModule());
                mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

                FusionJobFamilyResponse response = mapper.readValue(rootNode.toString(), FusionJobFamilyResponse.class);
                hasMore = response.hasMore();
                offset = response.offset() + limit;

                // System.out.println(Thread.currentThread().getStackTrace()[2]);

                // System.out.println("departmentTransform response:" + response);
                if (response != null) {
                    jobFamilyList.addAll(response.items());
                }

                // System.out.println("departmentTransform fusionFusionDepartmentList:" +
                // fusionFusionDepartmentList);

                for (FusionJobFamily fusionJobFamily : jobFamilyList) {
                    // System.out.println("fusionJob:>>" + fusionJob);

                    try {
                        jdbcClient.sql(SchedulerSql.insertJobFamilySql)
                                .params(fusionJobFamily.jobFamilyId())
                                .params(fusionJobFamily.effectiveStartDate())
                                .params(fusionJobFamily.effectiveEndDate())
                                .params(fusionJobFamily.jobFamilyName())
                                .params(fusionJobFamily.jobFamilyCode())
                                .params(fusionJobFamily.actionReasonId())
                                .params(fusionJobFamily.activeStatus())
                                .params(fusionJobFamily.creationDate())
                                .params(fusionJobFamily.lastUpdateDate())
                                .update();
                    } catch (Exception e) {
                        // TODO: Add logging service here to track DB calls
                        //System.out.println(
                        //        "error inserting FusionJobFamily:" + fusionJobFamily + ", error:" + e.getMessage());
                    }

                }

            } catch (JsonProcessingException e) {
                // System.out.println("error parsing fusionDepartment, rootNode:" + rootNode +
                // ", error:" + e.getMessage());
                //System.out.println("error parsing FusionJobFamily error:" + e.getMessage());
                // TODO: Add logging service here for response parsing error
            }

        }
        //System.out.println("jobFamiliesTransform end:");

        // System.out.println("departmentTransform
        // fusionFusionDepartmentList:"+fusionFusionDepartmentList);

    }

    public void workerTransform(String batchId, String serviceUrl, RestClient restClient, String encodedAuth,
            JdbcClient jdbcClient) throws JsonProcessingException {
        //System.out.println("workerTransform start:");

        List<errorLogs> errorLogs = new ArrayList<>();

        int limit = API_LIMIT;
        int offset = 0;
        boolean hasMore = true;
        while (hasMore) {
            String workerUrl = serviceUrl + "?orderBy=PersonId&limit=" + limit + "&offset=" + offset;
            //System.out.println("workerTransform workerUrl:" + workerUrl);
            String workerBody = restClient.get()
                    .uri(workerUrl)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Basic " + encodedAuth)
                    .retrieve()
                    .body(String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(workerBody);

            ObjectMapper mapper = new ObjectMapper();
            List<FusionWorker> fusionWorkerList = new ArrayList<>();

            try {
                mapper.registerModule(new JavaTimeModule());
                mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

                FusionWorkerResponse response = mapper.readValue(rootNode.toString(), FusionWorkerResponse.class);
                hasMore = response.hasMore();
                offset = response.offset() + limit;

                if (response != null) {
                    fusionWorkerList.addAll(response.items());
                }

                // System.out.println("workerTransform fusionWorkerList:" + fusionWorkerList);

                for (FusionWorker worker : fusionWorkerList) {

                    String citizenshipsLink = "";
                    String emailsLink = "";
                    String legislativeinfoLink = "";
                    String namesLink = "";
                    String phonesLinks = "";
                    String religionsLinks = "";
                    String workrelationshipsLink = "";
                    String workersdffLink = "";

                    for (LinksObject linksObject : worker.links()) {
                        if (Objects.equals(linksObject.name(), "citizenships")) {
                            citizenshipsLink = linksObject.href();
                        } else if (Objects.equals(linksObject.name(), "emails")) {
                            emailsLink = linksObject.href();
                        } else if (Objects.equals(linksObject.name(), "legislativeInfo")) {
                            legislativeinfoLink = linksObject.href();
                        } else if (Objects.equals(linksObject.name(), "names")) {
                            namesLink = linksObject.href();
                        } else if (Objects.equals(linksObject.name(), "phones")) {
                            phonesLinks = linksObject.href();
                        } else if (Objects.equals(linksObject.name(), "religions")) {
                            religionsLinks = linksObject.href();
                        } else if (Objects.equals(linksObject.name(), "workRelationships")) {
                            workrelationshipsLink = linksObject.href();
                        } else if (Objects.equals(linksObject.name(), "workersDFF")) {
                            workersdffLink = linksObject.href();
                        }

                    }

                    try {

                        jdbcClient.sql(SchedulerSql.insertWorkerSql)
                                .params(worker.personId())
                                .params(worker.personNumber())
                                .params(worker.correspondenceLanguage())
                                .params(worker.bloodType())
                                .params(worker.dateOfBirth())
                                .params(worker.dateOfDeath())
                                .params(worker.countryOfBirth())
                                .params(worker.regionOfBirth())
                                .params(worker.townOfBirth())
                                .params(worker.applicantNumber())
                                .params(worker.creationDate())
                                .params(worker.lastUpdateDate())
                                .params(citizenshipsLink)
                                .params(emailsLink)
                                .params(legislativeinfoLink)
                                .params(namesLink)
                                .params(phonesLinks)
                                .params(religionsLinks)
                                .params(workrelationshipsLink)
                                .params(workersdffLink)
                                .update();
                    } catch (Exception e) {
                        // TODO: Add logging service here to track DB calls
                        //System.out.println("error inserting FusionWorker:" + worker + ", error:" + e.getMessage());
                        errorLogs.add(new errorLogs(batchId, "worker", workerUrl, e.getMessage()));
                    }

                    // for (LinksObject linksObject : worker.links()) {
                    //
                    // if (Objects.equals(linksObject.name(), "citizenships")) {
                    // citizenshipsLink = linksObject.href();
                    //
                    // try {
                    // String citizenshipsBody = restClient.get()
                    // .uri(citizenshipsLink)
                    // .accept(MediaType.APPLICATION_JSON)
                    // .header("Authorization", "Basic " + encodedAuth)
                    // .retrieve()
                    // .body(String.class);
                    // JsonNode citizenshipsNode = objectMapper.readTree(citizenshipsBody);
                    // citizenshipTransform(worker.personId(), citizenshipsNode, jdbcClient);
                    // } catch (JsonProcessingException e) {
                    // // TODO: Add logging service here to track DB calls
                    // throw new RuntimeException(e);
                    // } finally {
                    // }
                    //
                    // }
                    // if (Objects.equals(linksObject.name(), "emails")) {
                    // emailsLink = linksObject.href();
                    //
                    // // Getting emails
                    // try {
                    // String emailsBody = restClient.get()
                    // .uri(emailsLink)
                    // .accept(MediaType.APPLICATION_JSON)
                    // .header("Authorization", "Basic " + encodedAuth)
                    // .retrieve()
                    // .body(String.class);
                    // JsonNode emailsNode = objectMapper.readTree(emailsBody);
                    // emailsTransform(worker.personId(), emailsNode, jdbcClient);
                    //// workerList.remove(fusionWorker);
                    // } catch (JsonProcessingException e) {
                    // System.err.println("Exception in json parsing emails : " + worker.personId()
                    // + " - " + e.getMessage());
                    // // TODO: Add logging service here to track DB calls
                    // throw new RuntimeException(e);
                    // } catch (Exception e) {
                    // // Log and handle the error appropriately
                    // System.err.println("Exception in processing emails: " + worker.personId() + "
                    // - " + e.getMessage());
                    // } finally {
                    // }
                    //
                    // }
                    // if (Objects.equals(linksObject.name(), "legislativeInfo")) {
                    // legislativeinfoLink = linksObject.href();
                    // }
                    // if (Objects.equals(linksObject.name(), "names")) {
                    // namesLink = linksObject.href();
                    // try {
                    // String restsBody = restClient.get()
                    // .uri(namesLink)
                    // .accept(MediaType.APPLICATION_JSON)
                    // .header("Authorization", "Basic " + encodedAuth)
                    // .retrieve()
                    // .body(String.class);
                    // JsonNode restsNode = objectMapper.readTree(restsBody);
                    // namesTransform(worker.personId(), restsNode, jdbcClient);
                    // } catch (JsonProcessingException e) {
                    // // TODO: Add logging service here to track DB calls
                    // throw new RuntimeException(e);
                    // } finally {
                    // }
                    // }
                    // if (Objects.equals(linksObject.name(), "phones")) {
                    // phonesLinks = linksObject.href();
                    //
                    // try {
                    //// System.out.println("phones start:");
                    // String restsBody = restClient.get()
                    // .uri(phonesLinks)
                    // .accept(MediaType.APPLICATION_JSON)
                    // .header("Authorization", "Basic " + encodedAuth)
                    // .retrieve()
                    // .body(String.class);
                    // JsonNode restsNode = objectMapper.readTree(restsBody);
                    // phonesTransform(worker.personId(), restsNode, jdbcClient);
                    // } catch (JsonProcessingException e) {
                    // // TODO: Add logging service here to track DB calls
                    // throw new RuntimeException(e);
                    // } finally {
                    //// System.out.println("phones end:");
                    // }
                    // }
                    // if (Objects.equals(linksObject.name(), "religions")) {
                    // religionsLinks = linksObject.href();
                    // }
                    // if (Objects.equals(linksObject.name(), "workRelationships")) {
                    // workrelationshipsLink = linksObject.href();
                    // }
                    // if (Objects.equals(linksObject.name(), "workersDFF")) {
                    // workersdffLink = linksObject.href();
                    // }
                    // }

                }

            } catch (JsonProcessingException e) {
                // System.out.println("error parsing fusionDepartment, rootNode:" + rootNode +
                // ", error:" + e.getMessage());
                //System.out.println("error parsing fusionDepartment error:" + e.getMessage());
                // TODO: Add logging service here for response parsing error
            }

        }
        //System.out.println("workerTransform end:");

        // Get all workers and get all other info from links api
        // List<FusionWorker> workerList = new ArrayList<>();
        List<FusionWorker> workerList = jdbcClient.sql(SchedulerSql.getAllWorkers).query(FusionWorker.class).list();
        // workerList = null;
        //System.out.println("worker counts:  " + workerList.size());
        ObjectMapper objectMapper = new ObjectMapper();
        ExecutorService exeService = Executors.newVirtualThreadPerTaskExecutor();

        try {
            // Use CompletableFuture for parallel execution
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            int workerCounts = 1;

            for (FusionWorker fusionWorker : workerList) {

                workerCounts = workerCounts + 1;

                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

                    // Getting citizenships
                    try {
                        String respBody = restClient.get()
                                .uri(fusionWorker.citizenshipsLink())
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Basic " + encodedAuth)
                                .retrieve()
                                .body(String.class);
                        JsonNode emailsNode = objectMapper.readTree(respBody);
                        citizenshipTransform(fusionWorker.personId(), emailsNode, jdbcClient);
                    } catch (JsonProcessingException e) {
                        // TODO: Add logging service here to track DB calls
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        // Log and handle the error appropriately
                        errorLogs.add(new errorLogs(batchId, "citizenships", fusionWorker.citizenshipsLink(),
                                e.getMessage()));
                        //System.err.println("Exception in processing citizenships: " + fusionWorker.personId() + " - "
                        //        + e.getMessage());
                    } finally {
                    }

                    // Getting emails
                    try {
                        String emailsBody = restClient.get()
                                .uri(fusionWorker.emailsLink())
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Basic " + encodedAuth)
                                .retrieve()
                                .body(String.class);
                        JsonNode emailsNode = objectMapper.readTree(emailsBody);
                        emailsTransform(fusionWorker.personId(), emailsNode, jdbcClient);
                    } catch (JsonProcessingException e) {
                        // TODO: Add logging service here to track DB calls
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        // Log and handle the error appropriately
                        errorLogs.add(new errorLogs(batchId, "citizenships", fusionWorker.citizenshipsLink(),
                                e.getMessage()));
                        //System.err.println(
                        //        "Exception in processing emails: " + fusionWorker.personId() + " - " + e.getMessage());
                    } finally {
                    }

                    // Getting phones
                    try {
                        String respBody = restClient.get()
                                .uri(fusionWorker.phonesLinks())
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Basic " + encodedAuth)
                                .retrieve()
                                .body(String.class);
                        JsonNode emailsNode = objectMapper.readTree(respBody);
                        phonesTransform(fusionWorker.personId(), emailsNode, jdbcClient);
                    } catch (JsonProcessingException e) {
                        // TODO: Add logging service here to track DB calls
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        // Log and handle the error appropriately
                        //System.err.println(
                        //        "Exception in processing phones: " + fusionWorker.personId() + " - " + e.getMessage());
                    } finally {
                    }

                    // Getting names
                    try {
                        String respBody = restClient.get()
                                .uri(fusionWorker.namesLink())
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Basic " + encodedAuth)
                                .retrieve()
                                .body(String.class);
                        JsonNode jnode = objectMapper.readTree(respBody);
                        namesTransform(fusionWorker.personId(), jnode, jdbcClient);
                    } catch (JsonProcessingException e) {
                        // TODO: Add logging service here to track DB calls
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        // Log and handle the error appropriately
                        //System.err.println(
                        //        "Exception in processing names: " + fusionWorker.personId() + " - " + e.getMessage());
                    } finally {
                    }

                    // Getting Work-relationships
                    try {
                        String respBody = restClient.get()
                                .uri(fusionWorker.workrelationshipsLink())
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Basic " + encodedAuth)
                                .retrieve()
                                .body(String.class);
                        JsonNode jnode = objectMapper.readTree(respBody);
                        namesTransform(fusionWorker.personId(), jnode, jdbcClient);
                    } catch (JsonProcessingException e) {
                        // TODO: Add logging service here to track DB calls
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        // Log and handle the error appropriately
                        //System.err.println(
                        //        "Exception in processing names: " + fusionWorker.personId() + " - " + e.getMessage());
                    } finally {
                    }

                }, exeService);

                futures.add(future);

            }

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        } catch (Exception exception) {

        } finally {
            exeService.shutdown();
        }

    }

    public void citizenshipTransform(long personId, JsonNode rootNode, JdbcClient jdbcClient) {
        ObjectMapper mapper = new ObjectMapper();
        List<FusionWorkerCitizenships> citizenshipsList = new ArrayList<>();
        // System.out.println("citizenshipTransform rootNode:" + rootNode);

        try {
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            FusionWorkerCitizenshipResponse response = mapper.readValue(rootNode.toString(),
                    FusionWorkerCitizenshipResponse.class);
            if (response != null) {
                citizenshipsList.addAll(response.items());
            }

            for (FusionWorkerCitizenships citizenships : citizenshipsList) {

                try {
                    jdbcClient.sql(SchedulerSql.insertWorkerCitizenshipsSql)
                            .params(personId)
                            .params(citizenships.citizenshipId())
                            .params(citizenships.citizenship())
                            .params(citizenships.fromDate())
                            .params(citizenships.toDate())
                            .params(citizenships.citizenshipStatus())
                            .params(citizenships.creationDate())
                            .params(citizenships.lastUpdateDate())
                            .update();
                } catch (Exception e) {
                    // TODO: Add logging service here to track DB calls
                    //System.out.println(
                    //        "error inserting FusionWorkerCitizenships:" + citizenships + ", error:" + e.getMessage());
                }

            }

        } catch (JsonProcessingException e) {
            // System.out.println("error parsing fusionDepartment, rootNode:" + rootNode +
            // ", error:" + e.getMessage());
            //System.out.println("error parsing FusionWorkerCitizenships error:" + e.getMessage());
            // TODO: Add logging service here for response parsing error
        }

        // System.out.println("departmentTransform
        // fusionFusionDepartmentList:"+fusionFusionDepartmentList);

    }

    public void emailsTransform(long personId, JsonNode rootNode, JdbcClient jdbcClient) {
        ObjectMapper mapper = new ObjectMapper();
        List<FusionWorkerEmails> fusionWorkerEmailsList = new ArrayList<>();
        // System.out.println("emailsTransform personId:" + personId);

        try {
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            FusionWorkerEmailResponse response = mapper.readValue(rootNode.toString(), FusionWorkerEmailResponse.class);
            if (response != null) {
                fusionWorkerEmailsList.addAll(response.items());
            }

            for (FusionWorkerEmails workerEmails : fusionWorkerEmailsList) {

                try {
                    jdbcClient.sql(SchedulerSql.insertWorkerEmailsSql)
                            .params(personId)
                            .params(workerEmails.emailAddressId())
                            .params(workerEmails.emailType())
                            .params(workerEmails.emailAddress())
                            .params(workerEmails.fromDate())
                            .params(workerEmails.toDate())
                            .params(workerEmails.primaryFlag())
                            .params(workerEmails.creationDate())
                            .params(workerEmails.lastUpdateDate())
                            .update();
                } catch (Exception e) {
                    // TODO: Add logging service here to track DB calls
                    //System.out.println(
                    //        "error inserting FusionWorkerEmails:" + workerEmails + ", error:" + e.getMessage());
                }

            }

        } catch (JsonProcessingException e) {
            // System.out.println("error parsing fusionDepartment, rootNode:" + rootNode +
            // ", error:" + e.getMessage());
            //System.out.println("error parsing FusionWorkerEmails error:" + e.getMessage());
            // TODO: Add logging service here for response parsing error
        } finally {
            //System.out.println("emailsTransform completed personId:" + personId);
        }

        // System.out.println("departmentTransform
        // fusionFusionDepartmentList:"+fusionFusionDepartmentList);

    }

    public void namesTransform(long personId, JsonNode rootNode, JdbcClient jdbcClient) {
        ObjectMapper mapper = new ObjectMapper();
        List<FusionWorkerNames> fusionWorkerNamesList = new ArrayList<>();
        // System.out.println("emailsTransform rootNode:" + rootNode);

        try {
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            FusionWorkerNamesResponse response = mapper.readValue(rootNode.toString(), FusionWorkerNamesResponse.class);
            if (response != null) {
                fusionWorkerNamesList.addAll(response.items());
            }

            for (FusionWorkerNames workerNames : fusionWorkerNamesList) {

                try {
                    jdbcClient.sql(SchedulerSql.insertWorkerNamesSql)
                            .params(personId)
                            .params(workerNames.personNameId())
                            .params(workerNames.effectiveStartDate())
                            .params(workerNames.effectiveEndDate())
                            .params(workerNames.legislationCode())
                            .params(workerNames.lastName())
                            .params(workerNames.firstName())
                            .params(workerNames.title())
                            .params(workerNames.preNameAdjunct())
                            .params(workerNames.suffix())
                            .params(workerNames.middleNames())
                            .params(workerNames.honors())
                            .params(workerNames.knownAs())
                            .params(workerNames.previousLastName())
                            .params(workerNames.displayName())
                            .params(workerNames.orderName())
                            .params(workerNames.listName())
                            .params(workerNames.fullName())
                            .params(workerNames.militaryRank())
                            .params(workerNames.nameLanguage())
                            .params(workerNames.nameInformation1())
                            .params(workerNames.nameInformation2())
                            .params(workerNames.nameInformation3())
                            .params(workerNames.nameInformation4())
                            .params(workerNames.nameInformation5())
                            .params(workerNames.nameInformation6())
                            .params(workerNames.nameInformation7())
                            .params(workerNames.nameInformation8())
                            .params(workerNames.nameInformation9())
                            .params(workerNames.nameInformation10())
                            .params(workerNames.nameInformation11())
                            .params(workerNames.nameInformation12())
                            .params(workerNames.nameInformation13())
                            .params(workerNames.nameInformation14())
                            .params(workerNames.nameInformation15())
                            .params(workerNames.nameInformation16())
                            .params(workerNames.nameInformation17())
                            .params(workerNames.nameInformation18())
                            .params(workerNames.nameInformation19())
                            .params(workerNames.nameInformation20())
                            .params(workerNames.nameInformation21())
                            .params(workerNames.nameInformation22())
                            .params(workerNames.nameInformation23())
                            .params(workerNames.nameInformation24())
                            .params(workerNames.nameInformation25())
                            .params(workerNames.nameInformation26())
                            .params(workerNames.nameInformation27())
                            .params(workerNames.nameInformation28())
                            .params(workerNames.nameInformation29())
                            .params(workerNames.nameInformation30())
                            .params(workerNames.creationDate())
                            .params(workerNames.lastUpdateDate())
                            .params(workerNames.localPersonNameId())
                            .params(workerNames.localEffectiveStartDate())
                            .params(workerNames.localEffectiveEndDate())
                            .params(workerNames.localLegislationCode())
                            .params(workerNames.localLastName())
                            .params(workerNames.localFirstName())
                            .params(workerNames.localTitle())
                            .params(workerNames.localPreNameAdjunct())
                            .params(workerNames.localSuffix())
                            .params(workerNames.localMiddleNames())
                            .params(workerNames.localHonors())
                            .params(workerNames.localKnownAs())
                            .params(workerNames.localPreviousLastName())
                            .params(workerNames.localDisplayName())
                            .params(workerNames.localOrderName())
                            .params(workerNames.localListName())
                            .params(workerNames.localFullName())
                            .params(workerNames.localMilitaryRank())
                            .params(workerNames.localNameLanguage())
                            .params(workerNames.localNameInformation1())
                            .params(workerNames.localNameInformation2())
                            .params(workerNames.localNameInformation3())
                            .params(workerNames.localNameInformation4())
                            .params(workerNames.localNameInformation5())
                            .params(workerNames.localNameInformation6())
                            .params(workerNames.localNameInformation7())
                            .params(workerNames.localNameInformation8())
                            .params(workerNames.localNameInformation9())
                            .params(workerNames.localNameInformation10())
                            .params(workerNames.localNameInformation11())
                            .params(workerNames.localNameInformation12())
                            .params(workerNames.localNameInformation13())
                            .params(workerNames.localNameInformation14())
                            .params(workerNames.localNameInformation15())
                            .params(workerNames.localNameInformation16())
                            .params(workerNames.localNameInformation17())
                            .params(workerNames.localNameInformation18())
                            .params(workerNames.localNameInformation19())
                            .params(workerNames.localNameInformation20())
                            .params(workerNames.localNameInformation21())
                            .params(workerNames.localNameInformation22())
                            .params(workerNames.localNameInformation23())
                            .params(workerNames.localNameInformation24())
                            .params(workerNames.localNameInformation25())
                            .params(workerNames.localNameInformation26())
                            .params(workerNames.localNameInformation27())
                            .params(workerNames.localNameInformation28())
                            .params(workerNames.localNameInformation29())
                            .params(workerNames.localNameInformation30())
                            .params(workerNames.localCreationDate())
                            .params(workerNames.localLastUpdateDate())
                            .update();
                } catch (Exception e) {
                    // TODO: Add logging service here to track DB calls
                    //System.out.println("error inserting workerNames:" + workerNames + ", error:" + e.getMessage());
                }

            }

        } catch (JsonProcessingException e) {
            // System.out.println("error parsing fusionDepartment, rootNode:" + rootNode +
            // ", error:" + e.getMessage());
            //System.out.println("error parsing workerNames error:" + e.getMessage());
            // TODO: Add logging service here for response parsing error
        }

        // System.out.println("departmentTransform
        // fusionFusionDepartmentList:"+fusionFusionDepartmentList);

    }

    public void phonesTransform(long personId, JsonNode rootNode, JdbcClient jdbcClient) {
        ObjectMapper mapper = new ObjectMapper();
        List<FusionWorkerPhones> fusionWorkerPhonesList = new ArrayList<>();

        try {
            mapper.registerModule(new JavaTimeModule());
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            FusionWorkerPhonesResponse response = mapper.readValue(rootNode.toString(),
                    FusionWorkerPhonesResponse.class);
            if (response != null) {
                fusionWorkerPhonesList.addAll(response.items());
            }

            for (FusionWorkerPhones workerPhones : fusionWorkerPhonesList) {

                try {
                    jdbcClient.sql(SchedulerSql.insertWorkerPhonesSql)
                            .params(personId)
                            .params(workerPhones.phoneId())
                            .params(workerPhones.phoneType())
                            .params(workerPhones.legislationCode())
                            .params(workerPhones.countryCodeNumber())
                            .params(workerPhones.areaCode())
                            .params(workerPhones.phoneNumber())
                            .params(workerPhones.extension())
                            .params(workerPhones.fromDate())
                            .params(workerPhones.toDate())
                            .params(workerPhones.validity())
                            .params(workerPhones.creationDate())
                            .params(workerPhones.lastUpdateDate())
                            .params(workerPhones.primaryFlag())
                            .update();
                } catch (Exception e) {
                    // TODO: Add logging service here to track DB calls
                    //System.out.println("error inserting workerPhones:" + workerPhones + ", error:" + e.getMessage());
                }

            }

        } catch (JsonProcessingException e) {
            // System.out.println("error parsing fusionDepartment, rootNode:" + rootNode +
            // ", error:" + e.getMessage());
            //System.out.println("error parsing FusionWorkerCitizenships error:" + e.getMessage());
            // TODO: Add logging service here for response parsing error
        }

        // System.out.println("departmentTransform
        // fusionFusionDepartmentList:"+fusionFusionDepartmentList);

    }

}
