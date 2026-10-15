package com.projectV.vetConsulatationApi.application.member.port.in;

/*
*   유스케이스 출력 (비밀번호 해시 제외).
*   - 왜: 도메인 Member를 Controller/응답에 그대로 내리면
*       passwordHash 등 내부 필드가 새어 나갈 수 있음
* */

import com.projectV.vetConsulatationApi.domain.member.Member;
import com.projectV.vetConsulatationApi.domain.member.MemberRole;
import com.projectV.vetConsulatationApi.domain.member.MemberStatus;

import java.util.Set;

public record MemberResult(
        Long id,
        String email,
        String name,
        String phone,
        MemberStatus status,
        Set<MemberRole> roles
) {

    // 도메인 -> 결과 DTO 변환 (해시 필드는 의도적으로 제외)
    public static MemberResult from(Member member) {
        return new MemberResult(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getPhone(),
                member.getStatus(),
                member.getRoles()
        );
    }

}
