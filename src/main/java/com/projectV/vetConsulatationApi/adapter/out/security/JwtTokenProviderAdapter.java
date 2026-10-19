package com.projectV.vetConsulatationApi.adapter.out.security;

import com.projectV.vetConsulatationApi.application.auth.port.out.TokenProviderPort;
import com.projectV.vetConsulatationApi.domain.member.Member;
import com.projectV.vetConsulatationApi.domain.member.MemberRole;
import com.projectV.vetConsulatationApi.domain.member.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TokenProviderPort의 jjwt 구현.
 * - 왜: Service는 create/parse만 호출. 서명 알고리즘·키는 이 어댑터에만 둠
 */
@Component
public class JwtTokenProviderAdapter implements TokenProviderPort {

    private final SecretKey secretKey;
    private final long accessTokenValiditySeconds;

    public JwtTokenProviderAdapter(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.access-token-validity-seconds:1800}") long accessTokenValiditySeconds
    ) {
        // Keys.hmacShaKeyFor: 문자열을 HMAC용 SecretKey로 변환 (길이 부족하면 예외)
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    @Override
    public String createAccessToken(Member member) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValiditySeconds * 1000);

        // roles claim: ["GUARDIAN", ...] — Step 6 hasRole 변환에 사용
        List<String> roles = member.getRoles().stream()
                .map(Enum::name)
                .toList();

        return Jwts.builder()
                .subject(String.valueOf(member.getId())) // sub = memberId
                .claim("email", member.getEmail())
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey) // HS256 계열로 서명
                .compact();
    }

    @Override
    public long getAccessTokenExpiresInSeconds() {
        return accessTokenValiditySeconds;
    }

    @Override
    public AccessTokenPayload parseAccessToken(String accessToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey) // 서명 검증 + 만료 검사
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();

            Long memberId = Long.valueOf(claims.getSubject());
            String email = claims.get("email", String.class);

            @SuppressWarnings("unchecked")
            Collection<String> roleNames = claims.get("roles", Collection.class);
            Set<MemberRole> roles = roleNames == null
                    ? Set.of()
                    : roleNames.stream()
                    .map(MemberRole::valueOf)
                    .collect(Collectors.toSet());

            return new AccessTokenPayload(memberId, email, roles);
        } catch (ExpiredJwtException e) {
            throw new InvalidTokenException("만료된 Access Token입니다.");
        } catch (Exception e) {
            throw new InvalidTokenException("유효하지 않은 Access Token입니다.");
        }
    }
}