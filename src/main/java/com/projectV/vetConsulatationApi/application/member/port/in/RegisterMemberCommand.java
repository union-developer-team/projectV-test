package com.projectV.vetConsulatationApi.application.member.port.in;

import com.projectV.vetConsulatationApi.domain.member.MemberRole;
/**
 * 회원가입 입력 값 묶음.
 * - 왜: Controller Request DTO와 도메인을 분리.
 *        web 어댑터에서 Request → Command 로 변환해 UseCase에 넘김
 * - rawPassword: 평문. Service에서 encode 후 바로 버리고, 로그에 찍지 않음
 */
public record RegisterMemberCommand(
        String email,
        String rawPassword,
        String name,
        String phone,
        MemberRole role   // null이면 Member.register가 GUARDIAN 처리
) {
}
