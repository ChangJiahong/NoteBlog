package com.cjh.note_blog.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * ：
 *  gson 工具类
 * @author CJH
 * on 2019/3/14
 */

public class GsonUtils {

    private static final Gson gson = new Gson();

    /**
     * 对象转josn字符串
     * @param object
     * @return
     */
    public static String toJsonString(Object object){
        return object==null?null:gson.toJson(object);
    }


    /**
     * 反序列化
     */
    public static <T> T json2Obj(String json, Class<T> tClass){
        //Json的解析类对象
        JsonParser parser = new JsonParser();
        parser.parse(json).getAsJsonObject();
        return gson.fromJson(json, tClass);
    }
}
