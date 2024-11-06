package org.dows.modules.uat.user.service;

import cn.hutool.core.lang.Dict;
import org.dows.core.crud.BaseService;
import org.dows.modules.uat.user.entity.BaseSysUserEntity;
import org.dows.core.uat.UserProvider;

/**
 * 系统用户
 */
public interface BaseSysUserService extends BaseService<BaseSysUserEntity>, UserProvider {
    /**
     * 修改用户信息
     *
     * @param body 用户信息
     */
    void personUpdate(Long userId, Dict body);

    /**
     * 移动部门
     *
     * @param departmentId 部门ID
     * @param userIds      用户ID集合
     */
    void move(Long departmentId, Long[] userIds);
}
