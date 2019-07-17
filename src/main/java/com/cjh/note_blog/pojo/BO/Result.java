package com.cjh.note_blog.pojo.BO;

/**
 * 业务对象
 * 服务层 向 业务层返回
 * @author ChangJiahong
 * @date 2019/7/16
 */
public class Result<T> {

    private Boolean success ;

    private Integer code ;

    private String msg ;

    private T data ;

    public Result(){
        this(true);
    }

    public Result(boolean success){
        this(success, "");
    }

    public Result(boolean success, String msg){
        this(success, msg, null);
    }

    public Result(boolean success, String msg, T data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public Result(Boolean success, Integer code, String msg){
        this(success, code, msg, null);
    }

    public Result(Boolean success, Integer code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    public static Result ok(){
        return new Result();
    }

    public static Result ok(String msg){
        return new Result(true, msg);
    }

    public static <T> Result ok(String msg, T data){
        return new Result<T>(true, msg, data);
    }

    public static <T> Result ok(T data){
        return new Result<T>(true, "", data);
    }

    public static Result fail(){
        return new Result(false);
    }

    public static Result fail(String msg){
        return fail(500, msg);
    }

    public static Result fail(int code, String msg){
        return new Result(false, code, msg);
    }

    public static <T> Result fial(String msg, T data){
        return fial(500, msg, data);
    }

    public static <T> Result fial(int code, String msg, T data){
        return new Result<T>(false,code, msg, data);
    }
    public Boolean isSuccess() {
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
