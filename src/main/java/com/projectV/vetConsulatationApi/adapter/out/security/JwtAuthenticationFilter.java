package com.projectV.vetConsulatationApi.adapter.out.security;

import com.projectV.vetConsulatationApi.application.auth.port.out.TokenProviderPort;
import com.projectV.vetConsulatationApi.domain.member.MemberRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * 매 요청마다 Authorization Bearer를 읽어 SecurityContext에 인증 정보를 넣음.
 *
 * OncePerRequestFilter : 한 요청에 필터가 두 번 돌지 않게 보장
 * SecurityContextHolder : 현재 스레드의 인증 정보 저장소 (Controller에서 조회 가능)
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProviderPort tokenProviderPort;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Bearer 없으면 그냥 통과 → 보호 URL은 Security가 401 처리
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                var payload = tokenProviderPort.parseAccessToken(token);

                // ROLE_ 접두사: Spring hasRole("GUARDIAN")가 내부적으로 ROLE_GUARDIAN을 찾음
                List<SimpleGrantedAuthority> authorities = payload.roles().stream()
                        .map(MemberRole::name)
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();

                // principal = memberId (Long). 비밀번호는 필요 없으니 credentials=null
                var authentication = new UsernamePasswordAuthenticationToken(
                        payload.memberId(),
                        null,
                        authorities
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("JwtAuthenticationFilter : doFilterInternal : 인증 설정 - memberId={}",
                        payload.memberId());
            } catch (Exception e) {
                // 깨진 토큰: 컨텍스트 비우고 통과 (보호 API는 결국 401)
                log.warn("JwtAuthenticationFilter : doFilterInternal : 토큰 검증 실패");
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}