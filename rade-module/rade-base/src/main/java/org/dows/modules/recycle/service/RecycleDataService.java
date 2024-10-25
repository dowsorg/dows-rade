package org.dows.modules.recycle.service;

import cn.hutool.json.JSONObject;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import org.dows.core.crud.BaseService;
import org.dows.modules.recycle.entity.RecycleDataEntity;

import java.util.List;

public interface RecycleDataService extends BaseService<RecycleDataEntity> {
    Object page(JSONObject requestParams, Page<RecycleDataEntity> page, QueryWrapper queryWrapper);

    /**
     * 恢复数据
     * 
     * @param ids
     * @return
     */
    Boolean restore(List<Long> ids);
}
