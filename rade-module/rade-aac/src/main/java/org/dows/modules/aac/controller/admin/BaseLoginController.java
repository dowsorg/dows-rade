package org.dows.modules.aac.controller.admin;

import cn.hutool.core.lang.Dict;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.core.annotation.RadeController;
import org.dows.core.security.SecurityUser;
import org.dows.core.web.Response;
import org.dows.modules.aac.service.BaseSysLoginService;
import org.dows.modules.rbac.service.BaseSysPermsService;
import org.dows.core.uat.UserProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 9:33 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@RequiredArgsConstructor
@Tag(name = "系统通用", description = "系统通用")
@RadeController()
public class BaseLoginController {

    final private BaseSysPermsService baseSysPermsService;

    final private BaseSysLoginService baseSysLoginService;


    final private UserProvider userProvider;


    @Operation(summary = "个人信息")
    @GetMapping("/person")
    public Response person(@RequestAttribute() Long adminUserId) {

        SecurityUser userInfo = userProvider.getUserInfoById(adminUserId);
        userInfo.setPassword(null);
        userInfo.setPasswordV(null);
        return Response.ok(userInfo);
    }

    @Operation(summary = "修改个人信息")
    @PostMapping("/personUpdate")
    public Response personUpdate(@RequestAttribute Long adminUserId, @RequestBody Dict body) {
        userProvider.personUpdate(adminUserId, body);
        return Response.ok();
    }

    @Operation(summary = "权限与菜单")
    @GetMapping("/permmenu")
    public Response permmenu(@RequestAttribute() Long adminUserId) {
        return Response.ok(baseSysPermsService.permmenu(adminUserId));
    }


    @Operation(summary = "退出")
    @PostMapping("/logout")
    public Response logout(@RequestAttribute Long adminUserId, @RequestAttribute String adminUsername) {
        baseSysLoginService.logout(adminUserId, adminUsername);
        return Response.ok();
    }

}

