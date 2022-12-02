package com.mastercontrol.rc.api;

import com.mastercontrol.rc.api.dto.*;
import com.mastercontrol.rc.api.rest.WorkflowApi;
import com.mastercontrol.rc.api.utils.RequestDtoFabric;
import com.mastercontrol.rc.api.utils.RequestUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.restassured.RestAssured;
import io.restassured.internal.RequestSpecificationImpl;
import io.restassured.parsing.Parser;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("Atomic")
@Feature("Master Control Atomic API Test")
@Epic("CRUD workflow test")
@SpringBootTest
class WorkflowApiAtomicTest {

    @Autowired
    private WorkflowApi workflowApi;

    @Issue("JIRA-12312312")
    @Test
    @DisplayName("Can create new workflow")
    void testCanCreateWorkFlow() {
        RestAssured.defaultParser = Parser.JSON;
        var workflowResponse = workflowApi.createWorkflow(AddWorkflowRequest.builder()
                .withAppId(RequestUtils.getFaker().name().name())
                .withName(RequestUtils.getFaker().funnyName().name())
                .withDescription(RequestUtils.getFaker().animal().name())
                .build());
        var workflow = workflowResponse.as(AddWorkflowResponse.class).getWorkflow();
        Assertions.assertThat(workflow).isInstanceOf(Workflow.class)
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("appId")
                .hasFieldOrProperty("name")
                .hasFieldOrProperty("description")
                .hasFieldOrProperty("revision")
                .hasFieldOrProperty("status")
                .hasFieldOrProperty("type")
                .hasFieldOrProperty("numberingPrefix")
                .hasFieldOrProperty("duration")
                .hasFieldOrProperty("durationUnit")
                .hasFieldOrProperty("createdDate")
                .hasFieldOrProperty("createdBy")
                .hasFieldOrProperty("lastModifiedDate")
                .hasFieldOrProperty("lastModifiedBy");
    }

    @Test
    @DisplayName("Car get workflow by id")
    public void testCanGetWorkflowById() {
        var request = RequestDtoFabric.getRandomAddWorkflowRequest();

        var workflowId = workflowApi
                .createWorkflow(request)
                .as(AddWorkflowResponse.class)
                .getWorkflow()
                .getId();

        var workflow = workflowApi.getWorkflow(workflowId).as(GetWorkflowResponse.class).getWorkflow();

        Assertions.assertThat(workflow).isInstanceOf(Workflow.class)
                .isNotNull()
                .hasFieldOrPropertyWithValue("appId", request.getAppId())
                .hasFieldOrPropertyWithValue("name", request.getName())
                .hasFieldOrPropertyWithValue("description", request.getDescription());
    }

    @Test
    @DisplayName("Can update workflow by id")
    public void testCanUpdateWorkflow() {
        var request = RequestDtoFabric.getRandomAddWorkflowRequest();

        var workflowId = workflowApi
                .createWorkflow(request)
                .as(AddWorkflowResponse.class)
                .getWorkflow()
                .getId();

        var updatedRequest = UpdateWorkflowRequest.builder()
                .withName("MY_NEW_NAME")
                .withDescription("MY_NEW_DESCRIPTION")
                .build();

        var updatedWorkFlow = workflowApi
                .updateWorkflow(workflowId, updatedRequest).as(AddWorkflowResponse.class).getWorkflow();

        Assertions.assertThat(updatedWorkFlow).isNotNull();
        Assertions.assertThat(updatedWorkFlow)
                .hasFieldOrPropertyWithValue("name", updatedRequest.getName());

    }

}
