package org.dows.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/7/2024 6:06 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rade.cache")
public class CacheProperties {
    private boolean enable;
    private String cacheName;
}

