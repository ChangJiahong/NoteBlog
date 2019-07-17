package com.cjh.note_blog.exc;

/**
 * @author CJH
 * on 2019/3/13
 */

public class MyException extends RuntimeException{

    public MyException() {
    }

    public MyException(String message) {
        super(message);
    }

    public MyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyException(Throwable cause) {
        super(cause);
    }
}
