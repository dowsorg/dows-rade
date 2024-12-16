package org.dows.dds;

import lombok.Data;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 7/12/2024 5:51 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class PoolSettings {
    // 池类型[hikari,duri]
    private String type;

    private Integer maximumPoolSize;

    private Integer minimumIdle;

    private Integer  connectionTimeout;

}

