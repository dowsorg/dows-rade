package org.dows.sbi.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/21/2024 12:06 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Slf4j
public class ZipUtil {

    public static void unzipByClassPathResource( String zipFileName, String outputDirectory) throws IOException {
        String currentDirectory = System.getProperty("user.dir");
        if (StrUtil.isBlank(outputDirectory)) {
            outputDirectory = currentDirectory;
            log.info("依赖解压到应用启动所在目录: {}", outputDirectory);
        }
        ClassPathResource classPathResource = new ClassPathResource(String.format("setup/%s", zipFileName));
        //InputStream zipInputStream = classPathResource.getInputStream();
        String zipPath = classPathResource.getURI().getPath();
        //InputStream zipInputStreamFromJar = getZipInputStreamFromJar(jarPath, zipFileName);
        // 创建以zipFileName命名的目录
        /*String unzipDir = outputDirectory + File.separator + zipFileName.substring(0, zipFileName.lastIndexOf("."));
        File dir = new File(unzipDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }*/
        unzipToDirectory(zipPath, outputDirectory);
        log.info("依赖解压到指定目录: {}", outputDirectory);
        //ZipUtil.unzip(zipInputStream, new File(outputDirectory), StandardCharsets.UTF_8);
    }


    public static void unzipByJar(String jarPath, String zipFileName, String outputDirectory) throws IOException {
        String currentDirectory = System.getProperty("user.dir");
        if (StrUtil.isBlank(outputDirectory)) {
            outputDirectory = currentDirectory;
            log.info("依赖解压到应用启动所在目录: {}", outputDirectory);
        }
        log.info("依赖解压到指定目录: {}", outputDirectory);
        InputStream zipInputStreamFromJar = getZipInputStreamFromJar(jarPath, zipFileName);
        //unzipToDirectory(zipInputStreamFromJar, outputDirectory);
    }

    public static InputStream getZipInputStreamFromJar(String jarPath, String zipFileName) throws IOException {
        JarFile jarFile = new JarFile(jarPath);
        ZipEntry zipEntry = jarFile.getEntry(zipFileName);
        if (zipEntry == null) {
            throw new IOException("Zip file not found in the jar: " + zipFileName);
        }
        return jarFile.getInputStream(zipEntry);
    }

    public static void unzipToDirectory(String zipFilePath, String outputDirPath) throws IOException {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            // 遍历每个条目
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                // 如果是目录，则创建对应的目录
                if (entry.isDirectory()) {
                    File directory = new File(outputDirPath, entry.getName());
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }
                } else {
                    // 如果是文件，则进行解压缩操作

                    // 获取文件在ZIP文件中的输入流
                    InputStream inputStream = zipFile.getInputStream(entry);

                    // 创建输出文件对象
                    File outputFile = new File(outputDirPath, entry.getName());

                    // 创建输出文件的父目录（如果不存在）
                    File parentDir = outputFile.getParentFile();
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }
                    // 创建输出文件的输出流
                    FileOutputStream outputStream = new FileOutputStream(outputFile);

                    // 缓冲区大小，可以根据需要调整
                    byte[] buffer = new byte[1024];
                    int length;

                    // 从输入流读取数据，并写入到输出流
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    // 关闭输出流和输入流
                    outputStream.close();
                    inputStream.close();
                }
            }
            // 关闭ZipFile对象
            zipFile.close();
            log.info("{}文件解压缩成功！", zipFilePath);
        } catch (IOException e) {
            log.error("{}文件解压缩失败: {}", zipFilePath, e.toString());
        }

        //ZipUtil.unzip(zipInputStream, new File(outputDirectory), StandardCharsets.UTF_8);
        /*ZipInputStream zis = new ZipInputStream(zipInputStream);
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            String entryName = zipEntry.getName();
            File outputFile = new File(outputDirectory, entryName);
            if (zipEntry.isDirectory()) {
                outputFile.mkdirs();
            } else {
                FileOutputStream fos = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();*/
    }
}