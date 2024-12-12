package org.dows.security;

import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 权限管理拦截器 监控用户行为
 */
@Slf4j
//@Component
//@RequiredArgsConstructor
public class RadeFilterSecurityInterceptor extends AuthorizationFilter  {

    //final private FilterInvocationSecurityMetadataSource securityMetadataSource;

    /**
     * Creates an instance.
     *
     * @param authorizationManager the {@link AuthorizationManager} to use
     */
    public RadeFilterSecurityInterceptor(AuthorizationManager<HttpServletRequest> authorizationManager) {
        super(authorizationManager);
    }


}