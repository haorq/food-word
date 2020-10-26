package com.meiyuan.catering.core.exception;

import com.meiyuan.catering.core.util.MessageUtils;

/**
 * @author admin
 */
public class AdminUnauthorizedException extends RuntimeException {

    private int code;

    private String message;

    public AdminUnauthorizedException() {
    }

    public AdminUnauthorizedException(int code) {
        this.code = code;
        this.message = MessageUtils.getMessage(code);
    }

    public AdminUnauthorizedException(int code, String... params) {
        this.code = code;
        this.message = MessageUtils.getMessage(code, params);
    }

    public AdminUnauthorizedException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public AdminUnauthorizedException(int code, Throwable e) {
        super(e);
        this.code = code;
        this.message = MessageUtils.getMessage(code);
    }

    public AdminUnauthorizedException(int code, Throwable e, String... params) {
        super(e);
        this.code = code;
        this.message = MessageUtils.getMessage(code, params);
    }

    public AdminUnauthorizedException(String message) {
        super(message);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.message = message;
    }

    public AdminUnauthorizedException(String message, Throwable e) {
        super(message, e);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.message = message;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
