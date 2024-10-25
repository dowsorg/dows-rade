package org.dows.core.plugin.upload.strategy;

import jakarta.servlet.http.HttpServletRequest;
import org.dows.core.plugin.PluginDetail;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public interface FileUploadStrategy {

    /**
     * 文件上传
     */
    Object upload(MultipartFile[] files, HttpServletRequest request, PluginDetail fileInfo)
            throws IOException;

    /**
     * 文件上传模式
     *
     * @return 上传模式
     */
    Map<String, String> getMode(String key);

    default boolean isAbsolutePath(String pathStr) {
        Path path = Paths.get(pathStr);
        return path.isAbsolute();
    }

    default String getExtensionName(String fileName) {
        if (fileName.contains(".")) {
            String[] names = fileName.split("[.]");
            return "." + names[names.length - 1];
        }
        return "";
    }
}
