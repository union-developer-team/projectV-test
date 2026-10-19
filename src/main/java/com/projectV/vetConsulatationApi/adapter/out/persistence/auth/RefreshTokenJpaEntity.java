package com.projectV.vetConsulatationApi.adapter.out.persistence.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * auth_refresh_tokens 테이블 매핑.
 * - 왜: 도메인에 JPA를 넣지 않고, Refresh 저장만 바깥 어댑터가 담당
 *
 * @Entity : JPA가 관리하는 영속 객체
 * @Table  : 실제 테이블명 (클래스명과 다를 때 명시)
 */
@Entity
@Table(name = "auth_refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA 스텍상 기본 생성자 필요, 외부 new 방지
public class RefreshTokenJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    /** DDL: token_hash CHAR(64) — SHA-256 hex */
    @Column(name = "token_hash", nullable = false, length = 64, unique = true)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /** null이면 유효, 값이 있으면 폐기됨 */
    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public static RefreshTokenJpaEntity create(Long memberId, String tokenHash, LocalDateTime expiresAt) {
        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity();
        entity.memberId = memberId;
        entity.tokenHash = tokenHash;
        entity.expiresAt = expiresAt;
        entity.revokedAt = null;
        entity.createdAt = LocalDateTime.now();
        return entity;
    }

    /** 로그아웃·Rotation 시 호출 */
    public void revoke() {
        this.revokedAt = LocalDateTime.now();
    }

}
