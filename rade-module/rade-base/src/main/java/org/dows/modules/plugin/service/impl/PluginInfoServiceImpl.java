package org.dows.modules.plugin.service.impl;

import static org.dows.modules.plugin.entity.table.PluginInfoEntityTableDef.PLUGIN_INFO_ENTITY;

import org.dows.core.crud.BaseServiceImpl;
import org.dows.core.plugin.PluginDetail;
import org.dows.modules.plugin.entity.PluginInfoEntity;
import org.dows.modules.plugin.mapper.PluginInfoMapper;
import org.dows.modules.plugin.service.PluginInfoService;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 插件信息服务类
 */
@Service
public class PluginInfoServiceImpl extends BaseServiceImpl<PluginInfoMapper, PluginInfoEntity>
        implements PluginInfoService {

    /**
     * 通过key获取插件信息
     */
    @Override
    public PluginInfoEntity getByKey(String key) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.and(PLUGIN_INFO_ENTITY.KEY.eq(key));
        return getOne(queryWrapper);
    }

    /**
     * 通过hook获取插件信息
     */
    @Override
    public PluginInfoEntity getPluginInfoByHook(String hook) {
        QueryWrapper queryWrapper = getPluginInfoEntityQueryWrapper().and(PLUGIN_INFO_ENTITY.HOOK.eq(hook))
                .and(PLUGIN_INFO_ENTITY.STATUS.eq(1)).limit(1);
        return getOne(queryWrapper);
    }

    /**
     * 通过id获取插件信息
     */
    @Override
    public PluginInfoEntity getPluginInfoById(Long id) {
        QueryWrapper queryWrapper = getPluginInfoEntityQueryWrapper().and(PLUGIN_INFO_ENTITY.ID.eq(id));
        return getOne(queryWrapper);
    }

    @Override
    public PluginInfoEntity newEmptyPlugin() {
        return new PluginInfoEntity();
    }

    @Override
    public List<PluginDetail> listPlugin(QueryWrapper eq) {
        return List.of();
    }

    @Override
    public boolean removePluginById(Long id) {
        return false;
    }

    @Override
    public boolean updatePlugin(PluginDetail one) {
        return false;
    }

    @Override
    public void savePlugin(PluginDetail pluginDetail) {
        this.save((PluginInfoEntity) pluginDetail);
    }

    /**
     * 获取查询对象
     */
    private QueryWrapper getPluginInfoEntityQueryWrapper() {
        return QueryWrapper.create();
    }
}
