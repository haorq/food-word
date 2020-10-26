package com.meiyuan.catering.core.exception;


import com.meiyuan.catering.core.util.MessageUtils;
import com.meiyuan.catering.core.util.Result;


/**
 * 自定义异常
 *
 * @author admin
 */
public class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    /**
     * 描述: 解决某些特殊情况,错误返回需要返回数据的情况
     *
     * @date 2020/6/29 16:26
     * @since v1.2.0
     */
    private Object data;

    public CustomException(int code) {
        super(ErrorCode.INTERNAL_SERVER_ERROR_MSG);
        this.code = code;
        this.msg = ErrorCode.INTERNAL_SERVER_ERROR_MSG;
    }

    public CustomException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public CustomException(int code, String msg, Object data) {
        super(msg);
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public CustomException(int code, Throwable e) {
        super(e);
        this.code = code;
        this.msg = MessageUtils.getMessage(code);
    }

    public CustomException(int code, Throwable e, String... params) {
        super(e);
        this.code = code;
        this.msg = MessageUtils.getMessage(code, params);
    }

    public CustomException(String msg) {
        super(msg);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
    }

    public CustomException(String msg, Throwable e) {
        super(msg, e);
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
    }

    public CustomException(Result result) {
        this.code = result.getCode();
        this.msg = result.getMsg();
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
