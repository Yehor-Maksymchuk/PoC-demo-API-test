package com.mastercontrol.rc.bdd.utils;

import com.mastercontrol.rc.bdd.dto.AddWorkflowRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestDtoFabric {

    public static AddWorkflowRequest getRandomAddWorkflowRequest() {
        return AddWorkflowRequest.builder()
                .withAppId(RequestUtils.getFaker().name().firstName())
                .withName(RequestUtils.getFaker().name().lastName())
                .withDescription(RequestUtils.getFaker().app().name())
                .build();
    }
}
