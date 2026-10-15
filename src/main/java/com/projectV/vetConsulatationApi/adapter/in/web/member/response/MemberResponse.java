package com.projectV.vetConsulatationApi.adapter.in.web.member.response;

/*
*   회원 API 응답 Body
*   - 왜: psswordHash를 응답에 절대 넣지 않기 위함
*   - MemberResult를 그대로 써도 되지만, web 전용 형태로 두는 편이 계층 경계가 명확함
*
* */

import com.projectV.vetConsulatationApi.application.member.port.in.MemberResult;
import com.projectV.vetConsulatationApi.domain.member.MemberRole;
import com.projectV.vetConsulatationApi.domain.member.MemberStatus;

import java.util.Set;

public record MemberResponse(
        Long id,
        String email,
        String name,
        String phone,
        MemberStatus status,
        Set<MemberRole> roles
) {

    public static MemberResponse from(MemberResult result) {
        return new MemberResponse(
                result.id(),
                result.email(),
                result.name(),
                result.phone(),
                result.status(),
                result.roles()
        );
    }

}
