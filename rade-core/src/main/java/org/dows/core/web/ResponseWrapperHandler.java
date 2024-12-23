package org.dows.core.web;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.dows.core.annotation.Skip;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 统一返回包装配置
 */

public class ResponseWrapperHandler implements HandlerMethodReturnValueHandler {


    @Resource
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;
    @Resource
    private ObjectMapper objectMapper;

    /**
     * 程序加载完毕后将配置的实例添加到 RequestMappingHandlerAdapter
     */
    @PostConstruct
    public void compare() {
        List<HandlerMethodReturnValueHandler> handlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> list = new ArrayList<>();
        list.add(this);
        // ！！！注意这里，需要将requestMappingHandlerAdapter 原有的返回值处理器添加进去，否则报错
        if (handlers != null) {
            list.addAll(handlers);
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(list);
    }

    /**
     * 判断是否要进行包装返回值
     * 类上没有@RestWrapper 注解，返回false, 则该类的所以方法都不进行包装
     * 类上加了@RestWrapper 注解，但方法上加了 @IgnoreWrapper 返回false 不进行包装
     *
     * @param methodParameter
     * @return
     */
    @Override
    public boolean supportsReturnType(MethodParameter methodParameter) {
        // TODO 优化 -> @EnableRestWrapper / DowsProperties
        if (!StrUtil.startWith(methodParameter.getDeclaringClass().getPackageName(), "org.dows")) {
            return false;
        }
        // 类上有@Skip 注解，直接返回false，不对返回值进行包装
        if (methodParameter.getContainingClass().isAnnotationPresent(Skip.class)) {
            return false;
        }
        // 如果是返回的就是response也不需要包装
        return !Response.class.isAssignableFrom(methodParameter.getParameterType());
    }

    /**
     * 处理返回值，进行包装！
     *
     * @param o
     * @param methodParameter
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @throws Exception
     */
    @Override
    public void handleReturnValue(Object o, MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest) throws Exception {

        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        if (null == response) {
            return;
        }
        Response<?> respBean;
        if (o instanceof Response) {
            // 如果返回值类型是 RespBean 就直接转成RespBean
            respBean = (Response<?>) o;
        } else {
            // 否则将该返回值设置为 RespBean类的data属性
            respBean = Response.ok(o);
        }
        modelAndViewContainer.setRequestHandled(true);
        // 设置响应格式
        response.setContentType("application/json");
        // 修改为jackson
        Streams.transfer(response, objectMapper.writeValueAsBytes(respBean)/*JSONUtil.toJsonStr(respBean).getBytes()*/);
    }

    public static class Streams {

        public static void transfer(HttpServletResponse response, byte[] bs) throws IOException {
            Streams.transfer(Streams.toInputStream(bs), response.getOutputStream());
        }

        public static int transfer(InputStream input, OutputStream output) throws IOException {
            long count = transferLarge(input, output);
            return count > 2147483647L ? -1 : (int) count;
        }

        public static long transferLarge(InputStream input, OutputStream output) throws IOException {
            byte[] buffer = new byte[8192];

            long count;
            int n;
            for (count = 0L; (n = input.read(buffer)) != -1; count += (long) n) {
                output.write(buffer, 0, n);
            }

            return count;
        }

        public static InputStream toInputStream(byte[] bs) {
            return new ByteArrayInputStream(bs);
        }


    }
}