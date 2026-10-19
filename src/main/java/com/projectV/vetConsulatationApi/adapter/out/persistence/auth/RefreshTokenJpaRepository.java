package com.projectV.vetConsulatationApi.adapter.out.persistence.auth;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Spring Data JPA Repository.
 * - 메서드 이름/쿼리만 선언하면 구현체는 Spring이 런타임에 만들어 줌
 */

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {

    /** 미폐기 토큰만 조회 */
    Optional<RefreshTokenJpaEntity> findByTokenHashAndRevokedAtIsNull(String tokenHash);
    List<RefreshTokenJpaEntity> findByMemberIdAndRevokedAtIsNull(Long memberId);
    /**
     * @Modifying : INSERT/UPDATE/DELETE용. 없으면 SELECT로만 취급되어 실패할 수 있음
     * clearAutomatically : 영속성 컨텍스트와 DB 불일치 방지
     */
    @Modifying(clearAutomatically = true)
    @Query("update RefreshTokenJpaEntity r set r.revokedAt = :now where r.tokenHash = :tokenHash and r.revokedAt is null")
    int revokeByTokenHash(@Param("tokenHash") String tokenHash, @Param("now") LocalDateTime now);

    @Modifying(clearAutomatically = true)
    @Query("update RefreshTokenJpaEntity r set r.revokedAt = :now where r.memberId = :memberId and r.revokedAt is null")
    int revokeAllByMemberId(@Param("memberId") Long memberId, @Param("now") LocalDateTime now);

}
