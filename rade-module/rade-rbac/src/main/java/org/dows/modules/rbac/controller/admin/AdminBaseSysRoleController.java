package org.dows.modules.rbac.controller.admin;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.modules.rbac.entity.BaseSysRoleEntity;
import org.dows.modules.rbac.entity.table.BaseSysRoleEntityTableDef;
import org.dows.modules.rbac.service.BaseSysRoleService;


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

        setPageOption(createOp()
                .keyWordLikeFields(BaseSysRoleEntityTableDef.BASE_SYS_ROLE_ENTITY.NAME,
                        BaseSysRoleEntityTableDef.BASE_SYS_ROLE_ENTITY.LABEL)
                .queryWrapper(QueryWrapper.create().and(qw -> {
                            qw.eq(BaseSysRoleEntityTableDef.BASE_SYS_ROLE_ENTITY.USER_ID.getName(), tokenInfo.getLong("userId"))
                                    .or(w -> {
                                        Object o = tokenInfo.get("roleIds");
                                        if (o != null) {
                                            w.in(BaseSysRoleEntityTableDef.BASE_SYS_ROLE_ENTITY.ID.getName(), new JSONArray(o).toList(Long.class));
                                        }
                                    });
                        }, !isAdmin).
                        and(BaseSysRoleEntityTableDef.BASE_SYS_ROLE_ENTITY.LABEL.ne("admin"))));
    }

}