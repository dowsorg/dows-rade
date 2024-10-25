package org.dows.modules.dict.controller.admin;

import cn.hutool.json.JSONObject;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.modules.dict.entity.DictTypeEntity;
import org.dows.modules.dict.service.DictTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import static org.dows.modules.dict.entity.table.DictTypeEntityTableDef.DICT_TYPE_ENTITY;

/**
 * 字典类型
 */
@Tag(name = "字典类型", description = "字典类型")
@RadeController(api = {"add", "delete", "update", "page", "list", "info"})
public class AdminDictTypeController extends BaseController<DictTypeService, DictTypeEntity> {

    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
        setPageOption(
            createOp().select(DICT_TYPE_ENTITY.ID, DICT_TYPE_ENTITY.KEY, DICT_TYPE_ENTITY.NAME));
    }
}
