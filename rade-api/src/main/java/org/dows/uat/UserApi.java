package org.dows.uat;

import cn.hutool.core.lang.Dict;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 */
public interface UserApi {
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


    /**
     * 根据用户ID查询用户名
     *
     * @param userIds 用户ID集合
     * @return 用户ID:用户名
     */
    Map<Long, String> selectUserNameByUserId(List<Long> userIds);


    /**
     * 根据关键字模糊查询用户ID
     *
     * @param keyword 关键字
     * @return 用户ID集合
     */
    List<Long> selectUserIdByKeywordWithLike(String keyword);

    Long[] loginDepartmentIds();

    UserInfo getUserInfoByUsername(String username);

    UserInfo getUserInfoById(Long userId);
}
