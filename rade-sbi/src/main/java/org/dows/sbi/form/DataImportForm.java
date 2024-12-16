package org.dows.sbi.form;

import lombok.Data;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/21/2024 4:07 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class DataImportForm {
    private String schema;
    private String progress;
    private String sqlStatement;
    private String dir;

}