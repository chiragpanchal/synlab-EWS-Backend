package com.ewsv3.ews.schedulers.dto.fusionDto.worker.names;

import com.ewsv3.ews.schedulers.dto.fusionDto.commons.LinksObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

public record FusionWorkerNames(

                @JsonIgnore long personId,

                @JsonProperty("PersonNameId") long personNameId,
                @JsonProperty("EffectiveStartDate") LocalDate effectiveStartDate,

                @JsonProperty("EffectiveEndDate") LocalDate effectiveEndDate,

                @JsonProperty("LegislationCode") String legislationCode,

                @JsonProperty("LastName") String lastName,

                @JsonProperty("FirstName") String firstName,

                @JsonProperty("Title") String title,

                @JsonProperty("PreNameAdjunct") String preNameAdjunct,

                @JsonProperty("Suffix") String suffix,

                @JsonProperty("MiddleNames") String middleNames,

                @JsonProperty("Honors") String honors,

                @JsonProperty("KnownAs") String knownAs,

                @JsonProperty("PreviousLastName") String previousLastName,

                @JsonProperty("DisplayName") String displayName,

                @JsonProperty("OrderName") String orderName,

                @JsonProperty("ListName") String listName,

                @JsonProperty("FullName") String fullName,

                @JsonProperty("MilitaryRank") String militaryRank,

                @JsonProperty("NameLanguage") String nameLanguage,

                @JsonProperty("NameInformation1") String nameInformation1,

                @JsonProperty("NameInformation2") String nameInformation2,

                @JsonProperty("NameInformation3") String nameInformation3,

                @JsonProperty("NameInformation4") String nameInformation4,

                @JsonProperty("NameInformation5") String nameInformation5,

                @JsonProperty("NameInformation6") String nameInformation6,

                @JsonProperty("NameInformation7") String nameInformation7,

                @JsonProperty("NameInformation8") String nameInformation8,

                @JsonProperty("NameInformation9") String nameInformation9,

                @JsonProperty("NameInformation10") String nameInformation10,

                @JsonProperty("NameInformation11") String nameInformation11,

                @JsonProperty("NameInformation12") String nameInformation12,

                @JsonProperty("NameInformation13") String nameInformation13,

                @JsonProperty("NameInformation14") String nameInformation14,

                @JsonProperty("NameInformation15") String nameInformation15,

                @JsonProperty("NameInformation16") String nameInformation16,

                @JsonProperty("NameInformation17") String nameInformation17,

                @JsonProperty("NameInformation18") String nameInformation18,

                @JsonProperty("NameInformation19") String nameInformation19,

                @JsonProperty("NameInformation20") String nameInformation20,

                @JsonProperty("NameInformation21") String nameInformation21,

                @JsonProperty("NameInformation22") String nameInformation22,

                @JsonProperty("NameInformation23") String nameInformation23,

                @JsonProperty("NameInformation24") String nameInformation24,

                @JsonProperty("NameInformation25") String nameInformation25,

                @JsonProperty("NameInformation26") String nameInformation26,

                @JsonProperty("NameInformation27") String nameInformation27,

                @JsonProperty("NameInformation28") String nameInformation28,

                @JsonProperty("NameInformation29") String nameInformation29,

                @JsonProperty("NameInformation30") String nameInformation30,

                @JsonIgnore() @JsonProperty("CreatedBy") String createdBy,

                @JsonProperty("CreationDate") OffsetDateTime creationDate,

                @JsonIgnore() @JsonProperty("LastUpdatedBy") String lastUpdatedBy,

                @JsonProperty("LastUpdateDate") OffsetDateTime lastUpdateDate,

                @JsonProperty("LocalPersonNameId") long localPersonNameId,

                @JsonProperty("LocalEffectiveStartDate") LocalDate localEffectiveStartDate,

                @JsonProperty("LocalEffectiveEndDate") LocalDate localEffectiveEndDate,

                @JsonProperty("LocalLegislationCode") String localLegislationCode,

                @JsonProperty("LocalLastName") String localLastName,

                @JsonProperty("LocalFirstName") String localFirstName,

                @JsonProperty("LocalTitle") String localTitle,

                @JsonProperty("LocalPreNameAdjunct") String localPreNameAdjunct,

                @JsonProperty("LocalSuffix") String localSuffix,

                @JsonProperty("LocalMiddleNames") String localMiddleNames,

                @JsonProperty("LocalHonors") String localHonors,

                @JsonProperty("LocalKnownAs") String localKnownAs,

                @JsonProperty("LocalPreviousLastName") String localPreviousLastName,

                @JsonProperty("LocalDisplayName") String localDisplayName,

                @JsonProperty("LocalOrderName") String localOrderName,

                @JsonProperty("LocalListName") String localListName,

                @JsonProperty("LocalFullName") String localFullName,

                @JsonProperty("LocalMilitaryRank") String localMilitaryRank,

                @JsonProperty("LocalNameLanguage") String localNameLanguage,

                @JsonProperty("LocalNameInformation1") String localNameInformation1,

                @JsonProperty("LocalNameInformation2") String localNameInformation2,

                @JsonProperty("LocalNameInformation3") String localNameInformation3,

                @JsonProperty("LocalNameInformation4") String localNameInformation4,

                @JsonProperty("LocalNameInformation5") String localNameInformation5,

                @JsonProperty("LocalNameInformation6") String localNameInformation6,

                @JsonProperty("LocalNameInformation7") String localNameInformation7,

                @JsonProperty("LocalNameInformation8") String localNameInformation8,

                @JsonProperty("LocalNameInformation9") String localNameInformation9,

                @JsonProperty("LocalNameInformation10") String localNameInformation10,

                @JsonProperty("LocalNameInformation11") String localNameInformation11,

                @JsonProperty("LocalNameInformation12") String localNameInformation12,

                @JsonProperty("LocalNameInformation13") String localNameInformation13,

                @JsonProperty("LocalNameInformation14") String localNameInformation14,

                @JsonProperty("LocalNameInformation15") String localNameInformation15,

                @JsonProperty("LocalNameInformation16") String localNameInformation16,

                @JsonProperty("LocalNameInformation17") String localNameInformation17,

                @JsonProperty("LocalNameInformation18") String localNameInformation18,

                @JsonProperty("LocalNameInformation19") String localNameInformation19,

                @JsonProperty("LocalNameInformation20") String localNameInformation20,

                @JsonProperty("LocalNameInformation21") String localNameInformation21,

                @JsonProperty("LocalNameInformation22") String localNameInformation22,

                @JsonProperty("LocalNameInformation23") String localNameInformation23,

                @JsonProperty("LocalNameInformation24") String localNameInformation24,

                @JsonProperty("LocalNameInformation25") String localNameInformation25,

                @JsonProperty("LocalNameInformation26") String localNameInformation26,

                @JsonProperty("LocalNameInformation27") String localNameInformation27,

                @JsonProperty("LocalNameInformation28") String localNameInformation28,

                @JsonProperty("LocalNameInformation29") String localNameInformation29,

                @JsonProperty("LocalNameInformation30") String localNameInformation30,

                @JsonIgnore() @JsonProperty("LocalCreatedBy") String localCreatedBy,

                @JsonProperty("LocalCreationDate") OffsetDateTime localCreationDate,

                @JsonIgnore() @JsonProperty("LocalLastUpdatedBy") String localLastUpdatedBy,

                @JsonProperty("LocalLastUpdateDate") OffsetDateTime localLastUpdateDate,

                @JsonIgnore List<LinksObject> links) {
}
