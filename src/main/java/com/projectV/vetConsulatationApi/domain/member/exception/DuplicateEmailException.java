package com.projectV.vetConsulatationApi.domain.member.exception;

import com.projectV.vetConsulatationApi.domain.common.exception.BusinessException;
import com.projectV.vetConsulatationApi.domain.common.exception.ErrorCode;

/*
*   이미 사용 중인 이메일로 가입을 시도할 때
*   - 왜: 유스케이스에서 "중복"을 표현하는 전용 예외
*       Handler가 ErrorCode.DUPLICATE_EMAIL -> HTTP 409 로 변환
*   - 참고: 실제 중복 검사는 application(Repository.exisByEmail)에서 하고,
*       여기서는 "실패 신호"만 담당
*
* */
public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
