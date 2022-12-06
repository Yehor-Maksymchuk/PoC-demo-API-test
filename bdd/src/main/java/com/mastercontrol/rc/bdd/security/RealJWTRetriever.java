package com.mastercontrol.rc.bdd.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastercontrol.rc.bdd.security.model.OktaProperties;
import com.mastercontrol.rc.bdd.security.model.OktaUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Purpose
 * Retrieve a bona fide real JWT from Okta.
 * <p>
 * Credit goes to Blaine Sorenson for the idea behind this code, he did the initial heavy lifting.
 */
@Slf4j
public class RealJWTRetriever implements JWTRetriever {
    private OktaProperties oktaProperties;
    private HttpClient httpClient;
    private HttpRequest request;
    private Map<String, String> jwtCache = new HashMap<>();


    public RealJWTRetriever(final String oktaConfigFile) {
        this.oktaProperties = getOktaProperties(oktaConfigFile);
        this.httpClient = HttpClient.newHttpClient();
    }

    public OktaProperties getOktaProperties(final String oktaConfigFile) {
        log.info("Calling getOktaProperties for filePath {}", oktaConfigFile);

        OktaProperties properties;
        try {
            ObjectMapper mapper = new ObjectMapper();
            File secrets = new File(oktaConfigFile);
            properties = mapper
                    .reader().forType(OktaProperties.class)
                    .readValue(secrets);
        } catch (Exception exception) {
            log.error("exception: " + exception);
            throw new RuntimeException("fatal error occurred.", exception);
        }
        log.info("Successfully loaded the OktaProperties.");
        return properties;
    }

    @Override
    public String getAccessToken(final String userName) throws Exception {
        log.info("getAccessToken userName={}", userName);
        String token = jwtCache.get(userName);
        if (token == null) {
            token = retrieveFromOktaAccessToken(userName);
            jwtCache.put(userName, token);
        }
        return token;
    }

    public String retrieveFromOktaAccessToken(final String userName) throws Exception {
        OktaUser user = getUser(userName);
        String stateToken = getStateToken();
        sendAuthenticationRequest(stateToken, user.getUsername(), user.getPassword());
        String authUriWithCode = sendRedirectRequestAndRetrieveCode(stateToken);
        String code = getCode(authUriWithCode);
        String token = sendTokenRequest(code);

        return token;
    }

    private OktaUser getUser(final String userName) {
        return oktaProperties.getOktaUsers().stream()
                .filter(user -> user.getUsername().equals(userName))
                .findFirst()
                .orElseThrow();
    }

    private String sendTokenRequest(final String code) throws Exception {
//        I changed from import org.apache.commons.collections.map.HashedMap to HashMap here.
        Map<String, String> map = new HashMap<>();
        map.put("client_id", oktaProperties.getOktaCommon().getClientId());
        map.put("redirect_uri", oktaProperties.getOktaCommon().getCallbackUrl());
        map.put("grant_type", "authorization_code");
        map.put("code_verifier", oktaProperties.getOktaCommon().getCodeVerifier());
        map.put("code", code);

        request = HttpRequest.newBuilder()
                .POST(buildFormDataFromMap(map))
                .uri(URI.create(oktaProperties.getOktaCommon().getDomain() + "/oauth2/default/v1/token"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("accept", "application/json")
                .build();

        HttpResponse<String> tokenResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> responseMap = objectMapper.readValue(tokenResponse.body(), Map.class);

        String token = responseMap.get("access_token");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("token was empty");
        }
        log.info("Successfully retrieved a Token from Okta");
        return token;
    }

    private String createAuthenticationRequestBody(final String stateToken, final String username, final String password) {
        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\",\"stateToken\":\"" + stateToken + "\"}";
        return requestBody;
    }

    private String getStateToken() throws URISyntaxException, IOException, InterruptedException {
        HttpRequest authorizeRequest = HttpRequest.newBuilder()
                .GET()
                .uri(new URIBuilder(URI.create(oktaProperties.getOktaCommon().getDomain() + "/oauth2/default/v1/authorize"))
                        .addParameter("client_id", oktaProperties.getOktaCommon().getClientId())
                        .addParameter("code_challenge", oktaProperties.getOktaCommon().getCodeChallenge())
                        .addParameter("code_challenge_method", oktaProperties.getOktaCommon().getCodeChallengeMethod())
                        .addParameter("nonce", "TEST")
                        .addParameter("redirect_uri", oktaProperties.getOktaCommon().getCallbackUrl())
                        .addParameter("response_type", "code")
                        .addParameter("state", "TEST")
                        .addParameter("scope", "groups")
                        .build()
                )
                .build();
        HttpResponse<String> authorizeResponse = httpClient.send(authorizeRequest, HttpResponse.BodyHandlers.ofString());
        log.info("Okta authorize request, response code = {}", authorizeResponse.statusCode());
        if (authorizeResponse.statusCode() != 200) {
            throw new RuntimeException("Okta Authorize request response status code=" + authorizeResponse.statusCode());
        }

        Pattern stateTokenPattern = Pattern.compile("stateToken = \'(.*)\'");
        Matcher m = stateTokenPattern.matcher(authorizeResponse.body());

        if (m.find()) {
            log.info("From the Okta authorize request response, the State Token was found.");
            String stateToken = m.group(1).replace("\\x2D", "-");
            return stateToken;
        }
        log.info("From the Okta authorize request response, the State Token was NOT found.");
        return null;
    }

    private String getCode(final String authUriWithCode) {
        Pattern stateTokenPattern = Pattern.compile("code=(.*)&");
        Matcher m = stateTokenPattern.matcher(authUriWithCode);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private void sendAuthenticationRequest(final String stateToken, final String username, final String password) throws IOException, InterruptedException {
        String requestBody = createAuthenticationRequestBody(stateToken, username, password);

        request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(oktaProperties.getOktaCommon().getDomain() + "/api/v1/authn"))
                .header("Content-Type", "application/json")
                .build();
        var authenticationResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Okta authentication request response status code = {}", authenticationResponse.statusCode());
        if (authenticationResponse.statusCode() != 200) {
            throw new RuntimeException("Okta Authentication request response status code=" + authenticationResponse.statusCode());
        }
    }

    private String sendRedirectRequestAndRetrieveCode(final String stateToken) throws IOException, InterruptedException, URISyntaxException {
        request = HttpRequest.newBuilder()
                .GET()
                .uri(new URIBuilder(URI.create(oktaProperties.getOktaCommon().getDomain() + "/login/step-up/redirect"))
                        .addParameter("stateToken", stateToken)
                        .build())
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpResponse<String> stepUpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Okta redirect, response code = {}", stepUpResponse.statusCode());
        if (stepUpResponse.statusCode() != 302) {
            log.error("Okta redirect failed, response code = {}", stepUpResponse.statusCode());
            throw new RuntimeException("Okta redirect request response status code=" + stepUpResponse.statusCode());
        }

        String authUriWithCode = stepUpResponse.headers().firstValue("location").get();
        log.info("The 'location' value returns the authUriWithCode.");

        return authUriWithCode;
    }

    private HttpRequest.BodyPublisher buildFormDataFromMap(final Map<String, String> data) {
        var builder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}


