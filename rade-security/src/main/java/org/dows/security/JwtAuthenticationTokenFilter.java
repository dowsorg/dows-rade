package org.dows.security;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.dows.core.cache.RadeCache;
import org.dows.core.config.IgnoredUrlsProperties;
import org.dows.core.enums.UserTypeEnum;
import org.dows.security.jwt.JwtTokenUtil;
import org.dows.security.jwt.JwtUser;
import org.dows.core.util.PathUtils;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

/**
 * Token过滤器
 */
@Order(1)
@Component
@RequiredArgsConstructor
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    final private JwtTokenUtil jwtTokenUtil;
    final private RadeCache radeCache;
    final private IgnoredUrlsProperties ignoredUrlsProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (PathUtils.isMatch(ignoredUrlsProperties.getAdminAuthUrls(), requestURI)) {
            // 请求路径在忽略后台鉴权url里支持通配符，放行
            chain.doFilter(request, response);
            return;
        }
        String authToken = request.getHeader("Authorization");
        if (!StrUtil.isEmpty(authToken)) {
            JWT jwt = jwtTokenUtil.getTokenInfo(authToken);

            Object userType = jwt.getPayload("userType");
            if (Objects.equals(userType, UserTypeEnum.APP.name())) {
                // app
                handlerAppRequest(request, jwt, authToken);
            } else {
                // admin
                handlerAdminRequest(request, jwt, authToken);
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * 处理app请求
     */
    private void handlerAppRequest(HttpServletRequest request, JWT jwt, String authToken) {
        String userId = jwt.getPayload("userId").toString();
        if (ObjectUtil.isNotEmpty(userId)
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = radeCache.get("app:userDetails:" + userId,
                    JwtUser.class);
            if (jwtTokenUtil.validateToken(authToken) && userDetails != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute("userId", jwt.getPayload("userId"));
                request.setAttribute("tokenInfo", jwt);
            }
        }
    }

    /**
     * 处理后台请求
     */
    private void handlerAdminRequest(HttpServletRequest request, JWT jwt, String authToken) {
        String username = jwt.getPayload("username").toString();
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = radeCache.get("admin:userDetails:" + username,
                    JwtUser.class);
            Integer passwordV = Convert.toInt(jwt.getPayload("passwordVersion"));
            Integer rv = radeCache.get("admin:passwordVersion:" + jwt.getPayload("userId"),
                    Integer.class);
            if (jwtTokenUtil.validateToken(authToken, username) && Objects.equals(passwordV, rv)
                    && userDetails != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                request.setAttribute("adminUsername", jwt.getPayload("username"));
                request.setAttribute("adminUserId", jwt.getPayload("userId"));
                request.setAttribute("tokenInfo", jwt);
            }
        }
    }
}
