package com.projectV.vetConsulatationApi.domain.member;

/*
*   회원 역할.
*   -  왜: DDL member_roles.role CHECK 값과 맞춤.
*       나중에 Spring Security에서 ROLE_GUARDIAN 같은 인가에 사용
*   - 지금은 도메인 값만 두고, Security 변환은 Step 5~6에서
* */

public enum MemberRole {

    GUARDIAN,         // 보호자 (기본 가입 역할)
    VETERINARIAN,     // 수의사
    HOSPITAL_ADMIN,   // 병원 관리자
    SUPER_ADMIN       // 플랫폼 최고 관리자

}
