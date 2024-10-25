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

    /*private Long id;

    @ColumnDefine(comment = "名称")
    private String name;

    @ColumnDefine(comment = "简介")
    private String description;

    @UniIndex
    @ColumnDefine(comment = "实例对象")
    private String key;

    @Index
    @ColumnDefine(comment = "Hook", length = 50)
    private String hook;

    @ColumnDefine(comment = "描述", type = "text")
    private String readme;

    @ColumnDefine(comment = "版本")
    private String version;

    @ColumnDefine(comment = "Logo(base64)", type = "text", notNull = true)
    private String logo;

    @ColumnDefine(comment = "作者")
    private String author;

    @ColumnDefine(comment = "状态 0-禁用 1-启用", defaultValue = "1")
    private Integer status;

    @ColumnDefine(comment = "插件的plugin.json", type = "json", notNull = true)
    @Column(typeHandler = Fastjson2TypeHandler.class)
    private PluginJson pluginJson;

    @ColumnDefine(comment = "配置", type = "json")
    @Column(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> config;

    @Ignore
    @Column(ignore = true)
    public String keyName;*/


    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

    String getKey();

    void setKey(String key);

    String getHook();

    void setHook(String hook);

    String getReadme();

    void setReadme(String readme);

    String getVersion();

    void setVersion(String version);

    String getLogo();

    void setLogo(String logo);

    String getAuthor();

    void setAuthor(String author);

    Integer getStatus();

    void setStatus(Integer status);

    PluginJson getPluginJson();

    void setPluginJson(PluginJson pluginJson);

    Map<String, Object> getConfig();

    void setConfig(Map<String, Object> config);

    String getKeyName();

    void setKeyName(String keyName);
}

