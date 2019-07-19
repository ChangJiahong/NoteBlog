package com.cjh.note_blog.handler;

import com.cjh.note_blog.Cache.service.ICache;
import com.cjh.note_blog.annotations.PassToken;
import com.cjh.note_blog.annotations.UserLoginToken;
import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.constant.WebConst;
import com.cjh.note_blog.pojo.BO.Result;
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

/**
 * url 拦截器
 * @author CJH
 * on 2019/3/13
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {

    private static final Logger LOGGE = LoggerFactory.getLogger(BaseInterceptor.class);

    private static final String USER_AGENT = "user-agent";

    @Autowired
    private ICache cache ;

    /**
     * 开始拦截 调用处理程序之前
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        // 判断跨域请求
        response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
        // 允许携带cookie
        response.setHeader("Access-Control-Allow-Credentials","true");

        String contextPath = request.getContextPath();

        String uri = request.getRequestURI();

        // 不是映射到方法的可以通过
        if (!(handler instanceof HandlerMethod)){
            LOGGE.info("["+uri+"]:未映射路径，默认跳过验证");
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Class cls = handlerMethod.getBeanType();
        boolean isPassToken = false;

        /*
            方法级优先级比类级的高，
            方法级没有说明按类级定义
            类级没有说明，默认为 Not pass
         */
        // 类级 验证是否有passToken注解
        isPassToken = isPassToken(cls, isPassToken);

        Method method=handlerMethod.getMethod();
        // 方法级 验证是否有passToken注解
        isPassToken = isPassToken(method, isPassToken);


        if (isPassToken){
            LOGGE.info("["+uri+"]:跳过验证");
            return true;
        }


        /*
            token = "Bearer "+token
         */
        String token = request.getHeader("token");

        if (StringUtils.isBlank(token)){
            token = request.getHeader("Authorization");
            if (StringUtils.isNotBlank(token) && token.startsWith("Bearer ")) {
                //如果header中存在token，则覆盖掉url中的token
                token = token.substring("Bearer ".length()); // "Bearer "之后的内容
            }
        }

        LOGGE.info("来路地址："+uri);
        LOGGE.info("contextPath："+contextPath);
        LOGGE.info("token:"+token);

        Result result = TokenUtil.checkToken(token);

        if (result.isSuccess()){
            // 身份验证成功
            // 允许访问
            // 保存user
            String email = (String) result.getData();
            // 缓存获取 用户信息，没有则返回登录失效
            User user = cache.get(email);
            if (user != null){
                request.setAttribute(WebConst.LOGIN_USER_KEY, user);
                return true;
            }
            // error: 登录状态失效
            result = Result.fail(StatusCode.LogonStateFailure);
        }

        response.setHeader("Content-type", "text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        // 身份验证失败
        // 没有访问权限
        out.print(GsonUtils.toJsonString(RestResponse.fail(result)));
        return false;
    }

    private boolean isPassToken(AnnotatedElement method, boolean isPassToken) {
        if (method.isAnnotationPresent(PassToken.class)){
            PassToken passToken = (PassToken) method.getAnnotation(PassToken.class);
            isPassToken = passToken.required();
        }else if (method.isAnnotationPresent(UserLoginToken.class)){
            UserLoginToken userLoginToken = (UserLoginToken) method.getAnnotation(UserLoginToken.class);
            isPassToken = !userLoginToken.required();
        }
        return isPassToken;
    }


    /**
     * 结束 调用处理程序之后
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 请求结束
        // 清除用户信息
        request.removeAttribute(WebConst.LOGIN_USER_KEY);

    }

    /**
     * 视图渲染之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
