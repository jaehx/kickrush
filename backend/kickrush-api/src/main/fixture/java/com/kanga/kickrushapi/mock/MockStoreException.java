package com.kanga.kickrushapi.mock;

import com.kanga.kickrushapi.common.ErrorCode;

public class MockStoreException extends RuntimeException {
    private final ErrorCode errorCode;

    public MockStoreException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
