package com.mastercontrol.rc.bdd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Workflow {
    @JsonProperty("id")
    private String id;
    @JsonProperty("appId")
    private String appId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("revision")
    private Integer revision;
    @JsonProperty("status")
    private String status;
    @JsonProperty("type")
    private Object type;
    @JsonProperty("numberingPrefix")
    private String numberingPrefix;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("durationUnit")
    private String durationUnit;
    @JsonProperty("createdDate")
    private String createdDate;
    @JsonProperty("createdBy")
    private String createdBy;
    @JsonProperty("lastModifiedDate")
    private String lastModifiedDate;
    @JsonProperty("lastModifiedBy")
    private String lastModifiedBy;
}
