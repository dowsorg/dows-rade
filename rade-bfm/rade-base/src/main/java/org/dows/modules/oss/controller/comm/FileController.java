package org.dows.modules.oss.controller.comm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.dows.core.annotation.RadeController;
import org.dows.core.plugin.upload.FileUploadStrategyFactory;
import org.dows.core.web.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 系统通用接口， 每个人都有权限操作
 */
@RequiredArgsConstructor
@Tag(name = "文件传输", description = "文件传输")
@RadeController()
@Slf4j
public class FileController {

    final private FileUploadStrategyFactory fileUploadStrategyFactory;


    @Operation(summary = "文件上传")
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public Response upload(@RequestPart(value = "upload", required = false)
                           @Parameter(description = "文件") MultipartFile[] files, HttpServletRequest request) {
        return Response.ok(fileUploadStrategyFactory.upload(files, request));
    }

    @Operation(summary = "文件上传模式")
    @GetMapping("/upload/mode")
    public Response uploadMode() {
        return Response.ok(fileUploadStrategyFactory.getMode());
    }




    @Operation(summary = "文件下载")
    @GetMapping(value = "/download", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public void download(String file, HttpServletResponse response) {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM.toString());
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s.%s", "test", "sql"));
        try {
            File files = new File(file);
            response.setContentLength(Long.valueOf(files.length()).intValue());
            FileInputStream fileInputStream = new FileInputStream(files);
            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(fileInputStream, outputStream);
            outputStream.flush();
        } catch (IOException e) {
            log.error("下载失败:{}", e.getMessage());
        }
    }

    @Operation(summary = "文件下传模式")
    @GetMapping("/download/mode")
    public Response downloadMode() {
        return Response.ok(fileUploadStrategyFactory.getMode());
    }
}
