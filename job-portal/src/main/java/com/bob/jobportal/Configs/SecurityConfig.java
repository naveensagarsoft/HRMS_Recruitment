package com.bob.jobportal.Configs;

import com.bob.jobportal.util.CookieBearerTokenResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CookieBearerTokenResolver tokenResolver;

    public SecurityConfig(CookieBearerTokenResolver tokenResolver) {
        this.tokenResolver = tokenResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {}) // Enable CORS with default settings
            .csrf(csrf -> csrf.disable()) // Disable CSRF
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 ->
                oauth2.bearerTokenResolver(tokenResolver)
                      .jwt(jwt -> jwt.decoder(jwtDecoder()))
            );

        return http.build();
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        String jwkSetUri = "https://dev-0rb6h2oznbwkonhz.us.auth0.com/.well-known/jwks.json";
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }
}
