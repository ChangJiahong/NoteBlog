package com.cjh.note_blog.pojo.VO;

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

    public static Map<Integer,String> messageMap = new HashMap();
    //初始化状态码与文字说明
    static {
        /* 成功状态码 */
        messageMap.put(200, "OK");

        /* 服务器错误 */
        messageMap.put(1000,"服务器错误");

        /* 参数错误：10001-19999 */
        messageMap.put(10001, "参数无效");
        messageMap.put(10002, "参数为空");
        messageMap.put(10003, "参数类型错误");
        messageMap.put(10004, "参数缺失");

        /* 用户错误：20001-29999*/
        messageMap.put(20001, "用户未登录");
        messageMap.put(20002, "账号不存在或密码错误");
        messageMap.put(20003, "账号已被禁用");
        messageMap.put(20004, "用户不存在");
        messageMap.put(20005, "用户已存在");
        messageMap.put(20006,"没有访问权限");

        /* 业务错误：30001-39999 */
        messageMap.put(30001, "某业务出现问题");
        messageMap.put(30002,"数据审核提交失败");

        /* 系统错误：40001-49999 */
        messageMap.put(40001, "系统繁忙，请稍后重试");

        /* 数据错误：50001-599999 */
        messageMap.put(50001, "数据未找到");
        messageMap.put(50002, "数据有误");
        messageMap.put(50003, "数据已存在");
        messageMap.put(50004,"查询出错");
        messageMap.put(50005,"执行数据库错误");

        /* 接口错误：60001-69999 */
        messageMap.put(60001, "内部系统接口调用异常");
        messageMap.put(60002, "外部系统接口调用异常");
        messageMap.put(60003, "该接口禁止访问");
        messageMap.put(60004, "接口地址无效");
        messageMap.put(60005, "接口请求超时");
        messageMap.put(60006, "接口负载过高");

        /* 权限错误：70001-79999 */
        messageMap.put(70001, "无权限访问");
    }


    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

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

    public static <T> RestResponse build(Integer status, String msg, T data) {
        return new RestResponse<T>(status, msg, data);
    }

    public static <T> RestResponse build(Integer status, T data) {
        return new RestResponse<T>(status, data);
    }

    public static <T> RestResponse ok(T data) {
        return new RestResponse<T>(data);
    }

    public static RestResponse ok() {
        return new RestResponse();
    }

    public static <T> RestResponse fail(String msg) {
        return new RestResponse<T>(500, msg, null);
    }

    public static <T> RestResponse fail(int status, String msg) {
        return new RestResponse<T>(status, msg, null);
    }

    public static <T> RestResponse fail(int status) {
        return new RestResponse<T>(status, null);
    }

    /**
     * 无参
     */
    public RestResponse() {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.status = 200;
    }

    public RestResponse(Integer status, String msg, T data) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public RestResponse(Integer status, T data) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.status = status;
        this.msg = messageMap.get(status);
        this.data = data;
    }

    public RestResponse(T data) {
        this.timestamp = System.currentTimeMillis() / 1000;
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }



    public Boolean isOK() {
        return this.status == 200;
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

    public Object getData() {
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

    /**
     *
     * @Description: 将json结果集转化为LeeJSONResult对象
     * 				需要转换的对象是一个类
     * @param jsonData
     * @param clazz
     * @return
     *
     * @author leechenxiang
     * @date 2016年4月22日 下午8:34:58
     */
    public static RestResponse formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, RestResponse.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @Description: 没有object对象的转化
     * @param json
     * @return
     *
     * @author leechenxiang
     * @date 2016年4月22日 下午8:35:21
     */
    public static RestResponse format(String json) {
        try {
            return MAPPER.readValue(json, RestResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @Description: Object是集合转化
     * 				需要转换的对象是一个list
     * @param jsonData
     * @param clazz
     * @return
     *
     * @author leechenxiang
     * @date 2016年4月22日 下午8:35:31
     */
    public static RestResponse formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

}
