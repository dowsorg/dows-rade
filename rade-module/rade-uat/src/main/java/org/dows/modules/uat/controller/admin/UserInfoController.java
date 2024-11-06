package org.dows.modules.uat.controller.admin;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.modules.user.entity.UserInfoEntity;
import org.dows.modules.user.service.UserInfoService;

import static org.dows.modules.user.entity.table.UserInfoEntityTableDef.USER_INFO_ENTITY;


@Tag(name = "用户信息", description = "用户信息")
@RadeController(api = {"add", "delete", "update", "page", "list", "info"})
public class UserInfoController extends BaseController<UserInfoService, UserInfoEntity> {

    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
        setPageOption(createOp()
                .fieldEq(USER_INFO_ENTITY.STATUS, USER_INFO_ENTITY.GENDER, USER_INFO_ENTITY.LOGIN_TYPE)
                .keyWordLikeFields(USER_INFO_ENTITY.NICK_NAME, USER_INFO_ENTITY.PHONE)
        );
    }
}
