package com.mastercontrol.rc.api.rest;

import com.mastercontrol.rc.api.dto.AddWorkflowRequest;
import com.mastercontrol.rc.api.dto.UpdateWorkflowRequest;

import com.mastercontrol.rc.api.security.JWTRetriever;
import com.mastercontrol.rc.api.security.RealJWTRetriever;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.mastercontrol.rc.api.utils.RequestUtils.getToken;

@Component
public class WorkflowApi {

    private final String filePath = "C:/Users/jzpeterson/core-platform/PoC-demo-API-test/client-secrets.json";
    private JWTRetriever jwtRetriever;
    private String token;

    public WorkflowApi() throws Exception {
        jwtRetriever = new RealJWTRetriever(filePath);
        token = jwtRetriever.getAccessToken("kyle@mcresearchlabs.net");
    }

    @Step("Create workflow by endpoint :/pcs/workflow/workflow/v1")
    public Response createWorkflow(AddWorkflowRequest requestBody) {
        return RestAssured.given()
                .filter(new AllureRestAssured())
                .headers(requestHeaders())
                .body(requestBody)
                .post("http://localhost:10007/pcs/workflow/workflow/v1");
    }

    @Step("Update workflow by endpoint :/pcs/workflow/workflow/v1/{0}")
    public Response updateWorkflow(String workflowId, UpdateWorkflowRequest requestBody) {
        return RestAssured.given()
                .filter(new AllureRestAssured())
                .headers(requestHeaders())
                .body(requestBody)
                .patch("http://localhost:10007/pcs/workflow/workflow/v1/" + workflowId);
    }

    @Step("Find workflow by endpoint :/pcs/workflow/workflow/v1/{0}")
    public Response getWorkflow(String workflowId) {
        return RestAssured.given()
                .filter(new AllureRestAssured())
                .headers(requestHeaders())
                .get("http://localhost:10007/pcs/workflow/workflow/v1/" + workflowId);
    }

    public synchronized Map<String, String> requestHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("accept", "application/json");
        headers.put("User-Agent", "PostmanRuntime/7.29.2");
        headers.put("Host", "localhost:10007");
        headers.put("Authorization", "Bearer " + getToken());
        return headers;
    }

    public static void main(String[] args) {
        System.out.println(getToken());
    }
}
