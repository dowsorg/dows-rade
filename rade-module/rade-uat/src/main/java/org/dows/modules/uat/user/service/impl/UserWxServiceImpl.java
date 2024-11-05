package org.dows.modules.uat.user.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjUtil;
import org.dows.core.crud.BaseServiceImpl;
import org.dows.core.exception.RadePreconditions;
import org.dows.modules.uat.user.entity.UserWxEntity;
import org.dows.modules.uat.user.mapper.UserWxMapper;
import org.dows.modules.uat.user.proxy.WxProxy;
import org.dows.modules.uat.user.service.UserWxService;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.stereotype.Service;

/**
 * 微信用户
 */
@Service
@RequiredArgsConstructor
public class UserWxServiceImpl extends BaseServiceImpl<UserWxMapper, UserWxEntity> implements UserWxService {

    private final WxProxy wxProxy;

    /**
     * 获得小程序用户信息
     */
    public UserWxEntity getMiniUserInfo(String code, String encryptedData, String iv) {
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
    }

    public UserWxEntity getBySave(UserWxEntity entity, int type) {
        UserWxEntity one = this.getOne(
                QueryWrapper.create().eq(UserWxEntity::getOpenid, entity.getOpenid()));
        if (ObjUtil.isEmpty(one)) {
            entity.setType(type);
            super.save(entity);
            return entity;
        }
        return one;
    }
}