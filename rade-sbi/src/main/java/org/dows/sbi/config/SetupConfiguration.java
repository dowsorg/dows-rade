package org.dows.sbi.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.dows.sbi.handler.ConfigSetting;
import org.dows.sbi.handler.DependProvider;
import org.dows.sbi.pojo.*;
import org.dows.sbi.properties.DependProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/22/2024 9:14 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Slf4j
@Configuration
public class SetupConfiguration implements DependProvider {

    @PostConstruct
    public void init() {
        log.info("init SetupConfiguration");
    }


    @Bean
    @ConfigurationProperties(prefix = "dows.sbi.dcfs")
    public List<String> dcfsProperties() {
        return new ArrayList<>();
    }

    @Bean
    @ConfigurationProperties(prefix = "dows.sbi.setup")
    public DependProperties dependProperties() {
        return new DependProperties();
    }

    @Override
    public List<String> getDcfs() {
        return dcfsProperties();
    }

    @Override
    public <T extends ConfigSetting> T getConfigSetting(Class<T> configSettingClass) {
        if (configSettingClass == JdkSetting.class) {
            return (T) dependProperties().getJdk();
        }
        if (configSettingClass == MariadbSetting.class) {
            return (T) dependProperties().getMariadb();
        }
        if (configSettingClass == NginxSetting.class) {
            return (T) dependProperties().getNginx();
        }
        if (configSettingClass == MongoSetting.class) {
            return (T) dependProperties().getMongo();
        }
        if (configSettingClass == AppSetting.class) {
            return (T) dependProperties().getApp();
        }
        if (configSettingClass == WebSetting.class) {
            return (T) dependProperties().getWeb();
        }
        throw new RuntimeException("未找到配置");
    }




    /*public JdkSetting getJdk() {
        return dependProperties().getJdk();
    }

    public MariadbSetting getMariadb() {
        return dependProperties().getMariadb();
    }

    public MongoSetting getMongo() {
        return dependProperties().getMongo();
    }

    public NginxSetting getNginx() {
        return dependProperties().getNginx();
    }*/

}

