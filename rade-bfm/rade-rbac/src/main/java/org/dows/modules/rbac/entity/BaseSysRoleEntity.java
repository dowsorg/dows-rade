package org.dows.modules.rbac.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.Fastjson2TypeHandler;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.autotable.annotation.enums.IndexTypeEnum;
import com.tangzc.mybatisflex.autotable.annotation.ColumnDefine;
import lombok.Getter;
import lombok.Setter;
import org.dows.core.crud.BaseEntity;

import java.util.List;

@Getter
@Setter
@Table(value = "base_sys_role", comment = "系统角色表")
public class BaseSysRoleEntity extends BaseEntity<BaseSysRoleEntity> {

    @Index
    @ColumnDefine(comment = "用户ID", notNull = true, type = "bigint")
    private Long userId;

    @ColumnDefine(comment = "名称", notNull = true)
    private String name;

    @Index(type = IndexTypeEnum.UNIQUE)
    @ColumnDefine(comment = "角色标签", notNull = true)
    private String label;

    @ColumnDefine(comment = "备注")
    private String remark;

    @ColumnDefine(comment = "数据权限是否关联上下级", defaultValue = "1")
    private Integer relevance;

    @ColumnDefine(comment = "菜单权限", type = "json")
    @Column(typeHandler = Fastjson2TypeHandler.class)
    private List<Long> menuIdList;

    @ColumnDefine(comment = "部门权限", type = "json")
    @Column(typeHandler = Fastjson2TypeHandler.class)
    private List<Long> departmentIdList;
}
