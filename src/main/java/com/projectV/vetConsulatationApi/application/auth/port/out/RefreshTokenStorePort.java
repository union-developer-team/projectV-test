package com.projectV.vetConsulatationApi.application.auth.port.out;

/*
*   Refresh Token 저장 Port
*   - 왜: DDL auth_refresh_token. 평문이 아니라 token_hash만 저장
* */

import java.time.LocalDateTime;
import java.util.Optional;

public interface RefreshTokenStorePort {

    void save(Long memberId, String tokenHash, LocalDateTime expiresAt);

    Optional<StoredRefreshToken> findValidByTokenHash(String tokenHash);

    void revoke(String tokenHash);

    void revokeAllMemberId(Long memberId);  // 로그아웃(선택) / 보안 강화

    record StoredRefreshToken(Long memberId, LocalDateTime expiresAt, Long id){}

}
