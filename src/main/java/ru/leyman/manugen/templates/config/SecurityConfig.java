package ru.leyman.manugen.templates.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OpaqueTokenIntrospector opaqueTokenIntrospector;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/yandex/callback").permitAll()
                        .anyRequest().authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 ->
                        oauth2.opaqueToken(opaqueToken ->
                                opaqueToken.introspector(opaqueTokenIntrospector)))
                .build();
    }

    private CorsConfigurationSource corsConfigurationSource() {
        var corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(List.of("http://localhost:3000"));
        corsConfig.setAllowedMethods(List.of(HttpMethod.GET.name(), HttpMethod.POST.name(),
                HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name()));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept",
                "Origin", "X-Requested-With"));
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);
        var corsConfigSource = new UrlBasedCorsConfigurationSource();
        corsConfigSource.registerCorsConfiguration("/**", corsConfig);
        return corsConfigSource;
    }

}
