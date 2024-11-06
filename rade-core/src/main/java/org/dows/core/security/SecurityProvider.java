package org.dows.core.security;

import cn.hutool.json.JSONObject;
import org.dows.core.enums.UserTypeEnum;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 10:48 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface SecurityProvider {
    String getAdminUsername();

    JSONObject getAdminUserInfo(JSONObject requestParams);

    void adminLogout(Long id, String userName);

    Long getCurrentUserId();

    UserTypeEnum getCurrentUserType();

}
