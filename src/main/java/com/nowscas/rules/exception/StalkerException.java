package com.nowscas.rules.exception;

import org.springframework.http.HttpStatus;

public class StalkerException extends RuntimeException {
    private final HttpStatus status;
    private final Object[] params;

    public StalkerException(HttpStatus status, String message, Object... params) {
        super(message);
        this.status = status;
        this.params = (Object[])params.clone();
    }

    public static StalkerException notFound(String message, Object... params) {
        return new StalkerException(HttpStatus.NOT_FOUND, message, params);
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public Object[] getParams() {
        return this.params;
    }
}
