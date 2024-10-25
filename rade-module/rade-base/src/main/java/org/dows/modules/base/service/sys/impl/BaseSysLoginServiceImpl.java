package org.dows.modules.base.service.sys.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import com.mybatisflex.core.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.dows.core.cache.RadeCache;
import org.dows.core.enums.UserTypeEnum;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.security.RadeSecurityUtil;
import org.dows.core.security.jwt.JwtTokenUtil;
import org.dows.modules.base.dto.sys.BaseSysLoginDto;
import org.dows.modules.base.entity.sys.BaseSysUserEntity;
import org.dows.modules.base.mapper.sys.BaseSysUserMapper;
import org.dows.modules.base.service.sys.BaseSysLoginService;
import org.dows.modules.base.service.sys.BaseSysPermsService;
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

    private final BaseSysUserMapper baseSysUserMapper;

    private final BaseSysPermsService baseSysPermsService;

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
        BaseSysUserEntity baseSysUserEntity =
                baseSysUserMapper.selectOneByQuery(
                        QueryWrapper.create()
                                .eq(BaseSysUserEntity::getUsername, baseSysLoginDto.getUsername()));
        RadePreconditions.check(
                ObjectUtil.isEmpty(baseSysUserEntity) || baseSysUserEntity.getStatus() == 0, "用户已禁用");
        Long[] roleIds = baseSysPermsService.getRoles(baseSysUserEntity);
        radeCache.del("verify:img:" + baseSysLoginDto.getCaptchaId());
        return generateToken(roleIds, baseSysUserEntity, null);
    }

    @Override
    public void logout(Long adminUserId, String username) {
        RadeSecurityUtil.adminLogout(adminUserId, username);
    }

    @Override
    public Object refreshToken(String refreshToken) {
        RadePreconditions.check(!jwtTokenUtil.validateRefreshToken(refreshToken), "错误的refreshToken");
        JWT jwt = jwtTokenUtil.getTokenInfo(refreshToken);
        RadePreconditions.check(jwt == null || !(Boolean) jwt.getPayload("isRefresh"),
                "错误的refreshToken");
        BaseSysUserEntity baseSysUserEntity =
                baseSysUserMapper.selectOneById(Convert.toLong(jwt.getPayload("userId")));
        Long[] roleIds = baseSysPermsService.getRoles(baseSysUserEntity);
        return generateToken(roleIds, baseSysUserEntity, refreshToken);
    }

    private Dict generateToken(Long[] roleIds, BaseSysUserEntity baseSysUserEntity, String refreshToken) {
        Dict tokenInfo =
                Dict.create()
                        .set("userType", UserTypeEnum.ADMIN.name())
                        .set("roleIds", roleIds)
                        .set("username", baseSysUserEntity.getUsername())
                        .set("userId", baseSysUserEntity.getId())
                        .set("passwordVersion", baseSysUserEntity.getPasswordV());
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
