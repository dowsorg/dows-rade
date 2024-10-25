package org.dows.core.plugin.upload.strategy;

import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.config.FileModeEnum;
import org.dows.core.plugin.PluginDetail;
import org.dows.core.plugin.RadePluginInvokers;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component("cloudFileUploadStrategy")
public class CloudFileUploadStrategy implements FileUploadStrategy {

    @Override
    public Object upload(MultipartFile[] files, HttpServletRequest request, PluginDetail fileInfo)
            throws IOException {
        return RadePluginInvokers.invokePlugin(fileInfo.getKey());
    }

    @Override
    public Map<String, String> getMode(String key) {
        try {
            Object mode = RadePluginInvokers.invoke(key, "getMode");
            if (Objects.nonNull(mode)) {
                return (Map) mode;
            }
        } catch (Exception ignore) {
        }
        return Map.of("mode", FileModeEnum.CLOUD.value(),
                "type", FileModeEnum.CLOUD.type());
    }
}
