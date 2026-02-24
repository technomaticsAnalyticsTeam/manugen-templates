package ru.leyman.manugen.templates.service;

public interface OAuthService {

    String exchangeForAccessToken(String authCode);

}
