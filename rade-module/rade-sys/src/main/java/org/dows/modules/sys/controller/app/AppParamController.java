package org.dows.modules.sys.controller.app;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.core.aid.RadeAid;
import org.dows.core.annotation.RadeController;
import org.dows.core.annotation.TokenIgnore;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.web.Response;
import org.dows.modules.sys.service.BaseSysParamService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.List;

/**
 * app通用接口
 */
@RequiredArgsConstructor
@Tag(name = "应用参数配置", description = "应用参数配置")
@RadeController
public class AppParamController {

    @Value("${rade.sysParam.allowKeys:[]}")
    private List<String> allowKeys;

    private final BaseSysParamService baseSysParamService;

    private final RadeAid radeAid;

    @TokenIgnore
    @Operation(summary = "实体信息与路径", description = "系统所有的实体信息与路径，供前端自动生成代码与服务")
    @GetMapping("/aid")
    public Response aid() {
        return Response.ok(radeAid.getApp());
    }

    @TokenIgnore
    @Operation(summary = "参数配置")
    @GetMapping("/param")
    public Response param(@RequestAttribute() JSONObject requestParams) {
        String key = requestParams.get("key", String.class);
        RadePreconditions.check(!allowKeys.contains(key), "非法操作");
        return Response.ok(baseSysParamService.dataByKey(key));
    }
}
