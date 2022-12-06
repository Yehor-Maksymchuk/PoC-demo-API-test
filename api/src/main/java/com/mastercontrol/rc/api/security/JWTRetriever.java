package com.mastercontrol.rc.api.security;

import org.springframework.stereotype.Service;

public interface JWTRetriever {
    String getAccessToken(String userName) throws Exception;
}
