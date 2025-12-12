// src/main/java/com/ihb/mytalentbackend/security/SecurityConfig.java
package com.ihb.mytalentbackend.security;

import com.ihb.mytalentbackend.security.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http

                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()


                        .requestMatchers("/api/admin/**").authenticated()

                        .requestMatchers("/api/user/me").authenticated()

                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")


                                .requestMatchers(HttpMethod.GET, "/api/talents/*/feedback").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/talents/*/requests").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/talents/*/requests").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/talents/*/requests/*/accept").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/trades/**").permitAll()


                        .requestMatchers(HttpMethod.GET, "/api/trades/**").permitAll()


                        .requestMatchers(HttpMethod.POST, "/api/trades/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/trades/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/trades/**").authenticated()

                        .requestMatchers("/api/store/**").authenticated()




                        .requestMatchers("/api/talents/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/files/upload").authenticated()
                        .requestMatchers("/uploads/**").permitAll()
                        .anyRequest().authenticated()
                )


                // JWT 필터
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
