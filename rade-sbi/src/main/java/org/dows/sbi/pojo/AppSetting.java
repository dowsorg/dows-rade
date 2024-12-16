package org.dows.sbi.pojo;

import lombok.Data;
import org.dows.sbi.handler.ConfigSetting;
import org.dows.sbi.handler.InstallSetting;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/25/2024 11:32 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class AppSetting implements ConfigSetting {

    @NestedConfigurationProperty
    private InstallSetting install;
    private String appId;
    private String port;
    private String name;
    private String host;
    private String serviceName = "SbiApi";

    private String ymlName="application.yml";

    private String includeYmlName ="application-datasource.yml,application-depends.yml,application-log.yml,application-cdc.yml,application-mongo.yml,application-mysql.yml";
    // 其他配置
    private String other = "AppSetting";

    public InstallSetting getInstallSetting() {
        return install;
    }
}

