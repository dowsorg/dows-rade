//package org.dows.app.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Objects;
//
//public class TenantWebFilter extends OncePerRequestFilter {
//
//    public static final String HEADER_TENANT_ID = "X-Tenant-Id";
//
//    public static String getTenantId(HttpServletRequest request) {
//
//        String tenantId = StringUtils.hasLength(request.getHeader(HEADER_TENANT_ID)) ?
//                request.getHeader(HEADER_TENANT_ID) : request.getHeader(HEADER_TENANT_ID.toLowerCase());
//
//        if (StringUtils.isEmpty(tenantId)) {
//            tenantId = getQueryParam(request.getQueryString(), HEADER_TENANT_ID);
//        }
//        return StringUtils.hasText(tenantId) ? tenantId : null;
//    }
//
//    public static String getQueryParam(String query, String key) {
//        if (Objects.isNull(query)) {
//            return null;
//        }
//        String[] params = query.split("&");
//        for (String param : params) {
//            String[] keyValue = param.split("=");
//            if (Objects.equals(key.toLowerCase(), keyValue[0].toLowerCase()) && keyValue.length > 1) {
//                return keyValue[1];
//            }
//        }
//        return null;
//    }
//
//    @Override
//
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
//        if (request.getRequestURI().equalsIgnoreCase("/harbor/clear")) {
//            chain.doFilter(request, response);
//        } else {
//            String tenantId = getTenantId(request);
//            if (tenantId != null) {
//                TenantContextHolder.setTenantId(tenantId);
//            }
//            try {
//                chain.doFilter(request, response);
//            } finally {
//                // 清除
//                TenantContextHolder.clear();
//            }
//        }
//    }
//}