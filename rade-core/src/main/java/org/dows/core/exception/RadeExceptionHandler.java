package org.dows.core.exception;

import cn.hutool.core.util.ObjUtil;
import org.dows.core.web.Response;
import lombok.extern.slf4j.Slf4j;
//import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 */
@RestControllerAdvice
@Slf4j
public class RadeExceptionHandler {

    @ExceptionHandler(RadeException.class)
    public Response handleRRException(RadeException e) {
        Response response = new Response();
        if (ObjUtil.isNotEmpty(e.getData())) {
            response.put("data", e.getData());
        } else {
            response.put("code", e.getCode());
            response.put("message", e.getMessage());
        }
        if (ObjUtil.isNotEmpty(e.getCause())) {
            log.error(e.getCause().getMessage(), e.getCause());
        }
        return response;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Response handleDuplicateKeyException(DuplicateKeyException e) {
        log.error(e.getMessage(), e);
        return Response.error("已存在该记录或值不能重复");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Response handleBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage(), e);
        return Response.error("账户密码不正确");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        return Response.error("不支持该请求方式，请区分POST、GET等请求方式是否正确");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Response handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        return Response.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Response.error();
    }

/*    @ExceptionHandler(WxErrorException.class)
    public Response handleException(WxErrorException e) {
        log.error(e.getMessage(), e);
        return Response.error(e.getMessage());
    }*/
}
