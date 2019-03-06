package com.liuqi.tools.codelife.util.exceptions;

/**
 * @Author: LiuQI
 * @Created: 2018/3/17 22:22
 * @Version: V1.0
 **/
public class RestException extends Exception {
    private String code;
    private String message;
    private Throwable t;

    RestException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    RestException(String code, String message, Throwable t) {
        this.code = code;
        this.message = message;
        this.t = t;
    }

    public String getCode() {
        return code;
    }

    public RestException setCode(String code) {
        this.code = code;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public RestException setMessage(String message) {
        this.message = message;
        return this;
    }

    public Throwable getT() {
        return t;
    }

    public RestException setT(Throwable t) {
        this.t = t;
        return this;
    }
}
