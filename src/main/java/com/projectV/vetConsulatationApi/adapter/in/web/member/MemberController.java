package com.projectV.vetConsulatationApi.adapter.in.web.member;

/*
*   회원 HTTP 어댑터.
*   - 왜: 웹 입출력을 UseCase에만 위임. JPA/Security 세부 구현은 모름
*
* */

import com.projectV.vetConsulatationApi.adapter.in.web.member.request.RegisterMemberRequest;
import com.projectV.vetConsulatationApi.adapter.in.web.member.response.MemberResponse;
import com.projectV.vetConsulatationApi.application.member.port.in.MemberResult;
import com.projectV.vetConsulatationApi.application.member.port.in.RegisterMemberUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final RegisterMemberUseCase resisterMemberUseCase;

    // 회원가입
    // @Valid: @NotBlank, @Email 같은 제약은 DTO에만 적어도 자동으로 검사되지 않습니다.
    //         컨트롤러 파라미터에 @Valid를 붙여야 Spring이 검증을 돌립니다.
    @PostMapping
    public ResponseEntity<MemberResponse> register (@Valid @RequestBody RegisterMemberRequest request) {
        log.info("MemberController : POST register : 회원가입 요청 - email={}", request.email());
        // request.password() 는 로그에 출력하지 않음

        MemberResult result = resisterMemberUseCase.register(request.toCommand());

        log.info("MemberController : POST register : 회원가입 응답 - memberId={}", result.id());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(MemberResponse.from(result));
    }

}
