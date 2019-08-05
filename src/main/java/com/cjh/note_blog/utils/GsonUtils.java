package com.cjh.note_blog.utils;

import com.cjh.note_blog.pojo.DO.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.io.IOException;

/**
 * ：
 *  gson 工具类
 * @author CJH
 * on 2019/3/14
 */

public class GsonUtils {

//    private static final Gson gson = new Gson();


    /**
     * 对象转josn字符串
     * @param object
     * @return
     */
    public static String toJsonString(Object object){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
//        return object==null?null:gson.toJson(object);
    }


    /**
     * 反序列化
     */
    public static <T> T json2Obj(String json, Class<T> tClass){

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        //Json的解析类对象
//        JsonParser parser = new JsonParser();
//        parser.parse(json).getAsJsonObject();
//        return gson.fromJson(json, tClass);
    }
}
