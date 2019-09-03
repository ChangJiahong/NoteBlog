package com.cjh.note_blog.exc;

import com.cjh.note_blog.constant.StatusCode;

/**
 * 非检查型异常
 * @author CJH
 * on 2019/3/13
 */

public class StatusCodeException extends RuntimeException{

    private StatusCode statusCode;

    public StatusCodeException(StatusCode statusCode) {
        this(statusCode, statusCode.msgCN());
    }

    public StatusCodeException(StatusCode statusCode, String msg) {
        super(msg);
        statusCode.setMsg(msg);
        this.statusCode = statusCode;
    }

    public StatusCodeException(StatusCode statusCode, Throwable cause) {
        super(statusCode.msgCN(), cause);
        this.statusCode = statusCode;
    }

    public StatusCode getStatusCode() {
        return statusCode;
    }
}
