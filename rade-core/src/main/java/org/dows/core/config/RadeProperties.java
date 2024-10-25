package org.dows.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * dows的配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rade")
public class RadeProperties {
    // 是否自动导入数据
    private Boolean initData = false;
    // token配置
    @NestedConfigurationProperty
    private TokenProperties token;
    // 文件配置
    @NestedConfigurationProperty
    private FileProperties file;
}
