package org.dows.modules.base.controller.admin.sys;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.core.web.Response;
import org.dows.modules.base.entity.sys.BaseSysDepartmentEntity;
import org.dows.modules.base.service.sys.BaseSysDepartmentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 系统部门
 */
@Tag(name = "系统部门", description = "系统部门")
@RadeController(api = {"add", "delete", "update", "list"})
public class AdminBaseSysDepartmentController
        extends BaseController<BaseSysDepartmentService, BaseSysDepartmentEntity> {

    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
    }

    @Operation(summary = "排序")
    @PostMapping("/order")
    public Response order(@RequestBody List<BaseSysDepartmentEntity> list) {
        this.service.order(list);
        return Response.ok();
    }
}