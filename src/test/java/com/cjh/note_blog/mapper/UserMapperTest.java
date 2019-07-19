package com.cjh.note_blog.mapper;

import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.pojo.VO.RestResponse;
import com.cjh.note_blog.utils.GsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * ：
 *  userMapper 测试
 * @author ChangJiahong
 * @date 2019/7/17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTest {

    @Autowired
    UserMapper userMapper ;

    @Test
    public void selectUserByEmial(){
        User user = userMapper.selectUserByEmial("2327085154@qq.com");
        System.out.println(GsonUtils.toJsonString(user));

    }
}
