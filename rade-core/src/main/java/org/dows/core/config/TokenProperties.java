package org.dows.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * token配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rade.token")
public class TokenProperties {
    // token 过期时间
    private Long expire;
    // refreshToken 过期时间
    private Long refreshExpire;
}
