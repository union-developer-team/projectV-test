package com.projectV.vetConsulatationApi.adapter.out.persistence.member;

import com.projectV.vetConsulatationApi.application.member.port.out.MemberRepositoryPort;
import com.projectV.vetConsulatationApi.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * MemberRepositoryPort의 JPA 구현체.
 * - 왜: application 계층은 Port만 알고, 실제 DB 접근은 이 어댑터만 담당
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MemberPersistenceAdapter implements MemberRepositoryPort {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberMapper memberMapper;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return memberJpaRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public Member save(Member member) {
        log.debug("MemberPersistenceAdapter : save : 회원 저장 시작 - email={}", member.getEmail());

        MemberJpaEntity savedEntity;

        if (member.getId() == null) {
            // 신규 가입
            MemberJpaEntity entity = memberMapper.toNewEntity(member);
            savedEntity = memberJpaRepository.save(entity);
            // 도메인 객체에도 DB가 발급한 id 반영
            member.assignId(savedEntity.getId());
        } else {
            // 수정·탈퇴 등 기존 회원
            MemberJpaEntity entity = memberJpaRepository.findById(member.getId())
                    .orElseThrow(() -> new IllegalStateException(
                            "저장 대상 회원이 DB에 없습니다. id=" + member.getId()
                    ));
            entity.updateMutableFields(
                    member.getName(),
                    member.getPhone(),
                    member.getStatus(),
                    member.getRoles(),
                    member.getUpdatedAt(),
                    member.getWithdrawnAt()
            );
            savedEntity = memberJpaRepository.save(entity);
        }

        Member result = memberMapper.toDomain(savedEntity);
        log.debug("MemberPersistenceAdapter : save : 회원 저장 완료 - memberId={}", result.getId());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id).map(memberMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email).map(memberMapper::toDomain);
    }
}