package com.mastercontrol.rc.api.utils;

import com.github.javafaker.Faker;
import com.mastercontrol.rc.api.security.JWTRetriever;
import com.mastercontrol.rc.api.security.RealJWTRetriever;
import lombok.Synchronized;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
@PropertySource("classpath:application.properties")
public class RequestUtils {
    private static Faker faker;

    @Synchronized
    public static Faker getFaker() {
        if (faker == null) {
            faker = new Faker();
        }
        return faker;
    }

    @Synchronized
    public static String getToken() {
        JWTRetriever jwtRetriever = new RealJWTRetriever("src/main/resources/client-secret.json");
        try {
            return jwtRetriever.getAccessToken("kyle@mcresearchlabs.net");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
