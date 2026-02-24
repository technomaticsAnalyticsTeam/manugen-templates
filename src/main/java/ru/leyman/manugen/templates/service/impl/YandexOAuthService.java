package ru.leyman.manugen.templates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.leyman.manugen.templates.service.OAuthService;

import java.util.Collections;

@Log4j2
@Service
@RequiredArgsConstructor
public class YandexOAuthService implements OAuthService {

    private final RestTemplate yandexExchangeClient;

    @Value("${spring.security.oauth2.client.exchange.yandex.exchange-url}")
    private String exchangeUrl;

    @Value("${spring.security.oauth2.client.exchange.yandex.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.exchange.yandex.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.exchange.yandex.redirect-uri}")
    private String redirectUri;

    @Override
    public String exchangeForAccessToken(String authCode) {
        var body = body(authCode);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        var request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = yandexExchangeClient.exchange(
                exchangeUrl, HttpMethod.POST, request, String.class);
        return response.getBody();
    }

    private MultiValueMap<String, String> body(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", authCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        return params;
    }

}
