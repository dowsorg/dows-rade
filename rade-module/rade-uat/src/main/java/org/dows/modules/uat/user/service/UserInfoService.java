package org.dows.modules.uat.user.service;

import org.dows.core.crud.BaseService;
import org.dows.modules.uat.user.entity.UserInfoEntity;

public interface UserInfoService extends BaseService<UserInfoEntity> {
    /**
     * 用户个人信息
     *
     * @param userId
     * @return
     */
    UserInfoEntity person(Long userId);

    /**
     * 更新用户密码
     */
    void updatePassword(Long userId, String password, String code);

    /**
     * 注销
     */
    void logoff(Long currentUserId);

    /**
     * 绑定手机号
     */
    void bindPhone(Long currentUserId, String phone, String code);
}
