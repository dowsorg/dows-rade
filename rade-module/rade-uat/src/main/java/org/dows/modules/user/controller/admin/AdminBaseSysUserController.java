package org.dows.modules.user.controller.admin;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.core.web.Response;
import org.dows.modules.user.entity.BaseSysUserEntity;
import org.dows.modules.user.service.BaseSysUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

/**
 * 系统用户
 */
@Tag(name = "系统用户", description = "系统用户")
@RadeController(api = {"add", "delete", "update", "page", "info"})
public class AdminBaseSysUserController extends BaseController<BaseSysUserService, BaseSysUserEntity> {

    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
    }

    @Operation(summary = "移动部门")
    @PostMapping("/move")
    public Response move(@RequestAttribute JSONObject requestParams) {
        service.move(requestParams.getLong("departmentId"), requestParams.get("userIds", Long[].class));
        return Response.ok();
    }

}