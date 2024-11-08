package org.dows.modules.sys.controller.app;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.dows.core.aid.RadeAid;
import org.dows.core.annotation.RadeController;
import org.dows.core.annotation.TokenIgnore;
import org.dows.core.web.Response;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/8/2024 9:32 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@RequiredArgsConstructor
@Tag(name = "应用接口文档", description = "应用接口文档")
@RadeController
public class AppApiController {

    private final RadeAid radeAid;

    @TokenIgnore
    @Operation(summary = "实体信息与路径", description = "系统所有的实体信息与路径，供前端自动生成代码与服务")
    @GetMapping("/doc")
    public Response doc() {
        return Response.ok(radeAid.getApp());
    }
}

