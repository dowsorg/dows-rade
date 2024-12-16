package org.dows.ddl.enums;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum SqlTypeAndJavaTypeEnum {
    BIGINT("BIGINT", Arrays.asList("long", "Long"), "(20)"),
    INT("INT", Arrays.asList("int", "Integer"), "(11)"),
    VARCHAR("VARCHAR", Arrays.asList("String", "char", "Character"), "(255)"),
    TINYINT("TINYINT", Arrays.asList("boolean", "Boolean"), "(1)"),
    DOUBLE("DOUBLE", Arrays.asList("double", "float", "Double", "Float"), ""),
    DATETIME("DATETIME", Collections.singletonList("Date"), ""),
    DECIMAL("DECIMAL", Collections.singletonList("BigDecimal"), "(18,2)");

    private final String sqlType;

    private final List<String> javaType;

    private final String defaultLength;

    SqlTypeAndJavaTypeEnum(String sqlType, List<String> javaType, String defaultLength) {
        this.sqlType = sqlType;
        this.javaType = javaType;
        this.defaultLength = defaultLength;
    }

    public static SqlTypeAndJavaTypeEnum findByJavaType(String javaType) {
        if (StrUtil.isBlank(javaType)) {
            throw new RuntimeException("异常实体");
        }
        for (SqlTypeAndJavaTypeEnum sqlTypeEnum : values()) {
            if (sqlTypeEnum.getJavaType().contains(javaType)) {
                return sqlTypeEnum;
            }
        }
        return VARCHAR;
    }

    public String getSqlType() {
        return sqlType;
    }

    public List<String> getJavaType() {
        return javaType;
    }

    public String getDefaultLength() {
        return defaultLength;
    }
}
