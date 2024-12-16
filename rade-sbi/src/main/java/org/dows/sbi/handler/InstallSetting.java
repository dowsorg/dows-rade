package org.dows.sbi.handler;

import lombok.Data;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/22/2024 10:03 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class InstallSetting {

//    /**
//     * 平台（windows,linux,mac...）
//     *
//     * @return
//     */
//    default String getPlatform() {
//        return null;
//    }
//
//    /**
//     * x86 x64 架构
//     *
//     * @return
//     */
//    default String getArchitecture() {
//        return "x64";
//    }
//
//    default String getName() {
//        return null;
//    }
//
//    default String getVersion() {
//        return null;
//    }
//
//    default String getDir() {
//        return null;
//    }
//    /**
//     * 文件扩展名
//     *
//     * @return
//     */
//    default String getExt() {
//        return "zip";
//    }

    // 文件名称
    protected String name;
    // 版本
    protected String version;

    //平台（windows,linux,mac...）
    protected String platform;
    //x86 x64 架构
    protected String architecture;
    //文件扩展名
    protected String ext;

    // 安装目录
    //protected String dir;
    protected String baseDir;

    public String getInstallFile() {
        return String.format("%s-%s-%s-%s.%s", getPlatform(), getArchitecture(), getName(), getVersion(), getExt());
    }

    public String getInstallDir() {
        return String.format("%s-%s-%s-%s", getPlatform(), getArchitecture(), getName(), getVersion());
    }

}

