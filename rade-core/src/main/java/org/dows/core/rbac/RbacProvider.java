package org.dows.core.rbac;

import org.dows.core.security.SecurityUser;

import java.util.List;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 10:48 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface RbacProvider {
    /**
     * 获取用户角色ID
     *
     * @param userId
     * @return
     */
    Long[] getRoles(Long userId);

    /**
     * 获得角色数组
     *
     * @param userInfo 用户
     * @return 返回角色数组
     */
    Long[] getRoles(SecurityUser userInfo);


    /**
     * 获得权限
     *
     * @param userId 用户ID
     * @return 返回用户相关的权限信息
     */
    String[] getPerms(Long userId);

    /**
     * 更新用户角色
     * @param userId
     * @param roleIds
     */
    void updateUserRole(Long userId, Long[] roleIds);

    String[] getAllPerms();



    /**
     * 根据角色获得部门ID
     *
     * @param roleIds 角色ID数组
     * @return 部门ID数组
     */
    Long[] getDepartmentIdsByRoleIds(Long[] roleIds);

    /**
     * 根据用户ID获得部门ID
     *
     * @param userId 角色ID数组
     * @return 部门ID数组
     */
    Long[] getDepartmentIdsByRoleIds(Long userId);



    default List<String> getRoleUri(){
        return null;
    };

}
