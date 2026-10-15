package com.projectV.vetConsulatationApi.application.member.service;

import com.projectV.vetConsulatationApi.application.member.port.in.MemberResult;
import com.projectV.vetConsulatationApi.application.member.port.in.RegisterMemberCommand;
import com.projectV.vetConsulatationApi.application.member.port.in.RegisterMemberUseCase;
import com.projectV.vetConsulatationApi.application.member.port.out.MemberRepositoryPort;
import com.projectV.vetConsulatationApi.application.member.port.out.PasswordEncoderPort;
import com.projectV.vetConsulatationApi.domain.member.Member;
import com.projectV.vetConsulatationApi.domain.member.exception.DuplicateEmailException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
*   회원가입 유스케이스 구성
*   - 왜: "중복 검사 > 해시 > 도메인 생성 > 저장" 흐름을 한곳에 모아
*       Controller/JPA와 분리하기 위함
* */

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterMemberService implements RegisterMemberUseCase {

    private final MemberRepositoryPort memberRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    @Transactional
    public MemberResult register(RegisterMemberCommand command) {
        log.info("RegisterMemberService : register : 회원가입 비즈니스 로직 시작");
        log.debug("RegisterMemberService : register : 요청 확인 - email={}", command.email());
        // rawPassword는 로그에 절대 출력하지 않음

        // 1) 이메일 중복 > 도메인 예외 > GlobalExceptionHandler > 409
        if (memberRepositoryPort.existsByEmail(command.email())) {
            log.warn("RegisterMemberService : register : 이메일 중복 - email={}", command.email());
            throw new DuplicateEmailException();
        }

        // 2) 평문 비밀번호 해싱 (도메인 밖)
        String passwordHash = passwordEncoderPort.encode(command.rawPassword());

        // 3) 도메인 규칙으로 회원 생성 (status=ACTIVE, 기본 역할 등)
        Member member = Member.register(
                command.email(),
                passwordHash,
                command.name(),
                command.phone(),
                command.role()
        );

        // 4) 저장 (실제 INSERT는 Step 3 어뎁터)
        Member saved = memberRepositoryPort.save(member);

        log.info("RegisterMemberService : register : 회원가입 완료 - memberId={}", saved.getId());
        return MemberResult.from(saved);
    }
}
