package com.mastercontrol.rc.bdd.definitions;

import com.mastercontrol.rc.bdd.CucumberRunner;
import com.mastercontrol.rc.bdd.dto.AddWorkflowRequest;
import com.mastercontrol.rc.bdd.dto.AddWorkflowResponse;
import com.mastercontrol.rc.bdd.dto.Workflow;
import com.mastercontrol.rc.bdd.rest.WorkflowApi;
import com.mastercontrol.rc.bdd.utils.RequestUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

@CucumberContextConfiguration
public class WorkflowDefinition extends CucumberRunner {

    private Response workflowResponse = null;
    private AddWorkflowRequest createWorkflow = null;

    @Given("Prepare create workflow request")
    public void createTestDataForCreateWorkflowRequest() {
        createWorkflow = AddWorkflowRequest.builder()
                .withAppId(RequestUtils.getFaker().name().name())
                .withName(RequestUtils.getFaker().funnyName().name())
                .withDescription(RequestUtils.getFaker().animal().name())
                .build();
    }

    @When("Call create workflow endpoint")
    public void sendCreateWorkflowRequest() {
        workflowResponse = workflowApi.createWorkflow(createWorkflow);
    }

    @Then("In response we have status code {int}")
    public void verifyStatusCode(int statusCode) {
        Assertions.assertThat(workflowResponse.statusCode()).isEqualTo(statusCode);
    }

    @And("Full set of fields")
    public void verifyAllFieldsOfCreateWorkflowRequests() {
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
                .hasFieldOrProperty("lastModifiedBy")
                .hasFieldOrPropertyWithValue("appId", workflow.getAppId())
                .hasFieldOrPropertyWithValue("name", workflow.getName())
                .hasFieldOrPropertyWithValue("description", workflow.getDescription());
    }
}
