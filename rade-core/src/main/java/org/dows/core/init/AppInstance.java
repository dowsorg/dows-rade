package org.dows.core.init;

import java.util.List;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 10/25/2024 12:52 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface AppInstance {

    default Class<?> getAppClass() {
        return null;
    }

    void restart(List<String> javaPathList);
}
