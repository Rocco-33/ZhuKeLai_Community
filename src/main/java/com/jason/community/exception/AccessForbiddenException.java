package com.jason.community.exception;

/**
 * 禁止访问的异常
 *
 * @author Jason
 * @version 1.0
 */
public class AccessForbiddenException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public AccessForbiddenException() {
    }

    public AccessForbiddenException(String message) {
        super(message);
    }

    public AccessForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessForbiddenException(Throwable cause) {
        super(cause);
    }
}
