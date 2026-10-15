package com.projectV.vetConsulatationApi.adapter.in.web.common;

import com.projectV.vetConsulatationApi.domain.common.exception.BusinessException;
import com.projectV.vetConsulatationApi.domain.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.validation.BindException;

import java.util.List;

/*
*   컨트롤러에서 터진 예외를 ErrorResponse로 바꿔 반환하는 전역 헨들러
*   - 왜: try-catch를 API마다 반복하지 않고, 싪패 응답 형식을 한곳에서 보장
*
* */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 도메인/유스케이스에서 던진 BussinessException > ErrorCode에 맞는 HTTP 상태
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("GlobalExceptionHandler : handleBusiness : 비지니스 예외 - code={}, message={}",
                errorCode.getCode(), e.getMessage());
        return ResponseEntity
                .status(errorCode.getHttpstatus())
                .body(ErrorResponse.of(errorCode.getCode(), e.getMessage()));
    }

    // @Valid 실패 > 필드별 이유를 errors에 담아 400 반환
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ErrorResponse> handleValidation(Exception e) {
        List<ErrorResponse.FieldError> fieldErrors;
        if(e instanceof MethodArgumentNotValidException manv) {
            fieldErrors = manv.getBindingResult().getFieldErrors().stream()
                    .map(fe -> ErrorResponse.FieldError.of(fe.getField(), fe.getDefaultMessage()))
                    .toList();
        } else {
            BindException be = (BindException) e;
            fieldErrors = be.getBindingResult().getFieldErrors().stream()
                    .map(fe -> ErrorResponse.FieldError.of(fe.getField(), fe.getDefaultMessage()))
                    .toList();
        }

        log.warn("GlobalExceptionHandler : handleValidation : 입력값 검증 실패");
        ErrorCode code = ErrorCode.INVALID_INPUT;
        return ResponseEntity
                .status(code.getHttpstatus())
                .body(ErrorResponse.of(code.getCode(), code.getMessage(), fieldErrors));
    }

    /** 그 외 모든 예외 → 500. 상세 스택은 로그에만, 클라이언트에는 일반 메시지만 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
        log.error("GlobalExceptionHandler : handleUnexpected : 서버 내부 오류", e);
        ErrorCode code = ErrorCode.INTERNAL_ERROR;
        return ResponseEntity
                .status(code.getHttpstatus())
                .body(ErrorResponse.of(code.getCode(), code.getMessage()));
    }
}
