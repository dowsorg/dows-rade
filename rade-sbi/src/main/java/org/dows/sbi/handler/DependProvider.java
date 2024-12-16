package org.dows.sbi.handler;

import java.util.List;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/22/2024 9:20 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface DependProvider {

    /**
     * 获取配置
     *
     * @param configSettingClass
     * @param <T>
     * @return
     */
    <T extends ConfigSetting> T getConfigSetting(Class<T> configSettingClass);

    /**
     * 获取dcfs(dynamic config file system)配置
     *
     * @return
     */
    List<String> getDcfs();

     /*JdkSetting getJdk();

     MariadbSetting getMariadb() ;

     MongoSetting getMongo() ;

     NginxSetting getNginx() ;*/
}
