package com.magician.route.commons.exception;

/**
 * Exception message thrown by failed parameter validation
 */
public class VerificationException extends Exception {
    public VerificationException() {
        super();
    }

    public VerificationException(String message) {
        super(message);
    }

    public VerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerificationException(Throwable cause) {
        super(cause);
    }

    protected VerificationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
