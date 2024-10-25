package org.dows.core.plugin.upload.strategy;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.dows.core.config.FileModeEnum;
import org.dows.core.config.LocalFileProperties;
import org.dows.core.exception.RadeException;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.plugin.PluginDetail;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Component("localFileUploadStrategy")
@RequiredArgsConstructor
public class LocalFileUploadStrategy implements FileUploadStrategy {

    final private LocalFileProperties localFileProperties;

    /**
     * 上传文件
     *
     * @param files 上传的文件
     * @return 文件路径
     */
    @Override
    public Object upload(MultipartFile[] files, HttpServletRequest request,
                         PluginDetail fileInfo) {
        RadePreconditions.check(StrUtil.isEmpty(localFileProperties.getBaseUrl()),
                "filePath 或 baseUrl 未配置");
        try {
            List<String> fileUrls = new ArrayList<>();
            String baseUrl = localFileProperties.getBaseUrl();
            // 绝对路径
            String filePath = System.getProperty("user.dir");
            String date = DateUtil.format(new Date(),
                    DatePattern.PURE_DATE_PATTERN);
            String relativePath =
                    File.separator + localFileProperties.getUploadPath() + File.separator + date;

            FileUtil.mkdir(filePath + relativePath);
            for (MultipartFile file : files) {
                // 保存文件
                String fileName = StrUtil.uuid().replaceAll("-", "") + getExtensionName(
                        Objects.requireNonNull(file.getOriginalFilename()));
                file.transferTo(new File(filePath + File.separator + relativePath
                        + File.separator + fileName));
                fileUrls.add(baseUrl + File.separator + date + File.separator + fileName);
            }
            if (fileUrls.size() == 1) {
                return fileUrls.get(0);
            }
            return fileUrls;
        } catch (Exception e) {
            throw new RadeException("文件上传失败", e);
        }
    }

    /**
     * 文件上传模式
     *
     * @return 上传模式
     */
    public Map<String, String> getMode(String key) {
        return Map.of("mode", FileModeEnum.LOCAL.value(),
                "type", FileModeEnum.LOCAL.type());
    }
}
