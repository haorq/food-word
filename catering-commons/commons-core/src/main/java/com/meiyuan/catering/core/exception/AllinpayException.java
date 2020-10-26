package com.meiyuan.catering.core.exception;

import lombok.Data;

@Data
public class AllinpayException extends RuntimeException {


    private String code = "500";
    private String message = "失败";


    public AllinpayException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public AllinpayException(String message) {
        super(message);
    }

    public AllinpayException(Exception e) {
        super(e);
    }
}
