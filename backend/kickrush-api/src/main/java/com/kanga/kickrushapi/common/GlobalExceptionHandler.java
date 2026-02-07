package com.kanga.kickrushapi.common;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kanga.kickrushapi.mock.MockStoreException;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ApiError.of(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ApiError> handleValidation(Exception ex) {
        return ResponseEntity.badRequest()
                .body(ApiError.of(ErrorCode.INVALID_PARAMETER, "잘못된 요청 파라미터입니다."));
    }

    @ExceptionHandler(MockStoreException.class)
    public ResponseEntity<ApiError> handleMockStore(MockStoreException ex) {
        HttpStatus status = mapStatus(ex.getErrorCode());
        return ResponseEntity.status(status)
                .body(ApiError.of(ex.getErrorCode(), ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(ApiError.of(ErrorCode.INVALID_PARAMETER, ex.getMessage()));
    }

    private HttpStatus mapStatus(ErrorCode code) {
        return switch (code) {
            case INVALID_PARAMETER, RELEASE_NOT_STARTED, RELEASE_ENDED -> HttpStatus.BAD_REQUEST;
            case UNAUTHORIZED, INVALID_CREDENTIALS, TOKEN_EXPIRED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case SHOE_NOT_FOUND, RELEASE_NOT_FOUND, ORDER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case STOCK_INSUFFICIENT, DUPLICATE_ORDER, DUPLICATE_EMAIL, ORDER_NOT_CANCELLABLE -> HttpStatus.CONFLICT;
            case LOCK_TIMEOUT -> HttpStatus.SERVICE_UNAVAILABLE;
        };
    }
}
