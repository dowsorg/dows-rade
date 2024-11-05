package org.dows.modules.uat.user.service;

import org.dows.core.crud.BaseService;
import org.dows.modules.uat.user.entity.UserWxEntity;

/**
 * 微信用户
 */
public interface UserWxService extends BaseService<UserWxEntity> {

    /**
     * 获取小程序用户信息
     */
    UserWxEntity getMiniUserInfo(String code, String encryptedData, String iv);
}