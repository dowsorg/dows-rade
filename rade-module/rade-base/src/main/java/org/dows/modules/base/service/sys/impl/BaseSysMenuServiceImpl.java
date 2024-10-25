package org.dows.modules.base.service.sys.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.core.aid.RadeAid;
import org.dows.core.crud.BaseServiceImpl;
import org.dows.core.crud.ModifyEnum;
import org.dows.core.init.AppInstance;
import org.dows.core.security.RadeSecurityUtil;
import org.dows.core.util.CompilerUtils;
import org.dows.core.util.PathUtils;
import org.dows.core.util.SpringContextUtils;
import org.dows.modules.base.entity.sys.BaseSysMenuEntity;
import org.dows.modules.base.mapper.sys.BaseSysMenuMapper;
import org.dows.modules.base.service.sys.BaseSysMenuService;
import org.dows.modules.base.service.sys.BaseSysPermsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统菜单
 */
@Service
@RequiredArgsConstructor
public class BaseSysMenuServiceImpl extends BaseServiceImpl<BaseSysMenuMapper, BaseSysMenuEntity>
        implements BaseSysMenuService {

    final private BaseSysPermsService baseSysPermsService;

    final private RadeAid radeAid;

    @Override
    public Object list(JSONObject requestParams, QueryWrapper queryWrapper) {
        List<BaseSysMenuEntity> list = baseSysPermsService.getMenus(RadeSecurityUtil.getAdminUsername());
        list.forEach(e -> {
            List<BaseSysMenuEntity> parent = list.stream()
                    .filter(sysMenuEntity -> e.getParentId() != null && e.getParentId()
                            .equals(sysMenuEntity.getId()))
                    .toList();
            if (!parent.isEmpty()) {
                e.setParentName(parent.get(0).getName());
            }
        });
        return list;
    }

    @Override
    public void modifyAfter(JSONObject requestParams, BaseSysMenuEntity sysMenuEntity,
                            ModifyEnum type) {
        if (sysMenuEntity != null && sysMenuEntity.getId() != null) {
            baseSysPermsService.refreshPermsByMenuId(requestParams.getLong("id"));
        }
        if (requestParams.get("ids") != null) {
            Long[] ids = requestParams.get("ids", Long[].class);
            for (Long id : ids) {
                baseSysPermsService.refreshPermsByMenuId(id);
            }
        }
    }

    @Override
    public boolean delete(Long... ids) {
        super.delete(ids);
        for (Long id : ids) {
            this.delChildMenu(id);
        }
        return true;
    }

    /**
     * 删除子菜单
     *
     * @param id 删除的菜单ID
     */
    private void delChildMenu(Long id) {
        List<BaseSysMenuEntity> delMenu = list(
                QueryWrapper.create().eq(BaseSysMenuEntity::getParentId, id));
        if (CollectionUtil.isEmpty(delMenu)) {
            return;
        }
        Long[] ids = delMenu.stream().map(BaseSysMenuEntity::getId).toArray(Long[]::new);
        if (ArrayUtil.isNotEmpty(ids)) {
            delete(ids);
            for (Long delId : ids) {
                this.delChildMenu(delId);
            }
        }
    }

    @Override
    public Object export(List<Long> ids) {
        List<BaseSysMenuEntity> list = list(
                QueryWrapper.create().in(BaseSysMenuEntity::getId, ids));
        List<BaseSysMenuEntity> parentList = list.stream()
                .filter(o -> ObjUtil.isEmpty(o.getParentId())).toList();
        Map<Long, List<BaseSysMenuEntity>> map = list.stream()
                .filter(o -> ObjUtil.isNotEmpty(o.getParentId()))
                .collect(Collectors.groupingBy(BaseSysMenuEntity::getParentId));
        parentList.forEach(o -> handler(o, map));
        return parentList;
    }

    private void handler(BaseSysMenuEntity parentBaseSysMenuEntity,
                         Map<Long, List<BaseSysMenuEntity>> map) {
        parentBaseSysMenuEntity.setChildMenus(
                map.getOrDefault(parentBaseSysMenuEntity.getId(), new ArrayList<>()));
        parentBaseSysMenuEntity.getChildMenus().forEach(o -> {
            handler(o, map);
            o.setId(null);
            o.setParentId(null);
            o.setCreateTime(null);
            o.setUpdateTime(null);
        });
        parentBaseSysMenuEntity.setId(null);
        parentBaseSysMenuEntity.setParentId(null);
        parentBaseSysMenuEntity.setCreateTime(null);
        parentBaseSysMenuEntity.setUpdateTime(null);
    }

    @Override
    public boolean importMenu(List<BaseSysMenuEntity> menus) {
        menus.forEach(this::importMenu);
        return true;
    }

    private void importMenu(BaseSysMenuEntity sysMenuEntity) {
        sysMenuEntity.save();
        if (ObjUtil.isNotEmpty(sysMenuEntity.getChildMenus())) {
            sysMenuEntity.getChildMenus().forEach(o -> {
                o.setParentId(sysMenuEntity.getId());
                importMenu(o);
            });
        }
    }

    @Override
    public void create(Map<String, Object> params) {
        radeAid.clear();
        String module = (String) params.get("module");
        String controller = (String) params.get("controller");
        String entity = (String) params.get("entity");
        String service = (String) params.get("service");
        String serviceImpl = (String) params.get("service-impl");
        String mapper = (String) params.get("mapper");

        String fileName = (String) params.get("fileName");
        List<String> javaPathList = new ArrayList<>();
        String modulesPath = PathUtils.getModulesPath();
        // 创建的模块地址
        String actModulePath = CompilerUtils.createModule(modulesPath, module);
        // 创建顺序不能调整，类加载的时候需按这个顺序加载，否则类找不到
        // 创建 entity
        String entityPath = CompilerUtils.createEntity(actModulePath, fileName, entity);
        javaPathList.add(entityPath);
        // 创建 mapper
        javaPathList.add(CompilerUtils.createMapper(actModulePath, fileName, mapper));
        // 创建 service
        javaPathList.add(CompilerUtils.createService(actModulePath, fileName, service));
        // 创建 serviceImpl
        javaPathList.add(CompilerUtils.createServiceImpl(actModulePath, fileName, serviceImpl));
        // 创建 controller
        javaPathList.add(CompilerUtils.createController(actModulePath, fileName, controller));
        // 构建TableDef
        CompilerUtils.compilerEntityTableDef(actModulePath, fileName, entityPath, javaPathList);

        AppInstance bean = SpringContextUtils.getBean(AppInstance.class);
        // 重启
        bean.restart(javaPathList);
    }
}