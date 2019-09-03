package com.cjh.note_blog.utils;

import java.util.UUID;

/**
 * ：
 * 网站工具类
 * @author ChangJiahong
 * @date 2019/7/19
 */
public class WebUtils {


    public static String getUUID(){
        UUID uuid= UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }
}
