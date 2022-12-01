package com.mastercontrol.rc.bdd.definitions;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.DisplayName;

public class CucumberHooks {

    @Before
    @DisplayName("Test configuration")
    public void setup() {
        RestAssured.defaultParser = Parser.JSON;
    }
}
