package com.cjh.note_blog.pojo.BO;

import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.pojo.VO.RestResponse;

/**
 * 业务对象
 * 服务层 向 业务层返回
 * @author ChangJiahong
 * @date 2019/7/16
 */
public class Result<T> {

    /**
     * 返回标志
     */
    private Boolean success ;

    /**
     * 状态码 + 状态信息
     */
    private StatusCode statusCode;

    /**
     * 如果有数据返回，表示返回的数据对象
     */
    private T data ;

    private Result(boolean success){
        this(success, StatusCode.OK);
    }

    /**
     *
     * @param success
     * @param statusCode
     */
    private Result(boolean success, StatusCode statusCode){
        this(success, statusCode, null);
    }

    /**
     *
     * @param success
     * @param statusCode
     * @param data
     */
    private Result(boolean success, StatusCode statusCode, T data) {
        this.success = success;
        this.statusCode = statusCode;
        this.data = data;
    }



    /**
     * 请求成功 默认返回
     * @return
     */
    public static Result ok() {
        return ok(StatusCode.OK);
    }

    /**
     * 返回状态码 无数据
     * @param statusCode
     * @return
     */
    public static Result ok(StatusCode statusCode) {
        return new Result(true, statusCode);
    }

    /**
     * 返回数据 状态码默认ok
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result ok(T data) {
        return ok(StatusCode.OK, data);
    }

    /**
     * 返回状态码 和 数据
     * @param statusCode
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result ok(StatusCode statusCode, T data) {
        return new Result<T>(true, statusCode, data);
    }


    /**
     * [失败] 请求失败 默认返回
     * @return
     */
    public static Result fail() {
        return fail(StatusCode.FAIL);
    }

    /**
     * [失败] 一般错误， 返回错误信息
     * @return
     */
    public static Result fail(String msg) {
        return fail(StatusCode.FAIL);
    }

    /**
     * [失败] 返回状态码 无数据
     * @param statusCode
     * @return
     */
    public static Result fail(StatusCode statusCode) {
        return new Result(false, statusCode);
    }

    /**
     * [失败] 返回数据 状态码默认 fail
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result fail(T data) {
        return fail(StatusCode.FAIL, data);
    }

    /**
     * [失败] 返回状态码 和 数据
     * @param statusCode
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result fail(StatusCode statusCode, T data) {
        return new Result<T>(false, statusCode, data);
    }


    public Boolean isSuccess() {
        return success;
    }

    /**
     * 获取状态码
     * @return
     */
    public StatusCode getStatusCode() {
        return statusCode;
    }

    /**
     * 设置状态码
     * @param statusCode
     */
    public void setStatusCode(StatusCode statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * 获取数据
     * @return
     */
    public T getData() {
        return data;
    }

    /**
     * 设置数据
     * @param data
     */
    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return this.statusCode.code();
    }

    public String getMsg() {
        return this.statusCode.msgCN();
    }
}
