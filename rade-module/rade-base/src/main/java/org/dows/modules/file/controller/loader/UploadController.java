package org.dows.modules.file.controller.loader;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.core.aid.RadeAid;
import org.dows.core.annotation.RadeController;
import org.dows.core.annotation.TokenIgnore;
import org.dows.core.plugin.upload.FileUploadStrategyFactory;
import org.dows.core.web.Response;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 系统通用接口， 每个人都有权限操作
 */
@RequiredArgsConstructor
@Tag(name = "文件上传", description = "文件上传")
@RadeController()
public class UploadController {

    final private FileUploadStrategyFactory fileUploadStrategyFactory;


    @Operation(summary = "文件上传")
    @PostMapping(/*value = "/upload",*/ consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public Response upload(@RequestPart(value = "upload", required = false)
                           @Parameter(description = "文件") MultipartFile[] files, HttpServletRequest request) {
        return Response.ok(fileUploadStrategyFactory.upload(files, request));
    }

    @Operation(summary = "文件上传模式")
    @GetMapping("/mode")
    public Response uploadMode() {
        return Response.ok(fileUploadStrategyFactory.getMode());
    }
}