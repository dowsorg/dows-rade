package org.dows.modules.base.controller.admin.sys;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.modules.base.entity.sys.BaseSysRoleEntity;
import org.dows.modules.base.service.sys.BaseSysRoleService;

import static org.dows.modules.base.entity.sys.table.BaseSysRoleEntityTableDef.BASE_SYS_ROLE_ENTITY;

/**
 * 系统角色
 */
@Tag(name = "系统角色", description = "系统角色")
@RadeController(api = {"add", "delete", "update", "page", "list", "info"})
public class AdminBaseSysRoleController extends BaseController<BaseSysRoleService, BaseSysRoleEntity> {

    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
        JSONObject tokenInfo = requestParams.getJSONObject("tokenInfo");
        boolean isAdmin = tokenInfo.getStr("username").equals("admin");

        setPageOption(createOp().keyWordLikeFields(BASE_SYS_ROLE_ENTITY.NAME, BASE_SYS_ROLE_ENTITY.LABEL).queryWrapper(QueryWrapper.create().and(qw -> {
            qw.eq(BASE_SYS_ROLE_ENTITY.USER_ID.getName(), tokenInfo.getLong("userId")).or(w -> {
                Object o = tokenInfo.get("roleIds");
                if (o != null) {
                    w.in(BASE_SYS_ROLE_ENTITY.ID.getName(), new JSONArray(o).toList(Long.class));
                }
            });
        }, !isAdmin).and(BASE_SYS_ROLE_ENTITY.LABEL.ne("admin"))));
    }

}