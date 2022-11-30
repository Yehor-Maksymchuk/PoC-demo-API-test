package com.mastercontrol.rc.bdd;

import com.mastercontrol.rc.bdd.rest.WorkflowApi;
import org.junit.platform.suite.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static io.cucumber.junit.platform.engine.Constants.*;

@Suite()
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameters(value = {
        @ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.mastercontrol.rc.bdd.definitions"),
        @ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "src/test/resources/features"),
        @ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm,progress,summary")
}
)
@SpringBootTest
public class CucumberRunner {
    @Autowired
    protected WorkflowApi workflowApi;
    @Value("${token}")
    protected String token;
}
