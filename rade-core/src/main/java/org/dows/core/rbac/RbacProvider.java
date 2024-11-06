package org.dows.core.rbac;

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
     * 更新用户角色
     * @param userId
     * @param roleIds
     */
    void updateUserRole(Long userId, Long[] roleIds);

    String[] getAllPerms();

}
