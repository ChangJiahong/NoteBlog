package com.cjh.note_blog.app.account.controller;

import com.cjh.note_blog.app.account.model.UserModel;
import com.cjh.note_blog.app.account.service.IAccountService;
import com.cjh.note_blog.annotations.PassToken;
import com.cjh.note_blog.annotations.UserLoginToken;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.base.controller.BaseController;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Role;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.pojo.VO.RestResponse;
import com.cjh.note_blog.utils.MD5;
import com.cjh.note_blog.utils.PojoUtils;
import com.cjh.note_blog.utils.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * ：账号登录、注销
 *
 * @author ChangJiahong
 * @date 2019/7/16
 */
@Api(tags = "登录/注销/注册接口")
@Validated
@RestController
@RequestMapping
public class AccountController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private IAccountService accountService;


    /**
     * 【公共方法】【免验证】
     * 登录请求
     *
     * @param name       用户名|email
     * @param password   密码
     * @param remeber_me 是否记住（非必要）
     * @param request    请求对象
     * @param response   响应对象
     * @return 统一返回对象
     */
    @ApiOperation(value = "登录", notes = "用户登录通过用户名或email")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "用户名或email", dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "用户密码", dataType = "string", paramType = "form")
    })
    @PassToken
    @PostMapping("/login")
    public RestResponse doLogin(@NotBlank(message = "name不能为空")
                                @Size(min = 3, message = "用户名或邮箱长度最少为3个字符")
                                @RequestParam String name,
                                @NotBlank(message = "密码不能为空")
                                @Size(min = 6, max = 36, message = "密码长度应在6~36字符之间")
                                @RequestParam String password,
                                @RequestParam(required = false) String remeber_me,
                                HttpServletRequest request,
                                HttpServletResponse response) {

        // 从缓存中获取用户信息
        User user = webCacheService.getUserFromCache(name);

        if (user != null) {
            if (!user.getPassword().equals(MD5.MD5Encode(password))) {
                // 密码错误
                return RestResponse.fail(StatusCode.PasswordMistake);
            }
            // ok: 账号已登录
            LOGGER.info("[" + user.getEmail() + "]: Logged account！No login is required！");
            // 从缓存获取token
            String token = webCacheService.getUserTokenFromCache(user.getEmail());
            // 保存token到请求头中，返回给客户端
            response.setHeader("Authorization", token);
            // 允许从请求头中获取token
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            return RestResponse.ok(StatusCode.TheAccountHasBeenLoggedIn, user);
        }

        Result<User> result = accountService.loginByEmailOrUsername(name, password);

        if (result.isSuccess()) {
            // 登录成功
            user = result.getData();
            // 保存用户信息到回话中
            request.setAttribute(WebConst.USER_LOGIN, user);
            // 生成token
            String token = TokenUtil.generateToken(user);
            // 保存token到请求头中，返回给客户端
            response.setHeader("Authorization", token);
            // 允许从请求头中获取token
            response.setHeader("Access-Control-Expose-Headers", "Authorization");
            // 保存token到缓存中
            webCacheService.putUserTokenToCache(user.getEmail(), token);
            // 用户信息缓存
            webCacheService.putUserToCache(user);

            LOGGER.info("[" + user.getEmail() + "]: Login successful!");

            return RestResponse.ok(user);
        } else {
            // 登录失败
            LOGGER.info("[" + name + "]: Login failure!The error message: " + result.getMsg());
            return RestResponse.fail(result);
        }
    }

    /**
     * 【私有方法】【验证】
     * 【个人用户权限】
     * 获取登录用户信息
     * 基础接口，可以用来验证token是否有效
     *
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取登录用户信息", notes = "获取登录用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header")
    })
    @UserLoginToken
    @GetMapping("/user")
    public RestResponse user() {
        User user = getUser();
        UserModel userModel = PojoUtils.copyToModel(user, UserModel.class);
        if (userModel != null) {
            return RestResponse.ok(userModel);
        }
        return RestResponse.fail();
    }

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @return 统一返回对象
     */
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "string", paramType = "path")
    })
    @PassToken
    @GetMapping("/user/{username}")
    public RestResponse userByName(@PathVariable(value = "username") String username) {
        Result<UserModel> result = accountService.getUserByName(username);
        if (result.isSuccess()) {
            return RestResponse.ok(result);
        }
        return RestResponse.fail(result);
    }

    /**
     * 【私有方法】【验证】
     * 【个人用户权限】
     * 注销请求
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "注销", notes = "用户登出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header")
    })
    @UserLoginToken
    @PostMapping("/logout")
    public RestResponse logout(HttpServletRequest request, HttpServletResponse response) {
        User user = getUser();
        if (user != null) {
            request.removeAttribute(WebConst.USER_LOGIN);
            // 从缓存移除用户信息
            webCacheService.removeUserFromCache(user.getEmail());
            LOGGER.info("[" + user.getEmail() + "]: logout of success！");
            // 注销成功
            return RestResponse.ok(StatusCode.LogoutOfSuccess);
        }
        // 注销成功
        return RestResponse.ok(StatusCode.LogoutOfSuccess);
    }


    /**
     * 【私有方法】【验证】
     * 【管理员权限】
     * 注册 管理员以上级别用户才能注册账户
     *
     * @param user
     * @return
     */
    @ApiOperation(value = "注册", notes = "用户注册，管理员以上级别用户才能注册账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "身份令牌", dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "user", value = "用户实体", dataType = "User", paramType = "body")
    })
    @UserLoginToken(Role.ADMIN)
    @PostMapping("/register")
    public RestResponse register(@RequestBody User user) {
        // TODO: 注册
        return RestResponse.ok();
    }

}
