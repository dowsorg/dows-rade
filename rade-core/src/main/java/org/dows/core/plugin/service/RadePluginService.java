package org.dows.core.plugin.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.dows.core.exception.RadeException;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.plugin.PluginDetail;
import org.dows.core.plugin.PluginJson;
import org.dows.core.plugin.PluginRepository;
import org.dows.core.plugin.RadePluginInvokers;
import org.dows.core.plugin.config.DynamicJarClassLoader;
import org.dows.core.util.MapExtUtil;
import org.dows.core.util.PathUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 插件服务类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RadePluginService {

    final private DynamicJarLoaderService dynamicJarLoaderService;

    //final private PluginInfoService pluginInfoService;

    final private PluginRepository<? extends PluginDetail> pluginRepository;

    @Value("${rade.plugin.path}")
    private String pluginPath;

    /**
     * 忽略无变更，无需更新的字段
     */
    private static void ignoreNoChange(PluginDetail pluginDetail, PluginDetail one) {
        if (ObjUtil.equals(pluginDetail.getLogo(), one.getLogo())) {
            // 头像没变，无需更新
            pluginDetail.setLogo(null);
        }
        if (ObjUtil.equals(pluginDetail.getReadme(), one.getReadme())) {
            // readme没变，无需更新
            pluginDetail.setReadme(null);
        }
    }

    public void init() {
        List<? extends PluginDetail> list = pluginRepository.listPlugin(1);
        if (ObjUtil.isEmpty(list)) {
            log.info("没有可初始化的插件");
            return;
        }
        list.forEach(this::initInstall);
    }

    /**
     * 系统启动初始化安装插件
     */
    private void initInstall(PluginDetail entity) {
        PluginJson pluginJson = entity.getPluginJson();
        File file = new File(pluginJson.getJarPath());
        // 检查文件是否存在
        if (!file.exists()) {
            log.warn("插件文件不存在，请重新安装!");
            return;
        }
        file = new File(pluginJson.getJarPath());
        if (file.exists()) {
            ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                dynamicJarLoaderService.install(pluginJson.getJarPath(), true);
                // 设置配置
                RadePluginInvokers.setPluginJson(entity.getKey(), entity);
            } catch (Exception e) {
                log.error("初始化{}插件失败", entity.getName(), e);
            } finally {
                Thread.currentThread().setContextClassLoader(originalClassLoader);
            }
        }
    }

    /**
     * 安装jar
     */
    public void install(MultipartFile file, boolean force) {
        File jarFile = null;
        ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        String key = null;
        try {
            // 保存jar文件
            jarFile = saveJarFile(file);
            String jarFilePath = jarFile.getAbsolutePath();
            // 加载jar
            PluginJson pluginJson = dynamicJarLoaderService.install(jarFilePath, force);
            key = pluginJson.getKey();
            // 保存插件信息入库
            savePluginInfo(pluginJson, jarFilePath, jarFile, force);
            // 把 ApplicationContext 对象传递打插件类中，使其在插件中也能正常使用spring bean对象
            RadePluginInvokers.setApplicationContext(pluginJson.getKey());
        } catch (PersistenceException persistenceException) {
            extractedAfterErr(jarFile, key);
            if (persistenceException.getMessage().contains("Duplicate entry")) {
                // 唯一键冲突
                RadePreconditions.returnData(
                        new RadePreconditions.ReturnData(1, "插件已存在，继续安装将覆盖"));
            }

            RadePreconditions.alwaysThrow(persistenceException.getMessage());
        } catch (RadeException e) {
            extractedAfterErr(jarFile, key);
            throw e;
        } catch (Exception e) {
            log.error("插件安装失败", e);
            extractedAfterErr(jarFile, key);
            RadePreconditions.alwaysThrow("插件安装失败", e);
        } finally {
            Thread.currentThread().setContextClassLoader(originalClassLoader);
        }
    }

    private void extractedAfterErr(File jarFile, String key) {
        FileUtil.del(jarFile);
        if (ObjUtil.isNotEmpty(key)) {
            // 报错失败，调用卸载
            dynamicJarLoaderService.uninstall(key);
        }
    }

    /**
     * 保存jar文件
     */
    private File saveJarFile(MultipartFile file) throws IOException {
        String pathStr = pluginPath;
        if (!PathUtils.isAbsolutePath(pluginPath)) {
            // 相对路径
            pathStr = System.getProperty("user.dir") + File.separator + pluginPath;
        }
        // 将路径字符串转换为 Path 对象
        Path path = Paths.get(pathStr);
        // 检查路径是否存在
        if (!Files.exists(path)) {
            // 如果路径不存在，则创建目录（包括父目录）
            Files.createDirectories(path);
        }
        String jarFilePath = path + File.separator + System.currentTimeMillis() + "_" + file.getOriginalFilename() + ".jar";
        File jarFile = new File(jarFilePath);
        file.transferTo(jarFile);
        return jarFile;
    }

    /**
     * 卸载
     */
    public void uninstall(Long id) {
        PluginDetail pluginDetailEntity = pluginRepository.getPluginInfoById(id);
        RadePreconditions.checkEmpty(pluginDetailEntity, "插件不存在");
        dynamicJarLoaderService.uninstall(pluginDetailEntity.getKey());
        boolean flag = pluginRepository.removePluginById(pluginDetailEntity.getId());
        //boolean flag = pluginDetailEntity.removeById();
        if (flag) {
            FileUtil.del(pluginDetailEntity.getPluginJson().getJarPath());
        }
    }

    /**
     * 保存插件信息
     */
    private void savePluginInfo(PluginJson pluginJson, String jarFilePath, File jarFile, boolean force) {
        RadePreconditions.checkEmpty(pluginJson, "插件安装失败");
        pluginJson.setJarPath(jarFilePath);
        PluginDetail pluginDetail = pluginRepository.newEmptyPlugin();
        BeanUtil.copyProperties(pluginJson, pluginDetail);
        setLogoOrReadme(pluginJson, pluginDetail);
        pluginDetail.setKey(pluginJson.getKey());
        pluginDetail.setPluginJson(pluginJson);
        if (force) {
            // 判断是否有同名插件， 有将其关闭
            closeSameNamePlugin(pluginJson);
            // 覆盖插件
            coverPlugin(pluginJson, pluginDetail);
            return;
        }
//        pluginDetail.save();
        pluginRepository.savePlugin(pluginDetail);
    }

    /**
     * 覆盖插件
     */
    private void coverPlugin(PluginJson pluginJson, PluginDetail pluginDetail) {
        // 通过key 找到id
        PluginDetail one = pluginRepository.getByKey(pluginJson.getKey());
        if (ObjUtil.isNotEmpty(one)) {
            String oldJarPath = one.getPluginJson().getJarPath();
            // 重新加载配置不更新
            pluginDetail.setConfig(one.getConfig());
            pluginDetail.getPluginJson().setConfig(one.getConfig());
            // 设置插件配置
            RadePluginInvokers.setPluginJson(pluginDetail.getKey(), pluginDetail);
            CopyOptions options = CopyOptions.create().setIgnoreNullValue(true);
            // 忽略无变更，无需更新的字段
            ignoreNoChange(pluginDetail, one);
            BeanUtil.copyProperties(pluginDetail, one, options);
            one.setStatus(1);
            boolean flag = pluginRepository.updatePlugin(one);
            if (flag) {
                // 覆盖时删除旧版本插件
                FileUtil.del(oldJarPath);
            }
        } else {
            pluginRepository.savePlugin(pluginDetail);
            //pluginDetail.save();
        }
    }

    /**
     * 关闭同名插件
     */
    private void closeSameNamePlugin(PluginJson pluginJson) {
        if (ObjUtil.isNotEmpty(pluginJson.getSameHookId())) {
            // 存在同名，已强制安装，需将原插件关闭
            PluginDetail sameHookPlugin = pluginRepository.newEmptyPlugin();
            sameHookPlugin.setStatus(0);
            sameHookPlugin.setId(pluginJson.getSameHookId());
            updatePlugin(sameHookPlugin);
        }
    }

    /**
     * 设置logo或readme
     */
    private void setLogoOrReadme(PluginJson pluginJson, PluginDetail pluginDetail) {
        if (ObjUtil.isNotEmpty(pluginJson.getLogo())) {
            DynamicJarClassLoader dynamicJarClassLoader = dynamicJarLoaderService
                    .getDynamicJarClassLoader(pluginJson.getKey());
            InputStream inputStream = dynamicJarClassLoader.getResourceAsStream(
                    pluginJson.getLogo());
            if (ObjUtil.isNotEmpty(inputStream)) {
                pluginDetail.setLogo(Base64Encoder.encode(IoUtil.readBytes(inputStream)));
            }
        }
        if (ObjUtil.isNotEmpty(pluginJson.getReadme())) {
            DynamicJarClassLoader dynamicJarClassLoader = dynamicJarLoaderService
                    .getDynamicJarClassLoader(pluginJson.getKey());
            InputStream inputStream = dynamicJarClassLoader.getResourceAsStream(
                    pluginJson.getReadme());
            if (ObjUtil.isNotEmpty(inputStream)) {
                pluginDetail.setReadme(StrUtil.str(IoUtil.readBytes(inputStream), "UTF-8"));
            }
        }
    }

    public void updatePlugin(PluginDetail entity) {
        PluginDetail dbPluginDetailEntity = pluginRepository.getPluginInfoById(entity.getId());
        // 调用插件更新配置标识
        boolean invokePluginConfig = false;
        if (!MapExtUtil.compareMaps(entity.getConfig(), dbPluginDetailEntity.getConfig())) {
            // 不一致，说明更新了配置
            entity.setPluginJson(dbPluginDetailEntity.getPluginJson());
            entity.getPluginJson().setConfig(entity.getConfig());
            // 更新了配置， 且插件是开启状态
            invokePluginConfig = ObjUtil.equals(dbPluginDetailEntity.getStatus(), 1);
        }
        if (!ObjUtil.equals(entity.getStatus(), dbPluginDetailEntity.getStatus())) {
            // 更新状态
            updateStatus(entity, dbPluginDetailEntity);
        }
        if (invokePluginConfig) {
            // 更新配置
            RadePluginInvokers.setPluginJson(dbPluginDetailEntity.getKey(), entity);
        }
        pluginRepository.updatePlugin(entity);
    }

    /**
     * 更新插件状态
     */
    private void updateStatus(PluginDetail entity, PluginDetail dbPluginDetailEntity) {
        // 更新状态
        Integer status = entity.getStatus();
        if (ObjUtil.equals(status, 1)) {
            if (ObjUtil.isNotEmpty(dbPluginDetailEntity.getHook())) {
                // 查找是否有同名hook，有同名hook,如果状态为开启不允许在开启，需先关闭原来
                PluginDetail hookPlugin = pluginRepository
                        .getPluginInfoByHook(dbPluginDetailEntity.getHook());
                if (ObjUtil.isNotEmpty(hookPlugin)) {
                    RadePreconditions.check(
                            !ObjUtil.equals(hookPlugin.getKey(), dbPluginDetailEntity.getKey())
                                    && ObjUtil.equals(hookPlugin.getStatus(), 1),
                            "插件已存在相同hook: {}，请选关闭{}插件,在开启当前插件（同名hook，只能有一个插件开启）",
                            hookPlugin.getHook(),
                            hookPlugin.getName());
                }
            }
            // 从关闭置为开启，触发重新加载jar
            initInstall(dbPluginDetailEntity);
        } else if (ObjUtil.equals(status, 0)) {
            // 插件关闭 卸载jar
            dynamicJarLoaderService.uninstall(dbPluginDetailEntity.getKey());
        }
    }

    /**
     * 通过hook获取插件
     */
    public PluginDetail getPluginInfoEntityByHook(String hook) {
        return pluginRepository.getPluginInfoByHook(hook);
    }

    /**
     * 获取插件实例对象
     */
    public Object getInstance(String key) {
        return dynamicJarLoaderService.getBeanInstance(key);
    }
}