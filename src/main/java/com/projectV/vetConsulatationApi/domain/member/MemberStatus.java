package com.projectV.vetConsulatationApi.domain.member;

/*
*   회원 계정 상태
*   - 왜: DDL members.status CHECK 값과 맞추고,
*       로그인, 수정, 탈퇴 가능 여부를 status로 판단하기 위함
*
* */
public enum MemberStatus {

    ACTIVE,         // 정상 - 로그인, 정보, 수정 가능
    SUSPENDED,      // 정지 - 로그인 불가 (관리자 조치)
    WITHDRAWN,      // 탈퇴 - 로그인, 수정 불가
}
