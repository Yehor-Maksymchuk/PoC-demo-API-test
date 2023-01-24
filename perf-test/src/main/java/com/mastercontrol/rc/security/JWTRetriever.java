package com.mastercontrol.rc.security;

public interface JWTRetriever {
    String getAccessToken(String userName) throws Exception;
}
