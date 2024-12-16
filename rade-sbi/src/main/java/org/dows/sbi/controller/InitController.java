package org.dows.sbi.controller;

import cn.hutool.core.io.resource.ClassPathResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 12/11/2024 10:28 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@RequiredArgsConstructor
@Slf4j
@RestController
public class InitController {

    @GetMapping("/iws")
    public void showInstallPage(String serviceName) throws IOException, InterruptedException {

        ClassPathResource classPathResource = new ClassPathResource("sbin.exe");
        String cmd = classPathResource.getUrl().getPath().substring(1);

        //--install "%s" && %s\bin\mysql_install_db.exe

        //String commd = "cmd /c runas /user:Administrator cmd";
        String mysqld = String.format("\"%smysqld.exe\"", "E:/workspaces/project/shdy/shdy-sbi/setup/pkg/database/bin/");
        String ws = String.format("sc create %s binPath= %s", serviceName, mysqld);

        //command.add(String.format("%s elevate %s", cmd, ws));
        // 去掉/c参数，使CMD进程不立即关闭
        // command.add("/c");
        //String cmd1 = String.format("%s elevatecmd runassystem %s", cmd, ws);
        //commands.add("dir");
        List<String> commands = new ArrayList<>();
        commands.add("E:/workspaces/project/shdy/dows-sbi/src/main//resources/sbin.exe");
        commands.add("elevate");
        commands.add("sc");
        commands.add("create");
        commands.add(serviceName);
        commands.add("binPath=\"E:/workspaces/project/shdy/shdy-sbi/setup/pkg/database/bin/mysqld.exe\"");
        ProcessBuilder pb = new ProcessBuilder();
        pb.inheritIO();
        pb.command(commands);

        /*Process p = null;
        try {
            p = pb.start();

            // 使用单独的线程来读取输出流
            Process finalP = p;
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(finalP.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // 使用单独的线程来读取错误流
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(finalP.getErrorStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // 等待进程结束
            p.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy(); // 销毁进程
            }
        }*/


        Process p = pb.start();
        // 获取CMD进程的输入流和输出流
        InputStream inputStream = p.getInputStream();
        OutputStream outputStream = p.getOutputStream();
        // 在这里可以读取初始命令（如dir）的输出
        int available = inputStream.available();
        if (available > 0) {
            byte[] buffer = new byte[available];
            inputStream.read(buffer);
            System.out.println(new String(buffer));
        }
        // 发送新的命令，例如执行ipconfig
        String newCommand = "ipconfig\n";
        outputStream.write(newCommand.getBytes());
        outputStream.flush();
        // 读取新命令的输出
        available = inputStream.available();
        if (available > 0) {
            byte[] buffer = new byte[available];
            inputStream.read(buffer);
            System.out.println(new String(buffer));
        }
        p.waitFor();

    }


    @GetMapping("/dd")
    public void dd(String serviceName) {

        try {
            List<String> commandList = new ArrayList<>();
            commandList.add("E:/workspaces/project/shdy/dows-sbi/src/main/resources/sbin.exe");
            commandList.add("elevate");
            commandList.add("execmd");
            //commandList.add("max");
//            commandList.add("/c");
            commandList.add("dir");
            ProcessBuilder pb = new ProcessBuilder(commandList);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            // 读取第一个命令的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // 复用Process执行第二个命令
            OutputStream outputStream = process.getOutputStream();
            List<String> secondCommandList = new ArrayList<>();
            secondCommandList.add("type README.md");
            String secondCommand = secondCommandList.get(0) + "\n";
            outputStream.write(secondCommand.getBytes());
            outputStream.flush();
            // 读取第二个命令的输出
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            // 等待进程结束
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


//        List<String> commands = new ArrayList<>();
//        commands.add("E:/workspaces/project/shdy/dows-sbi/src/main/resources/sbin.exe");
//        commands.add("elevate");
//        commands.add("sc");
//        commands.add("create");
//        commands.add(serviceName); // 假设serviceName是一个变量，需要替换为实际的服务名
//        commands.add("binPath=\"E:/workspaces/project/shdy/shdy-sbi/setup/pkg/database/bin/mysqld.exe\"");
//
//        ProcessBuilder pb = new ProcessBuilder();
//        //pb.inheritIO();
//        pb.redirectErrorStream(true);
//        pb.command(commands);
//
//        try {
//            Process process = pb.start(); // 启动进程
//
//            // 读取命令行结果
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"GBK"));
//            String line;
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            int exitCode = process.waitFor(); // 等待进程结束
//            System.out.println("Exit Code: " + exitCode);
//
//
//            List<String> stop = new ArrayList<>();
//            //stop.add("E:/workspaces/project/shdy/dows-sbi/src/main/resources/sbin.exe");
//            //stop.add("cmd.exe");
//            //stop.add("/c");
//            stop.add("sc");
//            stop.add("delete");
//            stop.add(serviceName); // 假设serviceName是一个变量，需要替换为实际的服务名
//            //pb.command(stop);
//
//            /*List<String> stopCommands = new ArrayList<>();
//            stopCommands.add("E:/workspaces/project/shdy/dows-sbi/src/main/resources/sbin.exe");
//            stopCommands.add("elevate");
//            stopCommands.add("sc");
//            stopCommands.add("delete");
//            stopCommands.add(serviceName);*/
//
//            // 获取进程的输出流，用于向进程写入命令（注意需要正确处理编码等情况）
//            java.io.OutputStream outputStream = process.getOutputStream();
//            // 将停止命令转换为字符串并添加换行符（模拟在命令行中按回车键的效果）
//            String stopCommandStr = String.join(" ", stop) + "\n";
//            // 写入命令到进程
//            outputStream.write(stopCommandStr.getBytes());
//            outputStream.flush();
//
//            // 再次读取命令行结果（针对停止命令的输出）
//            while ((line = reader.readLine()) != null) {
//                System.out.println(line);
//            }
//
//            exitCode = process.waitFor(); // 等待进程结束
//            System.out.println("Exit Code: " + exitCode);
//            log.info("..............");
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @GetMapping("/ddd")
    public void ddd(String serviceName) {
        List<String> commands = new ArrayList<>();
        commands.add("E:/workspaces/project/shdy/dows-sbi/src/main/resources/sbin.exe");
        commands.add("elevate");
        //commands.add("cmd"); // 假设您想要交互的命令行是cmd.exe

        ProcessBuilder pb = new ProcessBuilder();
        pb.inheritIO();
        pb.redirectErrorStream(true);
        pb.command(commands);

        try {
            Process process = pb.start(); // 启动进程

            // 读取命令行结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 向命令行发送命令
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.println(String.format("sc create %s binPath= \"E:/workspaces/project/shdy/shdy-sbi/setup/pkg/database/bin/mysqld.exe\"", serviceName));
            writer.flush();
            writer.println("exit"); // 发送退出命令

            int exitCode = process.waitFor(); // 等待进程结束
            System.out.println("Exit Code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

