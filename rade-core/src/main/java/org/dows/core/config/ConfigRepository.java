package org.dows.core.config;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 10/25/2024 3:12 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface ConfigRepository {

    /**
     * 更新配置
     *
     * @param key   键
     * @param value 值
     */
    void updateValue(String key, String value);

    /**
     * 获得值
     *
     * @param key 键
     * @return 值
     */
    String getValue(String key);

    /**
     * 获得值（带缓存）
     *
     * @param key 键
     * @return 值
     */
    String getValueWithCache(String key);

    /**
     * 设置值
     *
     * @param key 键
     * @param value 值
     */
    void setValue(String key, String value);
}
