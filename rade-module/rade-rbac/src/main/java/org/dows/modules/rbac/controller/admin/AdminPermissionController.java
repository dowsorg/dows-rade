package org.dows.modules.rbac.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.core.annotation.RadeController;
import org.dows.core.web.Response;
import org.dows.modules.rbac.service.BaseSysPermsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/8/2024 10:27 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */

@RequiredArgsConstructor
@Tag(name = "用户权限", description = "用户权限")
@RadeController()
public class AdminPermissionController {

    final private BaseSysPermsService baseSysPermsService;

    @Operation(summary = "菜单权限")
    @GetMapping("/menu/list")
    public Response menus(@RequestAttribute() Long adminUserId) {
        return Response.ok(baseSysPermsService.permmenu(adminUserId));
    }
}

