package com.mastercontrol.rc;

import com.mastercontrol.rc.security.JWTRetriever;
import com.mastercontrol.rc.security.RealJWTRetriever;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import lombok.Synchronized;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class GetWorkflowSimulation extends Simulation {
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

        ScenarioBuilder getWorkflow = scenario("Create workflow")
                .exec(http("Create workflow")
                        .get("/pcs/workflow/workflows/v1?appId=aqem")
                        .check(status().is(200))
                        .check(substring("workflow").exists())
                );
        setUp(getWorkflow.injectOpen(
                nothingFor(Duration.ofSeconds(4)),
                atOnceUsers(1)
                )
                .protocols(httpProtocolBuilder));
    }
}

