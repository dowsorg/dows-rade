package org.dows.modules.aac.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.system.UserInfo;
import lombok.RequiredArgsConstructor;
import org.dows.core.cache.RadeCache;
import org.dows.core.enums.UserTypeEnum;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.security.SecurityProvider;
import org.dows.core.security.SecurityUser;
import org.dows.modules.aac.BaseSysLoginDto;
import org.dows.modules.aac.service.BaseSysLoginService;
import org.dows.modules.rbac.service.BaseSysPermsService;
//import org.dows.security.provider.DefaultSecurityProvider;
import org.dows.security.jwt.JwtTokenUtil;
import org.dows.core.uat.UserProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BaseSysLoginServiceImpl implements BaseSysLoginService {

    private final RadeCache radeCache;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final BaseSysPermsService baseSysPermsService;


    private final UserProvider userProvider;

    private final SecurityProvider securityProvider;

    @Override
    public Object captcha(UserTypeEnum userTypeEnum, String type, Integer width, Integer height) {
        // 1、生成验证码 2、生成对应的ID并设置在缓存中，验证码过期时间30分钟；
        Map<String, Object> result = new HashMap<>();
        String captchaId = StrUtil.uuid();
        result.put("captchaId", captchaId);
        GifCaptcha gifCaptcha = CaptchaUtil.createGifCaptcha(width, height);
        if (ObjUtil.equals(userTypeEnum, UserTypeEnum.APP)) {
            gifCaptcha.setGenerator(new RandomGenerator("0123456789", 4));
        } else {
            gifCaptcha.setGenerator(new RandomGenerator(4));
        }
        gifCaptcha.setBackground(new Color(248, 248, 248));
        gifCaptcha.setMaxColor(60);
        gifCaptcha.setMinColor(55);
        result.put("data", "data:image/png;base64," + gifCaptcha.getImageBase64());
        radeCache.set("verify:img:" + captchaId, gifCaptcha.getCode(), 1800);
        return result;
    }

    @Override
    public void captchaCheck(String captchaId, String code) {
        String key = "verify:img:" + captchaId;
        String verifyCode = radeCache.get(key,
                String.class);
        boolean flag = StrUtil.isNotEmpty(verifyCode)
                && verifyCode.equalsIgnoreCase(code);
        if (!flag) {
            radeCache.del(key);
            RadePreconditions.alwaysThrow("验证码不正确");
        }
    }

    @Override
    public Object login(BaseSysLoginDto baseSysLoginDto) {
        // 1、检查验证码是否正确 2、执行登录操作
        captchaCheck(baseSysLoginDto.getCaptchaId(), baseSysLoginDto.getVerifyCode());
        UsernamePasswordAuthenticationToken upToken =
                new UsernamePasswordAuthenticationToken(
                        baseSysLoginDto.getUsername(), baseSysLoginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 查询用户信息并生成token
        SecurityUser userInfo = userProvider.getUserInfoByUsername(baseSysLoginDto.getUsername());
        /*BaseSysUserEntity baseSysUserEntity =
                baseSysUserMapper.selectOneByQuery(
                        QueryWrapper.create()
                                .eq(BaseSysUserEntity::getUsername, baseSysLoginDto.getUsername()));*/
        RadePreconditions.check(ObjectUtil.isEmpty(userInfo) || userInfo.getStatus() == 0, "用户已禁用");
        Long[] roleIds = baseSysPermsService.getRoles(userInfo);
        radeCache.del("verify:img:" + baseSysLoginDto.getCaptchaId());
        return generateToken(roleIds, userInfo, null);
    }

    @Override
    public void logout(Long adminUserId, String username) {
        securityProvider.adminLogout(adminUserId, username);
    }

    @Override
    public Object refreshToken(String refreshToken) {
        RadePreconditions.check(!jwtTokenUtil.validateRefreshToken(refreshToken), "错误的refreshToken");
        JWT jwt = jwtTokenUtil.getTokenInfo(refreshToken);
        RadePreconditions.check(jwt == null || !(Boolean) jwt.getPayload("isRefresh"), "错误的refreshToken");
        SecurityUser userInfo = userProvider.getUserInfoById(Convert.toLong(jwt.getPayload("userId")));
        /*BaseSysUserEntity baseSysUserEntity =
                baseSysUserMapper.selectOneById(Convert.toLong(jwt.getPayload("userId")));*/
        Long[] roleIds = baseSysPermsService.getRoles(userInfo);
        return generateToken(roleIds, userInfo, refreshToken);
    }

    private Dict generateToken(Long[] roleIds, SecurityUser userInfo, String refreshToken) {
        Dict tokenInfo =
                Dict.create()
                        .set("userType", UserTypeEnum.ADMIN.name())
                        .set("roleIds", roleIds)
                        .set("username", userInfo.getUsername())
                        .set("userId", userInfo.getId())
                        .set("passwordVersion", userInfo.getPasswordV());
        String token = jwtTokenUtil.generateToken(tokenInfo);
        if (StrUtil.isEmpty(refreshToken)) {
            refreshToken = jwtTokenUtil.generateRefreshToken(tokenInfo);
        }
        return Dict.create()
                .set("token", token)
                .set("expire", jwtTokenUtil.getExpire())
                .set("refreshToken", refreshToken)
                .set("refreshExpire", jwtTokenUtil.getRefreshExpire());
    }
}
