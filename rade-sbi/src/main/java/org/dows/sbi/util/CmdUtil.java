package org.dows.sbi.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.dows.sbi.form.MySQLInstallForm;
import org.dows.sbi.pojo.MariadbSetting;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/27/2024 4:05 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Slf4j
public class CmdUtil {

    public static void restartNg(String sbin,String nginxPath) {
        try {
            //Runtime.getRuntime().exec(String.format("%s elevate taskkill /f /im nginx.exe", sbin));
            /*List<String> commandList = new ArrayList<>();
            //stopCommand.add(String.format("cd %s", nginxPath));
            commandList.add(sbin);
            commandList.add("elevate");
            commandList.add(nginxPath);
            ProcessBuilder pb = new ProcessBuilder(commandList);
            pb.start();*/
            String format = String.format("%s elevate %s", sbin, nginxPath);
            log.info("重启Nginx命令:{}", format);
            Runtime.getRuntime().exec(format);
            log.info("重启Nginx命令执行成功");
            /*ProcessBuilder processBuilder = new ProcessBuilder(nginxPath);
            processBuilder.directory(new File(nginxPath).getParentFile());
            processBuilder.start();*/
            /*int stopExitCode = stopProcess.waitFor();
            if (stopExitCode == 0) {
                // 等待一段时间，确保Nginx完全停止，这里等待2秒（可以根据实际情况调整）
                Thread.sleep(2000);
                // 再启动Nginx
                List<String> startCommand = new ArrayList<>();
                startCommand.add("nginx");
                ProcessBuilder startPb = new ProcessBuilder("cmd.exe", "/c", startCommand.get(0));
                Process startProcess = startPb.start();
                int startExitCode = startProcess.waitFor();
                if (startExitCode == 0) {
                    log.info("Nginx重启成功");
                } else {
                    log.error("Nginx启动失败");
                }
            } else {
                log.error("Nginx停止失败");
            }*/
        } catch (IOException e) {
            log.error("重启Nginx时发生异常", e);
        }
    }


    public static void netStart(String sbin, String serviceName) throws IOException {
        //URL resource = CmdUtil.class.getClassLoader().getResource("cmd.exe");
        //assert resource != null;
        //String path = resource.getPath();
        //CmdUtil.restartNg();
//        Runtime.getRuntime().exec(String.format("%s elevate taskkill /f /im nginx.exe", sbin));
//        String nginxPath = SbiUtil.buildBaseDirPath("proxy").resolve("nginx.exe").toUri().getPath().substring(1);
//        restartNg(sbin,nginxPath);
        //Runtime.getRuntime().exec(String.format("%s elevate net start %s", sbin, "SbiProxy"));
        Runtime.getRuntime().exec(String.format("%s elevate net start %s", sbin, serviceName));
    }


    public static void runCommand(String cmd) {
        String command = String.format("powershell.exe Start-Process %s -verb RunAs", cmd);
        runCmd(command);
    }

    public static void stopAndDeleteService(String serviceName) {
        if (StrUtil.isBlank(serviceName)) {
            throw new RuntimeException("服务名称不能为空");
        }
        String stopCommand = "powershell.exe -Command \"Start-Process cmd.exe -ArgumentList '/c net stop %s & sc delete %s' -Verb RunAs\"";
        String cmd = stopCommand.replaceAll("%s", serviceName);
        runCmd(cmd);
    }


    public static void startService(String serviceName) {
        String stopCommand = "powershell.exe -Command \"Start-Process cmd.exe -ArgumentList '/c', 'net start %s' -Verb RunAs\"";
        String cmd = stopCommand.replaceAll("%s", serviceName);
        runCmd(cmd);
    }


