package com.cjh.note_blog.controller;

import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.pojo.VO.RestResponse;
import com.cjh.note_blog.service.IAccountService;
import com.cjh.note_blog.utils.PatternKit;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ：账号登录、注销
 *
 * @author ChangJiahong
 * @date 2019/7/16
 */

@RestController
@RequestMapping("/login")
public class AccountController extends BaseController{

    @Autowired
    private IAccountService accountService ;


    @PostMapping
    public RestResponse doLogin(@RequestParam String name,
                                @RequestParam String password,
                                @RequestParam(required = false) String remeber_me,
                                HttpServletRequest request,
                                HttpServletResponse response){

        if (StringUtils.isBlank(name) || StringUtils.isBlank(password)){
            return RestResponse.fail(500, "参数缺失");
        }

        Result<User> result = null;

        if (PatternKit.isEmail(name)){
            // 邮箱登录
            result = accountService.loginByEmail(name, password);
        }else {
            // 用户名登录
            result = accountService.loginByUsername(name, password);
        }

        if (result.isSuccess()){
            // 登录成功
            User user = result.getData();
            // 保存用户信息到回话中
            request.getSession().setAttribute(WebConst.LOGIN_USER_KEY, user);

            // TODO: print log
            return RestResponse.ok(user);
        }else {
            // 登录失败
            return RestResponse.fail(result.getCode(), result.getMsg());
        }
    }



}
