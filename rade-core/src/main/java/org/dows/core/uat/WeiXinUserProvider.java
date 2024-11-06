package org.dows.core.uat;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 5:09 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface WeiXinUserProvider {
    //WeiXinUser getMiniUserInfo(String code, String encryptedData, String iv);

    WeiXinUser newWeiXinUser();

    WeiXinUser getBySave(WeiXinUser weiXinUser, Integer type);
}
