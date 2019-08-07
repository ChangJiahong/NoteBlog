package com.cjh.note_blog.pojo.VO;

import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.pojo.BO.Result;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* 应用对象
* 业务层返回对象
* @author ChangJiahong
* @date 2019/7/16
*/
public class RestResponse<T> {

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private T data;

    /**
     * 服务器响应时间
     */
    private long timestamp;

    /**
     * 两个参数
     * status、msg
     * @param status
     * @param msg
     */
    private RestResponse(int status, String msg){
        this(status, msg, null);
    }

    /**
     * 三个参数
     * @param status
     * @param msg
     * @param data
     */
    private RestResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
        this.timestamp = System.currentTimeMillis() / 1000;
    }


    /**
     * 请求成功 默认返回
     * @return
     */
    public static RestResponse ok() {
        return ok(StatusCode.OK);
    }

    /**
     * 返回状态码 无数据
     * @param statusCode
     * @return
     */
    public static RestResponse ok(StatusCode statusCode) {
        return new RestResponse(statusCode.code(),statusCode.msgCN());
    }

    /**
     * 返回数据 状态码默认ok
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> ok(T data) {
        return ok(StatusCode.OK, data);
    }

    public static <T> RestResponse<T> ok(Result<T> result){
        return new RestResponse<T>(result.getCode(), result.getMsg(), result.getData());
    }

    /**
     * 返回状态码 和 数据
     * @param statusCode
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> ok(StatusCode statusCode, T data) {
        return new RestResponse<T>(statusCode.code(), statusCode.msgCN(), data);
    }


    /**
     * [失败] 请求失败 默认返回
     * @return
     */
    public static RestResponse fail() {
        return fail(StatusCode.FAIL);
    }

    /**
     * [失败]
     * @param result
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> fail(Result<T> result){
        return new RestResponse<T>(result.getCode(), result.getMsg(), result.getData());
    }

    /**
     * [失败] 返回状态码 无数据
     * @param statusCode
     * @return
     */
    public static RestResponse fail(StatusCode statusCode) {
        return new RestResponse(statusCode.code(),statusCode.msgCN());
    }

    /**
     * [失败] 返回数据 状态码默认 fail
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> fail(T data) {
        return fail(StatusCode.FAIL, data);
    }

    /**
     * [失败] 返回状态码 和 数据
     * @param statusCode
     * @param data
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> fail(StatusCode statusCode, T data) {
        return new RestResponse<T>(statusCode.code(), statusCode.msgCN(), data);
    }

    /**
     * 未知错误
     * @param code
     * @param msg
     * @return
     */
    public static RestResponse fail(int code, String msg){
        return new RestResponse(code, msg);
    }


    public Boolean isOK() {
        return StatusCode.isSuccess(this.status);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }



}
