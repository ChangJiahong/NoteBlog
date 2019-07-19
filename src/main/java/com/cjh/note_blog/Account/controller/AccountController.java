package com.cjh.note_blog.Account.controller;

import com.cjh.note_blog.Account.service.IAccountService;
import com.cjh.note_blog.annotations.PassToken;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.controller.BaseController;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.pojo.VO.RestResponse;
import com.cjh.note_blog.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping
public class AccountController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private IAccountService accountService ;


    /**
     * 登录请求
     * @param name 用户名|email
     * @param password 密码
     * @param remeber_me 是否记住（非必要）
     * @param request
     * @param response
     * @return
     */
    @PassToken
    @PostMapping("/login")
    public RestResponse doLogin(@RequestParam String name,
                                @RequestParam String password,
                                @RequestParam(required = false) String remeber_me,
                                HttpServletRequest request,
                                HttpServletResponse response){

        if (StringUtils.isBlank(name) || StringUtils.isBlank(password)){
            // error: 参数缺失
            return RestResponse.fail(StatusCode.ParametersAreMissing);
        }

        User user = getUser(request);

        if (user != null){
            // ok: 账号已登录
            LOGGER.info("["+user.getEmail()+"]: Logged account！No login is required！");
            return RestResponse.ok(StatusCode.TheAccountHasBeenLoggedIn,user);
        }

        Result<User> result = accountService.loginByEmailOrUsername(name, password);

        if (result.isSuccess()){
            // 登录成功
            user = result.getData();
            // 保存用户信息到回话中
            request.getSession().setAttribute(WebConst.LOGIN_USER_KEY, user);
            // 生成token
            String token = TokenUtil.generateToken(user);
            // 保存token到请求头中，返回给客户端
            response.setHeader("Authorization", token);
            // 保存token到缓存中
            cache.set(user.getEmail(), user);

            LOGGER.info("["+user.getEmail()+"]: Login successful!");

            return RestResponse.ok(user);
        }else {
            // 登录失败
            LOGGER.info("["+name+"]: Login failure!The error message: "+result.getMsg());
            return RestResponse.fail(result);
        }
    }


    /**
     * 注销请求
     * @param request
     * @param response
     * @return
     */
    @PassToken
    @PostMapping("/logout")
    public RestResponse logout(HttpServletRequest request, HttpServletResponse response){
        User user = getUser(request);
        if (user != null){
            request.getSession().removeAttribute(WebConst.LOGIN_USER_KEY);
            LOGGER.info("["+user.getEmail()+"]: Cancellation of success！");
            // 注销成功
            return RestResponse.ok(StatusCode.LogoutOfSuccess);
        }
        // 注销成功
        return RestResponse.ok(StatusCode.LogoutOfSuccess);
    }


}
