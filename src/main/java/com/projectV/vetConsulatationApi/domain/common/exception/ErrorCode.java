package com.projectV.vetConsulatationApi.domain.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/*
* API에서 쓸 수 있는 "실패 코드" 목록.
* - 왜: 컨트롤러마다 다른 메시지/상태를  쓰지 않고, 한곳에서 HTTP상태/코드/메시지를 맞추기 위함
* */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 요청 본문/파라미터 검증 실채(Bean Validation 등)
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "입력값이 올바르지 않습니다."),

    // 조회 대상 회원이 없을 때
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_NOT_FOUND", "회원을 찾을 수 없습니다."),

    // 회원가입 시 이미 등록된 이메일
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "DUPLICATE_EMAIL", "이미 사용 중인 이메일입니다."),

    // 탍퇴/정지 회원에 허용되지 않은 동작 (수정/로그인)
    INVALID_MEMBER_STATUS(HttpStatus.BAD_REQUEST, "INVALID_MEMBER_STATUS", "유효하지 않은 회원 상태입니다."),

    // 예측하지 못한 서버 오류 (클라이언트에 상세 원인은 숨김)
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 오류가 발생했습니다."),

    // 로그인 실패 (이메일 없음 / 비밀번호 불일치 - 메시지는 동일하게)
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "이메일 또는 비밀번호가 올바르지 않습니다."),

    // Access/Refresh 토큰이 없거나 위조, 만료, 폐기
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", "유효하지 않은 토큰입니다.");

    private final HttpStatus httpstatus;    // HTTP 응답 상태
    private final String code;              // 클라이언트가 분기할 기계용 코드
    private final String message;           // 기본 안내 메시지
}
