package org.dows.core.uat;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 4:57 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface WeiXinUser {

    Long getId();

    void setId(Long id);

    //微信unionid
    String getUnionid();

    void setUnionid(String unionid);

    //微信openid
    String getOpenid();

    void setOpenid(String openid);

    String getAvatarUrl();

    void setAvatarUrl(String avatarUrl);

    String getNickName();

    void setNickName(String nickName);

    Integer getGender();

    void setGender(Integer gender);

    String getLanguage();

    void setLanguage(String language);

    String getCity();

    void setCity(String city);

    String getProvince();

    void setProvince(String province);

    String getCountry();

    void setCountry(String country);

    //类型 0-小程序 1-公众号 2-H5 3-APP 默认0
    Integer getType();

    void setType(Integer type);
}
