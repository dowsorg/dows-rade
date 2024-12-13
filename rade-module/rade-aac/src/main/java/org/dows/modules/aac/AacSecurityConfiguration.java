package org.dows.modules.aac;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.api.aac.AuthStatusCode;
import org.dows.core.annotation.TokenIgnore;
import org.dows.core.enums.UserTypeEnum;
import org.dows.core.rbac.RbacProvider;
import org.dows.security.*;
import org.dows.security.filter.HandlerExceptionResolverFilter;
import org.dows.security.filter.AuthenticationJwtTokenFilter;
import org.dows.security.handler.RadeAccessDeniedHandler;
import org.dows.security.handler.RadeAuthenticationFailureHandler;
import org.dows.security.handler.RadeAuthenticationSuccessHandler;
import org.dows.security.handler.RadeLogoutHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.reflect.Method;
import java.util.*;

@EnableWebSecurity
@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties({AacProperties.class})
public class AacSecurityConfiguration {

    // 用户详情
    //final private UserDetailsService userDetailsService;
    final private UserDetailsManager userDetailsManager;
    // jwt 过滤
    final private AuthenticationJwtTokenFilter authenticationJwtTokenFilter;
    // 全局异常过滤
    //final private RadeSecurityExceptionFilter radeSecurityExceptionFilter;
    final private HandlerExceptionResolverFilter handlerExceptionResolverFilter;
    // 401
    final private RadeUnauthorizedEntryPoint radeUnauthorizedEntryPoint;
    // 403
    final private RadeAccessDeniedHandler radeAccessDeniedHandler;

    // 忽略权限控制的地址
    //final private IgnoredUrlsProperties ignoredUrlsProperties;
    private final AacProperties aacProperties;

    final private RequestMappingHandlerMapping requestMappingHandlerMapping;

    final RbacProvider rbacProvider;

    /**
     * 禁用不必要的默认filter，处理异常响应内容
     */
    private void commonHttpSetting(HttpSecurity http) throws Exception {
        // 禁用SpringSecurity默认filter。这些filter都是非前后端分离项目的产物，用不上.
        // yml配置文件将日志设置DEBUG模式，就能看到加载了哪些filter
        // logging:
        //    level:
        //       org.springframework.security: DEBUG
        // 表单登录/登出、session管理、csrf防护等默认配置，如果不disable。会默认创建默认filter
        http.formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // requestCache用于重定向，前后端分析项目无需重定向，requestCache也用不上
                .requestCache(cache -> cache
                        .requestCache(new NullRequestCache())
                )
                // 无需给用户一个匿名身份
                .anonymous(AbstractHttpConfigurer::disable);

        // 处理 SpringSecurity 异常响应结果。响应数据的结构，改成业务统一的JSON结构。不要框架默认的响应结构
        /*http.exceptionHandling(exceptionHandling ->
                exceptionHandling
                        // 认证失败异常
                        .authenticationEntryPoint(authenticationExceptionHandler)
                        // 鉴权失败异常
                        .accessDeniedHandler(authorizationExceptionHandler)
        );
        // 其他未知异常. 尽量提前加载。
        http.addFilterBefore(globalSpringSecurityExceptionHandler, SecurityContextHolderFilter.class);*/
    }


    /** 登录api */
//    @Bean
//    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
//        commonHttpSetting(http);
//
//        String enterUrl = "/user/login/*";
//
//        // 使用securityMatcher限定当前配置作用的路径
//        http.securityMatcher(enterUrl)
//                .authorizeHttpRequests(authorize ->
//                        authorize
//                                .anyRequest()
//                                .authenticated());
//
//        LoginSuccessHandler loginSuccessHandler = applicationContext.getBean(LoginSuccessHandler.class);
//        LoginFailHandler loginFailHandler = applicationContext.getBean(LoginFailHandler.class);
//
//        // 加一个登录方式。用户名、密码登录
//        UsernameAuthenticationFilter usernameLoginFilter = new UsernameAuthenticationFilter(
//                new AntPathRequestMatcher("/user/login/username", HttpMethod.POST.name()),
//                new ProviderManager(
//                        List.of(applicationContext.getBean(UsernameAuthenticationProvider.class))),
//                loginSuccessHandler,
//                loginFailHandler);
//        http.addFilterBefore(usernameLoginFilter, UsernamePasswordAuthenticationFilter.class);
//
//        // 加一个登录方式。短信验证码 登录
//        SmsAuthenticationFilter smsLoginFilter = new SmsAuthenticationFilter(
//                new AntPathRequestMatcher("/user/login/sms", HttpMethod.POST.name()),
//                new ProviderManager(
//                        List.of(applicationContext.getBean(SmsAuthenticationProvider.class))),
//                loginSuccessHandler,
//                loginFailHandler);
//        http.addFilterBefore(smsLoginFilter, UsernamePasswordAuthenticationFilter.class);
//
//
//        // 加一个登录方式。Gitee 登录
//        GiteeAuthenticationFilter giteeFilter = new GiteeAuthenticationFilter(
//                new AntPathRequestMatcher("/user/login/gitee", HttpMethod.POST.name()),
//                new ProviderManager(
//                        List.of(applicationContext.getBean(GiteeAuthenticationProvider.class))),
//                loginSuccessHandler,
//                loginFailHandler);
//        http.addFilterBefore(giteeFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }


