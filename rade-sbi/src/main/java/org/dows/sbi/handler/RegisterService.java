package org.dows.sbi.handler;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/25/2024 4:23 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface RegisterService {
    String getServiceName();

    default void setAppId(String appId) {
    }

    default void setPreSetupName(String preSetupName) {
    }
    String getAppId();

    default void setHost(String host) {
    }

    default String getHost() {
        return "localhost";
    }

    String getPort();

    default String getIndex() {
        return "index";
    }

    default String getSetupName() {
        return null;
    }

    default String getPreSetupName() {
        return null;
    }

    ;
}
