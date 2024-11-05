package org.dows.modules.uat.user.controller.app;

import cn.hutool.json.JSONObject;
import com.mybatisflex.core.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.annotation.RadeController;
import org.dows.core.crud.BaseController;
import org.dows.core.web.Response;
import org.dows.core.security.RadeSecurityUtil;
import org.dows.modules.uat.user.entity.UserAddressEntity;
import org.dows.modules.uat.user.service.UserAddressService;
import org.springframework.web.bind.annotation.GetMapping;

import static org.dows.modules.user.entity.table.UserAddressEntityTableDef.USER_ADDRESS_ENTITY;

/**
 * 用户模块-收货地址
 */
@Tag(name = "用户模块-收货地址", description = "用户模块-收货地址")
@RadeController(api = {"add", "delete", "update", "page", "list", "info"})
public class AppUserAddressController extends BaseController<UserAddressService, UserAddressEntity> {
    @Override
    protected void init(HttpServletRequest request, JSONObject requestParams) {
        setPageOption(
                createOp()
                        .queryWrapper(
                                QueryWrapper.create()
                                        .and(USER_ADDRESS_ENTITY.USER_ID.eq(RadeSecurityUtil.getCurrentUserId()))
                                        .orderBy(
                                                USER_ADDRESS_ENTITY.IS_DEFAULT.getName(), false)));

        setListOption(
                createOp()
                        .queryWrapper(
                                QueryWrapper.create()
                                        .and(USER_ADDRESS_ENTITY.USER_ID.eq(RadeSecurityUtil.getCurrentUserId()))
                                        .orderBy(
                                                USER_ADDRESS_ENTITY.IS_DEFAULT.getName(), false)));
    }

    @Operation(summary = "默认地址", description = "默认地址")
    @GetMapping("/default")
    public Response getDefault() {
        Long userId = RadeSecurityUtil.getCurrentUserId();
        return Response.ok(this.service.getDefault(userId));
    }
}