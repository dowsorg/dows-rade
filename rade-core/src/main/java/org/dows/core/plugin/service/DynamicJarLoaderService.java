package org.dows.core.plugin.service;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.core.exception.RadePreconditions;
import org.dows.core.plugin.PluginDetail;
import org.dows.core.plugin.PluginJson;
import org.dows.core.plugin.PluginRepository;
import org.dows.core.plugin.config.DynamicJarClassLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 动态加载jar包
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DynamicJarLoaderService {

    final private PluginRepository<? extends PluginDetail> pluginInfoService;

    private final Map<String, DynamicJarClassLoader> dynamicJarClassLoaderMap = new ConcurrentHashMap<>();

    private final Map<String, Object> pluginMap = new ConcurrentHashMap<>();

    /**
     * 加载class
     */
    private static List<Class<?>> loadClass(String key, URL jarUrl,
                                            DynamicJarClassLoader dynamicJarClassLoader) throws IOException {
        // 加载类
        List<Class<?>> plugins = new ArrayList<>();
        try (JarFile jarFile = ((JarURLConnection) jarUrl.openConnection()).getJarFile()) {
            List<JarEntry> list = jarFile.stream().filter(o -> o.getName().endsWith(".class")).toList();
            List<JarEntry> firstNElements = getFirstNElements(list, 100);
            // 先加载前100个类，主够包含了插件主类，其余的异步加载
            dynamicJarClassLoader.loadClass(firstNElements, plugins);
            // 异步加载插件依赖类
            dynamicJarClassLoader.asyncLoadClass(key, list);
        }
        return plugins;
    }

    public static <T> List<T> getFirstNElements(List<T> list, int n) {
        // 确保 n 不超过 list 的大小
        if (list.size() <= n) {
            return new ArrayList<>(list); // 返回原列表的副本
        } else {
            return new ArrayList<>(list.subList(0, n)); // 返回前 N 个元素的副本
        }
    }

    public PluginJson install(String jarFilePath, boolean force) throws Exception {
        URL jarUrl = new URL("jar:file:" + new File(jarFilePath).getAbsolutePath() + "!/");
        DynamicJarClassLoader dynamicJarClassLoader = new DynamicJarClassLoader(new URL[]{jarUrl},
                Thread.currentThread().getContextClassLoader());
        Thread.currentThread().setContextClassLoader(dynamicJarClassLoader);
        PluginJson pluginJson = getPluginJsonAndCheck(force, dynamicJarClassLoader);
        // 历史如果有安装过，先卸载
        uninstall(pluginJson.getKey());
        // 加载class
        List<Class<?>> plugins = loadClass(pluginJson.getKey(), jarUrl, dynamicJarClassLoader);
        // 校验插件
        checkPlugin(plugins);
        // 注册插件,目前一个插件包，只允许有一个插件入口
        registerPlugin(pluginJson.getKey(), plugins.get(0), dynamicJarClassLoader, force);
        return pluginJson;
    }

    /**
     * 校验，获取插件配置
     */
    private PluginJson getPluginJsonAndCheck(boolean force, DynamicJarClassLoader dynamicJarClassLoader) {
        InputStream inputStream = dynamicJarClassLoader.getResourceAsStream("plugin.json");
        RadePreconditions.check(ObjUtil.isEmpty(inputStream), "不合规插件：未找到plugin.json文件");
        String pluginJsonStr = StrUtil.str(IoUtil.readBytes(inputStream), "UTF-8");
        PluginJson pluginJson = JSONUtil.toBean(pluginJsonStr, PluginJson.class);
        RadePreconditions.check(ObjUtil.isEmpty(pluginJson.getKey()), "该插件缺少唯一标识");
        if (!force) {
            PluginDetail byKey = pluginInfoService.getByKey(pluginJson.getKey());
            if (ObjUtil.isNotEmpty(byKey)) {
                RadePreconditions.returnData(
                        new RadePreconditions.ReturnData(1, "插件已存在，继续安装将覆盖"));
            }
        }
        if (ObjUtil.isNotEmpty(pluginJson.getHook())) {
            // 查找hook是否已经存在,提示是否要替换，原hook将关闭
            PluginDetail pluginDetailEntity = pluginInfoService
                    .getPluginInfoByHook(pluginJson.getHook());
            if (!force) {
                RadePreconditions.returnData(
                        ObjUtil.isNotEmpty(pluginDetailEntity)
                                && !ObjUtil.equals(pluginDetailEntity.getKey(), pluginJson.getKey()),
                        new RadePreconditions.ReturnData(1,
                                "插件已存在相同hook: {}，继续安装将关闭原来插件（同名hook，只能有一个状态开启）",
                                pluginJson.getHook()));

            } else if (ObjUtil.isNotEmpty(pluginDetailEntity)
                    && !ObjUtil.equals(pluginDetailEntity.getKey(), pluginJson.getKey())) {
                // 存在同名hook，需将原hook修改为关闭
                pluginJson.setSameHookId(pluginDetailEntity.getId());
            }
        }
        return pluginJson;
    }

    /**
     * 注册插件
     */
    private void registerPlugin(String key, Class<?> pluginClazz,
                                DynamicJarClassLoader dynamicJarClassLoader,
                                boolean force) {
        if (!force && pluginMap.containsKey(key)) {
            dynamicJarClassLoader.unload();
            RadePreconditions.returnData(
                    new RadePreconditions.ReturnData(1, "插件已存在，继续安装将覆盖"));
        }
        if (ObjUtil.isNotEmpty(key)) {
            pluginMap.remove(key);
            pluginMap.put(key, ReflectUtil.newInstance(pluginClazz));
            dynamicJarClassLoaderMap.put(key, dynamicJarClassLoader);
        }
        log.info("插件{}注册成功.", key);
    }

    /**
     * 卸载
     */
    public void uninstall(String key) {
        DynamicJarClassLoader dynamicJarClassLoader = getDynamicJarClassLoader(key);
        if (dynamicJarClassLoader == null) {
            return;
        }
        log.info("插件{}开始卸载", key);
        pluginMap.remove(key);
        dynamicJarClassLoader.unload();
        log.info("插件{}卸载完成", key);
    }

    /**
     * 校验插件是否合法
     */
    private void checkPlugin(List<Class<?>> plugins) {
        RadePreconditions.checkEmpty(plugins, "未找到插件程序");
        int size = plugins.size();
        RadePreconditions.check(size == 0,
                "没找到符合规范的插件,插件需有@RadePlugin注解，且以Plugin结尾(如：DemoPlugin)");
        RadePreconditions.check(size > 1,
                "识别到当前安装包有多个插件(只能有一个@RadePlugin注解)，一次只支持一个插件导入");
    }

    /**
     * 获取插件实例对象
     */
    public Object getBeanInstance(String key) {
        RadePreconditions.checkEmpty(key, "插件key is null");
        if (pluginMap.containsKey(key)) {
            return pluginMap.get(key);
        }
        RadePreconditions.alwaysThrow("插件 {} 未找到", key);
        return null;
    }

    /**
     * 获取自定义类加载器
     */
    public DynamicJarClassLoader getDynamicJarClassLoader(String key) {
        return dynamicJarClassLoaderMap.get(key);
    }
}
