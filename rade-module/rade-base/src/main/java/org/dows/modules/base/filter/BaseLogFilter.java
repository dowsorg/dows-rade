package org.dows.modules.base.filter;

import cn.hutool.json.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.modules.base.service.sys.BaseSysLogService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(10)
@RequiredArgsConstructor
public class BaseLogFilter implements Filter {

    final private BaseSysLogService baseSysLogService;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
        FilterChain filterChain)
        throws IOException, ServletException {
        // 记录日志
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        baseSysLogService.record(request, (JSONObject) request.getAttribute("requestParams"));
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
