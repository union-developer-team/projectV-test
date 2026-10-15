package com.projectV.vetConsulatationApi.domain.common.exception;

import lombok.Getter;

/*
 *   업무 규칙 위반 시 던지는 공통 예외.
 *   - 왜: DuplicateEmail, MemberNotFound 등이 전부 이 타입을 상속하면
 *   GlobalExceptionHandler 한 곳에서 HTTP 응답으로 바꿀 수 있음
 *
 * */
@Getter
public class BusinessException extends RuntimeException {

    // 어떤 ErrorCode인지 알아야 Handler가 status/code를 채울 수 있음
    private final ErrorCode errorCode;


    public BusinessException(ErrorCode errorCode) {
        // 메시지는 ErrorCode 기본값을 사용
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message) {
        // 상황에 맞는 구체 메시지가 필요할 때(예: 이미 탈퇴한 회원입니다.)
        super(message);
        this.errorCode = errorCode;
    }
}
