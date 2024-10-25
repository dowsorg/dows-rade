package org.dows.core.plugin;

import java.util.List;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 10/25/2024 1:54 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface PluginRepository<T extends PluginDetail> {

    T getByKey(String key);

    T getPluginInfoByHook(String hook);

    T getPluginInfoById(Long id);

    T newEmptyPlugin();

    List<? extends PluginDetail> listPlugin(Integer state);

    boolean removePluginById(Long id);

    boolean updatePlugin(PluginDetail one);

    void savePlugin(PluginDetail pluginDetail);

}
