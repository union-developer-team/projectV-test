package com.projectV.vetConsulatationApi.application.member.port.out;

/*
*   비밀번호 해싱 Port.
*   - 왜: 도메인/유스케이스가 BCrypt,Spring Security를 직접 의존하지 않게 함.
*        Step 3~5에서 BCryptPasswordEncoder가 구현
*
* */

public interface PasswordEncoderPort {

    // 평문 -> 해시. Member에는 이 결과만 넣는다.
    String encode(String rawPassword);

    // 로그인 시 사용 (step 5). 지금은 선언만 해 두어도 됨
    boolean matches(String rawPassword, String passwordHash);
}
