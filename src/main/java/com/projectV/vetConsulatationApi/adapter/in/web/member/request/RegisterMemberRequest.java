package com.projectV.vetConsulatationApi.adapter.in.web.member.request;

/*
*   회원가입 HTTP 요청 Body
*   - 왜: web 계층 검증(@Valid)과 application Command를 분리하기 위함
*   - 비밀번호는 로그/toString에 넣지 말 것
* */

import com.projectV.vetConsulatationApi.application.member.port.in.RegisterMemberCommand;
import com.projectV.vetConsulatationApi.domain.member.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterMemberRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 64, message = "비밀번호는 8~64자여야 합니다.")
        String password,

        @NotBlank(message = "이름은 필수입니다.")
        @Size(max = 100, message = "이름은 100자 이하여야 합니다.")
        String name,

        @Size(max = 20, message = "전화번호는 20자 이하여야 합니다.")
        String phone,

        // null이면 Service/도메인에서 GUARDIAN 처리
        MemberRole role
) {

    // Request > UseCase Command 변환
    public RegisterMemberCommand toCommand() {
        return  new RegisterMemberCommand(email, password, name, phone, role);
    }

}
