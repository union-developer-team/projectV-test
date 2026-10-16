package com.projectV.vetConsulatationApi.application.auth.port.out;

/*
*   Access JWT 발급, 파싱 Port
*   - 왜: LoginService가 jjwt/Nimbus를 직접 모르게 하기 위함
* */

import com.projectV.vetConsulatationApi.domain.member.Member;
import com.projectV.vetConsulatationApi.domain.member.MemberRole;

import java.util.Set;

public interface TokenProviderPort {

    // Access JWT 문자열 발급 (sub=memberId, role claim 포함)
    String createAccessToken(Member member);

    // 만료 초 (응답 expiresIn용)
    long getAccessTokenExpiresInSeconds();

    // Bearer 검증 후 클레임 추출. 실패 시 InvalidTokenException
    AccessTokenPayload parseAccessToken(String accessToken);

    record AccessTokenPayload(Long memberId, String email, Set<MemberRole> roles){}
}
