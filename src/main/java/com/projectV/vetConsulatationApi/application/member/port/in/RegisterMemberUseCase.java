package com.projectV.vetConsulatationApi.application.member.port.in;

/**
 * 회원가입 유스케이스 입구.
 * - 왜: Controller는 이 인터페이스만 호출. 구현체(Service) 교체가 쉬움
 */

public interface RegisterMemberUseCase {

    MemberResult register(RegisterMemberCommand command);

}
