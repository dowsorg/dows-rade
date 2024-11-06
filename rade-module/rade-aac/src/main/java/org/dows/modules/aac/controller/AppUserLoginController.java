package org.dows.modules.aac.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.aac.AacApi;
import org.dows.core.annotation.RadeController;
import org.dows.core.annotation.TokenIgnore;
import org.dows.core.enums.UserTypeEnum;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.web.Response;
import org.dows.modules.aac.service.BaseSysLoginService;
import org.dows.modules.aac.service.UserLoginService;
import org.dows.modules.uat.user.controller.app.params.CaptchaParam;
import org.dows.modules.uat.user.controller.app.params.LoginParam;
import org.dows.modules.uat.user.controller.app.params.RefreshTokenParam;
import org.dows.modules.uat.user.controller.app.params.SmsCodeParam;
//import org.dows.modules.uat.user.service.UserLoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Tag(name = "用户登录", description = "用户登录")
@RadeController
public class AppUserLoginController {

    private final UserLoginService userLoginService;

    private final BaseSysLoginService baseSysLoginService;

    /**
     * 小程序登录
     */
    @TokenIgnore
    @Operation(summary = "小程序登录")
    @PostMapping("/mini")
    public Response mini(@RequestBody LoginParam param) {
        String code = param.getCode();
        String encryptedData = param.getEncryptedData();
        String iv = param.getIv();
        RadePreconditions.checkEmpty(code);
        RadePreconditions.checkEmpty(encryptedData);
        RadePreconditions.checkEmpty(iv);
        return Response.ok(userLoginService.mini(code, encryptedData, iv));
    }

    /**
     * 公众号登录
     */
    @TokenIgnore
    @Operation(summary = "公众号登录")
    @PostMapping("/mp")
    public Response mp(@RequestBody LoginParam param) {
        String code = param.getCode();
        RadePreconditions.checkEmpty(code);
        return Response.ok(userLoginService.mp(code));
    }

    /**
     * 微信APP授权登录
     */
    @TokenIgnore
    @Operation(summary = "微信APP授权登录")
    @PostMapping("/wxApp")
    public Response wxApp(@RequestBody LoginParam param) {
        String code = param.getCode();
        RadePreconditions.checkEmpty(code);
        return Response.ok(userLoginService.wxApp(code));
    }

    /**
     * 手机号登录
     */
    @TokenIgnore
    @Operation(summary = "手机号登录")
    @PostMapping("/phone")
    public Response phone(
            @RequestBody LoginParam param) {
        String phone = param.getPhone();
        String smsCode = param.getSmsCode();
        RadePreconditions.checkEmpty(phone);
        RadePreconditions.checkEmpty(smsCode);
        return Response.ok(userLoginService.phoneVerifyCode(phone, smsCode));
    }

    /**
     * 一键手机号登录
     */
    @TokenIgnore
    @Operation(summary = "一键手机号登录")
    @PostMapping("/uniPhone")
    public Response uniPhone(
            @RequestBody LoginParam param) {
        String accessToken = param.getAccess_token();
        String openid = param.getOpenid();
        String appId = param.getAppId();
        RadePreconditions.checkEmpty(accessToken);
        RadePreconditions.checkEmpty(openid);
        RadePreconditions.checkEmpty(appId);
        return Response.ok(userLoginService.uniPhone(accessToken, openid, appId));
    }

    /**
     * 绑定小程序手机号
     */
    @TokenIgnore
    @Operation(summary = "绑定小程序手机号")
    @PostMapping("/miniPhone")
    public Response miniPhone(@RequestBody LoginParam param) {
        String code = param.getCode();
        String encryptedData = param.getEncryptedData();
        String iv = param.getIv();
        RadePreconditions.checkEmpty(code);
        RadePreconditions.checkEmpty(encryptedData);
        RadePreconditions.checkEmpty(iv);
        return Response.ok(userLoginService.miniPhone(code, encryptedData, iv));
    }

    /**
     * 图片验证码
     */
    @TokenIgnore
    @Operation(summary = "图片验证码")
    @GetMapping("/captcha")
    public Response captcha(
            @ModelAttribute CaptchaParam param) {
        String type = param.getType();
        Integer width = param.getWidth();
        Integer height = param.getHeight();

        RadePreconditions.checkEmpty(type);
        RadePreconditions.checkEmpty(width);
        RadePreconditions.checkEmpty(height);

        return Response.ok(baseSysLoginService.captcha(UserTypeEnum.APP, type, width, height));
    }

    /**
     * 验证码
     */
    @TokenIgnore
    @Operation(summary = "验证码")
    @PostMapping("/smsCode")
    public Response smsCode(
            @RequestBody SmsCodeParam param) {
        String phone = param.getPhone();
        String captchaId = param.getCaptchaId();
        String code = param.getCode();

        RadePreconditions.checkEmpty(phone);
        RadePreconditions.checkEmpty(captchaId);
        RadePreconditions.checkEmpty(code);
        userLoginService.smsCode(phone, captchaId, code);
        return Response.ok();
    }

    /**
     * 刷新token
     */
    @TokenIgnore
    @Operation(summary = "刷新token")
    @PostMapping("/refreshToken")
    public Response refreshToken(@RequestBody RefreshTokenParam param) {
        return Response.ok(userLoginService.refreshToken(param.getRefreshToken()));
    }

    /**
     * 密码登录
     */
    @TokenIgnore
    @Operation(summary = "密码登录")
    @PostMapping("/password")
    public Response password(
            @RequestBody LoginParam param) {
        String phone = param.getPhone();
        String password = param.getPassword();

        RadePreconditions.checkEmpty(phone);
        RadePreconditions.checkEmpty(password);
        return Response.ok(userLoginService.password(phone, password));
    }
}
