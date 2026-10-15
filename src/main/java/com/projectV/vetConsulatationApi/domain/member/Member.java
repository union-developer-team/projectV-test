package com.projectV.vetConsulatationApi.domain.member;

/*
*   회원 도메인 모델 (순수 Java - JPA/Security 없음).
*   - 왜: "가입, 탈퇴, 로그인 가능 여부" 같은 규칙을 DB/프레임워크가 아니라
*         비즈니스 객체에 두기 위함 (헥사고날의 안쪽)
*   - passwordHash만 보관: 평문 비밀번호는 절대 필드에 두지 않음 (해싱은 바깥 Port)
* */

import com.projectV.vetConsulatationApi.domain.member.exception.InvalidMemberStatusException;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class Member {

    private Long id;
    private String email;
    private String passwordHash;       // DDL: password_hash — 이미 해시된 값만
    private String name;
    private String phone;
    private MemberStatus status;       // DDL: status
    private Set<MemberRole> roles;     // DDL: member_roles
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime withdrawnAt; // DDL: withdrawn_at (탈퇴 시에만)

    // 외부에서 new Member()로 임의 생성하지 목하게 private
    private Member() {
        this.roles = new HashSet<>();
    }

    /**
     * 신규 가입용 팩토리.
     * - passwordHash: PasswordEncoderPort.encode() 결과를 받아서 넣음
     * - 기본 역할: 지정 없으면 GUARDIAN
     * - 초기 status: ACTIVE
     */
    public static Member register(
            String email,
            String passwordHash,
            String name,
            String phone,
            MemberRole initialRole
    ) {
        Objects.requireNonNull(email, "email");
        Objects.requireNonNull(passwordHash, "passwordHash");
        Objects.requireNonNull(name, "name");
        Member member = new Member();
        member.email = email;
        member.passwordHash = passwordHash;
        member.name = name;
        member.phone = phone;
        member.status = MemberStatus.ACTIVE;
        member.roles.add(initialRole != null ? initialRole : MemberRole.GUARDIAN);
        LocalDateTime now = LocalDateTime.now();
        member.createdAt = now;
        member.updatedAt = now;
        return member;
    }

    /**
     * DB에서 읽어온 값으로 도메인 객체를 다시 만들 때 사용.
     * - 왜: JPA 엔티티 ≠ 도메인 Member. 매퍼가 이 메서드로만 복원하게 하면
     *        생성 규칙(register)과 조회 복원이 섞이지 않음
     */
    public static Member reconstitute(
            Long id,
            String email,
            String passwordHash,
            String name,
            String phone,
            MemberStatus status,
            Set<MemberRole> roles,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime withdrawnAt
    ) {
        Member member = new Member();
        member.id = id;
        member.email = email;
        member.passwordHash = passwordHash;
        member.name = name;
        member.phone = phone;
        member.status = status;
        member.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
        member.createdAt = createdAt;
        member.updatedAt = updatedAt;
        member.withdrawnAt = withdrawnAt;
        return member;
    }

    /**
     * 회원 탈퇴.
     * - status → WITHDRAWN, withdrawnAt 기록
     * - 이미 탈퇴면 예외 (중복 탈퇴 방지)
     */
    public void withdraw() {
        if (this.status == MemberStatus.WITHDRAWN) {
            throw new InvalidMemberStatusException("이미 탈퇴한 회원입니다.");
        }
        LocalDateTime now = LocalDateTime.now();
        this.status = MemberStatus.WITHDRAWN;
        this.withdrawnAt = now;
        this.updatedAt = now;
    }

    /**
     * 프로필(이름·전화) 수정.
     * - 탈퇴 회원은 수정 불가
     */
    public void updateProfile(String name, String phone) {
        if (this.status == MemberStatus.WITHDRAWN) {
            throw new InvalidMemberStatusException("탈퇴한 회원은 정보를 수정할 수 없습니다.");
        }
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 로그인 가능 여부.
     * - ACTIVE만 true. SUSPENDED/WITHDRAWN은 false
     * - 왜: 로그인 유스케이스·Security가 "가능하냐"만 물어보고,
     *        판단 기준은 도메인이 갖게 하기 위함
     */
    public boolean isLoginAllowed() {
        return this.status == MemberStatus.ACTIVE;
    }

    /** 외부에서 roles 컬렉션을 직접 바꾸지 못하게 읽기 전용으로 반환 */
    public Set<MemberRole> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    /**
     * 저장 후 DB가 만든 id를 도메인에 반영.
     * - 왜: register 시점에는 id가 없고, save 이후에만 생김
     */
    public void assignId(Long id) {
        this.id = id;
    }

}
