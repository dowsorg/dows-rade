package org.dows.modules.sys.controller.admin;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.core.aid.RadeAid;
import org.dows.core.annotation.RadeController;
import org.dows.core.annotation.TokenIgnore;
import org.dows.core.crud.BaseController;
import org.dows.core.web.Response;
import org.dows.modules.sys.entity.BaseSysParamEntity;
import org.dows.modules.sys.entity.table.BaseSysParamEntityTableDef;
import org.dows.modules.sys.service.BaseSysParamService;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * 系统参数配置
 */
@RequiredArgsConstructor
@Tag(name = "系统参数配置", description = "系统参数配置")
@RadeController(api = {"add", "delete", "update", "page", "info"})
public class ConfigParamController extends BaseController<BaseSysParamService, BaseSysParamEntity> {

    private final RadeAid radeAid;

    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
        setPageOption(createOp().fieldEq(BaseSysParamEntityTableDef.BASE_SYS_PARAM_ENTITY.DATA_TYPE)
                .keyWordLikeFields(BaseSysParamEntityTableDef.BASE_SYS_PARAM_ENTITY.NAME, BaseSysParamEntityTableDef.BASE_SYS_PARAM_ENTITY.KEY_NAME));
    }

    @Operation(summary = "根据键返回网页的参数值")
    @GetMapping("/html")
    public String html(String key) {
        return service.htmlByKey(key);
    }


    @TokenIgnore
    @Operation(summary = "实体信息与路径", description = "系统所有的实体信息与路径，供前端自动生成代码与服务")
    @GetMapping("/aid")
    public Response aid() {
        return Response.ok(radeAid.getAdmin());
    }
}