package org.dows.app;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.BaseMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.core.config.InitializerProperties;
import org.dows.core.crud.EntityUtils;
import org.dows.core.crud.service.MapperProviderService;
import org.dows.modules.rbac.entity.BaseSysMenuEntity;
import org.dows.modules.rbac.service.BaseSysMenuService;
import org.dows.modules.sys.entity.BaseSysConfEntity;
import org.dows.modules.sys.service.BaseSysConfService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 数据库初始数据初始化 在 classpath:rade/data/db 目录下创建.json文件 并定义表数据， 由该类统一执行初始化
 **/
@Slf4j
@Component
@RequiredArgsConstructor
public class DBFromJsonInit implements ApplicationRunner {

    final private BaseSysConfService baseSysConfService;

    final private BaseSysMenuService baseSysMenuService;

    final private MapperProviderService mapperProviderService;

    final private ApplicationEventPublisher eventPublisher;

    final private InitializerProperties initializerProperties;

    /*@Value("${rade.initData}")
    private boolean initData;

    @Value("${rade.dataDir}")
    private String dataDir;

    @Value("${rade.menuDir}")
    private String menuDir;*/

    @Override
    public void run(ApplicationArguments args) {
        if (!initializerProperties.isInitData()) {
            return;
        }
        // 初始化自定义的数据
        extractedDb();
        // 初始化菜单数据
        extractedMenu();
        // 发送数据库初始化完成事件
        eventPublisher.publishEvent(new DbInitCompleteEvent(this));
        log.info("数据初始化完成！");
    }

    private List<File> getResourceFile(String dataDir) throws IOException {
        List<File> files = new ArrayList<>();
        if (StrUtil.startWith(dataDir, "file://")) {
            File file = new FileSystemResourceLoader().getResource(dataDir).getFile();
            if (file.isDirectory()) {
                files.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
            } else {
                files.add(file);
            }
        } else if (StrUtil.startWith(dataDir, "classpath*://") || StrUtil.startWith(dataDir, "classpath://")) {
            // 加载 JSON 文件
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(dataDir);
            for (Resource resource : resources) {
                files.add(resource.getFile());
            }
        }
        return files;
    }

    /**
     * 解析插入业务数据
     */
    private void extractedDb() {
        try {
            List<File> files = getResourceFile(initializerProperties.getDataDir());
            // 遍历所有.json文件
            analysisResources(files);
        } catch (Exception e) {
            log.error("Failed to initialize data", e);
        }
    }

    private void analysisResources(/*Resource[] resources*/List<File> files)
            throws IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String prefix = "db_";
        for (File resourceFile : files) {
            String fileName = prefix + resourceFile.getName();
            String value = baseSysConfService.getValue(fileName);
            if (StrUtil.isNotEmpty(value)) {
                log.info("{} 业务数据已初始化过...", fileName);
                continue;
            }
            String jsonStr = IoUtil.read(new FileInputStream(resourceFile), StandardCharsets.UTF_8);
            JSONObject jsonObject = JSONUtil.parseObj(jsonStr);
            // 遍历 JSON 文件中的数据
            analysisJson(jsonObject);

            BaseSysConfEntity baseSysUserEntity = new BaseSysConfEntity();
            baseSysUserEntity.setCKey(fileName);
            baseSysUserEntity.setCValue("success");
            // 当前文件已加载
            baseSysConfService.add(baseSysUserEntity);
            log.info("{} 业务数据初始化成功...", fileName);
        }
    }

    private void analysisJson(JSONObject jsonObject)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Map<String, Class<?>> tableMap = EntityUtils.findTableMap();
        for (String tableName : jsonObject.keySet()) {
            JSONArray records = jsonObject.getJSONArray(tableName);
            // 根据表名生成实体类名和 Mapper 接口名
            Class<?> entityClass = tableMap.get(tableName);
            BaseMapper<?> baseMapper = mapperProviderService.getMapperByEntityClass(entityClass);
            // 插入
            insertList(baseMapper, entityClass, records);
        }
    }

    /**
     * 插入列表数据
     */
    private void insertList(BaseMapper baseMapper, Class<?> entityClass, JSONArray records)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // 插入数据
        for (int i = 0; i < records.size(); i++) {
            JSONObject record = records.getJSONObject(i);
            Object entity = JSONUtil.toBean(record, entityClass);
            Method getIdMethod = entityClass.getMethod("getId");
            Object id = getIdMethod.invoke(entity);
            if (ObjUtil.isNotEmpty(id) && ObjUtil.isNotEmpty(baseMapper.selectOneById((Long) id))) {
                // 数据库已经有值了
                continue;
            }
            if (ObjUtil.isNotEmpty(id)) {
                // 带id插入
                baseMapper.insertSelectiveWithPk(entity);
            } else {
                baseMapper.insert(entity);
            }
        }
    }

    /**
     * 解析插入菜单数据
     */
    public void extractedMenu() {
        try {
            String prefix = "menu_";
            List<File> files = getResourceFile(initializerProperties.getMenuDir());
            for (File resourceFile : files) {
                String fileName = prefix + resourceFile.getName();
                String value = baseSysConfService.getValue(fileName);
                if (StrUtil.isNotEmpty(value)) {
                    log.info("{} 菜单数据已初始化过...", fileName);
                    continue;
                }
                analysisResources(resourceFile, fileName);
            }
        } catch (Exception e) {
            log.error("Failed to initialize data", e);
        }
    }

    private void analysisResources(File resourceFile, String fileName) throws IOException {
        String jsonStr = IoUtil.read(new FileInputStream(resourceFile), StandardCharsets.UTF_8);
        // 使用 解析 JSON 字符串
        JSONArray jsonArray = JSONUtil.parseArray(jsonStr);
        // 遍历 JSON 数组
        for (Object obj : jsonArray) {
            JSONObject jsonObj = (JSONObject) obj;
            // 将 JSON 对象转换为 Menu 对象
            parseMenu(jsonObj, null);
        }
        BaseSysConfEntity baseSysUserEntity = new BaseSysConfEntity();
        baseSysUserEntity.setCKey(fileName);
        baseSysUserEntity.setCValue("success");
        // 当前文件已加载
        baseSysConfService.add(baseSysUserEntity);
        log.info("{} 菜单数据初始化成功...", fileName);
    }

    // 递归解析 JSON 对象为 Menu 对象
    private void parseMenu(JSONObject jsonObj, BaseSysMenuEntity parentMenuEntity) {
        BaseSysMenuEntity menuEntity = BeanUtil.copyProperties(jsonObj, BaseSysMenuEntity.class);
        if (ObjUtil.isNotEmpty(parentMenuEntity)) {
            menuEntity.setParentName(parentMenuEntity.getName());
            menuEntity.setParentId(parentMenuEntity.getId());
        }
        baseSysMenuService.add(menuEntity);
        // 递归处理子菜单
        JSONArray childMenus = jsonObj.getJSONArray("childMenus");
        if (childMenus != null) {
            for (Object obj : childMenus) {
                JSONObject childObj = (JSONObject) obj;
                parseMenu(childObj, menuEntity);
            }
        }
    }

    @Getter
    public static class DbInitCompleteEvent {
        private final Object source;

        public DbInitCompleteEvent(Object source) {
            this.source = source;
        }

    }
}
