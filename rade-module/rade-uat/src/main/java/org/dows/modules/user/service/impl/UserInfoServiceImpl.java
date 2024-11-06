package org.dows.modules.user.service.impl;

import cn.hutool.crypto.digest.MD5;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.core.crud.BaseServiceImpl;
import org.dows.core.sms.SendSceneEnum;
import org.dows.core.sms.UserSmsUtil;
import org.dows.core.uat.UatUser;
import org.dows.modules.user.entity.UserInfoEntity;
import org.dows.modules.user.mapper.UserInfoMapper;
import org.dows.modules.user.service.UserInfoService;
//import org.dows.modules.user.util.UserSmsUtil;
//import org.dows.modules.user.util.UserSmsUtil.SendSceneEnum;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoMapper, UserInfoEntity> implements
        UserInfoService {

    private final UserSmsUtil userSmsUtil;

    @Override
    public UserInfoEntity person(Long userId) {
        UserInfoEntity info = getById(userId);
        info.setPassword(null);
        return info;
    }

    @Override
    public void updatePassword(Long userId, String password, String code) {
        UserInfoEntity info = getById(userId);
        userSmsUtil.checkVerifyCode(info.getPhone(), code, SendSceneEnum.ALL);
        info.setPassword(MD5.create().digestHex(password));
        info.updateById();
    }

    @Override
    public void logoff(Long userId) {
        UserInfoEntity info = new UserInfoEntity();
        info.setId(userId);
        info.setStatus(2);
        info.setNickName("已注销-00" + userId);
        info.updateById();
    }

    @Override
    public void bindPhone(Long userId, String phone, String code) {
        userSmsUtil.checkVerifyCode(phone, code, SendSceneEnum.ALL);
        UserInfoEntity info = new UserInfoEntity();
        info.setId(userId);
        info.setPhone(phone);
        info.updateById();
    }

    @Override
    public UatUser newUserInfo() {
        return new UserInfoEntity();
    }

    @Override
    public UatUser getUserInfoByPhone(String phone) {
        return getOne(QueryWrapper.create().eq(UserInfoEntity::getPhone, phone));
    }

    @Override
    public UatUser getUserInfoById(Long userId) {
        return getById(userId);
    }

    @Override
    public UatUser getUserInfoByUnionId(String unionId) {
        return getOne(QueryWrapper.create().eq(UserInfoEntity::getUnionid, unionId));
    }

    @Override
    public void saveUserInfo(UatUser userInfoEntity) {
        save((UserInfoEntity) userInfoEntity);
    }
}
