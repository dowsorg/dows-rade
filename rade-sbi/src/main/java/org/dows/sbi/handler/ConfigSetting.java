package org.dows.sbi.handler;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/22/2024 9:46 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface ConfigSetting {

    default InstallSetting getInstallSetting() {
        return null;
    }
    default String getYmlName() {
        return null;
    }

    default String getHost() {
        return null;
    }

    default String getPassword() {
        return null;
    }
    default String getUsername() {
        return null;
    }
    default String getBaseDir() {
        return null;
    }
    ;
}

