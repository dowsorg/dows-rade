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

    /*@Index
    @ColumnDefine(comment = "部门ID", type = "bigint")
    private Long departmentId;

    @ColumnDefine(comment = "姓名")
    private String name;

    @Index(type = IndexTypeEnum.UNIQUE)
    @ColumnDefine(comment = "用户名", length = 100, notNull = true)
    private String username;

    @ColumnDefine(comment = "密码", notNull = true)
    private String password;

    @ColumnDefine(comment = "密码版本", defaultValue = "1")
    private Integer passwordV;

    @ColumnDefine(comment = "昵称", notNull = true)
    private String nickName;

    @ColumnDefine(comment = "头像")
    private String headImg;

    @ColumnDefine(comment = "手机号")
    private String phone;

    @ColumnDefine(comment = "邮箱")
    private String email;

    @ColumnDefine(comment = "备注")
    private String remark;

    @ColumnDefine(comment = "状态 0:禁用 1：启用", defaultValue = "1")
    private Integer status;

    // 部门名称
    @Column(ignore = true)
    private String departmentName;

    // 角色名称
    @Column(ignore = true)
    private String roleName;

    @ColumnDefine(comment = "socketId")
    private String socketId;*/

    Long getId();

    void setId(Long id);

    Long getDepartmentId();

    void setDepartmentId(Long departmentId);

    String getName();

    void setName(String name);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    Integer getPasswordV();

    void setPasswordV(Integer passwordV);

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

    Integer getStatus();

    void setStatus(Integer status);

    String getDepartmentName();

    void setDepartmentName(String departmentName);

    String getRoleName();

    void setRoleName(String roleName);

    String getSocketId();

    void setSocketId(String socketId);
}

