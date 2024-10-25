package org.dows.modules.base.entity.sys;

import org.dows.core.crud.BaseEntity;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.core.handler.Fastjson2TypeHandler;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mybatisflex.autotable.annotation.ColumnDefine;
import com.mybatisflex.annotation.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(value = "base_sys_log", comment = "系统日志表")
public class BaseSysLogEntity extends BaseEntity<BaseSysLogEntity> {

    @Index
    @ColumnDefine(comment = "用户ID", type = "bigint")
    private Long userId;

    @ColumnDefine(comment = "行为", length = 1000)
    private String action;

    @ColumnDefine(comment = "IP", length = 50)
    private String ip;

    @ColumnDefine(comment = "参数", type = "json")
    @Column(typeHandler = Fastjson2TypeHandler.class)
    private Object params;

    // 用户名称
    @Column(ignore = true)
    private String name;
}
