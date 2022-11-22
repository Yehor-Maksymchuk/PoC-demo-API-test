package com.mastercontrol.rc.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class UpdateWorkflowRequest {
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("type")
    private String type;
    @JsonProperty("numberingPrefix")
    private String numberingPrefix;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("durationUnit")
    private String durationUnit;
}
