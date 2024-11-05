package org.dows.modules.uat.user.service;

import org.dows.core.crud.BaseService;
import org.dows.modules.uat.user.entity.UserAddressEntity;

/**
 * 用户模块-收货地址
 */
public interface UserAddressService extends BaseService<UserAddressEntity> {

    /**
     * 获取默认地址
     */
    Object getDefault(Long userId);
}
