package ru.leyman.manugen.templates;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;

import java.util.Optional;

@UtilityClass
public class UserContext {

    public static Long getUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(BearerTokenAuthentication.class::cast)
                .map(BearerTokenAuthentication::getTokenAttributes)
                .map(attr -> attr.get("id"))
                .map(Long.class::cast)
                .orElseThrow(() -> new RuntimeException("Can't getUserId from security context"));
    }

}
