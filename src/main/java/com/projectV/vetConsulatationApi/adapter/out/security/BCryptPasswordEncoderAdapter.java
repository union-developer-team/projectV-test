package com.projectV.vetConsulatationApi.adapter.out.security;

import com.projectV.vetConsulatationApi.application.member.port.out.PasswordEncoderPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * PasswordEncoderPort의 BCrypt 구현.
 * - 왜: Service는 encode/matches만 호출. BCrypt 세부사항은 어댑터에 둠
 * - 평문 비밀번호는 로그에 남기지 않음
 */
@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String passwordHash) {
        return passwordEncoder.matches(rawPassword, passwordHash);
    }
}