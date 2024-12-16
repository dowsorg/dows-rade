package org.dows.modules.user.service;

import org.dows.core.crud.BaseService;
import org.dows.core.uat.WeiXinUserProvider;
import org.dows.modules.user.entity.UserWxEntity;

/**
 * 微信用户
 */
public interface UserWxService extends BaseService<UserWxEntity>, WeiXinUserProvider {

    /**
     * 获取小程序用户信息
     */
    //UserWxEntity getMiniUserInfo(String code, String encryptedData, String iv);
}
