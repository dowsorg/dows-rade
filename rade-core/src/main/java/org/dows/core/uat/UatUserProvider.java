package org.dows.core.uat;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 4:54 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface UatUserProvider {

    UatUser newUserInfo();

    UatUser getUserInfoByPhone(String phone);

    UatUser getUserInfoById(Long userId);

    UatUser getUserInfoByUnionId(String unionId);

    void saveUserInfo(UatUser userInfoEntity);
}
