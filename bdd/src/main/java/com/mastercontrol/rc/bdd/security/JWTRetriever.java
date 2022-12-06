package com.mastercontrol.rc.bdd.security;

public interface JWTRetriever {
    String getAccessToken(String userName) throws Exception;
}