    /*@Bean
    public SecurityFilterChain myApiFilterChain(HttpSecurity http) throws Exception {
        // 使用securityMatcher限定当前配置作用的路径
        http.securityMatcher("/open-api/business-1")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        commonHttpSetting(http);

        MyJwtAuthenticationFilter openApi1Filter = new MyJwtAuthenticationFilter(
                applicationContext.getBean(JwtService.class));
        // 加一个登录方式。用户名、密码登录
        http.addFilterBefore(openApi1Filter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }*/

    /*@Bean
    public SecurityFilterChain thirdApiFilterChain(HttpSecurity http) throws Exception {
        // 不使用securityMatcher限定当前配置作用的路径。所有没有匹配上指定SecurityFilterChain的请求，都走这里鉴权
        http.securityMatcher("/open-api/business-2")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
        commonHttpSetting(http);

        OpenApi2AuthenticationFilter openApiFilter = new OpenApi2AuthenticationFilter();
        // 加一个登录方式。用户名、密码登录
        http.addFilterBefore(openApiFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }*/

    /**
     * 使用启动登录
     * @return
     */
    public boolean isEnableLogin() {
        return aacProperties.getLogin().isEnabled();
    }

    /**
     * 获取白名单
     *
     * @return
     */
    public String[] getWhitelist() {
        return aacProperties.getWhitelist();
    }


