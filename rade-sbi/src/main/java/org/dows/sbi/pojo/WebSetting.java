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
public class WebSetting implements ConfigSetting {

    @NestedConfigurationProperty
    private InstallSetting install;
    // 其他配置
    private String other = "WebSetting";

    private String appId;
    private String assets;
    private String statics;
    private String name;
    private String version;
    private String dist;
    private String port;
    private String host;

    private String ymlName="application.yml";

    public InstallSetting getInstallSetting() {
        return install;
    }
}

