package com.projectV.vetConsulatationApi.domain.member.exception;

import com.projectV.vetConsulatationApi.domain.common.exception.BusinessException;
import com.projectV.vetConsulatationApi.domain.common.exception.ErrorCode;

public class InvalidTokenException extends BusinessException {

    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }

    public InvalidTokenException(String message) {
        super(ErrorCode.INVALID_TOKEN, message);
    }
}