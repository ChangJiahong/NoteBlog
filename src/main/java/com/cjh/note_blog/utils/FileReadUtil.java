package com.cjh.note_blog.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/8/6
 */
public class FileReadUtil {


    public static String readAll(String s) throws UnsupportedEncodingException {
        InputStream stream = FileReadUtil.class.getClassLoader().getResourceAsStream(s);
        assert stream != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

        List<String> list = reader.lines().collect(Collectors.toList());
        StringBuilder content = new StringBuilder();

        for (String st : list){
            content.append(st).append("\n");
        }

        return content.toString();
    }
}
