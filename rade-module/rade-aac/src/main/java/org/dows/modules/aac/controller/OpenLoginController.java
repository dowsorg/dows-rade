package org.dows.modules.aac.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.core.annotation.RadeController;
import org.dows.core.enums.UserTypeEnum;
import org.dows.core.web.Response;
import org.dows.modules.aac.BaseSysLoginDto;
import org.dows.modules.aac.service.BaseSysLoginService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 9:36 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@RequiredArgsConstructor
@Tag(name = "系统开放", description = "系统开放")
@RadeController()
public class OpenLoginController {

    final private BaseSysLoginService baseSysLoginService;


    @Operation(summary = "登录")
    @PostMapping("/login")
    public Response login(@RequestBody BaseSysLoginDto baseSysLoginDto) {
        return Response.ok(baseSysLoginService.login(baseSysLoginDto));
    }

    @Operation(summary = "验证码")
    @GetMapping("/captcha")
    public Response captcha(@Parameter(description = "类型：svg|base64") @RequestParam(defaultValue = "base64") String type,
                            @Parameter(description = "宽度") @RequestParam(defaultValue = "150") Integer width,
                            @Parameter(description = "高度") @RequestParam(defaultValue = "50") Integer height) {
        return Response.ok(baseSysLoginService.captcha(UserTypeEnum.ADMIN, type, width, height));
    }

    @Operation(summary = "刷新token")
    @GetMapping("/refreshToken")
    public Response refreshToken(String refreshToken) {
        return Response.ok(baseSysLoginService.refreshToken(refreshToken));
    }
}

