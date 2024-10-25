package org.dows.modules.base.controller.admin.sys;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.web.Response;
import org.dows.modules.base.entity.sys.BaseSysMenuEntity;
import org.dows.modules.base.service.sys.BaseSysMenuService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 系统菜单
 */
@Tag(name = "系统菜单", description = "系统菜单")
@RadeController(api = {"add", "delete", "update", "page", "list", "info"})
public class AdminBaseSysMenuController extends
    BaseController<BaseSysMenuService, BaseSysMenuEntity> {

    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
    }

    @Operation(summary = "创建代码", description = "创建代码")
    @PostMapping("/create")
    public Response create(@RequestBody() Map<String, Object> params) {
        RadePreconditions.checkEmpty(params.get("module"), "module参数不能为空");
        RadePreconditions.checkEmpty(params.get("entity"), "entity参数不能为空");
        RadePreconditions.checkEmpty(params.get("controller"), "controller参数不能为空");
        RadePreconditions.checkEmpty(params.get("service"), "service参数不能为空");
        RadePreconditions.checkEmpty(params.get("service-impl"), "service-impl参数不能为空");
        RadePreconditions.checkEmpty(params.get("mapper"), "mapper参数不能为空");
        RadePreconditions.checkEmpty(params.get("fileName"), "fileName参数不能为空");
        this.service.create(params);
        return Response.ok();
    }

    @Operation(summary = "导出", description = "导出")
    @PostMapping("/export")
    public Response export(@RequestBody Map<String, Object> params) {
        return Response.ok(this.service.export(getIds(params)));
    }

    @Operation(summary = "导入", description = "导入")
    @PostMapping("/import")
    public Response importMenu(@RequestBody Map<String, List<BaseSysMenuEntity>> params) {
        RadePreconditions.checkEmpty(params.get("menus"), "参数不能为空");
        return Response.ok(this.service.importMenu(params.get("menus")));
    }
}