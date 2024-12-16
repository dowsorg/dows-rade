package org.dows.sbi.pojo;

import lombok.Data;
import org.dows.sbi.handler.ConfigSetting;
import org.dows.sbi.handler.InstallSetting;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/22/2024 9:44 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class MssqlSetting implements ConfigSetting {
    @NestedConfigurationProperty
    private InstallSetting install;
    // 其他配置
    private String other = "mssql";

    private String ymlName="application-mssql.yml";

    private String binlog;
    private String baseDir;
    private String logDir;
    private String dataDir;
    private String serviceName ="SbiMysql";
    private String pluginDir;
    private String username;
    private String password;
    private String host;
    private String port;
    private String databaseName;

    public InstallSetting getInstallSetting() {
        return install;
    }


}

