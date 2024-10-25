package org.dows.modules.base.service.sys.impl;

import org.dows.core.crud.BaseServiceImpl;
import org.dows.core.cache.RadeCache;
import org.dows.modules.base.entity.sys.BaseSysConfEntity;
import org.dows.modules.base.mapper.sys.BaseSysConfMapper;
import org.dows.modules.base.service.sys.BaseSysConfService;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 系统配置
 */
@Service
@RequiredArgsConstructor
public class BaseSysConfServiceImpl extends BaseServiceImpl<BaseSysConfMapper, BaseSysConfEntity>
        implements BaseSysConfService {

    private final RadeCache radeCache;

    @Override
    public void updateValue(String key, String value) {
        UpdateChain.of(BaseSysConfEntity.class).set(BaseSysConfEntity::getCValue, value)
                .eq(BaseSysConfEntity::getCKey, key).update();
    }

    @Override
    public String getValue(String key) {
        BaseSysConfEntity baseSysConfEntity = getOne(QueryWrapper.create().eq(BaseSysConfEntity::getCKey, key));
        if (baseSysConfEntity != null) {
            return baseSysConfEntity.getCValue();
        }
        return null;
    }

    @Override
    public String getValueWithCache(String key) {
        String value = radeCache.get(key, String.class);
        if (value != null) {
            return value;
        }
        value = getValue(key);
        if (value != null) {
            radeCache.set(key, value);
        }
        return value;
    }

    @Override
    public void setValue(String key, String value) {
        BaseSysConfEntity baseSysConfEntity = new BaseSysConfEntity();
        baseSysConfEntity.setCKey(key);
        baseSysConfEntity.setCValue(value);
        save(baseSysConfEntity);
    }
}