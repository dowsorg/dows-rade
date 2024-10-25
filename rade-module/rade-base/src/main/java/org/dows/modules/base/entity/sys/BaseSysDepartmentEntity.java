package org.dows.modules.base.entity.sys;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.tangzc.mybatisflex.autotable.annotation.ColumnDefine;
import lombok.Getter;
import lombok.Setter;
import org.dows.core.crud.BaseEntity;

/**
 * 系统部门
 */
@Getter
@Setter
@Table(value = "base_sys_department", comment = "系统部门")
public class BaseSysDepartmentEntity extends BaseEntity<BaseSysDepartmentEntity> {
    @ColumnDefine(comment = "部门名称", notNull = true)
    private String name;

    @ColumnDefine(comment = "上级部门ID", type = "bigint")
    private Long parentId;

    @ColumnDefine(comment = "排序", defaultValue = "0")
    private Integer orderNum;

    // 父菜单名称
    @Column(ignore = true)
    private String parentName;
}
