package org.dows.modules.dict.service;

import org.dows.core.crud.BaseService;
import org.dows.modules.dict.entity.DictInfoEntity;

import java.util.List;

/**
 * 字典信息
 */
public interface DictInfoService extends BaseService<DictInfoEntity> {
    /**
     * 字典数据
     *
     * @param types 字典类型
     * @return
     */
    Object data(List<String> types);
}
