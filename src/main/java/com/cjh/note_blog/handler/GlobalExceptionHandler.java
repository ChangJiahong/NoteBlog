package com.cjh.note_blog.handler;

import com.cjh.note_blog.constant.StatusCode;
import com.cjh.note_blog.exc.ExecutionDatabaseExcepeion;
import com.cjh.note_blog.exc.MyException;
import com.cjh.note_blog.pojo.VO.RestResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 全局异常处理
 * @author CJH
 * on 2019/3/14
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = MyException.class)
    public RestResponse tipException(Exception e) {
        LOGGER.error("find myException:e={}",e.getMessage());
        e.printStackTrace();
        return RestResponse.fail(500, e.getMessage());
    }

    /**
     * 普通异常
     * 服务器运行异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    public RestResponse exception(Exception e){
        LOGGER.error("find exception:e={}",e.getMessage());
        e.printStackTrace();
        return RestResponse.fail(StatusCode.ServerError, e.getMessage());
    }

    /**
     * 参数校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public RestResponse constraintViolationException(ConstraintViolationException e){
        String er = e.getConstraintViolations().stream().map(x -> x.getMessage()).collect(Collectors.joining(", ", "{", "}"));

//        StringUtils.join(",", e.getConstraintViolations());
        LOGGER.error("find exception:e={}",er);
//        e.printStackTrace();
        // 参数验证错误
        return RestResponse.fail(StatusCode.ParameterVerificationError, er);
    }

    /**
     * 参数缺失异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public RestResponse missingServletRequestParameterException(Exception e){
        LOGGER.error("find missingServletRequestParameterException:e={}",e.getMessage());
//        e.printStackTrace();
        return RestResponse.fail(StatusCode.ParametersAreMissing, e.getMessage());
    }

    /**
     * 请求格式错误
     * @param e
     * @return
     */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public RestResponse httpMessageNotReadableException(Exception e){
        LOGGER.error("find httpMessageNotReadableException:e={}",e.getMessage());
//        e.printStackTrace();
        return RestResponse.fail(404, "请求格式不正确");
    }

    /**
     * 没有找到页面异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public RestResponse httpRequestMethodNotSupportedException(Exception e){
        LOGGER.error("find httpRequestMethodNotSupportedException:e={}",e.getMessage());
//        e.printStackTrace();
        return RestResponse.fail(404, e.getMessage());
    }

    /**
     * 执行数据库错误
     * @param e
     * @return
     */
    @ExceptionHandler(value = {ExecutionDatabaseExcepeion.class})
    public RestResponse executionDatabaseExcepeion(Exception e){
        LOGGER.error("find executionDatabaseExcepeion:e={}",e.getMessage());
//        e.printStackTrace();
        return RestResponse.fail(StatusCode.ExecutionDatabaseError, e.getMessage());
    }


    /**
     * token不合法
     * @param e
     * @return
     */
    @ExceptionHandler(value = {MalformedJwtException.class})
    public RestResponse malformedJwtException(MalformedJwtException e){
        LOGGER.error("find malformedJwtException:e={}", e.getMessage());
        e.printStackTrace();
        return RestResponse.fail(StatusCode.TokenIsNotValid, e.getMessage());
    }

    /**
     * token过期
     * @param e
     * @return
     */
    @ExceptionHandler(value = {ExpiredJwtException.class})
    public RestResponse expiredJwtException(ExpiredJwtException e){
        LOGGER.error("find expiredJwtException:e={}", e.getMessage());
        e.printStackTrace();
        return RestResponse.fail(StatusCode.TokenExpired, e.getMessage());
    }


}
