package com.medilabo.gateway_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/** Sets the access rules for gateway routes. */
@Configuration
public class SecurityConfig {

        /** Protects backend routes with JWT authentication.
         * @param http Spring security configuration
         * @return configured security filter chain
         * @throws Exception if the security configuration cannot be built
         */
        @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                // Protect backend routes with JWT authentication.
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/patients/**",
                                "/notes/**",
                                "/risk/**"
                        ).authenticated()
                        .anyRequest().permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                }));

        return http.build();
    }
}