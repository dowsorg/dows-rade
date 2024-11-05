package org.dows.modules.uat.user.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.autotable.annotation.enums.IndexTypeEnum;
import com.tangzc.mybatisflex.autotable.annotation.ColumnDefine;
import lombok.Getter;
import lombok.Setter;
import org.dows.core.crud.BaseEntity;
import org.dows.core.security.SecurityDetail;

@Getter
@Setter
@Table(value = "base_sys_user", comment = "系统用户表")
public class BaseSysUserEntity extends BaseEntity<BaseSysUserEntity> implements SecurityDetail {
    @Index
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
    private String socketId;
}
