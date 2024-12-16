package org.dows.dds;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfiguration {

    /**
     * https://blog.95id.com/dynamic-datasource-in-springboot
     * https://www.jianshu.com/p/6a1c4536fe71
     * spring:
     *   datasource:
     *     db1:
     *       driver-class-name: com.mysql.jdbc.Driver
     *       jdbc-url: jdbc:mysql://localhost:3306/db1?characterEncoding=utf8&useSSL=false
     *       username: root
     *       password: 123456
     *     db2:
     *       driver-class-name: com.mysql.jdbc.Driver
     *       jdbc-url: jdbc:mysql://localhost:3306/db2?characterEncoding=utf8&useSSL=false
     *       username: root
     *       password: 123456
     * @return
     */
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.db1")
    public DataSource db1() {
        System.out.println("db1 creating ......");
        return DataSourceBuilder.create().build();
    }
    
    @Bean
    @ConfigurationProperties("spring.datasource.db2")
    public DataSource db2() {
        System.out.println("db2 creating ......");
        return DataSourceBuilder.create().build();
    }
}