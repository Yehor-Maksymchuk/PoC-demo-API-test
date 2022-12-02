package com.mastercontrol.rc.api.security;

public interface JWTRetriever {
    String getAccessToken(String userName) throws Exception;
}
