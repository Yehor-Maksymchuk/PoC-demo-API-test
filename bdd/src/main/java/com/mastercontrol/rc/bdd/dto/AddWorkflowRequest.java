package com.mastercontrol.rc.bdd.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class AddWorkflowRequest {
    @JsonProperty("appId")
    private String appId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
}
