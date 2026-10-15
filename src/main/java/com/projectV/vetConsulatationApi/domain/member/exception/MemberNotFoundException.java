package com.projectV.vetConsulatationApi.domain.member.exception;

import com.projectV.vetConsulatationApi.domain.common.exception.BusinessException;
import com.projectV.vetConsulatationApi.domain.common.exception.ErrorCode;

/**
 *  id/email로 회원을 찾지 못했을 때.
 *  - 왜: 조회·수정·탈퇴에서 "없음"을 통일된 404 응답으로 보내기 위함
 */

public class MemberNotFoundException extends BusinessException {

    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
