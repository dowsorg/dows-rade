package org.dows.security.filter;

import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * 图形验证码校验过滤器
 */
//@Component
@RequiredArgsConstructor
public class CaptchaValidationFilter extends OncePerRequestFilter {
    private static final AntPathRequestMatcher LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/v1/api/aac/login", "POST");


    public static final String CAPTCHA_CODE_PARAM_NAME = "captchaCode";
    public static final String CAPTCHA_KEY_PARAM_NAME = "captchaKey";
    private final CodeGenerator codeGenerator;
    // 改内存CaffeineTemplate
//    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        // 检验登录接口的验证码
        if (LOGIN_PATH_REQUEST_MATCHER.matches(request)) {
            // 请求中的验证码
            String captchaCode = request.getParameter(CAPTCHA_CODE_PARAM_NAME);
            // TODO 兼容没有验证码的版本(线上请移除这个判断)
            if (StrUtil.isBlank(captchaCode)) {
                chain.doFilter(request, response);
                return;
            }
            // 缓存中的验证码
            String verifyCodeKey = request.getParameter(CAPTCHA_KEY_PARAM_NAME);
//            String cacheVerifyCode = (String) redisTemplate.opsForValue().get(SecurityConstants.CAPTCHA_CODE_PREFIX + verifyCodeKey);
            String cacheVerifyCode = "";
            if (cacheVerifyCode == null) {
                //Response.fail("验证码超时");
            } else {
                // 验证码比对
                if (codeGenerator.verify(cacheVerifyCode, captchaCode)) {
                    chain.doFilter(request, response);
                } else {
                    //Response.fail("验证码错误");
                }
            }
        } else {
            // 非登录接口放行
            chain.doFilter(request, response);
        }
    }

}
