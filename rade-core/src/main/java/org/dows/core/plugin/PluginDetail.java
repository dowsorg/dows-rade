package org.dows.core.plugin;

import java.util.Map;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 10/25/2024 1:56 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface PluginDetail {

    Long getId();
    void setId(Long id);

    //名称
    String getName();
    void setName(String name);

    //简介
    String getDescription();
    void setDescription(String description);

    //实例对象
    String getKey();
    void setKey(String key);

    //Hook
    String getHook();
    void setHook(String hook);

    //描述
    String getReadme();
    void setReadme(String readme);

    // 版本
    String getVersion();
    void setVersion(String version);

    //Logo(base64)
    String getLogo();
    void setLogo(String logo);

    // 作者
    String getAuthor();
    void setAuthor(String author);

    // 状态 0-禁用 1-启用
    Integer getStatus();
    void setStatus(Integer status);

    //插件的plugin.json
    PluginJson getPluginJson();
    void setPluginJson(PluginJson pluginJson);

    //配置
    Map<String, Object> getConfig();
    void setConfig(Map<String, Object> config);

    String getKeyName();
    void setKeyName(String keyName);
}

