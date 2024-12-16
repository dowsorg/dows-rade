package org.dows.sbi.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 12/9/2024 10:36 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Configuration
public class SbiInterceptorConfiguration implements HandlerInterceptor, WebMvcConfigurer {
    @Bean
    public HandlerInterceptor contextPathInterceptor() {
        return new SbiInterceptorConfiguration();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(contextPathInterceptor())
                .addPathPatterns("/**");
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String contextPath = request.getContextPath();
        if (modelAndView != null) {
            ModelMap model = modelAndView.getModelMap();
            model.addAttribute("contextPath", contextPath);
        }
    }
}

