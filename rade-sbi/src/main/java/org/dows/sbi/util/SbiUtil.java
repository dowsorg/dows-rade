package org.dows.sbi.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.extra.template.TemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.dows.sbi.handler.ConfigSetting;
import org.dows.sbi.pojo.AppSetting;
import org.dows.sbi.pojo.MariadbSetting;
import org.dows.sbi.pojo.MongoSetting;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 12/4/2024 9:29 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Slf4j
public class SbiUtil {

    public static String decryptData(String appId) throws IOException {
        if (StrUtil.isBlank(appId)) {
            throw new RuntimeException("appId不能为空");
        }
        // 生成AES密钥（与加密时相同）
        AES aes = SecureUtil.aes(String.format("%s%s", appId, "00000").getBytes());
        Path sqlPath = Path.of(System.getProperty("user.dir"), "bin")
                .resolve(String.format("%s.data", appId));
        if (!Files.exists(sqlPath)) {
            sqlPath = Path.of(System.getProperty("user.dir"), "setup", "bin")
                    .resolve(String.format("%s.data", appId));
        }
        String content = IoUtil.readUtf8(FileChannel.open(sqlPath));
        // 解密文件数据并分割\n-- dml --\n
        return aes.decryptStr(HexUtil.decodeHex(content));
    }

    /**
     * @param baseDir
     * @return
     * @throws IOException
     */
    public static Path buildBaseDirPath(String baseDir) throws IOException {
        // String baseDir = installSetting.getBaseDir();
        if (StrUtil.isBlank(baseDir)) {
            throw new RuntimeException("安装目录不能为空");
        }
        Path root = Path.of(System.getProperty("user.dir"));
        //Path basePath = Path.of(baseDir);
        Path basePath = root.resolve(baseDir);
        if (!Files.exists(basePath)) {
            basePath = root.resolve("setup").resolve(baseDir);
        }
        //root = basePath;
        log.info("当前安装路径：{}", basePath.toUri().getPath().substring(1));
        if (!Files.exists(root)) {
            //FileUtil.del(root.toFile());
            throw new RuntimeException("安装目录不存在，请初始化...");
        }
        // 根据配置解压到文件
        //ZipUtil.unzipByClassPathResource(installSetting.getInstallFile(), dir);
        return basePath;
    }


