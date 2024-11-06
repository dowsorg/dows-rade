package org.dows.uat;

import lombok.Data;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/6/2024 11:47 AM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class UserInfo {


    private Long id;
    //@Index
    //@ColumnDefine(comment = "部门ID", type = "bigint")
    private Long departmentId;

    //@ColumnDefine(comment = "姓名")
    private String name;

    //@Index(type = IndexTypeEnum.UNIQUE)
    //@ColumnDefine(comment = "用户名", length = 100, notNull = true)
    private String username;

    //@ColumnDefine(comment = "密码", notNull = true)
    private String password;

    //@ColumnDefine(comment = "密码版本", defaultValue = "1")
    private Integer passwordV;

    //@ColumnDefine(comment = "昵称", notNull = true)
    private String nickName;

    //@ColumnDefine(comment = "头像")
    private String headImg;

    //@ColumnDefine(comment = "手机号")
    private String phone;

   // @ColumnDefine(comment = "邮箱")
    private String email;

    //@ColumnDefine(comment = "备注")
    private String remark;

    //@ColumnDefine(comment = "状态 0:禁用 1：启用", defaultValue = "1")
    private Integer status;

    // 部门名称
    //@Column(ignore = true)
    private String departmentName;

    // 角色名称
    //@Column(ignore = true)
    private String roleName;

    //@ColumnDefine(comment = "socketId")
    private String socketId;

}

