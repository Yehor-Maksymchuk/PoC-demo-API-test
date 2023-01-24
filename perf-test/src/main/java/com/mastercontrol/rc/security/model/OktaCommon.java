package com.mastercontrol.rc.security.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OktaCommon {
    private String domain;
    private String clientId;
    private String codeChallenge;
    private String codeChallengeMethod;
    private String codeVerifier;
    private String callbackUrl;
}
