package org.dows.sbi.handler;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.TemplateEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.sbi.form.*;
import org.dows.sbi.pojo.*;
import org.dows.sbi.util.MongoUtil;
import org.dows.sbi.util.SbiUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/22/2024 9:26 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ConfigHandler {
    private final TemplateEngine classpathTemplateEngine;
    private final DependProvider dependProvider;
    private final ProgressHandler progressHandler;
    // 配置设置
    private final Map<String, Object> CONFIG_SETTING = new ConcurrentHashMap<>();
    private static final int MAX_MESSAGE_LENGTH = 1024;

    /**
     * mariaDB配置
     *
     * @param configSetting
     * @param mysqlInstallForm
     */
    public void configMariadb(MariadbSetting configSetting, MySQLInstallForm mysqlInstallForm) {
        InstallSetting installSetting = configSetting.getInstall();
        try {
            String serviceName = mysqlInstallForm.getServiceName();
            if (StrUtil.isBlank(serviceName)) {
                throw new RuntimeException("服务名不能为空");
            }
            Path rootDir = SbiUtil.buildBaseDirPath(installSetting.getBaseDir());
            Path dataDir = rootDir.resolve("data");
            configSetting.setDataDir(dataDir.toUri().getPath().substring(1));
            configSetting.setPluginDir(rootDir.resolve("lib/plugin").toUri().getPath().substring(1));
            configSetting.setBinlog("mysql-bin");
            configSetting.setUsername(mysqlInstallForm.getUsername());
            configSetting.setPassword(mysqlInstallForm.getPassword());
            configSetting.setPort(mysqlInstallForm.getPort());
            configSetting.setBaseDir(rootDir.toUri().getPath().substring(1));
//            configSetting.setHost(mysqlInstallForm.getHost());
            configSetting.setDatabaseName(mysqlInstallForm.getDatabaseName());
//            checkMariadb(configSetting);
            // 根据配置生成配置文件
            SbiUtil.buildConfFile("my.ini", dataDir, configSetting, classpathTemplateEngine);
            SbiUtil.changeMariadbPwd(configSetting);
            CONFIG_SETTING.put(configSetting.getYmlName(), configSetting);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /*public void checkMariadb(MariadbSetting mariadbSetting) {
        try {
            SbiUtil.getConnection(mariadbSetting);
        } catch (ClassNotFoundException | SQLException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }*/

    /**
     * mssql 配置
     *
     * @param configSetting
     * @param mssqlInstallForm
     */
    public void configMssqldb(MssqlSetting configSetting, MssqlInstallForm mssqlInstallForm) {
        checkMssqldb(configSetting, mssqlInstallForm);
        configSetting.setHost(mssqlInstallForm.getHost());
        configSetting.setPort(mssqlInstallForm.getPort());
        configSetting.setDatabaseName(mssqlInstallForm.getDatabaseName());
        configSetting.setUsername(mssqlInstallForm.getUsername());
        configSetting.setPassword(mssqlInstallForm.getPassword());
        CONFIG_SETTING.put(configSetting.getYmlName(), configSetting);
    }

    public void checkMssqldb(MssqlSetting configSetting, MssqlInstallForm mssqlInstallForm) {
        // 检查数据库是否存在以及是否可以建立连接
        Connection connection = null;
        try {
            // 构建连接字符串
            String connectionUrl = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true;", mssqlInstallForm.getHost(), mssqlInstallForm.getPort(), mssqlInstallForm.getDatabaseName());
            // 加载数据库驱动
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // 尝试建立连接
            try {
                connection = DriverManager.getConnection(connectionUrl, mssqlInstallForm.getUsername(), mssqlInstallForm.getPassword());
                if (connection != null && !connection.isClosed()) {
                    log.info("成功连接到数据库：" + mssqlInstallForm.getDatabaseName());
                } else {
                    log.info("无法建立数据库连接");
                    throw new RuntimeException("无法建立数据库连接");
                }
            } catch (SQLException e) {
                log.info("数据库连接失败：" + e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            log.info("找不到数据库驱动：" + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.info("关闭数据库连接失败：" + e.getMessage());
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }


    /**
     * mongo配置
     *
     * @param configSetting
     * @param mongoInstallForm
     */
    public void configMongo(MongoSetting configSetting, MongoInstallForm mongoInstallForm) {
        InstallSetting installSetting = configSetting.getInstall();
        // todo 需要开启mongo的认证 使用admin 账号赋予其他账号权限
        try {
            Path rootDir = SbiUtil.buildBaseDirPath(installSetting.getBaseDir());
            configSetting.setDataDir(rootDir.toUri().getPath().substring(1));
            configSetting.setBaseDir(rootDir.toUri().getPath().substring(1));
            configSetting.setLogDir(rootDir.resolve("mongod.log").toUri().getPath().substring(1));
            configSetting.setHost(mongoInstallForm.getHost());
            configSetting.setPort(mongoInstallForm.getPort());
            configSetting.setUsername(mongoInstallForm.getUsername());
            configSetting.setPassword(mongoInstallForm.getPassword());
            configSetting.setDatabaseName(mongoInstallForm.getDatabaseName());
            MongoUtil.updateUserPassword(configSetting);

            // 根据配置生成配置文件
            SbiUtil.buildConfFile("mongod.conf", rootDir, configSetting, classpathTemplateEngine);
            CONFIG_SETTING.put(configSetting.getYmlName(), configSetting);
//            CmdUtil.startService("mongoDB");
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * nginx 配置
     *
     * @param configSetting
     * @param nginxInstallForm
     */
    public void configNginx(NginxSetting configSetting, NginxInstallForm nginxInstallForm) {

    }


    /**
     * app 配置，检查目录下载app 配置文件，这里app.jar提前放入安装目录
     *
     * @param configSetting
     * @param appInstallForm
     */
    public void configApp(AppSetting configSetting, AppInstallForm appInstallForm) {
        // 检查端口，有冲突让用户调整更换其他端口
        if (!NetUtil.isUsableLocalPort(Integer.parseInt(appInstallForm.getPort()))) {
            throw new RuntimeException("端口已被占用，请更换其他端口");
        }
        configSetting.setAppId(appInstallForm.getAppId());
        configSetting.setPort(appInstallForm.getPort());
        configSetting.setHost(appInstallForm.getHost());
        CONFIG_SETTING.put(configSetting.getYmlName(), configSetting);
        // 其他依赖, log,datasource....等，依赖于其他数据源
        String[] otherYmlConfigs = configSetting.getIncludeYmlName().split(",");
        for (String otherYmlConfig : otherYmlConfigs) {
            SbiUtil.buildIncludeYmlConfig(otherYmlConfig, CONFIG_SETTING);
        }
        try {
            // 确定安装目录
            InstallSetting installSetting = configSetting.getInstall();
            Path rootDir = SbiUtil.buildBaseDirPath(installSetting.getBaseDir());

            SbiUtil.replaceVariable(rootDir.getParent().resolve("api.xml"), "$PORT", configSetting.getPort());
            Path configsFolder = rootDir.resolve("configs");
            if (!Files.exists(configsFolder)) {
                Files.createDirectories(configsFolder);
            }
            List<String> dcfs = dependProvider.getDcfs();
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath*:configs/*");
            for (Resource resource : resources) {
                // 获取资源的文件名
                String cfn = resource.getFilename();
                if (cfn != null) {
                    // 目标文件路径
                    Path targetPath = configsFolder.resolve(cfn);
                    try (InputStream inputStream = resource.getInputStream();
                         FileOutputStream fileOutputStream = new FileOutputStream(targetPath.toFile())) {
                        // 将资源复制到目标文件
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }
                        fileOutputStream.close();
                        // 如果文件名包含在dcfs集合中，则动态替换配置文件
                        if (dcfs.contains(cfn)) {
                            SbiUtil.buildConfFile(cfn, configsFolder, CONFIG_SETTING.get(cfn), classpathTemplateEngine);
                        }
                    } catch (IOException e) {
                        // 处理异常
                        log.error("复制文件失败", e);
                        throw new RuntimeException(e);
                    }
                }
            }
            // 检或查目录下载web 和 app 配置文件，这里aap.jar提前放入安装目录
            if (!Files.exists(rootDir.resolve("app.jar"))) {
                // todo 此处可动态在线下载安装或更新
                //Files.copy(Paths.get("app.jar"), rootDir.resolve("app.jar"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 检查目录下载web，这里dist.zip提前放入安装目录
     *
     * @param configSetting
     * @param appInstallForm
     */
    public void configWeb(WebSetting configSetting, AppInstallForm appInstallForm) {
        try {
            InstallSetting installSetting = configSetting.getInstall();
            // 确定安装目录
            Path rootDir = SbiUtil.buildBaseDirPath(installSetting.getBaseDir());
            /*
             * feat:安排的配置文件目录,检或查目录下载web 和 proxy 配置文件，这里dist.zip提前放入安装目录，可检查版本
             * if (!Files.exists(rootDir.resolve(Path.of("html", "dist.zip")))) {
             *   // 此处可动态在线下载安装或更新
             *   //Files.copy(Paths.get("dist.zip"), rootDir.resolve("dist.zip"));
             * }
             */
            // 获取app配置中的host,port
            AppSetting appSetting = (AppSetting) CONFIG_SETTING.get(configSetting.getYmlName());
            configSetting.setAppId(appSetting.getAppId());
            configSetting.setHost(appSetting.getHost());
            configSetting.setPort(appSetting.getPort());
            configSetting.setName(appInstallForm.getAppId());
            // 客户端安装，服务端调整
            Path distPath = rootDir.resolve("html").resolve("dist");
            Path configPath = rootDir.resolve("conf").resolve("web");
            String staticsPath = distPath.resolve("statics").toUri().getPath().substring(1);
            String assetsPath = distPath.resolve("assets").toUri().getPath().substring(1);
            configSetting.setDist(distPath.toUri().getPath().substring(1));
            configSetting.setAssets(assetsPath);
            configSetting.setStatics(staticsPath);
            // 基于web.conf.ftl 生成proxy配置
            SbiUtil.buildConfFile("web.conf", configPath, configSetting, classpathTemplateEngine);
            SbiUtil.buildConfFile("wcf.js", distPath, configSetting, classpathTemplateEngine);

            // 下载到或生成到web
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 初始化
     */
    public void init(String sDmlAndDdl) {
        log.info("开始初始化...");
        // 读取mysql文件，然后执行
        MariadbSetting mariadbSetting = (MariadbSetting) CONFIG_SETTING.get("application-mysql.yml");
        AppSetting appSetting = (AppSetting) CONFIG_SETTING.get("application.yml");
        try {
//            String decSql = SbiUtil.decryptData(appSetting.getAppId());
//            String[] sDmlAndDdl = decSql.split("\n-- depends --\n");
            String[] ddlDml = sDmlAndDdl.split("\n-- dml --\n");
            // 方式插入语句中存在sql注释，影响导入
            String dmlSQL = ddlDml[1].replaceAll("--.*$", "");
            String[] ddlStatements = ddlDml[0].split(";");
            String[] dmlStatements = dmlSQL.split(";");
            // 开始推送进度
            progressHandler.start(ddlStatements.length + dmlStatements.length, 80, "/topic/progress",50);
            Connection connection = SbiUtil.getConnection(mariadbSetting);
            /*
             * Statement statement = connection.createStatement();
             * for (String ddlStatement : ddlStatements) {
             *      statement.execute(ddlStatement);
             *      push();
             * }
             */
            if (ddlStatements.length > 0) {
                String finalSql = ddlDml[0].trim();
                connection.createStatement().execute(finalSql);
            }
            if (dmlStatements.length > 0) {
                String finalSql = dmlSQL.trim();
                connection.createStatement().execute(SbiUtil.mergeSemicolons(finalSql));
            }
            connection.close();
            progressHandler.finish();
            log.info("初始化完成");
        } catch (Exception e) {
            log.error(e.getMessage());
            progressHandler.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void appProgress(Path apiLogPath, String port) {
        try {
            // 开始推送进度
            progressHandler.start(500, 1, "/topic/app",100);
            checkAppFinish(apiLogPath, port);
            log.info("应用启动完成");
        } catch (Exception e) {
            progressHandler.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    /*public static void watchFile(Path logFilePath)  {
        log.info("开始监控文件: {}", logFilePath);
        try{
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path parentDir = logFilePath.getParent();

            // 注册监控事件
            parentDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                Thread.sleep(500);
                WatchKey key = null; // 等待直到一个事件发生
                key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    // 检查是否是我们关心的文件变化事件
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY &&
                            ((WatchEvent<Path>) event).context().equals(logFilePath.getFileName())) {
                        log.info( "Detected modification in log file.");
                        // 在这里处理文件变化，例如读取新追加的内容
                        readNewContent(logFilePath);
                    }
                }
                key.reset(); // 重置key，以便再次使用
            }
        }catch (Exception e) {
            log.error("Error watching file: {}", logFilePath, e);
        }

    }*/

    private boolean readNewContent(Path logFilePath, String keyword, String errorKeyword,Map<String,Long> positionMap) {
        RandomAccessFile file = null;
        try {
            Long lastReadPosition = positionMap.get("filePosition");
            log.info("开始读取文件: {}", logFilePath);
            file = new RandomAccessFile(logFilePath.toFile(), "r");
            // 从上次读取的位置开始
            file.seek(lastReadPosition);
            boolean finish = false;
            boolean success = false;
            boolean error = false;
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = file.readLine()) != null) {
                log.info("========" + line);
                sb.append(line).append("\n");
                if (line.contains(keyword)) {
                    log.info("Found keyword in log file: {}", line);
                    finish = true;
                    success = true;
                }
                if (line.contains(errorKeyword)) {
                    log.info("Found error keyword in log file: {}", line);
                    finish = true;
                    error = true;
                }
            }
            if (sb.length() > 0) {
//                Dict dict = Dict.of("log", sb.toString());
//                progressHandler.sendMessage("/topic/log", dict);
                sendMessagesInChunks(sb.toString());
                // 清空StringBuilder
                sb.setLength(0);
            }
            // 更新上次读取的位置
            lastReadPosition = file.getFilePointer();
            positionMap.put("filePosition", lastReadPosition);
            if(success){
                progressHandler.finish();
            }
            if(error){
                progressHandler.error("启动失败，请查看日志");
            }
            return finish;
        } catch (Exception e) {
            log.error("Error ", e);
        } finally {
            try {
                if (file != null) {
                    file.close();
                }
            } catch (IOException e) {
                log.error("Error closing file: {}", logFilePath, e);
            }
        }
        return false;
    }


    private void sendMessagesInChunks(String message) throws InterruptedException {
        // 按行分割消息
        String[] lines = message.split("\n", -1);
        int totalLines = lines.length;
        int chunkLines = totalLines / 100;
        int remainder = totalLines % 100;

        int start = 0;
        for (int i = 0; i < 100; i++) {
            Thread.sleep(100);
            // 计算结束索引，考虑剩余行数
            int end = start + chunkLines + (i < remainder ? 1 : 0);
            // 确保不超过总行数
            end = Math.min(end, totalLines);
            StringBuilder chunkBuilder = new StringBuilder();
            for (int j = start; j < end; j++) {
                // 追加行并添加换行符
                chunkBuilder.append(lines[j]).append("\n");
            }
            String chunk = chunkBuilder.toString();
            Dict dict = Dict.of("log", chunk);
            progressHandler.sendMessage("/topic/log", dict);
            // 更新起始位置
            start = end;
        }
    }

    public void checkAppFinish(Path logFilePath, String port) {
        String targetKeyword = String.format("started on port %s", port);
        String shutdownKeyword = "This Context has been already destroyed";
        Map<String, Long> positionMap = new HashMap<>();
        positionMap.put("filePosition", 0L);

        log.info("开始监控文件: {}" , logFilePath);
        try {
            long startTime = System.currentTimeMillis();
            while (true) {
                TimeUnit.SECONDS.sleep(1);
                long currentTime = System.currentTimeMillis();
                // 5分钟无修改则退出
                if (currentTime - startTime > 5 * 60 * 1000) {
                    System.out.println("No modifications detected in the last 5 minutes. Exiting.");
                    return;
                }
                if (readNewContent(logFilePath, targetKeyword, shutdownKeyword, positionMap)) {
                    return;
                }
            }
        } catch (Exception e) {
            System.err.println("Error watching file: " + logFilePath + ", " + e.getMessage());
        }
    }



    public AppSetting getAppInfo() {
        return (AppSetting) CONFIG_SETTING.get("application.yml");
    }

    public void setAppInfo(String appId) {
        AppSetting appSetting = new AppSetting();
        appSetting.setAppId(appId);
        CONFIG_SETTING.put(appSetting.getYmlName(), appSetting);
    }
}

