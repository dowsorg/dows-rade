package org.dows.ddl.utils;

import cn.hutool.core.util.StrUtil;
import org.dows.ddl.init.Context;
import org.dows.ddl.model.Setting;

import java.util.concurrent.ConcurrentHashMap;

public class SqlTypeMapUtil {

    private static SqlTypeMapUtil sqlTypeMapUtil;

    private SqlTypeMapUtil() {
    }

    public static synchronized SqlTypeMapUtil getInstance() {
        if (sqlTypeMapUtil == null) {
            sqlTypeMapUtil = new SqlTypeMapUtil();
        }
        return sqlTypeMapUtil;
    }

    public ConcurrentHashMap<String, ConvertBean> convertMapInit() {
        Setting.SettingProperties properties = Setting.getInstance().properties;
        ConcurrentHashMap<String, ConvertBean> convertMap = new ConcurrentHashMap<>();
        convertMap.put("int", new ConvertBean(properties.getIntType(), properties.getIntDefaultLength()));
        convertMap.put("Integer", new ConvertBean(properties.getIntType(), properties.getIntDefaultLength()));
        convertMap.put("long", new ConvertBean(properties.getLongType(), properties.getLongDefaultLength()));
        convertMap.put("Long", new ConvertBean(properties.getLongType(), properties.getLongDefaultLength()));
        convertMap.put("double", new ConvertBean(properties.getDoubleType(), properties.getDoubleDefaultLength()));
        convertMap.put("Double", new ConvertBean(properties.getDoubleType(), properties.getDoubleDefaultLength()));
        convertMap.put("float", new ConvertBean(properties.getFloatType(), properties.getFloatDefaultLength()));
        convertMap.put("Float", new ConvertBean(properties.getFloatType(), properties.getFloatDefaultLength()));
        convertMap.put("boolean", new ConvertBean(properties.getBooleanType(), properties.getBooleanDefaultLength()));
        convertMap.put("Boolean", new ConvertBean(properties.getBooleanType(), properties.getBooleanDefaultLength()));
        convertMap.put("Date", new ConvertBean(properties.getDateType(), properties.getDateDefaultLength()));
        convertMap.put("String", new ConvertBean(properties.getStringType(), properties.getStringDefaultLength()));
        convertMap.put("char", new ConvertBean(properties.getStringType(), properties.getStringDefaultLength()));
        convertMap.put("Character", new ConvertBean(properties.getStringType(), properties.getStringDefaultLength()));
        convertMap.put("BigDecimal", new ConvertBean(properties.getBigDecimalType(), properties.getBigDecimalDefaultLength()));
        return convertMap;
    }

    public ConvertBean typeConvert(String javaType) {
        if (StrUtil.isBlank(javaType)) {
            return null;
        }
        return Context.convertMap.get(javaType);
    }

    public static class ConvertBean {
        private String sqlType;
        private String sqlTypeLength;

        public ConvertBean() {
        }

        public ConvertBean(String sqlType, String sqlTypeLength) {
            this.sqlType = sqlType;
            this.sqlTypeLength = sqlTypeLength;
        }

        public String getSqlType() {
            return sqlType;
        }

        public void setSqlType(String sqlType) {
            this.sqlType = sqlType;
        }

        public String getSqlTypeLength() {
            return sqlTypeLength;
        }

        public void setSqlTypeLength(String sqlTypeLength) {
            this.sqlTypeLength = sqlTypeLength;
        }
    }
}
