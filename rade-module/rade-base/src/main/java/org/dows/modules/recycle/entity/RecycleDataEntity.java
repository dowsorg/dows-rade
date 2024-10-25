package org.dows.modules.recycle.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.Fastjson2TypeHandler;
import com.tangzc.autotable.annotation.Ignore;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mybatisflex.autotable.annotation.ColumnDefine;
import lombok.Getter;
import lombok.Setter;
import org.dows.core.crud.BaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 数据回收站 软删除的时候数据会回收到该表
 */
@Getter
@Setter
@Table(value = "recycle_data", comment = "数据回收站表")
public class RecycleDataEntity extends BaseEntity<RecycleDataEntity> {

    @ColumnDefine(comment = "表信息", type = "json")
    @Column(typeHandler = Fastjson2TypeHandler.class)
    private EntityInfo entityInfo;

    @Index()
    @ColumnDefine(comment = "操作人", notNull = true)
    private Long userId;

    @ColumnDefine(comment = "被删除的数据", type = "json")
    @Column(typeHandler = Fastjson2TypeHandler.class)
    private List<Object> data;

    @ColumnDefine(comment = "请求的接口", notNull = true)
    private String url;

    @ColumnDefine(comment = "请求参数", type = "json", notNull = true)
    @Column(typeHandler = Fastjson2TypeHandler.class)
    private Map<String, Object> params;

    @ColumnDefine(comment = "删除数据条数", defaultValue = "1")
    private Integer count;

    @Setter
    @Getter
    public static class EntityInfo {

        // entityClassName
        public String entityClassName;
    }

    @Ignore
    @Column(ignore = true) // 操作人名称
    public String userName;
}
