package org.dows.security.handler;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RadeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Map<String,Object> result = new HashMap<>();
        Object principal = authentication.getPrincipal();
        result.put("code",2000);
        result.put("message","登录认证成功");
        result.put("data",principal);

        String json = JSONUtil.toJsonStr(result);

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().println(json);
    }


    /*public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Object principal = authentication.getPrincipal();
        if (principal == null || !(principal instanceof UserLoginInfo)) {
            ExceptionTool.throwException(
                    "登陆认证成功后，authentication.getPrincipal()返回的Object对象必须是：UserLoginInfo！");
        }
        UserLoginInfo currentUser = (UserLoginInfo) principal;
        currentUser.setSessionId(UUID.randomUUID().toString());

        // 生成token和refreshToken
        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("token", generateToken(currentUser));
        responseData.put("refreshToken", generateRefreshToken(currentUser));

        // 一些特殊的登录参数。比如三方登录，需要额外返回一个字段是否需要跳转的绑定已有账号页面
        Object details = authentication.getDetails();
        if (details instanceof Map) {
            Map detailsMap = (Map)details;
            responseData.putAll(detailsMap);
        }

        // 虽然APPLICATION_JSON_UTF8_VALUE过时了，但也要用。因为Postman工具不声明utf-8编码就会出现乱码
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        writer.print(JSON.stringify(Result.data(responseData, "${login.success:登录成功！}")));
        writer.flush();
        writer.close();
    }

    public String generateToken(UserLoginInfo currentUser) {
        long expiredTime = TimeTool.nowMilli() + TimeUnit.MINUTES.toMillis(10); // 10分钟后过期
        currentUser.setExpiredTime(expiredTime);
        return jwtService.createJwt(currentUser, expiredTime);
    }

    private String generateRefreshToken(UserLoginInfo loginInfo) {
        return jwtService.createJwt(loginInfo, TimeTool.nowMilli() + TimeUnit.DAYS.toMillis(30));
    }*/
}
