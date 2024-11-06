package org.dows.core.uat;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 4:57 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface UatUser {

    // 主键
    Long getId();

    void setId(Long id);

    //登录唯一ID
    String getUnionid();

    void setUnionid(String unionid);

    //头像
    String getAvatarUrl();

    void setAvatarUrl(String avatarUrl);

    //昵称
    String getNickName();

    void setNickName(String nickName);

    String getPhone();

    void setPhone(String phone);

    //性别 0-未知 1-男 2-女
    Integer getGender();

    void setGender(Integer gender);

    //状态 0-禁用 1-正常 2-已注销
    Integer getStatus();

    void setStatus(Integer status);

    //登录方式 0-小程序 1-公众号 2-H5
    String getLoginType();

    void setLoginType(String loginType);

    //密码
    String getPassword();

    void setPassword(String password);
}
