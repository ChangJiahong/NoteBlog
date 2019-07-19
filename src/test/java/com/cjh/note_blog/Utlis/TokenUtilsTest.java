package com.cjh.note_blog.Utlis;

import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.utils.GsonUtils;
import com.cjh.note_blog.utils.TokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * ：
 * token 测试
 * @author ChangJiahong
 * @date 2019/7/18
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class TokenUtilsTest {

    @Test
    public void GenerateToken(){
        System.out.println(TokenUtil.getSecret());
        System.out.println(TokenUtil.getExpiration());
    }

    @Test
    public void checkToken(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyMzI3MDg1MTU0QHFxLmNvbSIsImNyZWF0ZWQiOjE1NjM1MDQ5MjkyMjEsInJvbGVzIjpbeyJyaWQiOjEsInJuYW1lIjoic3VwZXJBZG1pbiIsImNyZWF0ZWQiOjE1NjMyNTgzMzUwMDB9LHsicmlkIjoyLCJybmFtZSI6ImFkbWluIiwiY3JlYXRlZCI6MTU2MzI1ODY3OTAwMH0seyJyaWQiOjMsInJuYW1lIjoidXNlciIsImNyZWF0ZWQiOjE1NjMyNTg3MTUwMDB9XSwiaWQiOjEsImV4cCI6MTU2NDEwOTcyOSwidXNlciI6eyJ1aWQiOjEsInVzZXJuYW1lIjoiYWRtaW4iLCJlbWFpbCI6IjIzMjcwODUxNTRAcXEuY29tIiwicGFzc3dvcmQiOiJlMTBhZGMzOTQ5YmE1OWFiYmU1NmUwNTdmMjBmODgzZSIsImltZ1VybCI6Imh0dHBzOi8vYXZhdGFyczEuZ2l0aHVidXNlcmNvbnRlbnQuY29tL3UvMjQ2MDM0ODE_cz00MDAmdT00NTIzMTUyZWQwOWQ2Nzk3NzViMzE3NWUxNTgxZjA1MzU3ZDJiM2I0JnY9NCIsInNleCI6dHJ1ZSwiYWdlIjoyMiwiY3JlYXRlZCI6MTU2MzMzMDQ5ODAwMCwicm9sZXMiOlt7InJpZCI6MSwicm5hbWUiOiJzdXBlckFkbWluIiwiY3JlYXRlZCI6MTU2MzI1ODMzNTAwMH0seyJyaWQiOjIsInJuYW1lIjoiYWRtaW4iLCJjcmVhdGVkIjoxNTYzMjU4Njc5MDAwfSx7InJpZCI6Mywicm5hbWUiOiJ1c2VyIiwiY3JlYXRlZCI6MTU2MzI1ODcxNTAwMH1dfX0.OZUxKha8L430P8QSkPggwHHdF91cZPSObNWerKnnr6Y";


        User user = new User();

        user.setEmail("2327085154@qq.com");
        user.setUsername("cjh");

        token = TokenUtil.generateToken(user);

        System.out.println(token);

        Result result = TokenUtil.checkToken(token);
        if (result.isSuccess()){
            System.out.println(GsonUtils.toJsonString(result.getData()));
            user = (User) result.getData();
            System.out.println(user.getEmail());
        }else {
            System.out.println(result.getMsg());
        }


    }

    @Test
    public void test(){
        User user = new User();
        user.setEmail("2327085154@qq.com");
        user.setUsername("cjh");
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        user.setImgUrl("https://avatars1.githubusercontent.com/u/24603481?s=400&u=4523152ed09d679775b3175e1581f05357d2b3b4&v=4");
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);

        System.out.println(map.get("user"));
    }
}
