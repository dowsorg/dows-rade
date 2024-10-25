package org.dows.modules.base.controller.admin.sys;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.modules.base.entity.sys.BaseSysParamEntity;
import org.dows.modules.base.service.sys.BaseSysParamService;
import org.springframework.web.bind.annotation.GetMapping;

import static org.dows.modules.base.entity.sys.table.BaseSysParamEntityTableDef.BASE_SYS_PARAM_ENTITY;

/**
 * 系统参数配置
 */
@Tag(name = "系统参数配置", description = "系统参数配置")
@RadeController(api = {"add", "delete", "update", "page", "info"})
public class AdminBaseSysParamController extends BaseController<BaseSysParamService, BaseSysParamEntity> {

    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
        setPageOption(createOp().fieldEq(BASE_SYS_PARAM_ENTITY.DATA_TYPE)
                .keyWordLikeFields(BASE_SYS_PARAM_ENTITY.NAME, BASE_SYS_PARAM_ENTITY.KEY_NAME));
    }

    @Operation(summary = "根据键返回网页的参数值")
    @GetMapping("/html")
    public String html(String key) {
        return service.htmlByKey(key);
    }
}