package org.dows.modules.uat.user.service.impl;

import org.dows.core.crud.BaseServiceImpl;
import org.dows.modules.uat.user.entity.UserAddressEntity;
import org.dows.modules.uat.user.mapper.UserAddressMapper;
import org.dows.modules.uat.user.service.UserAddressService;
import com.mybatisflex.core.query.QueryWrapper;
import org.springframework.stereotype.Service;

/**
 * 用户模块-收货地址
 */
@Service
public class UserAddressServiceImpl extends BaseServiceImpl<UserAddressMapper, UserAddressEntity> implements UserAddressService {

    @Override
    public Object getDefault(Long userId) {
        return this.getOne(QueryWrapper.create().eq(UserAddressEntity::getUserId, userId)
                .eq(UserAddressEntity::getIsDefault, true).limit(1));
    }
}