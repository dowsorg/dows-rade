package org.dows.sbi.form;

import lombok.Data;
import org.dows.sbi.handler.RegisterService;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/21/2024 2:48 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class MySQLInstallForm implements RegisterService {
    private String username = "root";
    private String password;
    private String host = "127.0.0.1";
    private String port = "3306";
    private String databaseName;
    private String baseDir;
    private String binlog;
    private String logDir;
    private String dataDir;
    private String serviceName ="SbiMysql";
    private String appId;
    private String setupName= "mysql";
    private String preSetupName;

    public boolean isHostSet() {
        return host!= null &&!host.isEmpty();
    }

    public boolean isUsernameSet() {
        return username!= null &&!username.isEmpty();
    }


}