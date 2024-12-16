package org.dows.sbi.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.sbi.form.*;
import org.dows.sbi.handler.ConfigHandler;
import org.dows.sbi.handler.DependProvider;
import org.dows.sbi.handler.RegisterService;
import org.dows.sbi.pojo.*;
import org.dows.sbi.util.CmdUtil;
import org.dows.sbi.util.SbiUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/21/2024 1:56 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@RequiredArgsConstructor
@Slf4j
@Controller
public class SetupController {
    private final ConfigHandler configHandler;
    private final DependProvider dependProvider;
    private final ThreadPoolExecutor threadPoolExecutor;

    private static final String MYSQL_STEP = "mysql";
    private static final String MSSQL_STEP = "mssql";
    private static final String MONGO_STEP = "mongo";
    private static final String NGINX_STEP = "nginx";
    private static final String APP_STEP = "app";
    private static final String INIT_STEP = "init";
    private static final String STARTUP_STEP = "startup";
    private static final String ip;
    private static final Map<String, Class<? extends RegisterService>> FORM_MAP = new HashMap<>();
    private static final Map<String, List<String>> DEPENDS_MAP = new ConcurrentHashMap<>();
    private static final Map<String, AppInfo> APP_INFO_MAP = new ConcurrentHashMap<>();

    @Value("${spring.application.name}")
    private String appName;
    @Value("${server.port}")
    private String port;

    @PostConstruct
    public void init() {
        FORM_MAP.put(MYSQL_STEP, MySQLInstallForm.class);
        FORM_MAP.put(MSSQL_STEP, MssqlInstallForm.class);
        FORM_MAP.put(MONGO_STEP, MongoInstallForm.class);
        FORM_MAP.put(NGINX_STEP, NginxInstallForm.class);
        FORM_MAP.put(APP_STEP, AppInstallForm.class);
        FORM_MAP.put(INIT_STEP, AppInstallForm.class);
    }

    static {
        ip = getIp();
    }

    public static String getIp() {
        String ip = "127.0.0.1";
        /*try {
            InetAddress localHost = InetAddress.getLocalHost();
            log.info("本地主机IP地址: {}", localHost.getHostAddress());
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            log.error("无法获取本地主机IP地址: {}", e.getMessage());
        }*/
        return ip;
    }

