package com.projectV.vetConsulatationApi.adapter.out.persistence.member;

import com.projectV.vetConsulatationApi.domain.member.MemberRole;
import com.projectV.vetConsulatationApi.domain.member.MemberStatus;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * members + member_roles 테이블용 JPA 엔티티.
 * - 왜: 도메인 Member에 @Entity를 붙이지 않고, DB 매핑만 바깥 어댑터에 두기 위함
 */
@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 필요, 외부 new 방지
public class MemberJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    /** DDL: password_hash */
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 20)
    private String phone;

    /** DDL: status (ACTIVE/SUSPENDED/WITHDRAWN) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MemberStatus status;

    /**
     * DDL: member_roles
     * - 역할이 enum 값만 있으므로 별도 엔티티 대신 ElementCollection 사용
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "member_roles",
            joinColumns = @JoinColumn(name = "member_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private Set<MemberRole> roles = new HashSet<>();

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "withdrawn_at")
    private LocalDateTime withdrawnAt;

    /**
     * 신규 저장용 — 도메인 값을 엔티티 필드로 옮김
     */
    public static MemberJpaEntity create(
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
        MemberJpaEntity entity = new MemberJpaEntity();
        entity.email = email;
        entity.passwordHash = passwordHash;
        entity.name = name;
        entity.phone = phone;
        entity.status = status;
        entity.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
        entity.createdAt = createdAt;
        entity.updatedAt = updatedAt;
        entity.withdrawnAt = withdrawnAt;
        return entity;
    }

    /**
     * 수정·탈퇴 저장용 — 이미 영속화된 엔티티에 도메인 변경분 반영
     * (email/passwordHash는 가입 후 이 메서드에서 바꾸지 않음)
     */
    public void updateMutableFields(
            String name,
            String phone,
            MemberStatus status,
            Set<MemberRole> roles,
            LocalDateTime updatedAt,
            LocalDateTime withdrawnAt
    ) {
        this.name = name;
        this.phone = phone;
        this.status = status;
        this.roles.clear();
        if (roles != null) {
            this.roles.addAll(roles);
        }
        this.updatedAt = updatedAt;
        this.withdrawnAt = withdrawnAt;
    }
}