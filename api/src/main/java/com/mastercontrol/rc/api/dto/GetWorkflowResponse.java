package com.mastercontrol.rc.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
public class GetWorkflowResponse {
    @JsonProperty("workflow")
    private Workflow workflow;
    @JsonProperty("nodes")
    private List<Nodes> nodes;
    @JsonProperty("nodeLayout")
    private NodeLayout nodeLayout;
}
