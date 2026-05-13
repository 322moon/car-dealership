package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/cars/**", "/api/models/**", "/api/details/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/cars/**", "/api/details/**").hasAnyRole("WAREHOUSE_ADMIN", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/cars/**", "/api/details/**").hasAnyRole("WAREHOUSE_ADMIN", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/cars/**", "/api/details/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/models/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/models/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/models/**").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/orders/in-stock", "/api/orders/configured").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/orders/*/*/cancel").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/orders/in-stock/*/ready-for-pickup",
                                "/api/orders/configured/*/approve-by-warehouse",
                                "/api/orders/configured/*/ready-for-pickup"
                        ).hasAnyRole("WAREHOUSE_ADMIN", "ADMIN")
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/orders/in-stock/*/approve-by-manager",
                                "/api/orders/in-stock/*/awaiting-payment",
                                "/api/orders/in-stock/*/paid",
                                "/api/orders/in-stock/*/complete",
                                "/api/orders/configured/*/awaiting-payment",
                                "/api/orders/configured/*/paid",
                                "/api/orders/configured/*/awaiting-delivery",
                                "/api/orders/configured/*/complete"
                        ).hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/test-drives/cars/*/availability").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/test-drives/cars/*/availability").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/test-drives").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/test-drives/*/confirm").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/test-drives/**").hasAnyRole("MANAGER", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/test-drives/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(keycloakJwtAuthenticationConverter()))
                );
        return http.build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> keycloakJwtAuthenticationConverter() {
        return jwt -> new JwtAuthenticationToken(jwt, extractRealmRoles(jwt), jwt.getSubject());
    }

    private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) {
            return List.of();
        }

        Object roles = realmAccess.get("roles");
        if (!(roles instanceof Collection<?> roleNames)) {
            return List.of();
        }

        return roleNames.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .map(GrantedAuthority.class::cast)
                .toList();
    }
}
