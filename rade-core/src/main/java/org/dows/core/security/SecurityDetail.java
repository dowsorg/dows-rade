package org.dows.core.security;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 10/25/2024 4:10 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
public interface SecurityDetail {

    Long getId();
    void setId(Long id);

    //部门ID
    Long getDepartmentId();
    void setDepartmentId(Long departmentId);
    //部门名称
    String getDepartmentName();
    void setDepartmentName(String departmentName);

    //姓名
    String getName();
    void setName(String name);

    //用户名
    String getUsername();
    void setUsername(String username);

    String getPassword();
    void setPassword(String password);

    Integer getPasswordV();
    void setPasswordV(Integer passwordV);

    //昵称
    String getNickName();
    void setNickName(String nickName);

    String getHeadImg();
    void setHeadImg(String headImg);

    String getPhone();
    void setPhone(String phone);

    String getEmail();
    void setEmail(String email);

    String getRemark();
    void setRemark(String remark);

    //状态 0:禁用 1：启用
    Integer getStatus();
    void setStatus(Integer status);



    // 角色名称
    String getRoleName();
    void setRoleName(String roleName);

    String getSocketId();
    void setSocketId(String socketId);
}

