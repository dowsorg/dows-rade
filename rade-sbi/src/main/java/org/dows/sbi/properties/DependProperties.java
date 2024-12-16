package org.dows.sbi.properties;

import lombok.Data;
import org.dows.sbi.pojo.*;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/22/2024 9:12 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class DependProperties {
    @NestedConfigurationProperty
    private JdkSetting jdk;
    @NestedConfigurationProperty
    private MariadbSetting mariadb;
    @NestedConfigurationProperty
    private MongoSetting mongo;
    @NestedConfigurationProperty
    private NginxSetting nginx;
    @NestedConfigurationProperty
    private AppSetting app;
    @NestedConfigurationProperty
    private WebSetting web;
}

