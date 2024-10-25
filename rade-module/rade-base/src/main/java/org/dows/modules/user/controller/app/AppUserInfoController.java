package org.dows.modules.user.controller.app;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.core.annotation.RadeController;
import org.dows.core.web.Response;
import org.dows.core.crud.EntityUtils;
import org.dows.core.security.RadeSecurityUtil;
import org.dows.modules.user.entity.UserInfoEntity;
import org.dows.modules.user.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@RequiredArgsConstructor
@Tag(name = "用户信息", description = "用户信息")
@RadeController
public class AppUserInfoController {

    private final UserInfoService userInfoService;

    @Operation(summary = "用户个人信息", description = "获得App、小程序或者其他应用的用户个人信息")
    @GetMapping("/person")
    public Response person() {
        Long userId = RadeSecurityUtil.getCurrentUserId();
        UserInfoEntity userInfoEntity = userInfoService.person(userId);
        return Response.ok(EntityUtils.toMap(userInfoEntity,
                "password"));
    }

    @Operation(summary = "更新用户信息")
    @PostMapping("/updatePerson")
    public Response updatePerson(@RequestAttribute JSONObject requestParams) {
        UserInfoEntity infoEntity = requestParams.toBean(UserInfoEntity.class);
        infoEntity.setId(RadeSecurityUtil.getCurrentUserId());
        return Response.ok(
                userInfoService.updateById(infoEntity)
        );
    }

    @Operation(summary = "更新用户密码")
    @PostMapping("/updatePassword")
    public Response updatePassword(
            @RequestAttribute JSONObject requestParams
    ) {
        String password = requestParams.get("password", String.class);
        String code = requestParams.get("code", String.class);
        userInfoService.updatePassword(RadeSecurityUtil.getCurrentUserId(), password, code);
        return Response.ok();
    }

    @Operation(summary = "注销")
    @PostMapping("/logoff")
    public Response logoff() {
        userInfoService.logoff(RadeSecurityUtil.getCurrentUserId());
        return Response.ok();
    }

    @Operation(summary = "绑定手机号")
    @PostMapping("/bindPhone")
    public Response bindPhone(
            @RequestAttribute JSONObject requestParams) {
        String phone = requestParams.get("phone", String.class);
        String code = requestParams.get("code", String.class);
        userInfoService.bindPhone(RadeSecurityUtil.getCurrentUserId(), phone, code);
        return Response.ok();
    }
}
