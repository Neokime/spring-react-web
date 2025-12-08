// src/main/java/com/ihb/mytalentbackend/security/SecurityConfig.java
package com.ihb.mytalentbackend.security;

import com.ihb.mytalentbackend.security.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // SecurityConfig.java 안 authorizeHttpRequests 부분

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        // 피드백 조회는 모두 허용
                        .requestMatchers(HttpMethod.GET, "/api/talents/*/feedback").permitAll()

                        // 🔥 신청 목록은 반드시 로그인 필요
                        .requestMatchers(HttpMethod.GET, "/api/talents/*/requests").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/talents/*/requests").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/talents/*/requests/*/accept").authenticated()

                        // 그 외 /api/talents/** 는 기본적으로 인증 필요
                        .requestMatchers("/api/talents/**").authenticated()

                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
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