    public static void runCmd(String command) {
        try {
            log.info("执行命令:{}", command);
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
            processBuilder.redirectErrorStream(true);
            //Process process = processBuilder.inheritIO().start();
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info("执行响应:{}", line);
            }
            int i = process.waitFor();
            log.info("执行结果:{}", i == 0 ? "success" : "error");
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    private void configMysqlPassword(Path rootDir, MySQLInstallForm mysqlInstallForm) {
        String mysqlBin = rootDir.resolve("bin").toUri().getPath().substring(1);
        File file = new File(mysqlBin);
        // 要执行的命令列表
        List<String> commandList = new ArrayList<>();
        commandList.add(mysqlBin);
        commandList.add("mysql");
        commandList.add("-uroot");
        //commandList.add("");
        commandList.add("-p");
        //commandList.add("");
        commandList.add("-e");
        String dcl = String.format("use mysql; UPDATE user SET authentication_string=password('%s') where user='root'; FLUSH PRIVILEGES; exit;", mysqlInstallForm.getPassword());
        commandList.add(dcl);
        runCmd(file, commandList);
        //runCmd();
    }


    private void runCmd(File file, List<String> commandList) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commandList);
            processBuilder.directory(file);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Password changed successfully.");
            } else {
                System.out.println("Error changing password.");
            }
            /*// 获取命令执行的输出流和错误流，以便查看执行结果和可能出现的错误信息
            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            InputStream errorStream = process.getErrorStream();
            InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
            BufferedReader errorBufferedReader = new BufferedReader(errorStreamReader);

            // 读取并输出命令执行的正常输出结果
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.info("cmd success result: {}", line);
            }
            // 读取并输出命令执行的错误输出结果
            while ((line = errorBufferedReader.readLine()) != null) {
                log.info("cmd error result: {}", line);
            }*/
            // 等待命令执行完成
            //process.wait();

        } catch (IOException | InterruptedException e) {
            log.error("exception: {}", e.getMessage());
        }
    }


    public void installService(Path root, MySQLInstallForm mySQLInstallForm) {
        try {
            ClassPathResource classPathResource = new ClassPathResource("depends/WinSW-x64.exe");
            Path binPath = Path.of(root.resolve("bin").toUri());
            Path exeFile = binPath.resolve("mysql_install_db-service.exe");
            Files.copy(classPathResource.getInputStream(), exeFile, StandardCopyOption.REPLACE_EXISTING);

            Map<String, Object> mariadb = new HashMap<>();
            mySQLInstallForm.setBaseDir(root.toUri().getPath().substring(1));
            mySQLInstallForm.setLogDir(root.toUri().getPath().substring(1));
            mySQLInstallForm.setDataDir(root.resolve("data").toUri().getPath().substring(1));
            mariadb.put("mariadb", mySQLInstallForm);
            //String render = classpathTemplateEngine.getTemplate("mysql_install_db-service.xml.ftl").render(mariadb);

            Path serviceConfigXml = binPath.resolve("mysql_install_db-service.xml");

            if (Files.exists(serviceConfigXml)) {
                Files.copy(serviceConfigXml, binPath.resolve(String.format("mysql_install_db-service.xml_%s", System.currentTimeMillis())));
                Files.delete(serviceConfigXml);
            }
            Files.createFile(serviceConfigXml);
            //Files.write(serviceConfigXml, render.getBytes());
            exeService(exeFile.toUri().getPath().substring(1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void exeService(String exeFilePath) {
        try {
            // 获取Runtime实例
            Runtime runtime = Runtime.getRuntime();
            // 执行.exe文件 "cmd.exe", "/c", batFilePath
            //String cmd = String.format("runas /user:Administrator cmd /c %s", command);
            String cmd = String.format("cmd /c %s install", exeFilePath);
            Process process = runtime.exec(cmd);

            // 获取命令执行的输出流和错误流
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();
            // 读取输出流并打印输出内容
            BufferedReader outputReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            String line;
            while ((line = outputReader.readLine()) != null) {
                log.info("输出: {}", line);
            }
            // 读取错误流并打印错误内容（如果有）
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream, "GBK"));
            while ((line = errorReader.readLine()) != null) {
                log.error("错误: {}", line);
            }
            // 等待命令执行完成，并获取退出值
            int exitValue = process.waitFor();
            log.info("命令执行完毕，退出值: {}", exitValue);
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
        }
    }


    public void startService() {
        String command = "net start MySql";
        try {
            // 创建ProcessBuilder对象，并设置命令和相关参数
            //"echo", "1227", "|", "runas", "/user:Administrator"
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", command);
            pb.redirectErrorStream(true);
            // 启动进程
            Process process = pb.start();
            // 可以读取命令执行的输出信息（如果需要）
            // 例如，以下代码可以读取并打印输出信息到控制台
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }
            reader.close();
            // 等待命令执行完成，并获取退出值
            int exitValue = process.waitFor();
            log.info("命令执行完毕，退出值: {}", exitValue);
        } catch (IOException | InterruptedException e) {
            log.error("命令执行完毕，退出值: {}", e.getMessage());
        }
    }


    public static int countFiles(File folder) {
        int count = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    count++;
                } else if (file.isDirectory()) {
                    count += countFiles(file); // 递归统计子文件夹中的文件
                }
            }
        }
        return count;
    }

    /*public static void main(String[] args) {
        String appPath = "C:\\path\\to\\your\\application.exe";
        String runasCommand = String.format("runas /user:Administrator \"%s\"", appPath);

        Advapi32 advapi32 = Native.load("advapi32", Advapi32.class, W32APIOptions.DEFAULT_OPTIONS);
        WinDef.HWND hwnd = null; // 窗口句柄，可以为null
        int showCommand = 1; // 1表示SW_NORMAL，即正常显示窗口



        Winsvc.SC_HANDLE scHandle = advapi32.OpenSCManager();
        boolean success = (boolean) scHandle;
        //ShellExecute(hwnd, "runas", "cmd.exe", "/C " + runasCommand, null, showCommand) != null;

        if (success) {
            System.out.println("已以管理员身份运行应用程序。");
        } else {
            System.out.println("无法以管理员身份运行应用程序。");
        }
    }*/
    /*public static void main(String[] args) {
        String appPath = "C:\\path\\to\\your\\application.exe";
        String runasCommand = String.format("runas /user:Administrator \"%s\"", appPath);

        Advapi32 advapi32 = Native.load("advapi32", Advapi32.class, W32APIOptions.DEFAULT_OPTIONS);
        WinDef.HWND hwnd = null; // 窗口句柄，可以为null
        int showCommand = 1; // 1表示SW_NORMAL，即正常显示窗口

        boolean success = advapi32.ShellExecute(hwnd, "runas", "cmd.exe", "/C " + runasCommand, null, showCommand) != null;

        if (success) {
            System.out.println("已以管理员身份运行应用程序。");
        } else {
            System.out.println("无法以管理员身份运行应用程序。");
        }
    }*/


    public void installService1(Path root, MariadbSetting mariadbSetting) {
        //String workDir, String dataDir,
        String drive = root.toUri().getPath().substring(0, 2);
        //String command0 = String.format("cd /%s %s", drive, workDir);
        //String command = String.format("%s/mysql_install_db.exe --datadir=%s --service=MySql --password=%s", root.toUri().getPath(), password);
        String command = String.format("%sbin/mysql_install_db.exe --service=MySql --password=%s", root.toUri().getPath().substring(1), "pwd");
        //String command = String.join(" && ", command0, command1);
        try {
            String bat = "E:\\workspaces\\project\\shdy\\shdy-sbi\\sbi-config\\src\\main\\resources\\bat\\start.bat";
            // 创建ProcessBuilder对象，指定要执行的.bat文件的命令
            //ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "echo", "1227", "|", "runas", "/user:Administrator", bat);

            String cmd = String.format("runas /user:Administrator cmd /c %s", command);
            // 创建运行时对象
            Runtime runtime = Runtime.getRuntime();
            // 创建命令
            //String command = "cmd /c your-command";
            // 执行命令
            Process process = runtime.exec(cmd);
            // 等待命令执行完成
            process.waitFor();


//            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c" /* "/c" ,"echo", "1227", "|", "runas", "/user:Administrator"*/, cd + "&&" + command);
//            pb.redirectErrorStream(true);
            // 启动进程
            //Process process = pb.start();


            // 可以读取命令执行的输出信息（如果需要）
            // 例如，以下代码可以读取并打印输出信息到控制台
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            reader.close();
            // 等待命令执行完成，并获取退出值
            int exitValue = process.waitFor();
            System.out.println("命令执行完毕，退出值: " + exitValue);
            log.info("命令执行完毕，退出值: {}", exitValue);
        } catch (IOException | InterruptedException e) {
            log.error("命令执行完毕，退出值: {}", e.getMessage());
        }
    }

    private static Optional<String> getStoredPassword() {
        try {
            // 使用ProcessBuilder执行cmdkey命令获取存储的密码
            ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "cmdkey", "/list", "|", "findstr", "/C:AdminCommandAccess", "|", "for", "/F", "tokens=3", "delims= ", "%%a", "do", "echo", "%%a");
            pb.redirectErrorStream(true);
            Process process = pb.start();
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String password = reader.readLine();
            reader.close();
            if (password != null && !password.isEmpty()) {
                return Optional.of(password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}

