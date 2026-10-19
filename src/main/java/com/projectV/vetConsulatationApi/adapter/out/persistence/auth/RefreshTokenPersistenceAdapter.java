package com.projectV.vetConsulatationApi.adapter.out.persistence.auth;

import com.projectV.vetConsulatationApi.application.auth.port.out.RefreshTokenStorePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * RefreshTokenStorePort의 JPA 구현.
 * - @Component : Spring Bean 등록 → LoginService 생성자 주입 대상
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RefreshTokenPersistenceAdapter implements RefreshTokenStorePort {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    @Transactional
    public void save(Long memberId, String tokenHash, LocalDateTime expiresAt) {
        log.debug("RefreshTokenPersistenceAdapter : save : Refresh 저장 - memberId={}", memberId);
        refreshTokenJpaRepository.save(
                RefreshTokenJpaEntity.create(memberId, tokenHash, expiresAt)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StoredRefreshToken> findValidByTokenHash(String tokenHash) {
        return refreshTokenJpaRepository.findByTokenHashAndRevokedAtIsNull(tokenHash)
                .map(e -> new StoredRefreshToken(e.getMemberId(), e.getExpiresAt(), e.getId()));
    }

    @Override
    @Transactional
    public void revoke(String tokenHash) {
        int updated = refreshTokenJpaRepository.revokeByTokenHash(tokenHash, LocalDateTime.now());
        log.debug("RefreshTokenPersistenceAdapter : revoke : 폐기 건수={}", updated);
    }

    @Override
    @Transactional
    public void revokeAllMemberId(Long memberId) {
        // Port 메서드명이 revokeAllMemberId 이므로 그대로 구현
        int updated = refreshTokenJpaRepository.revokeAllByMemberId(memberId, LocalDateTime.now());
        log.debug("RefreshTokenPersistenceAdapter : revokeAllMemberId : memberId={}, 건수={}",
                memberId, updated);
    }
}