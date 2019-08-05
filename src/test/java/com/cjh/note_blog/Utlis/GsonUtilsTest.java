package com.cjh.note_blog.Utlis;

import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.utils.GsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

/**
 * :
 *
 * @author ChangJiahong
 * @date 2019/8/5
 */
public class GsonUtilsTest {

    @Test
    public void parseJson() throws IOException {
        String json = "{\n" +
                "        \"uid\": 1,\n" +
                "        \"username\": \"admin\",\n" +
                "        \"email\": \"2327085154@qq.com\",\n" +
                "        \"imgUrl\": \"https://avatars1.githubusercontent.com/u/24603481?s=400&u=4523152ed09d679775b3175e1581f05357d2b3b4&v=4\",\n" +
                "        \"sex\": true,\n" +
                "        \"age\": 22,\n" +
                "        \"created\": \"2019-07-17T02:28:18.000+0000\",\n" +
                "        \"roles\": [\n" +
                "            \"user\"\n" +
                "        ]\n" +
                "    }";

        /**
         * ObjectMapper支持从byte[]、File、InputStream、字符串等数据的JSON反序列化。
         */
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.readValue(json, User.class);
        System.out.println(user);

        System.out.println(GsonUtils.toJsonString(user));
    }


}
