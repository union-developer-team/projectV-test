package com.projectV.vetConsulatationApi.domain.member.exception;

import com.projectV.vetConsulatationApi.domain.common.exception.BusinessException;
import com.projectV.vetConsulatationApi.domain.common.exception.ErrorCode;

/**
 * 현재 status로는 할 수 없는 동작일 때 (예: 이미 탈퇴한 회원 수정).
 * - 왜: Member.withdraw() / updateProfile() 같은 도메인 규칙 위반을
 *        ErrorCode.INVALID_MEMBER_STATUS 로 바깥에 알리기 위함
 */

public class InvalidMemberStatusException extends BusinessException {

    public InvalidMemberStatusException(String message) {
        // ErrorCode는 공통, 메시지는 상황별로 구체화
        super(ErrorCode.INVALID_MEMBER_STATUS, message);
    }

}
