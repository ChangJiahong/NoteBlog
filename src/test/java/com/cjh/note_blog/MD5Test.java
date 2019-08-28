package com.cjh.note_blog;

import com.cjh.note_blog.utils.MD5;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ：
 *
 * @author ChangJiahong
 * @date 2019/7/17
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MD5Test {

    /**
     * 加密测试
     */
    @Test
    public void encode(){
        String source = "123456";
        String res = MD5.MD5Encode(source);
        System.out.println(res);
    }
}
