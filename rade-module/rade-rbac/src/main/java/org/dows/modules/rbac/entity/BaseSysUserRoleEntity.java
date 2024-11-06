package org.dows.modules.rbac.entity;

import com.mybatisflex.annotation.Table;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mybatisflex.autotable.annotation.ColumnDefine;
import lombok.Getter;
import lombok.Setter;
import org.dows.core.crud.BaseEntity;

@Getter
@Setter
@Table(value = "base_sys_user_role", comment = "系统用户角色表")
public class BaseSysUserRoleEntity extends BaseEntity<BaseSysUserRoleEntity> {
    @Index
    @ColumnDefine(comment = "用户ID", type = "bigint")
    private Long userId;

    @Index
    @ColumnDefine(comment = "角色ID", type = "bigint")
    private Long roleId;
}