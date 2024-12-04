//package org.dows.app.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.sql.DataSource;
//
//@Configuration
//
//public class TenantAutoConfiguration {
//
//    @Bean
//    public FilterRegistrationBean tenantContextWebFilter() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new TenantWebFilter());
//        registrationBean.setOrder(-104);
//        return registrationBean;
//    }
//
//    @Autowired
//    private DynamicDataSourceProperties properties;
//
//    @Bean
//    public DataSource dataSource(List providers) {
//        MyDynamicRoutingDataSource dataSource = new MyDynamicRoutingDataSource(providers);
//        dataSource.setPrimary(properties.getPrimary());
//        dataSource.setStrict(properties.getStrict());
//        dataSource.setStrategy(properties.getStrategy());
//        dataSource.setP6spy(properties.getP6spy());
//        dataSource.setSeata(properties.getSeata());
//        dataSource.setGraceDestroy(properties.getGraceDestroy());
//        return dataSource;
//    }
//
//}