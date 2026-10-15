package com.projectV.vetConsulatationApi.adapter.out.persistence.member;

import com.projectV.vetConsulatationApi.domain.member.Member;
import org.springframework.stereotype.Component;

/**
 * 도메인 Member ↔ JPA Entity 변환.
 * - 왜: 변환 규칙을 한곳에 모아 어댑터 코드가 읽기 쉬워짐
 */
@Component
public class MemberMapper {

    /** 도메인 → 신규 엔티티 (id 없음) */
    public MemberJpaEntity toNewEntity(Member member) {
        return MemberJpaEntity.create(
                member.getEmail(),
                member.getPasswordHash(),
                member.getName(),
                member.getPhone(),
                member.getStatus(),
                member.getRoles(),
                member.getCreatedAt(),
                member.getUpdatedAt(),
                member.getWithdrawnAt()
        );
    }

    /** 엔티티 → 도메인 (reconstitute로 복원) */
    public Member toDomain(MemberJpaEntity entity) {
        return Member.reconstitute(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getName(),
                entity.getPhone(),
                entity.getStatus(),
                entity.getRoles(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getWithdrawnAt()
        );
    }
}