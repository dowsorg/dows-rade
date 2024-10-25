package org.dows.modules.base.service.sys.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.core.cache.RadeCache;
import org.dows.core.crud.BaseServiceImpl;
import org.dows.modules.base.entity.sys.BaseSysParamEntity;
import org.dows.modules.base.mapper.sys.BaseSysParamMapper;
import org.dows.modules.base.service.sys.BaseSysParamService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统参数配置
 */
@Service
@RequiredArgsConstructor
public class BaseSysParamServiceImpl extends BaseServiceImpl<BaseSysParamMapper, BaseSysParamEntity>
        implements BaseSysParamService {

    final private RadeCache radeCache;

    @Override
    public String htmlByKey(String key) {
        String data = dataByKey(key);
        return "<html><body>" + (StrUtil.isNotEmpty(data) ? data : "key notfound")
                + "</body></html>";
    }

    @Override
    public String dataByKey(String key) {
        BaseSysParamEntity baseSysParamEntity = radeCache.get(key, BaseSysParamEntity.class);
        if (baseSysParamEntity == null) {
            baseSysParamEntity = getOne(
                    QueryWrapper.create().eq(BaseSysParamEntity::getKeyName, key));
        }
        if (baseSysParamEntity != null) {
            radeCache.set("param:" + baseSysParamEntity.getKeyName(), baseSysParamEntity);
            return baseSysParamEntity.getData();
        }
        return null;
    }

    @Override
    public void modifyAfter(JSONObject requestParams, BaseSysParamEntity baseSysParamEntity) {
        List<BaseSysParamEntity> list = this.list();
        list.forEach(e -> {
            radeCache.set("param:" + e.getKeyName(), e);
        });
    }
}