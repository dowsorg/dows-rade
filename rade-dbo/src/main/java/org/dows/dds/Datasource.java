package org.dows.dds;

import java.lang.annotation.*;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 7/11/2024 3:11 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Datasource {
    String name();
}
