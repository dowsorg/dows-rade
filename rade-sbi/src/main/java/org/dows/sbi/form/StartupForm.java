package org.dows.sbi.form;

import lombok.Data;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/21/2024 4:54 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class StartupForm {

    private String host;
    private String port;
    private String username;
    private String password;
    private String index;
}