    @GetMapping("/startNg")
    public @ResponseBody void testNg(){
        try {
            String nginxPath = SbiUtil.buildBaseDirPath("proxy").resolve("nginx.exe").toUri().getPath().substring(1);
            String sbinPath = SbiUtil.buildBaseDirPath("bin").resolve("sbin.exe").toUri().getPath().substring(1);
            CmdUtil.restartNg(sbinPath,nginxPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/preview")
    public String index(Model model) {
        model.addAttribute("message", String.format("欢迎使用%s引导安装程序!", appName));
        return "preview";
    }

    @GetMapping("/install")
    public String showInstallPage(Model model, String appId, String preStep) {

        if (StrUtil.isNotBlank(appId)) {
            configHandler.setAppInfo(appId);
        } else {
            appId = configHandler.getAppInfo().getAppId();
        }
        List<String> depends = DEPENDS_MAP.get(appId);
        if (depends == null) {
            String content = "";
            try {
                content = SbiUtil.decryptData(appId);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] dependStr = content.split("\n-- depends --\n");
            String sDepends = dependStr[1];
            String[] da = sDepends.split("\n-- appId --\n");
            String[] app = da[1].split(",");
            if (!appId.equalsIgnoreCase(app[0])) {
                throw new RuntimeException("请输入正确的应用ID!");
            }
            AppInfo appInfo = new AppInfo();
            appInfo.setAppId(appId);
            appInfo.setAppCode(app[1]);
            appInfo.setSDmlAndDdl(dependStr[0]);
            APP_INFO_MAP.put(appId, appInfo);
            depends = new ArrayList<>(Arrays.asList(da[0].split(",")));
            depends.add("app");
            depends.add("init");
            DEPENDS_MAP.put(appId, depends);
            depends = new ArrayList<>(Arrays.asList(da[0].split(",")));
        }
        if (preStep == null && CollectionUtil.isNotEmpty(depends)) {
            preStep = depends.get(0);
        }
        if (MYSQL_STEP.equals(preStep) && depends.contains(preStep)) {
            preStep = MYSQL_STEP;
            MySQLInstallForm mySQLInstallForm = new MySQLInstallForm();
            mySQLInstallForm.setAppId(appId);
            mySQLInstallForm.setHost(ip);
            mySQLInstallForm.setUsername("root");
            model.addAttribute("mysqlInstallForm", mySQLInstallForm);
        }
        if (MSSQL_STEP.equals(preStep) && depends.contains(preStep)) {
            preStep = MSSQL_STEP;
            MssqlInstallForm msSQLInstallForm = new MssqlInstallForm();
            msSQLInstallForm.setHost(ip);
            msSQLInstallForm.setAppId(appId);
            msSQLInstallForm.setUsername("root");
            model.addAttribute("mssqlInstallForm", msSQLInstallForm);
        }
        if (MONGO_STEP.equals(preStep) && depends.contains(preStep)) {
            preStep = MONGO_STEP;
            MongoInstallForm mongoInstallForm = new MongoInstallForm();
            mongoInstallForm.setAppId(appId);
            mongoInstallForm.setHost(ip);
            model.addAttribute("mongoInstallForm", mongoInstallForm);
        }
        if (APP_STEP.equals(preStep) && depends.contains(preStep)) {
            preStep = APP_STEP;
            AppInstallForm appInstallForm = new AppInstallForm();
            appInstallForm.setAppId(appId);
            appInstallForm.setHost(ip);
            model.addAttribute("appInstallForm", appInstallForm);
        }
        if (INIT_STEP.equals(preStep)) {
            preStep = INIT_STEP;
        }
        model.addAttribute("step", preStep);
        return "install";
    }


    @PostMapping("/install/mysql")
    public String processMySQLInstall(@ModelAttribute("mysqlInstallForm") MySQLInstallForm mysqlInstallForm, Model model) {
        MariadbSetting configSetting = dependProvider.getConfigSetting(MariadbSetting.class);
        configHandler.configMariadb(configSetting, mysqlInstallForm);
        setNextSetup(mysqlInstallForm, model);
        return "install";
    }


    @PostMapping("/install/mssql")
    public String processMssqlInstall(@ModelAttribute("mssqlInstallForm") MssqlInstallForm mssqlInstallForm, Model model) {
        MssqlSetting configSetting = new MssqlSetting();
        configHandler.configMssqldb(configSetting, mssqlInstallForm);
        setNextSetup(mssqlInstallForm, model);
        return "install";
    }


    @PostMapping("/install/mongo")
    public String processMongoInstall(@ModelAttribute("mongoInstallForm") MongoInstallForm mongoInstallForm, Model model) {
        MongoSetting configSetting = dependProvider.getConfigSetting(MongoSetting.class);
        configHandler.configMongo(configSetting, mongoInstallForm);
        setNextSetup(mongoInstallForm, model);
        return "install";
    }

    @PostMapping("/install/app")
    public String processAppInstall(@ModelAttribute("AppInstallForm") AppInstallForm appInstallForm, Model model) {
        AppSetting configSetting = dependProvider.getConfigSetting(AppSetting.class);
        WebSetting webSetting = dependProvider.getConfigSetting(WebSetting.class);
        AppInfo appInfo = APP_INFO_MAP.get(appInstallForm.getAppId());
        configSetting.setName(appInfo.getAppCode());
        configHandler.configApp(configSetting, appInstallForm);
        // 配置web
        configHandler.configWeb(webSetting, appInstallForm);
        setNextSetup(appInstallForm, model);
        threadPoolExecutor.execute(() -> {
            configHandler.init(appInfo.getSDmlAndDdl());
        });
        Path sbinPath = null;
        try {
            sbinPath = SbiUtil.buildBaseDirPath("bin").resolve("sbin.exe");
            String sbin = sbinPath.toUri().getPath().substring(1);
            Runtime.getRuntime().exec(String.format("%s elevate taskkill /f /im nginx.exe", sbin));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "install";
    }


    /**
     * 初始化数据
     *
     * @param model
     * @return
     */
    @PostMapping("/install/init")
    public String init(Model model) {
        model.addAttribute("step", STARTUP_STEP);
        DataImportForm dataImportForm = new DataImportForm();
        dataImportForm.setProgress("0");
        model.addAttribute("dataImportForm", dataImportForm);
        StartupForm startupForm = new StartupForm();
        AppSetting appInfo = configHandler.getAppInfo();
        AppInfo appInfo1 = APP_INFO_MAP.get(appInfo.getAppId());
        // 第一次启动执行
//        if(!appInfo1.isStarted()){
//
//        }
        try {
            Path apiLogPath = SbiUtil.buildBaseDirPath("logs").resolve("api.out.log");
            // 清空日志文件内容
            Files.write(apiLogPath, "".getBytes());
            Path sbinPath = SbiUtil.buildBaseDirPath("bin").resolve("sbin.exe");
            String sbin = sbinPath.toUri().getPath().substring(1);
            CmdUtil.netStart(sbin, appInfo.getServiceName());
            CmdUtil.netStart(sbin, "SbiProxy");
            threadPoolExecutor.execute(() -> {
                configHandler.appProgress(apiLogPath,appInfo.getPort());
            });
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        String index = String.format("dev/web/%s", appInfo.getAppId());
        startupForm.setIndex(String.format("http://%s/%s", appInfo.getHost(), index));
        model.addAttribute("startup", startupForm);
        return "install";
    }


    @PostMapping("/install/startup")
    public void successStartup(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Model model) {
        // 在这里处理应用程序安装逻辑，例如启动应用程序等
        StartupForm startupForm = new StartupForm();
        String index = "index";
        String appIndex = String.format("http://localhost:%s/%s", port, index);
        //model.addAttribute("step", STARTUP_STEP);
        //model.addAttribute("startup", startupForm);
        //return "install";
        //Path startPath = Path.of(System.getProperty("user.dir")).resolve("start.bat");
        //String startBat = startPath.toUri().getPath().substring(1);
        //AppSetting appInfo = configHandler.getAppInfo();
        /*String contextPath = httpServletRequest.getContextPath();
        model.addAttribute("contextPath", contextPath);*/
        /*try {
            CmdUtil.netStart(appInfo.getServiceName());
            httpServletResponse.sendRedirect(appIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/
    }


    @MessageMapping("/progress")
    @SendTo("/topic/progress")
    public String handleProgress(String message) {
        return message;
    }

    @GetMapping("/preStep")
    public @ResponseBody String getPreStep(String appId, String step) {
        List<String> depends = DEPENDS_MAP.get(appId);
        if (depends != null) {
            int index = depends.indexOf(step);
            if (index >= 1) {
                return depends.get(index - 1);
            }
        }
        return step;
    }


    private static void setNextSetup(RegisterService registerService, Model model) {
        registerService.setHost(ip);
        List<String> depends = DEPENDS_MAP.get(registerService.getAppId());
        if (depends != null) {
            int index = depends.indexOf(registerService.getSetupName());
            if ((index != -1) && depends.size() > index + 1) {
                model.addAttribute("step", depends.get(index + 1));
                String installForm = depends.get(index + 1) + "InstallForm";
                Class<? extends RegisterService> nextRegister = FORM_MAP.get(depends.get(index + 1));
                if (nextRegister != null) {
                    try {
                        RegisterService registerService1 = nextRegister.getDeclaredConstructor().newInstance();
                        registerService1.setHost(ip);
                        registerService1.setAppId(registerService.getAppId());
                        registerService1.setPreSetupName(registerService.getSetupName());
                        model.addAttribute(installForm, registerService1);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    /*@PostMapping("/install/import")
    public String processImportData(@ModelAttribute("dataImportForm") DataImportForm dataImportForm) {
        // 在这里处理数据导入的逻辑
        return "install";
    }*/

    /*@PostMapping("/install/nginx")
    public String processNginxInstall(HttpServletRequest httpServletRequest, @ModelAttribute("nginxInstallForm") NginxInstallForm nginxInstallForm, Model model) {
        NginxSetting configSetting = dependProvider.getConfigSetting(NginxSetting.class);
        setupHandler.configNginx(configSetting,nginxInstallForm);
        collectService(httpServletRequest, nginxInstallForm);
        // 处理nginx 安装
        model.addAttribute("step", APP_STEP);
        model.addAttribute("appInstallForm", new AppInstallForm());
        return "install";
    }*/

    /*private static void collectService(HttpServletRequest httpServletRequest, RegisterService registerService) {
        HttpSession session = httpServletRequest.getSession();
        Set<RegisterService> registerServices = SESSION_MAP.get(session);
        if (registerServices == null) {
            registerServices = new HashSet<>();
        }
        registerServices.add(registerService);
        SESSION_MAP.put(session, registerServices);
    }*/

}