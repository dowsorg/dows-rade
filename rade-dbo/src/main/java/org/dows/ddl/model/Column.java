package org.dows.ddl.model;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.dows.ddl.enums.SqlTypeAndJavaTypeEnum;
import org.dows.ddl.utils.SqlTypeMapUtil;

import java.util.Objects;

@Data
public class Column {
    public String name;
    private String type;
    private Boolean primaryKey;
    private String defaultValue;
    private Boolean notNull = false;
    private String comment;

    public Column() {
    }

    public Column(String name, String type) {
        this.name = name;
        this.type = type;
        this.primaryKey = false;
    }

    public Column(String name, String type, Boolean primaryKey, String comment) {
        this.name = name;
        this.type = type;
        this.primaryKey = primaryKey;
        this.comment = comment;
    }

    public static Column newField(String name, String type, boolean primaryKey, String comment) {
        return new Column(name, type, primaryKey, comment);
    }

    public static Column newField(String name, String type) {
        return new Column(name, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return Objects.equals(name, column.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getTableColumn() {
        return StrUtil.toUnderlineCase(this.name);
    }

    public String getSqlType() {
        SqlTypeMapUtil.ConvertBean convertBean = SqlTypeMapUtil.getInstance().typeConvert(this.type);
        if (null != convertBean) {
            return convertBean.getSqlType() + convertBean.getSqlTypeLength();
        }
        /*兜底配置*/
        return getSqlTypeForMapping() + getSqlTypeSize();
    }

    /**
     * 获取mysql类型
     */
    public String getSqlTypeForMapping() {
        /*类型映射*/
        return SqlTypeAndJavaTypeEnum.findByJavaType(this.type).getSqlType();
    }

    public String getSqlTypeSize() {
        return SqlTypeAndJavaTypeEnum.findByJavaType(this.type).getDefaultLength();
    }

}
