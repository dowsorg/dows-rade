package org.dows.modules.log.controller;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.core.crud.CrudOption;
import org.dows.core.web.Response;
import org.dows.modules.log.entity.BaseSysLogEntity;
import org.dows.modules.log.service.BaseSysLogService;
import org.dows.modules.sys.service.BaseSysConfService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

/**
 * 系统日志
 */
@RequiredArgsConstructor
@Tag(name = "系统日志", description = "系统日志")
@RadeController(api = {"page"})
public class AdminBaseSysLogController extends BaseController<BaseSysLogService, BaseSysLogEntity> {

    private final BaseSysConfService baseSysConfService;

    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
        CrudOption<BaseSysLogEntity> option = createOp()
                .keyWordLikeFields(BaseSysUserEntityTableDef.BASE_SYS_USER_ENTITY.NAME, BaseSysLogEntityTableDef.BASE_SYS_LOG_ENTITY.PARAMS);
        setPageOption(option);
    }

    @Operation(summary = "清理日志")
    @PostMapping("/clear")
    public Response clear() {
        service.clear(true);
        return Response.ok();
    }

    @Operation(summary = "设置日志保存时间")
    @PostMapping("/setKeep")
    public Response setKeep(@RequestAttribute JSONObject requestParams) {
        baseSysConfService.updateValue("logKeep", requestParams.getStr("value"));
        return Response.ok();
    }

    @Operation(summary = "获得日志报错时间")
    @PostMapping("/getKeep")
    public Response getKeep() {
        return Response.ok(baseSysConfService.getValue("logKeep"));
    }
}
