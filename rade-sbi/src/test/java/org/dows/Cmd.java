package org.dows;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 12/11/2024 11:48 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public class Cmd {


    public static void main(String[] args) {
        //--install "%s" && %s\bin\mysql_install_db.exe
        try {

            String commd = "cmd /c runas /user:Administrator cmd";
            String mysqld = String.format("%smysqld.exe", "E:\\workspaces\\project\\shdy\\shdy-sbi\\setup\\pkg\\database\\bin\\");
            String ws = String.format("sc create %s binPath= %s", "serviceName", mysqld);
            List<String> command = new ArrayList<>();
            //command.add(String.format("%s elevate %s", cmd, ws));
            // 去掉/c参数，使CMD进程不立即关闭
            // command.add("/c");
            //command.add("dir");
            command.add(commd);
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.inheritIO();
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
        } catch (IOException | InterruptedException e) {
        }
    }
}

