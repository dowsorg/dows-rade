package org.dows.modules.space.controller.admin;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.modules.space.entity.SpaceInfoEntity;
import org.dows.modules.space.service.SpaceInfoService;

import static org.dows.modules.space.entity.table.SpaceInfoEntityTableDef.SPACE_INFO_ENTITY;

/**
 * 文件空间信息
 */
@Tag(name = "文件空间信息", description = "文件空间信息")
@RadeController(api = {"add", "delete", "update", "page", "list", "info"})
public class AdminSpaceInfoController extends BaseController<SpaceInfoService, SpaceInfoEntity> {
    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
        setPageOption(createOp().fieldEq(SPACE_INFO_ENTITY.TYPE, SPACE_INFO_ENTITY.CLASSIFY_ID));
    }
}
