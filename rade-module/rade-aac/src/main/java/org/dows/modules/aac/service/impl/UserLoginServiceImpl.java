package org.dows.modules.aac.service.impl;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.MD5;
import cn.hutool.jwt.JWT;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.error.WxErrorException;
import org.dows.core.cache.RadeCache;
import org.dows.core.enums.UserTypeEnum;
import org.dows.core.exception.RadePreconditions;
import org.dows.modules.aac.security.jwt.JwtTokenUtil;
import org.dows.modules.aac.security.jwt.JwtUser;
import org.dows.modules.base.service.sys.BaseSysLoginService;
import org.dows.modules.uat.user.entity.UserInfoEntity;
import org.dows.modules.uat.user.entity.UserWxEntity;
import org.dows.modules.uat.user.proxy.WxProxy;
import org.dows.modules.uat.user.service.UserInfoService;
import org.dows.modules.uat.user.service.UserLoginService;
import org.dows.modules.uat.user.service.UserWxService;
import org.dows.modules.uat.user.util.UserSmsUtil;
import org.dows.modules.uat.user.util.UserSmsUtil.SendSceneEnum;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserLoginServiceImpl implements UserLoginService {

    private final static List<GrantedAuthority> authority =
            List.of(new SimpleGrantedAuthority("ROLE_" + UserTypeEnum.APP.name()));
    private final RadeCache radeCache;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserInfoService userInfoService;
    private final UserSmsUtil userSmsUtil;
    private final BaseSysLoginService baseSysLoginService;
    private final UserWxService userWxService;
    private final WxProxy wxProxy;

    @Override
    public void smsCode(String phone, String captchaId, String code) {
        // 校验图片验证码，不通过直接抛异常
        baseSysLoginService.captchaCheck(captchaId, code);
        userSmsUtil.sendVerifyCode(phone, SendSceneEnum.ALL);
        radeCache.del("verify:img:" + captchaId);
    }

    @Override
    public Object phoneVerifyCode(String phone, String smsCode) {
        // 校验短信验证码，不通过直接抛异常
        userSmsUtil.checkVerifyCode(phone, smsCode, SendSceneEnum.ALL);
        return generateTokenByPhone(phone);
    }


    @Override
    public Object refreshToken(String refreshToken) {
        RadePreconditions.check(!jwtTokenUtil.validateRefreshToken(refreshToken), "错误的refreshToken");
        JWT jwt = jwtTokenUtil.getTokenInfo(refreshToken);
        RadePreconditions.check(jwt == null || !(Boolean) jwt.getPayload("isRefresh"),
                "错误的refreshToken");
        Long userId = Convert.toLong(jwt.getPayload("userId"));
        return generateToken(userId, refreshToken);
    }

    @Override
    public Object mini(String code, String encryptedData, String iv) {
        UserWxEntity userWxEntity = userWxService.getMiniUserInfo(code, encryptedData, iv);
        return wxLoginToken(userWxEntity);
    }

    private Object wxLoginToken(UserWxEntity userWxEntity) {
        String unionId = ObjUtil.isNotEmpty(userWxEntity.getUnionid()) ? userWxEntity.getUnionid()
                : userWxEntity.getOpenid();
        UserInfoEntity userInfoEntity = userInfoService.getOne(
                QueryWrapper.create().eq(UserInfoEntity::getUnionid, unionId));
        if (ObjUtil.isEmpty(userInfoEntity)) {
            userInfoEntity = new UserInfoEntity();
            userInfoEntity.setNickName(ObjUtil.isNotEmpty(userWxEntity.getNickName()) ? userWxEntity.getNickName() : generateRandomNickname());
            userInfoEntity.setGender(userWxEntity.getGender());
            userInfoEntity.setAvatarUrl(userWxEntity.getAvatarUrl());
            userInfoEntity.setUnionid(unionId);
            userInfoEntity.save();
        }
        return generateToken(userInfoEntity, null);
    }

    @Override
    public Object mp(String code) {
        return null;
    }

    @Override
    public Object wxApp(String code) {
        return null;
    }

    @Override
    public Object uniPhone(String accessToken, String openid, String appId) {
        return null;
    }

    @Override
    public Object miniPhone(String code, String encryptedData, String iv) {
        try {
            WxMaPhoneNumberInfo phoneNumber = wxProxy.getPhoneNumber(code);
            RadePreconditions.checkEmpty(phoneNumber, "微信登录失败");
            return generateTokenByPhone(phoneNumber.getPhoneNumber());
        } catch (WxErrorException e) {
            RadePreconditions.alwaysThrow(e.getMessage(), e);
        }
        RadePreconditions.alwaysThrow("微信登录失败");
        return null;
    }

    @Override
    public Object password(String phone, String password) {
        UserInfoEntity userInfoEntity = userInfoService.getOne(
                QueryWrapper.create().eq(UserInfoEntity::getPhone, phone));
        RadePreconditions.checkEmpty(userInfoEntity, "账号或密码错误");
        if (userInfoEntity.getPassword().equals(MD5.create().digestHex(password))) {
            return generateToken(userInfoEntity, null);
        }
        RadePreconditions.checkEmpty(userInfoEntity, "账号或密码错误");
        return null;
    }

    /**
     * 前置已校验用户的手机号，
     * 根据手机号找到用户生成token
     */
    private Object generateTokenByPhone(String phone) {
        UserInfoEntity userInfoEntity = userInfoService.getOne(
                QueryWrapper.create().eq(UserInfoEntity::getPhone, phone));
        if (ObjUtil.isEmpty(userInfoEntity)) {
            userInfoEntity = new UserInfoEntity();
            userInfoEntity.setPhone(phone);
            // 生成随机昵称
            userInfoEntity.setNickName(generateRandomNickname());
            userInfoEntity.save();
        }
        return generateToken(userInfoEntity, null);
    }

    /**
     * @return 生成的昵称
     */
    private String generateRandomNickname() {
        // 定义昵称的长度
        int length = 8;
        // 生成随机字符串
        return RandomUtil.randomString(length);
    }

    /**
     * 生成token
     */
    private Dict generateToken(Long userId, String refreshToken) {
        UserInfoEntity userInfoEntity = userInfoService.getById(userId);
        return generateToken(userInfoEntity, refreshToken);
    }

    private Dict generateToken(UserInfoEntity userInfoEntity, String refreshToken) {
        Dict tokenInfo = Dict.create()
                .set("userType", UserTypeEnum.APP.name())
                .set("userId", userInfoEntity.getId());
        String token = jwtTokenUtil.generateToken(tokenInfo);
        if (ObjUtil.isEmpty(refreshToken)) {
            refreshToken = jwtTokenUtil.generateRefreshToken(tokenInfo);
        }
        JwtUser jwtUser = new JwtUser(userInfoEntity.getId(),
                authority,
                ObjUtil.equals(userInfoEntity.getStatus(), 1));
        radeCache.set("app:userDetails:" + jwtUser.getUserId(), jwtUser);
        return Dict.create()
                .set("token", token)
                .set("expire", jwtTokenUtil.getExpire())
                .set("refreshToken", refreshToken)
                .set("refreshExpire", jwtTokenUtil.getRefreshExpire());
    }
}
