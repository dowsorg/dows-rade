package org.dows.modules.space.controller.admin;

import cn.hutool.json.JSONObject;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.modules.space.entity.SpaceTypeEntity;
import org.dows.modules.space.service.SpaceTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 文件空间信息
 */
@Tag(name = "文件空间信息", description = "文件空间信息")
@RadeController(api = { "add", "delete", "update", "page", "list", "info" })
public class AdminSpaceTypeController extends BaseController<SpaceTypeService, SpaceTypeEntity> {

    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {

    }
}