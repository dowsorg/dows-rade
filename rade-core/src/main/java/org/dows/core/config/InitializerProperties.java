package org.dows.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/7/2024 6:01 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "rade.initializer")
public class InitializerProperties {

    private boolean initData;

    private String dataDir;

    private String menuDir;

}