    public void setLoginEnable(Boolean enable) {
        aacProperties.getLogin().setEnabled(enable);
    }
    /**
     * 不鉴权的api
     */
    @Bean
    public SecurityFilterChain publicApiFilterChain(HttpSecurity http) throws Exception {
        commonHttpSetting(http);
        http
                // 使用securityMatcher限定当前配置作用的路径
                .securityMatcher("/open-api/business-3")
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return http.build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 动态获取忽略的URL
        configureIgnoredUrls();
        // 请求授权设置
        httpSecurity.authorizeHttpRequests(
                conf -> {
                    conf.requestMatchers(aacProperties.getWhitelist()).permitAll();
                    conf.requestMatchers("/admin/**").authenticated();
                    conf.requestMatchers("/app/**").hasRole(UserTypeEnum.APP.name());
                    conf.anyRequest() .access((authentication, object) -> {
                        if (!isEnableLogin()) {
                            return new AuthorizationDecision(true);
                        } else {
                            Authentication ac = authentication.get();
                            try {
                                AacUser aacUser = (AacUser) ac.getPrincipal();
                                if (aacUser.isSuperAccount()) {
                                    // 超级管理员放行
                                    return new AuthorizationDecision(true);
                                }
                            } catch (Exception e) {
                                //InvalidBearerTokenException,CredentialsExpiredException,AuthenticationServiceException
                                throw new CredentialsExpiredException(AuthStatusCode.UNAUTHORIZED.getDescribe());
                            }
                            Collection<? extends GrantedAuthority> authorities = ac.getAuthorities();
                            //表示请求的 URL 地址和数据库的地址是否匹配上了
                            boolean isMatch = false;
                            //获取当前请求的 URL 地址
                            String requestURI = object.getRequest().getRequestURI();
                            /*Map<String, List<RbacUriRoleResponse>> result = rbacProvider.getRoleUri();
                            if (CollectionUtil.isNotEmpty(result)) {
                                Set<String> uris = result.keySet();
                                for (String uri : uris) {
                                    if (uri.equals(requestURI)) {
                                        isMatch = true;
                                        List<RbacUriRoleResponse> rbacUriRoleResponses = result.get(uri);
                                        for (GrantedAuthority authority : authorities) {
                                            for (RbacUriRoleResponse rbacUriRoleRespons : rbacUriRoleResponses) {
                                                if (authority.getAuthority().equals(String.valueOf(rbacUriRoleRespons.getRoleId()))) {
                                                    //说明当前登录用户具备当前请求所需要的角色
                                                    return new AuthorizationDecision(true);
                                                }
                                            }
                                        }
                                    }
                                }
                            }*/
                            if (!isMatch) {
                                //说明请求的 URL 地址和数据库的地址没有匹配上，对于这种请求，统一只要登录就能访问
                                if (authentication.get() instanceof AnonymousAuthenticationToken) {
                                    return new AuthorizationDecision(false);
                                }
                            }
                            return new AuthorizationDecision(false);
                        }
                    });
                });
        // 请求头设置
        httpSecurity.headers(config -> config.frameOptions(FrameOptionsConfig::disable));
        // 登录设置
        httpSecurity.formLogin(from -> {
            from.loginPage("login")
                    .passwordParameter("")
                    .usernameParameter("")
                    // 失败页面或登录页面的错误表示如login?后的failure,前端页面可以拿到该字段做处理
                    .failureUrl("login?failure")
                    .successHandler(new RadeAuthenticationSuccessHandler())
                    .failureHandler(new RadeAuthenticationFailureHandler());
        });

        // 允许网页iframe

        // 异常处理
        httpSecurity.exceptionHandling(exception -> {
            // 未登录
            exception.authenticationEntryPoint(radeUnauthorizedEntryPoint);
            // 拒绝访问
            exception.accessDeniedHandler(radeAccessDeniedHandler);
        });
        // 并发会话控制 httpSecurity.sessionManagement(conf -> conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        httpSecurity.sessionManagement(session -> {
            session.maximumSessions(1)
                    .expiredSessionStrategy(new RadSessionExpiredStrategy());
        });
        //注销成功时的处理
        httpSecurity.logout(logout -> {
            logout.logoutSuccessHandler(new RadeLogoutHandler());
        });
        // 跨域
        httpSecurity.cors(Customizer.withDefaults());
        // csrf攻击防御
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        // 其他未知异常. 尽量提前加载
        httpSecurity.addFilterBefore(handlerExceptionResolverFilter, SecurityContextHolderFilter.class)
                .addFilterBefore(authenticationJwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    private void configureIgnoredUrls() {
        Map<RequestMappingInfo, HandlerMethod> mappings = requestMappingHandlerMapping.getHandlerMethods();
        List<String> handlerCtr = new ArrayList<>();
        mappings.forEach((requestMappingInfo, handlerMethod) -> {
            Method method = handlerMethod.getMethod();
            TokenIgnore tokenIgnore = AnnotatedElementUtils.findMergedAnnotation(method, TokenIgnore.class);
            TokenIgnore tokenIgnoreCtr = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), TokenIgnore.class);
            if (!handlerCtr.contains(handlerMethod.getBeanType().getName()) && tokenIgnoreCtr != null) {
                requestMappingInfo.getPathPatternsCondition().getPatterns().forEach(pathPattern -> {
                    String[] prefixs = pathPattern.getPatternString().split("/");
                    // 去除最后一个路径
                    List<String> urls = new ArrayList<>();
                    urls.addAll(Arrays.asList(prefixs).subList(0, prefixs.length - 1));
                    // 遍历 tokenIgnoreCtr.value()
                    for (String path : tokenIgnoreCtr.value()) {
                        aacProperties.getAdminAuthUrls().add(String.join("/", urls) + "/" + path);
                    }
                    if (tokenIgnoreCtr.value().length == 0) {
                        // 通配
                        aacProperties.getAdminAuthUrls().add(String.join("/", urls) + "/**");
                    }
                    handlerCtr.add(handlerMethod.getBeanType().getName());
                });
            }
            if (tokenIgnore != null) {
                StringBuilder url = new StringBuilder();
                RequestMapping classRequestMapping = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), RequestMapping.class);
                if (classRequestMapping != null) {
                    for (String path : classRequestMapping.value()) {
                        url.append(path);
                    }
                }
                if (requestMappingInfo.getPathPatternsCondition() == null) {
                    return;
                }
                for (PathPattern path : requestMappingInfo.getPathPatternsCondition().getPatterns()) {
                    url.append(path);
                }
                aacProperties.getAdminAuthUrls().add(url.toString());
            }
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return DigestUtils.md5DigestAsHex(((String) rawPassword).getBytes());
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(
                        DigestUtils.md5DigestAsHex(((String) rawPassword).getBytes()));
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //authProvider.setUserDetailsService(userDetailsService);
        authProvider.setUserDetailsService(userDetailsManager);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