    /**
     * 获取mysql连接
     *
     * @param mariadbSetting
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection getConnection(MariadbSetting mariadbSetting) throws ClassNotFoundException, SQLException {
        String url = String.format("jdbc:mariadb://%s:3306/mysql?%s", "localhost", "serverTimezone=GMT%2B8&autoReconnect=true&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false");
        String user = "root";
        String password = mariadbSetting.getPassword();
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection(url, user, password);
    }


    /**
     * config password
     * use mysql;
     * UPDATE user SET authentication_string=password('${mysql.password}') where user='root';
     * FLUSH PRIVILEGES;
     *
     * @param configSetting
     */
    public static void changeMariadbPwd(MariadbSetting configSetting) throws IOException {
        UsernamePassword usernamePassword = SbiUtil.getUsernamePassword(configSetting);
        configSetting.setPassword(usernamePassword.pwd());
        Connection connection = null;
        Statement statement = null;
        try {
            connection = SbiUtil.getConnection(configSetting);
             statement = connection.createStatement();

            String dcl = String.format("ALTER USER '%s'@'%s' IDENTIFIED BY '%s'", usernamePassword.user(), "%", usernamePassword.newPassword());
            String dclGrant = String.format("GRANT ALL PRIVILEGES ON *.* TO '%s'@'%s' WITH GRANT OPTION", usernamePassword.user(), "%");
            statement.execute(dcl);
            statement.execute(dclGrant);

            //SbiUtil.RP.put("root", newPassword);
            Files.writeString(usernamePassword.rpPath(), String.format("%s,%s", usernamePassword.user(), usernamePassword.newPassword()), StandardOpenOption.TRUNCATE_EXISTING);
            // 更新回来
            configSetting.setPassword(usernamePassword.newPassword());
            log.info("Password changed successfully");
        } catch (SQLException e) {
            log.error("Password changed failed: {}", e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (statement != null){
                    statement.close();
                }
                if (connection != null){
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static UsernamePassword getUsernamePassword(ConfigSetting configSetting) throws IOException {
        String newPassword = configSetting.getPassword();
        String baseDir = configSetting.getBaseDir();
        Path rpPath = Path.of(baseDir).resolve("rp.data");
        if (!Files.exists(rpPath)) {
            Files.createFile(rpPath);
        }
        String rpContent = Files.readString(rpPath, StandardCharsets.UTF_8);
        String user = configSetting.getUsername();
        String pwd = null;
        if (!StrUtil.isBlank(rpContent)) {
            String[] split = rpContent.split(",");
            user = split[0];
            pwd = split[1];
        }
        return new UsernamePassword(newPassword, rpPath, user, pwd);
    }

    public record UsernamePassword(String newPassword, Path rpPath, String user, String pwd) {
    }

    /**
     * @param fullFileName
     * @param rootDir
     * @param installForm
     * @return
     * @throws IOException
     */
    public static Path buildConfFile(String fullFileName, Path rootDir, Object installForm, TemplateEngine templateEngine) throws IOException {
        // 根据配置生成配置文件
        Map<String, Object> map = new HashMap<>();
        String configFile = StrUtil.subPre(fullFileName, fullFileName.lastIndexOf("."));
        char separator = '-';
        map.put(StrUtil.toCamelCase(configFile, separator), installForm);
        String render = templateEngine.getTemplate(String.format("%s.ftl", fullFileName)).render(map);
        Files.createDirectories(rootDir);
        Path confFile = rootDir.resolve(fullFileName);
        if (Files.exists(confFile)) {
            // backup
            Files.copy(confFile, rootDir.resolve(fullFileName + "_" + System.currentTimeMillis()));
            Files.delete(confFile);
        }
        Files.createFile(confFile);
        Files.write(confFile, render.getBytes(StandardCharsets.UTF_8));
        return confFile;
    }


    /**
     * 根据文件名构建对应的yml 应用所依赖的配置对象
     * "application-datasource.yml,application-depends.yml,application-log.yml,application-cdc.yml,application-mongo.yml,application-mysql.yml"
     *
     * @param includeYmlConfig
     * @return
     */
    public static void buildIncludeYmlConfig(String includeYmlConfig, Map<String, Object> CONFIG_SETTING) {
        AppSetting application = (AppSetting) CONFIG_SETTING.get("application.yml");
        MariadbSetting mysql = (MariadbSetting) CONFIG_SETTING.get("application-mysql.yml");
        if (null == mysql) {
            mysql = new MariadbSetting();
        }
        mysql.setDatabaseName(application.getAppId());
        Object mongo = CONFIG_SETTING.get("application-mongo.yml");
        if (null == mongo) {
            mongo = new MongoSetting();
        }
        Object mssql = CONFIG_SETTING.get("application-mssql.yml");
        if ("application-datasource.yml".equals(includeYmlConfig)) {
            Dict dict = Dict.of("mysql", mysql).set("mssql", mssql);
            CONFIG_SETTING.put(includeYmlConfig, dict);
        } else if ("application-log.yml".equals(includeYmlConfig)) {
            Dict dict = Dict.of("mysql", mysql).set("application", application);
            CONFIG_SETTING.put(includeYmlConfig, dict);
        } else if ("application-depends.yml".equals(includeYmlConfig)) {
            CONFIG_SETTING.put(includeYmlConfig, mysql);
        } else if ("application-mongo.yml".equalsIgnoreCase(includeYmlConfig)) {
            CONFIG_SETTING.put(includeYmlConfig, mongo);
        } else if ("application-cdc.yml".equalsIgnoreCase(includeYmlConfig)) {
            Dict dict = Dict.of("mysql", mysql).set("mssql", mssql);
            CONFIG_SETTING.put(includeYmlConfig, dict);
        }
    }


    /**
     * 将字符串中连续的分号合并为一个分号。
     *
     * @param input 需要处理的字符串
     * @return 处理后的字符串
     */
    public static String mergeSemicolons(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        // 用于记录上一个字符是否为分号
        boolean lastWasSemicolon = false;

        for (char c : input.toCharArray()) {
            if (c == ';') {
                if (!lastWasSemicolon) {
                    // 如果上一个字符不是分号，则添加一个分号到结果中
                    result.append(c);
                    lastWasSemicolon = true;
                }
            } else {
                // 如果当前字符不是分号，则添加到结果中，并重置lastWasSemicolon
                result.append(c);
                lastWasSemicolon = false;
            }
        }

        return result.toString();
    }

    public static void replaceVariable(Path filePath, String variable, String replacement) {
        //Path apiXml = rootDir.resolve("api.xml");
        try {
            // 读取文件内容
            String content = Files.readString(filePath, StandardCharsets.UTF_8);
            // 替换变量
            String replacedContent = content.replace(variable, replacement);
            // 写入替换后的内容
            Files.writeString(filePath, replacedContent, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            // 处理异常，例如重新抛出或记录日志
            throw new RuntimeException("Failed to modify file", e);
        }
    }
}

