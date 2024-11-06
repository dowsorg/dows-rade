package org.dows.app;

import org.dows.core.security.SecurityProvider;
import org.dows.security.DefaultSecurityProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 2:48 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Configuration
public class SecurityConfig {

    @Bean
    @ConditionalOnMissingBean(SecurityProvider.class)
    public SecurityProvider securityProvider() {
        return new DefaultSecurityProvider();
    }

}

