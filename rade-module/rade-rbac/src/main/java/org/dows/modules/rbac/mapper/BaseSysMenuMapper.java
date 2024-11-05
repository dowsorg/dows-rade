package org.dows.modules.rbac.mapper;

import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.dows.modules.rbac.entity.BaseSysMenuEntity;

import java.util.List;

/**
 * 系统菜单
 */
public interface BaseSysMenuMapper extends BaseMapper<BaseSysMenuEntity> {
    /**
     * 根据角色ID获得所有菜单
     *
     * @param roleIds 角色ID集合
     * @return SysMenuEntity
     */
    List<BaseSysMenuEntity> getMenus(@Param("roleIds") Long[] roleIds);
}
