package com.avazhenin.demo.controller;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.apache.http.client.config.RequestConfig;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/token")
public class Token {

    @Value("${keycloak.auth-server-url}")
    private String authServerURL;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;

    @PostMapping("/get")
    public ResponseEntity<?> login(@RequestBody Map<String, String> user) {
        try {
            var form = List.of(
                new NameValuePairImpl("client_id", clientId),
//                new NameValuePairImpl("client_secret", "YOUR_CLIENT_SECRET"),
                new NameValuePairImpl("grant_type", "password"),
                new NameValuePairImpl("username", user.get("username")),
                new NameValuePairImpl("password", user.get("password"))
            );

            var requestConfig = RequestConfig.custom().build();
            var httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            var request = new HttpPost(String.format("%s/realms/%s/protocol/openid-connect/token", authServerURL, clientId));
            request.setEntity(new UrlEncodedFormEntity(form));
            JSONObject json = new JSONObject(EntityUtils.toString(httpClient.execute(request).getEntity()));

            return ResponseEntity.ok(Map.of("token", json.get("access_token")));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.toString());
        }
    }
}

class NameValuePairImpl implements NameValuePair {
    private final String name;
    private final String value;

    public NameValuePairImpl(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }
}
