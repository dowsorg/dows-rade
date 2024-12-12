package org.dows.security;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RadeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        Map<String,Object> result = new HashMap<>();
        Object principal = authentication.getPrincipal();
        result.put("code",2000);
        result.put("message","登录认证成功");
        result.put("data",principal);

        String json = JSONUtil.toJsonStr(result);

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }
}
