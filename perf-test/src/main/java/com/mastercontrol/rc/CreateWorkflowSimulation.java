package com.mastercontrol.rc;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.mastercontrol.rc.security.JWTRetriever;
import com.mastercontrol.rc.security.RealJWTRetriever;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class CreateWorkflowSimulation extends Simulation {

    public class AddWorkflowRequest {
        @JsonProperty("appId")
        private String appId;
        @JsonProperty("name")
        private String name;
        @JsonProperty("description")
        private String description;

        public AddWorkflowRequest(String appId, String name, String description) {
            this.appId = appId;
            this.name = name;
            this.description = description;
        }
    }

    ScenarioBuilder createWorkflow;

    @Synchronized
    public static String getToken() {
        JWTRetriever jwtRetriever = new RealJWTRetriever("/Users/ymaksymchuk/source/mc/poc-demo-api/demo/perf-test/src/main/resources/client-secret.json");
        try {
            return jwtRetriever.getAccessToken("kyle@mcresearchlabs.net");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    {

        AddWorkflowRequest request = new AddWorkflowRequest(
                "Gatling Test Name",
                "Gatling Test Description",
                "Gatling Test appId");



        HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("http://localhost:10007");

        createWorkflow = scenario("Create workflow")
                .exec(http("Create workflow")
                        .post("/pcs/workflow/workflow/v1")
                        .headers(requestHeaders())
                        .body(CoreDsl.StringBody("""
                                {
                                  "appId": "#{randomUuid()}",
                                  "name": "#{randomUuid()}",
                                  "description": "#{randomUuid()}"
                                }
                                """)).asJson()
                        .check(status().is(201))
                        .check(substring("workflow").find(1).exists())
                );
        setUp(createWorkflow
                .injectOpen(OpenInjectionStep.atOnceUsers(10))
                .protocols(httpProtocolBuilder));


    }


}
