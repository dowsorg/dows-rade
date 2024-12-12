//package org.dows.security;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//public class GiteeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
//  private static final Logger logger = LoggerFactory.getLogger(GiteeAuthenticationFilter.class);
//
//  public GiteeAuthenticationFilter(AntPathRequestMatcher pathRequestMatcher,
//                                   AuthenticationManager authenticationManager,
//                                   AuthenticationSuccessHandler authenticationSuccessHandler,
//                                   AuthenticationFailureHandler authenticationFailureHandler) {
//    super(pathRequestMatcher);
//    setAuthenticationManager(authenticationManager);
//    setAuthenticationSuccessHandler(authenticationSuccessHandler);
//    setAuthenticationFailureHandler(authenticationFailureHandler);
//  }
//
//  @Override
//  public Authentication attemptAuthentication(HttpServletRequest request,
//                                              HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
//    logger.debug("use GiteeAuthenticationFilter");
//
//    // 提取请求数据
//    String requestJsonData = request.getReader().lines()
//        .collect(Collectors.joining(System.lineSeparator()));
//    Map<String, Object> requestMapData = JSON.parseToMap(requestJsonData);
//    String code = requestMapData.get("code").toString();
//
//    GiteeAuthentication authentication = new GiteeAuthentication();
//    authentication.setCode(code);
//    authentication.setAuthenticated(false); // 提取参数阶段，authenticated一定是false
//    return this.getAuthenticationManager().authenticate(authentication);
//  }
//
//}