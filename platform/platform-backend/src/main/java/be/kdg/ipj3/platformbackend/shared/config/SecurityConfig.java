package be.kdg.ipj3.platformbackend.shared.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/games/favorite/**").authenticated()
                        .requestMatchers("/api/lobby","/api/lobby/**").authenticated()
                        .requestMatchers("/api/user/**").authenticated()
                        .requestMatchers("/api/user/friends/**").authenticated()
                        .requestMatchers("/api/games").permitAll()
                        .anyRequest().permitAll()
                )
                .sessionManagement(mgmt -> mgmt.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(rs -> rs.jwt(jwt -> jwtAuthenticationConverter()))
                ;
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(@Value("${cors.allowed-origins}") String allowedOrigins) {
        final var source = new UrlBasedCorsConfigurationSource();
        final var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(allowedOrigins.split(",")));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        final var jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
        return jwtConverter;
    }
}



