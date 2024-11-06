package org.dows.aac;

import cn.hutool.json.JSONObject;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 10:48 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface AacApi {
    String getAdminUsername();

    JSONObject getAdminUserInfo(JSONObject requestParams);

    void adminLogout(Long id, String userName);

    Long getCurrentUserId();

}
