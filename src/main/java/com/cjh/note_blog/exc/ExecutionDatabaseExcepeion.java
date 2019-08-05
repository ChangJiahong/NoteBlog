package com.cjh.note_blog.exc;

import com.cjh.note_blog.constant.StatusCode;

/**
 * ：
 * 执行数据库错误异常
 * @author ChangJiahong
 * @date 2019/7/18
 */
public class ExecutionDatabaseExcepeion extends StatusCodeException {

    public ExecutionDatabaseExcepeion() {
        super(StatusCode.ExecutionDatabaseError);
    }

    public ExecutionDatabaseExcepeion(String msg) {
        super(StatusCode.ExecutionDatabaseError, msg);
    }

    public ExecutionDatabaseExcepeion(Throwable cause) {
        super(StatusCode.ExecutionDatabaseError, cause);
    }
}
