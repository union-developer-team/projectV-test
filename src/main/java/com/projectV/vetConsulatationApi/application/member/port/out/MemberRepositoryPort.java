package com.projectV.vetConsulatationApi.application.member.port.out;

/*
*   회원 저장소 Port (애플리케이션 -> 영속성)
*   - 왜: Service가 JPA/MySQL을 직접 모르게 하기 위함.
*        Step 3에서 MemberPersistenceAdapter가 이 인터페이스를 구현
* */

import com.projectV.vetConsulatationApi.domain.member.Member;

import java.util.Optional;

public interface MemberRepositoryPort {

    // 이메일 중복 가입 방지용
    boolean existsByEmail(String email);

    // 신규/변경 회원 저장 후, id가 채워진 Member 반환
    Member save(Member member);

    // 조회, 수정, 탈퇴에서 사용 (Register에서는 안 씀, 미리 선언해도 됨)
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
}
