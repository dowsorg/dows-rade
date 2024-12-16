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
public class NginxInstallForm implements RegisterService {

    private String baseDir;
    private String logDir;
    private String dataDir;
    private String serviceName = "SbiNginx";
    private String port;
    private String host;
    private String appId;
}
