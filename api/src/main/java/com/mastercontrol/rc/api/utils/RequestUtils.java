package com.mastercontrol.rc.api.utils;

import com.github.javafaker.Faker;
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
    @Value("${token}")
    private String token;

    @Synchronized
    public static Faker getFaker() {
        if (faker == null) {
            faker = new Faker();
        }
        return faker;
    }


}
