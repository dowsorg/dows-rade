package org.dows.modules.base.controller.admin;

import org.dows.core.annotation.RadeController;
import org.dows.core.enums.UserTypeEnum;
import org.dows.core.aid.RadeAid;
import org.dows.core.web.Response;
import org.dows.modules.base.dto.sys.BaseSysLoginDto;
import org.dows.modules.base.service.sys.BaseSysLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 系统开放接口，无需权限校验
 */
@RequiredArgsConstructor
@Tag(name = "系统开放", description = "系统开放")
@RadeController()
public class AdminBaseOpenController {

    final private BaseSysLoginService baseSysLoginService;

    final private RadeAid radeAid;

    @Operation(summary = "实体信息与路径", description = "系统所有的实体信息与路径，供前端自动生成代码与服务")
    @GetMapping("/eps")
    public Response eps() {
        return Response.ok(radeAid.getAdmin());
    }

    @Operation(summary = "获得网页内容的参数值")
    @GetMapping("/html")
    public Response html() {
        return Response.ok();
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    public Response login(@RequestBody BaseSysLoginDto baseSysLoginDto) {
        return Response.ok(baseSysLoginService.login(baseSysLoginDto));
    }

    @Operation(summary = "验证码")
    @GetMapping("/captcha")
    public Response captcha(@Parameter(description = "类型：svg|base64") @RequestParam(defaultValue = "base64") String type,
                            @Parameter(description = "宽度") @RequestParam(defaultValue = "150") Integer width,
                            @Parameter(description = "高度") @RequestParam(defaultValue = "50") Integer height) {
        return Response.ok(baseSysLoginService.captcha(UserTypeEnum.ADMIN, type, width, height));
    }

    @Operation(summary = "刷新token")
    @GetMapping("/refreshToken")
    public Response refreshToken(String refreshToken) {
        return Response.ok(baseSysLoginService.refreshToken(refreshToken));
    }
}
