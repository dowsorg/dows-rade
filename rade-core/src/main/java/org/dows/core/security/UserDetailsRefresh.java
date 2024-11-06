package org.dows.core.security;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 11:56 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface UserDetailsRefresh {

    void refreshUserDetails(String username);
}
