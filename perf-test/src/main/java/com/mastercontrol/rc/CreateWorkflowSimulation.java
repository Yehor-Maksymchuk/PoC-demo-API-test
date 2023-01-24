package com.mastercontrol.rc;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.mastercontrol.rc.security.JWTRetriever;
import com.mastercontrol.rc.security.RealJWTRetriever;
import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import lombok.*;

import java.time.Duration;
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

        @Override
        public String toString() {
            return "{" +
                    "\"appId\":\"" + appId + '\"' +
                    ", \"name\":\"" + name + '\"' +
                    ", \"description\":\"" + description + '\"' +
                    '}';
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
        headers.put("Host", "us-west-2.svc.mastercontrol.technology");
        headers.put("Authorization", "Bearer " + getToken());
        return headers;
    }

    {
        HttpProtocolBuilder httpProtocolBuilder = http.baseUrl("https://us-west-2.svc.mastercontrol.technology")
                .headers(requestHeaders());

        createWorkflow = scenario("Create workflow")
                .exec(http("Create workflow")
                        .post("/pcs/workflow/workflow/v1")
                        .body(CoreDsl.StringBody("""
                                {
                                  "appId": "#{randomUuid()}",
                                  "name": "#{randomUuid()}",
                                  "description": "#{randomUuid()}"
                                }
                                """)).asJson()
                        .check(status().is(201))
                        .check(substring("workflow").exists())
                        .check(substring("id").exists())
                        .check(substring("appId").exists())
                        .check(substring("name").exists())
                        .check(substring("description").exists())
                        .check(substring("revision").exists())
                        .check(substring("status").exists())
                        .check(substring("type").exists())
                        .check(substring("numberingPrefix").exists())
                        .check(substring("duration").exists())
                        .check(substring("durationUnit").exists())
                        .check(substring("durationInBusinessDays").exists())
                        .check(substring("createdDate").exists())
                        .check(substring("createdBy").exists())
                        .check(substring("lastModifiedDate").exists())
                        .check(substring("lastModifiedBy").exists())
                );
        setUp(createWorkflow.injectOpen(
                        nothingFor(Duration.ofSeconds(4)),
                        atOnceUsers(10),
                        rampUsersPerSec(2).to(10).during(Duration.ofSeconds(3)))
                .protocols(httpProtocolBuilder));
    }
}
