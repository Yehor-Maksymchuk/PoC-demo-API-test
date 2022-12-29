package com.mastercontrol.rc.api;

import com.mastercontrol.rc.api.dto.*;
import com.mastercontrol.rc.api.rest.WorkflowApi;
import com.mastercontrol.rc.api.utils.RequestUtils;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Tag("e2e")
@Feature("Master Control e2e API Test")
@Epic("CRUD workflow test e2e")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class WorkflowApiE2eTest {
    @Autowired
    private WorkflowApi workflowApi;

    private String workflowId = null;

    @BeforeAll
    public static void beforeE2e2Tests() {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Order(1)
    @Test
    @DisplayName("Can create new workflow")
    void testCanCreateWorkFlow() {

        var workflowResponse = workflowApi.createWorkflow(AddWorkflowRequest.builder()
                .withAppId(RequestUtils.getFaker().name().name())
                .withName(RequestUtils.getFaker().funnyName().name())
                .withDescription(RequestUtils.getFaker().animal().name())
                .build());
        var workflow = workflowResponse.as(AddWorkflowResponse.class).getWorkflow();

        this.workflowId = workflow.getId();
        System.out.println(workflowId);
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

    @Order(2)
    @Test
    @DisplayName("Car get workflow by id")
    public void testCanGetWorkflowById() {

        System.out.println(workflowId);
        var workflow = workflowApi.getWorkflow(workflowId).as(GetWorkflowResponse.class).getWorkflow();

        Assertions.assertThat(workflow).isInstanceOf(Workflow.class)
                .isNotNull()
                .hasFieldOrPropertyWithValue("appId", workflow.getAppId())
                .hasFieldOrPropertyWithValue("name", workflow.getName())
                .hasFieldOrPropertyWithValue("description", workflow.getDescription());
    }

    @Order(3)
    @Test
    @DisplayName("Can update workflow by id")
    public void testCanUpdateWorkflow() {

        var updatedRequest = UpdateWorkflowRequest.builder()
                .withName("MY_NEW_NAME")
                .withDescription("MY_NEW_DESCRIPTION")
                .build();

        var updatedWorkFlow = workflowApi
                .updateWorkflow(workflowId, updatedRequest)
                .body()
                .as(AddWorkflowResponse.class)
                .getWorkflow();

        Assertions.assertThat(updatedWorkFlow).isNotNull();
        Assertions.assertThat(updatedWorkFlow)
                .hasFieldOrPropertyWithValue("name", updatedRequest.getName());
    }
}
