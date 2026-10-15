package com.projectV.vetConsulatationApi.config;

/*
*   최소 Security 설정 (JWT 인가 전).
*   - 왜: 회원가입 API를 공개하고, 세션 대신 나중에 JWT(Stateless)를 붙이기 위함
*   - Step 5~6에서 login/refresh permitAll, me는 authenticated 로 확장
* */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)  // API + JWT 전제. 폼 로그인 세션 CSRF 비활성
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/v1/members").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated()
                );
        // httpBasic/formLogin 기본 로그인 화면은 쓰지 않음 (나중에 JWT 필터 추가)
        return http.build();
    }
}
