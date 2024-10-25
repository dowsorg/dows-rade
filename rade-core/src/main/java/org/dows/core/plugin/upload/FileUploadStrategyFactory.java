package org.dows.core.plugin.upload;

import cn.hutool.core.util.ObjUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.plugin.PluginDetail;
import org.dows.core.plugin.service.RadePluginService;
import org.dows.core.plugin.upload.strategy.FileUploadStrategy;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.dows.core.plugin.PluginConsts.uploadHook;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileUploadStrategyFactory {

    final private ApplicationContext applicationContext;

    final private RadePluginService radePluginService;

    private FileUploadStrategy getStrategy(PluginDetail fileInfo) {
        if (ObjUtil.isEmpty(fileInfo)) {
            return applicationContext.getBean("localFileUploadStrategy", FileUploadStrategy.class);
        }
        return applicationContext.getBean("cloudFileUploadStrategy", FileUploadStrategy.class);
    }

    public Object upload(MultipartFile[] files, HttpServletRequest request) {
        PluginDetail fileInfo = radePluginService.getPluginInfoEntityByHook(uploadHook);
        try {
            return getStrategy(fileInfo).upload(files, request, fileInfo);
        } catch (IOException e) {
            log.error("上传文件失败", e);
            RadePreconditions.alwaysThrow("上传文件失败 {}", e.getMessage());
        }
        return null;
    }

    public Object getMode() {
        PluginDetail fileInfo = radePluginService.getPluginInfoEntityByHook(uploadHook);
        String key = null;
        if (ObjUtil.isNotEmpty(fileInfo)) {
            key = fileInfo.getKey();
        }
        return getStrategy(fileInfo).getMode(key);
    }
}