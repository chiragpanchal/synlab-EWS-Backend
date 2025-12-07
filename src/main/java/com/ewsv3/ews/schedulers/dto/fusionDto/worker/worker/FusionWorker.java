package com.ewsv3.ews.schedulers.dto.fusionDto.worker.worker;

import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record FusionWorker(

                @JsonProperty("PersonId") long personId,

                @JsonProperty("PersonNumber") String personNumber,

                @JsonProperty("CorrespondenceLanguage") String correspondenceLanguage,

                @JsonProperty("BloodType") String bloodType,

                @JsonProperty("DateOfBirth") LocalDate dateOfBirth,

                @JsonProperty("DateOfDeath") LocalDate dateOfDeath,

                @JsonProperty("CountryOfBirth") String countryOfBirth,

                @JsonProperty("RegionOfBirth") String regionOfBirth,

                @JsonProperty("TownOfBirth") String townOfBirth,

                @JsonProperty("ApplicantNumber") String applicantNumber,

                @JsonIgnore() @JsonProperty("CreatedBy") String createdBy,

                @JsonProperty("CreationDate") OffsetDateTime creationDate,

                @JsonIgnore() @JsonProperty("LastUpdatedBy") String lastUpdatedBy,

                @JsonProperty("LastUpdateDate") OffsetDateTime lastUpdateDate,

                @JsonIgnore() String citizenshipsLink,

                @JsonIgnore() String emailsLink,

                @JsonIgnore() String legislativeinfoLink,

                @JsonIgnore() String namesLink,

                @JsonIgnore() String phonesLinks,

                @JsonIgnore() String religionsLinks,

                @JsonIgnore() String workrelationshipsLink,

                @JsonIgnore() String workersdffLink,

                // @JsonIgnore
                // List<Object> workersDFF,
                //
                // @JsonIgnore
                // List<Object> addresses,
                //
                // @JsonProperty("citizenships")
                // List<FusionWorkerCitizenships> citizenships,
                //
                // @JsonIgnore
                // List<Object> driverLicenses,
                //
                // @JsonProperty("emails")
                // List<FusionWorkerEmails> emails,
                //
                // @JsonIgnore
                // List<Object> ethnicities,
                //
                // @JsonIgnore
                // List<Object> externalIdentifiers,
                //
                // @JsonIgnore
                // List<Object> legislativeInfo,
                //
                // @JsonProperty("names")
                // List<FusionWorkerNames> names,
                //
                // @JsonIgnore
                // List<Object> nationalIdentifiers,
                //
                // @JsonIgnore
                // List<Object> otherCommunicationAccounts,
                //
                // @JsonIgnore
                // List<Object> passports,
                //
                // @JsonProperty("phones")
                // List<FusionWorkerPhones> phones,
                //
                // @JsonIgnore
                // List<Object> photos,
                //
                // @JsonProperty("religions")
                // List<FusionWorkerReligions> religions,
                //
                // @JsonIgnore
                // List<Object> visasPermits,
                //
                // @JsonProperty("workRelationships")
                // List<FusionWorkRelationships> workRelationships,
                //
                // @JsonIgnore
                // List<Object> disabilities,
                //
                // @JsonIgnore
                // List<Object> messages,

                List<LinksObject> links) {
}
