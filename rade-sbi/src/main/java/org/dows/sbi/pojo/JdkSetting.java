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
public class JdkSetting implements ConfigSetting {

    @NestedConfigurationProperty
    private InstallSetting install;
    // 其他配置
    private String other = "jdk";
    /*// 文件名称
    private String name = "jdk";
    // 版本
    private String version = "17.0.10";
    // 安装目录
    private String dir;
    //平台（windows,linux,mac...）
    private String platform;
    //x86 x64 架构
    private String architecture;
    //文件扩展名
    private String ext = ".zip";*/
    public InstallSetting getInstallSetting() {
        return install;
    }
}

