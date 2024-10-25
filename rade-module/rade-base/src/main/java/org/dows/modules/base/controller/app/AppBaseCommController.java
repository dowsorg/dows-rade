package org.dows.modules.base.controller.app;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.core.aid.RadeAid;
import org.dows.core.annotation.RadeController;
import org.dows.core.annotation.TokenIgnore;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.plugin.upload.FileUploadStrategyFactory;
import org.dows.core.web.Response;
import org.dows.modules.base.service.sys.BaseSysParamService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * app通用接口
 */
@RequiredArgsConstructor
@Tag(name = "应用通用", description = "应用通用")
@RadeController
public class AppBaseCommController {

    private final RadeAid radeAid;

    private final BaseSysParamService baseSysParamService;
    final private FileUploadStrategyFactory fileUploadStrategyFactory;
    @Value("${rade.sysParam.allowKeys:[]}")
    private List<String> allowKeys;

    @TokenIgnore
    @Operation(summary = "参数配置")
    @GetMapping("/param")
    public Response param(@RequestAttribute() JSONObject requestParams) {
        String key = requestParams.get("key", String.class);
        RadePreconditions.check(!allowKeys.contains(key), "非法操作");
        return Response.ok(baseSysParamService.dataByKey(key));
    }

    @TokenIgnore
    @Operation(summary = "实体信息与路径", description = "系统所有的实体信息与路径，供前端自动生成代码与服务")
    @GetMapping("/aid")
    public Response aid() {
        return Response.ok(radeAid.getApp());
    }


    @Operation(summary = "文件上传")
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public Response upload(@RequestPart(value = "upload", required = false) @Parameter(description = "文件") MultipartFile[] files,
                           HttpServletRequest request) {
        return Response.ok(fileUploadStrategyFactory.upload(files, request));
    }

    @Operation(summary = "文件上传模式")
    @GetMapping("/uploadMode")
    public Response uploadMode() {
        return Response.ok(fileUploadStrategyFactory.getMode());
    }
}
