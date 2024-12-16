package org.dows.modules.dict.controller.admin;

import cn.hutool.core.lang.Dict;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.core.web.Response;
import org.dows.modules.dict.entity.DictInfoEntity;
import org.dows.modules.dict.entity.table.DictInfoEntityTableDef;
import org.dows.modules.dict.service.DictInfoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 字典信息
 */
@Tag(name = "字典信息", description = "字典信息")
@RadeController(api = {"add", "delete", "update", "page", "list", "info"})
public class AdminDictInfoController extends BaseController<DictInfoService, DictInfoEntity> {
    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
        setListOption(createOp().fieldEq(DictInfoEntityTableDef.DICT_INFO_ENTITY.TYPE_ID)
                .keyWordLikeFields(DictInfoEntityTableDef.DICT_INFO_ENTITY.NAME)
                .queryWrapper(QueryWrapper.create().orderBy(DictInfoEntityTableDef.DICT_INFO_ENTITY.CREATE_TIME, false)));
    }

    @Operation(summary = "获得字典数据", description = "获得字典数据信息")
    @PostMapping("/data")
    public Response data(@RequestBody Dict body) {
        return Response.ok(this.service.data(body.get("types", null)));
    }
}
