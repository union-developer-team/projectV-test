package com.projectV.vetConsulatationApi.adapter.out.persistence.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA Repository.
 * - 왜: SQL 접근을 PersistenceAdapter 뒤로 감춤.
 *        Service는 이 타입을 직접 주입받지 않음
 */
public interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, Long> {

    boolean existsByEmail(String email);

    Optional<MemberJpaEntity> findByEmail(String email);
}