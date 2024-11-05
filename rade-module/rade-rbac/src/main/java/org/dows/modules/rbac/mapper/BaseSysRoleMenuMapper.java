package org.dows.modules.rbac.mapper;

import com.mybatisflex.core.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.dows.modules.rbac.entity.BaseSysRoleMenuEntity;

/**
 * 系统角色菜单
 */
public interface BaseSysRoleMenuMapper extends BaseMapper<BaseSysRoleMenuEntity> {
    /**
     * 跟菜单关联的所有用户
     *
     * @param menuId 菜单
     * @return 所有用户ID
     */
    Long[] userIds(@Param("menuId") Long menuId);
}