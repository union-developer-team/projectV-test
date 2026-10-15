package com.projectV.vetConsulatationApi.adapter.in.web.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

/*
*   클라이언트로 내려주는 공통 에러 응답 부문
*   - 왜: 성공/실패와 무관하게 에러 형식을 통일해 프론트, 모바일이 한 방식으로 처리 가능
*   - 위치: web 어뎁터(도메인은 HTTP JSON 형식을 모름)
*
* */

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY) // null/빈 필드는 JSON에서 생략
public class ErrorResponse {

    private String code;                    // ErrorCode.code (예: DUPLICATE_EMAIL)
    private String message;                 // 사용자/클라이언용 안내
    private LocalDateTime timestamp;        // 발생 시 (디버길 및 로그 대조용)
    private final List<FieldError> errors;   // 필드별 검 실패 목록 (없으면 생략)

    // 일반 비지니스/서버 오류 - 필드 상세 없 code + message
    public static ErrorResponse of(String code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // 입력값 검 실패용 - 어떤 필드가 왜 틀렸는지까지 포함
    public static ErrorResponse of(String code, String message, List<FieldError> errors) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();
    }

    /*
    *   Bean Validation 실패 시 필드 단위 정보.
    *   예: field="email", reason = "이메일 형식이 올바르지 않습니다."
    * */
    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String reason;

        public static FieldError of(String field, String reason) {
            return FieldError.builder()
                    .field(field)
                    .reason(reason)
                    .build();
        }
    }
}
