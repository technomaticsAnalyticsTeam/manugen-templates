package ru.leyman.manugen.templates.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * Simple token authentication filter for development/internal use.
 * Validates tokens created by SimpleAuthController.
 */
@Component
public class SimpleAuthTokenFilter extends OncePerRequestFilter {

    @Value("${app.simple-auth.enabled:false}")
    private boolean simpleAuthEnabled;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        if (!simpleAuthEnabled) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            // Accept simple tokens (format: "simple-token-{id}")
            if (token.startsWith("simple-token-")) {
                String userId = token.substring("simple-token-".length());
                
                // Create simple authentication (no validation for dev mode)
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        "user-" + userId,
                        null,
                        Collections.emptyList()
                    );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        filterChain.doFilter(request, response);
    }

}
