package ru.leyman.manugen.templates.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.*;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionException;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;
import ru.leyman.manugen.templates.domain.entity.User;
import ru.leyman.manugen.templates.repo.UserRepo;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
public class YandexTokenIntrospector implements OpaqueTokenIntrospector {
    private static final ParameterizedTypeReference<Map<String, Object>> STRING_OBJECT_MAP_TYPE =
            new ParameterizedTypeReference<>() {};
    private static final String EMAIL_FIELD = "default_email";
    private static final String LOGIN_FIELD = "login";
    private static final String NAME_FIELD = "real_name";

    private final Converter<String, RequestEntity<?>> requestEntityConverter;
    private final RestOperations restOperations;
    private final UserRepo userRepo;

    public YandexTokenIntrospector(@Value("${spring.security.oauth2.resourceserver.opaquetoken.introspection-uri}") String yandexIntrospectionUri,
                                   RestOperations restOperations, UserRepo userRepo) {
        Assert.notNull(yandexIntrospectionUri, "introspectionUri cannot be null");
        Assert.notNull(restOperations, "restOperations cannot be null");
        this.requestEntityConverter = this.defaultRequestEntityConverter(URI.create(yandexIntrospectionUri));
        this.restOperations = restOperations;
        this.userRepo = userRepo;
    }

    private Converter<String, RequestEntity<?>> defaultRequestEntityConverter(URI introspectionUri) {
        return (token) -> {
            HttpHeaders headers = this.requestHeaders(token);
            return new RequestEntity<>(headers, HttpMethod.GET, introspectionUri);
        };
    }

    private HttpHeaders requestHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);
        return headers;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        RequestEntity<?> requestEntity = this.requestEntityConverter.convert(token);
        if (requestEntity == null) {
            throw new OAuth2IntrospectionException("requestEntityConverter returned a null entity");
        } else {
            ResponseEntity<Map<String, Object>> responseEntity =
                    this.restOperations.exchange(requestEntity, STRING_OBJECT_MAP_TYPE);
            if (responseEntity.getStatusCode() != HttpStatus.OK) {
                throw new OAuth2IntrospectionException("Introspection endpoint responded with "
                        + responseEntity.getStatusCode());
            }
            Map<String, Object> claims = Objects.requireNonNullElse(responseEntity.getBody(), Collections.emptyMap());
            var email = Optional.ofNullable(claims.get(EMAIL_FIELD))
                    .map(String.class::cast)
                    .orElseThrow(() -> new OAuth2IntrospectionException(
                            "Introspection endpoint responded without " + EMAIL_FIELD));
            var userId = userRepo.findByEmail(email)
                    .or(() -> Optional.of(userRepo.save(mapToUser(claims))))
                    .map(User::getId)
                    .orElseThrow(() -> new OAuth2IntrospectionException("Can't find user by email " + email));
            claims.put("id", userId);
            return new OAuth2IntrospectionAuthenticatedPrincipal(claims, Collections.emptyList());
        }
    }

    private User mapToUser(Map<String, Object> claims) {
        var login = Optional.ofNullable(claims.get(LOGIN_FIELD))
                .map(String.class::cast)
                .filter(StringUtils::hasText)
                .orElseThrow(() -> new OAuth2IntrospectionException(
                        "Introspection endpoint responded without " + LOGIN_FIELD));
        var name = Optional.ofNullable(claims.get(NAME_FIELD))
                .map(String.class::cast)
                .filter(StringUtils::hasText)
                .orElseThrow(() -> new OAuth2IntrospectionException(
                        "Introspection endpoint responded without " + NAME_FIELD));
        var email = Optional.ofNullable(claims.get(EMAIL_FIELD))
                .map(String.class::cast)
                .filter(StringUtils::hasText)
                .orElseThrow(() -> new OAuth2IntrospectionException(
                        "Introspection endpoint responded without " + EMAIL_FIELD));
        return new User(login, name, email);
    }

}
