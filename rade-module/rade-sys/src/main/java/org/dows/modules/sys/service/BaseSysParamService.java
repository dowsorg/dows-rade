package org.dows.modules.sys.service;

import org.dows.core.crud.BaseService;
import org.dows.modules.sys.entity.BaseSysParamEntity;

/**
 * 系统参数配置
 */
public interface BaseSysParamService extends BaseService<BaseSysParamEntity> {
    /**
     * 根据key获得网页内容
     *
     * @param key 键
     * @return 网页内容
     */
    String htmlByKey(String key);

    /**
     * 根据key获得数据
     *
     * @param key 键
     * @return 数据
     */
    String dataByKey(String key);
}
