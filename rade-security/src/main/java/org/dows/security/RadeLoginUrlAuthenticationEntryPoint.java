package org.dows.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @description: </br>
 * 重定向到登陆的 入口端点
 * @author: lait.zhang@gmail.com
 * @date: 2/28/2024 9:55 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public class RadeLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    //重定向策略
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * loginFormUrl可以在其中找到登录页面的URL
     * 应该是相对于web应用程序上下文路径（包括前导｛@code/｝）或绝对URL
     */
    public RadeLoginUrlAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        //获取登陆表单的地址
        String loginForm = determineUrlToUseForThisRequest(request, response, authException);
        if (!UrlUtils.isAbsoluteUrl(loginForm)) {
            //如果不是绝对路径，调用父类方法处理
            // /login
            super.commence(request, response, authException);
            return;
        }
        //请求路径
        StringBuffer requestUrl = request.getRequestURL();
        //请求参数
        String queryString = request.getQueryString();
        if (!StringUtils.isEmpty(queryString)) {
            //如果请求的查询参数 不是空的 把查询参数拼接到请求路径上
            requestUrl.append("?").append(queryString);
        }
        //目标参数
        String targetStr = URLEncoder.encode(requestUrl.toString(), StandardCharsets.UTF_8);
        //回调地址
        String targetUrl = loginForm + "?target=" + targetStr;
        //请求重定向
        this.redirectStrategy.sendRedirect(request, response, targetUrl);
    }
}