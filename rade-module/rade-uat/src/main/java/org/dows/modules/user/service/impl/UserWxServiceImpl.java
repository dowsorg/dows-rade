package org.dows.modules.user.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.core.crud.BaseServiceImpl;
import org.dows.core.uat.WeiXinUser;
import org.dows.modules.user.entity.UserWxEntity;
import org.dows.modules.user.mapper.UserWxMapper;
import org.dows.modules.user.service.UserWxService;
import org.springframework.stereotype.Service;

/**
 * 微信用户
 */
@Service
@RequiredArgsConstructor
public class UserWxServiceImpl extends BaseServiceImpl<UserWxMapper, UserWxEntity> implements UserWxService {

    //private final WxProxy wxProxy;
    /**
     * 获得小程序用户信息
     */
    /*public UserWxEntity getMiniUserInfo(String code, String encryptedData, String iv) {
        // 获取 session
        WxMaJscode2SessionResult result = null;
        try {
            result = wxProxy.getSessionInfo(code);
            // 解密数据
            WxMaUserInfo wxMaUserInfo = wxProxy.getUserInfo(result.getSessionKey(), encryptedData, iv);
            if (ObjUtil.isNotEmpty(wxMaUserInfo)) {
                UserWxEntity userWxEntity = BeanUtil.copyProperties(wxMaUserInfo, UserWxEntity.class);
                userWxEntity.setOpenid(result.getOpenid());
                userWxEntity.setUnionid(wxMaUserInfo.getUnionId());
                return getBySave(userWxEntity, 0);
            }
        } catch (WxErrorException e) {
            RadePreconditions.alwaysThrow(e.getMessage(), e);
        }
        RadePreconditions.alwaysThrow("获得小程序用户信息");
        return null;
    }*/

    @Override
    public WeiXinUser newWeiXinUser() {
        return new UserWxEntity();
    }

    @Override
    public WeiXinUser getBySave(WeiXinUser weiXinUser, Integer type) {
        return doGetBySave((UserWxEntity) weiXinUser, type);
    }

    private UserWxEntity doGetBySave(UserWxEntity entity, Integer type) {
        UserWxEntity one = this.getOne(QueryWrapper.create().eq(UserWxEntity::getOpenid, entity.getOpenid()));
        if (ObjUtil.isEmpty(one)) {
            entity.setType(type);
            super.save(entity);
            return entity;
        }
        return one;
    }
}