//package org.dows.app.config;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
///**
// * @description: </br>
// *
// * @author: lait.zhang@gmail.com
// * @date: 11/18/2024 9:51 AM
// * @history: </br>
// * <author>      <time>      <version>    <desc>
// * 修改人姓名      修改时间        版本号       描述
// */
//public class TenantDsInterceptor implements HandlerInterceptor {
//
//    @Autowired
//    private DynamicDataSourceHandler dynamicDataSourceService;
//
//    @Override
//    public boolean intercept(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String appid = request.getHeader("appid");
//        if (appid!= null) {
//            dynamicDataSourceService.addDataSource(appid);
//            DataSourceContextHolder.setDataSourceType(appid);
//        }
//        return true;
//    }
//
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        // 可在此处进行一些后续处理，如清理数据源类型等
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        DataSourceContextHolder.clearDataSourceType();
//    }
//}