package org.dows.core.web.request;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.enums.UserTypeEnum;
import org.dows.core.security.RadeSecurityUtil;
import org.dows.core.util.BodyReaderHttpServletRequestWrapper;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 封装请求参数 URL参数 和 body JSON 到同一个 JSONObject 方便读取
 */
@Component
@Order(2)
public class RequestParamsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        // 防止流读取一次后就没有了, 所以需要将流继续写出去
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        JSONObject requestParams = new JSONObject();
        if (StrUtil.isNotEmpty(request.getContentType()) && request.getContentType().contains("multipart/form-data")) {
            servletRequest.setAttribute("requestParams", requestParams);
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            BodyReaderHttpServletRequestWrapper requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
            String body = requestWrapper.getBodyString(requestWrapper);
            if (StrUtil.isNotEmpty(body) && JSONUtil.isTypeJSON(body) && !JSONUtil.isTypeJSONArray(
                    body)) {
                requestParams = JSONUtil.parseObj(body);
            }

            // 登录状态，设置用户id
            setUserId(requestParams);

            requestParams.set("body", body);
            requestParams.putAll(getAllRequestParam(request));

            Object jwtObj = request.getAttribute("tokenInfo");
            if (jwtObj != null) {
                requestParams.set("tokenInfo", ((JWT) jwtObj).getPayload().getClaimsJson());
            }
            requestWrapper.setAttribute("requestParams", requestParams);

            filterChain.doFilter(requestWrapper, servletResponse);
        }
    }

    private void setUserId(JSONObject requestParams) {
        UserTypeEnum userTypeEnum = RadeSecurityUtil.getCurrentUserType();
        switch (userTypeEnum) {
            // 只有登录了，才有用户类型， 不然为 UNKNOWN 状态
            case ADMIN -> {
                // 管理后台由于之前已经有逻辑再了，怕会影响到，如果自己有传了值不覆盖
                Object o = requestParams.get("userId");
                if (ObjUtil.isNotEmpty(o)) {
                    return;
                }
                requestParams.set("userId", RadeSecurityUtil.getCurrentUserId());
            }
            // app端，userId 为当前登录的用户id
            case APP -> requestParams.set("userId", RadeSecurityUtil.getCurrentUserId());
        }
    }

    /**
     * 获取客户端请求参数中所有的信息
     */
    private Map<String, Object> getAllRequestParam(final HttpServletRequest request) {
        Map<String, Object> res = new HashMap<>();
        Enumeration<?> temp = request.getParameterNames();
        if (null != temp) {
            while (temp.hasMoreElements()) {
                String en = (String) temp.nextElement();
                String value = request.getParameter(en);
                res.put(en, value);
                // 如果字段的值为空，判断若值为空，则删除这个字段>
                if (null == res.get(en) || "".equals(res.get(en))) {
                    res.remove(en);
                }
            }
        }
        return res;
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
