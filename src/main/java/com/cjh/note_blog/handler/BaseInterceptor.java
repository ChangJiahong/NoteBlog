package com.cjh.note_blog.handler;

import com.cjh.note_blog.annotations.AllowHeaders;
import com.cjh.note_blog.app.cache.service.ICacheService;
import com.cjh.note_blog.annotations.PassToken;
import com.cjh.note_blog.annotations.UserLoginToken;
import com.cjh.note_blog.conf.WebConfig;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.pojo.BO.Result;
import com.cjh.note_blog.pojo.DO.Role;
import com.cjh.note_blog.pojo.DO.User;
import com.cjh.note_blog.pojo.VO.RestResponse;
import com.cjh.note_blog.utils.GsonUtils;
import com.cjh.note_blog.utils.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * url 拦截器
 *
 * @author CJH
 * on 2019/3/13
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {

    private static final Logger LOGGE = LoggerFactory.getLogger(BaseInterceptor.class);

    private static final String USER_AGENT = "user-agent";

    @Autowired
    private ICacheService webCacheService;

    /**
     * 需要验证的权限
     */
    private String requiredRole;

    @Autowired
    private WebConfig webConfig;

    /**
     * 开始拦截 调用处理程序之前
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        // 判断跨域请求
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        // 默认允许携带token
        response.setHeader("Access-Control-Allow-Methods", "POST,GET,DELETE");

        String contextPath = request.getContextPath();

//        webConfig.root = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();

        String uri = request.getRequestURI() + " - " + request.getMethod();

        // 不是映射到方法的可以通过
        if (!(handler instanceof HandlerMethod)) {
            LOGGE.info("[" + uri + "]:未映射路径，默认跳过验证");
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 检查是否需要验证token
        boolean isPassToken = checkPassToken(handlerMethod);

        List<String> headers = allowHeaders(handlerMethod);
        response.addHeader("Access-Control-Allow-Headers", StringUtils.join(headers, ","));


        /*
            从请求头
            获取token
         */
        String token = TokenUtil.getTokenFromRequest(request);

        LOGGE.info("来路地址：" + uri);
        LOGGE.info("contextPath：" + contextPath);
        LOGGE.info("token:" + token);

        // 验证用户身份
        Result result = verifyUser(token, request);
        if (result.isSuccess()){
            User user = (User) result.getData();
            request.setAttribute(WebConst.USER_LOGIN, user);
            LOGGE.info("[" + uri + "]:欢迎 "+user.getUsername()+" 用户");
            return true;
        }else if (isPassToken){
            LOGGE.info("[" + uri + "]:免验证");
            LOGGE.info("[" + uri + "]:匿名访问");
            return true;
        }

        response.setHeader("Content-type", "text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        // 身份验证失败
        // 没有访问权限
        out.print(GsonUtils.toJsonString(RestResponse.fail(result)));
        return false;
    }

    private boolean compareRole(List<String> roleList, String requiredRole) {
        boolean bingo = false;
        int b = getIntegerForRole(requiredRole);

        for (String role : roleList) {
            int a = getIntegerForRole(role);
            if (a >= b) {
                bingo = true;
            }
        }
        return bingo;
    }

    /**
     * 验证user
     * @param token
     * @param request
     * @return
     */
    Result verifyUser(String token, HttpServletRequest request) {
        // 验证解析token
        Result result = TokenUtil.checkToken(token);

        if (result.isSuccess()) {
            // 身份验证成功
            // 允许访问
            // 保存user
            String email = (String) result.getData();
            // 缓存获取token信息
            String uToken = webCacheService.getUserTokenFromCache(email);
            // 缓存获取 用户信息，没有则返回登录失效
            User user = webCacheService.getUserFromCache(email);
            // 缓存中有该用户令牌才有效
            if (token.equals(uToken) && user != null) {
                // 验证权限
                boolean bingo = compareRole(user.getRoles(), requiredRole);

                if (bingo) {
                    return Result.ok(user);
                }
                // error: 用户权限不足
                return Result.fail(StatusCode.InadequateUserRights);

            } else {
                // error: 登录状态失效
                return Result.fail(StatusCode.LogonStateFailure);
            }
        }
        return result;
    }

    /**
     * 获得角色值
     *
     * @param role
     * @return
     */
    private int getIntegerForRole(String role) {
        switch (role) {
            case Role.SUPER_ADMIN:
                return 3;
            case Role.ADMIN:
                return 2;
            case Role.USER:
                return 1;
            default:
                return 0;
        }
    }

    private List<String> allowHeaders(HandlerMethod handlerMethod) {
        List<String> headers = new ArrayList<>();
        headers.add("content-type");
        headers.add("token");
        headers.add("Authorization");
        Method cls = handlerMethod.getMethod();
        if (cls.isAnnotationPresent(AllowHeaders.class)) {
            AllowHeaders allowHeaders = (AllowHeaders) cls.getAnnotation(AllowHeaders.class);
            for (String s : allowHeaders.value()) {
                headers.add(s);
            }
        }
        return headers;
    }

    private boolean checkPassToken(HandlerMethod handlerMethod) {

        Class cls = handlerMethod.getBeanType();
        /*
            是否验证
            false 验证
            true 取消验证
         */
        boolean isPassToken = false;

        /*
            方法级优先级比类级的高，
            方法级没有说明按类级定义
            类级没有说明，默认为 Not pass
         */
        // 类级 验证是否有passToken注解
        isPassToken = isPassToken(cls, isPassToken);

        Method method = handlerMethod.getMethod();
        // 方法级 验证是否有passToken注解
        isPassToken = isPassToken(method, isPassToken);
        return isPassToken;
    }

    /**
     * 检查token注解
     *
     * @param ae
     * @param isPassToken
     * @return
     */
    private boolean isPassToken(AnnotatedElement ae, boolean isPassToken) {
        if (ae.isAnnotationPresent(UserLoginToken.class)) {
            // 有 UserLoginToken 注释，进行权限验证
            UserLoginToken userLoginToken = (UserLoginToken) ae.getAnnotation(UserLoginToken.class);
            requiredRole = userLoginToken.value();

            isPassToken = false;
        } else if (ae.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = (PassToken) ae.getAnnotation(PassToken.class);
            // 有 PassToken 注释，不要验证
            isPassToken = true;
        }
        return isPassToken;
    }


    /**
     * 结束 调用处理程序之后
     *
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 请求结束
        // 清除用户信息
        request.removeAttribute(WebConst.USER_LOGIN);
    }

    /**
     * 视图渲染之后
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
