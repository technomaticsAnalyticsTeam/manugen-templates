package ru.leyman.manugen.templates.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.leyman.manugen.templates.service.OAuthService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final OAuthService authService;

    @GetMapping("yandex/callback")
    public String exchangeForAccessToken(@RequestParam String authCode) {
        return authService.exchangeForAccessToken(authCode);
    }

}
