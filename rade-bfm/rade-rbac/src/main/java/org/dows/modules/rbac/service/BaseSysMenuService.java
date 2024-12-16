package org.dows.modules.rbac.service;

import org.dows.core.crud.BaseService;
import org.dows.modules.rbac.entity.BaseSysMenuEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统菜单
 */
public interface BaseSysMenuService extends BaseService<BaseSysMenuEntity> {

    Object export(List<Long> ids);

    boolean importMenu(List<BaseSysMenuEntity> menus);

    void create(Map<String, Object> params);
}
