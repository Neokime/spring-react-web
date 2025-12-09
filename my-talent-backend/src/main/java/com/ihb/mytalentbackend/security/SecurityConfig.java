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

                // 🔥 Spring Security CORS 확실하게 활성화
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()

                        // 🔽 피드백 조회는 공개
                        .requestMatchers(HttpMethod.GET, "/api/talents/*/feedback").permitAll()

                        // 🔽 재능 신청은 인증 필요
                        .requestMatchers(HttpMethod.GET, "/api/talents/*/requests").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/talents/*/requests").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/talents/*/requests/*/accept").authenticated()

                        // 🔽 재능 CRUD는 인증 필요
                        .requestMatchers("/api/talents/**").authenticated()

                        // 🔽 파일 업로드만 추가하면 끝!
                        .requestMatchers(HttpMethod.POST, "/api/upload").authenticated()

                        // 🔽 업로드된 파일 접근 허용
                        .requestMatchers("/api/files/**").permitAll()

                        .anyRequest().authenticated()
                )


                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    // 🔥 CORS 설정 추가 (Authorization 헤더 허용)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));

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
