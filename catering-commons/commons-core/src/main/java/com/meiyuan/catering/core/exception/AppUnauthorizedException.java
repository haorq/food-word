package com.meiyuan.catering.core.exception;

import com.meiyuan.catering.core.util.MessageUtils;

/**
 * @author mt
 * @description app无权限异常
 * @date 2020/4/318:19
 * @since v1.0.0
 */
public class AppUnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    public AppUnauthorizedException() {
        this.code = 401;
        this.message = "您的账号已发生变更";
    }

    public AppUnauthorizedException(int code) {
        this.code = code;
        this.message = MessageUtils.getMessage(code);
    }

    public AppUnauthorizedException(int code, String... params) {
        this.code = code;
        this.message = MessageUtils.getMessage(code, params);
    }

    public AppUnauthorizedException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public AppUnauthorizedException(int code, Throwable e) {
        super(e);
        this.code = code;
        this.message = MessageUtils.getMessage(code);
    }

    public AppUnauthorizedException(int code, Throwable e, String... params) {
        super(e);
        this.code = code;
        this.message = MessageUtils.getMessage(code, params);
    }

    public AppUnauthorizedException(String message) {
        super(message);
        this.code = 401;
        this.message = message;
    }

    public AppUnauthorizedException(String message, Throwable e) {
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
