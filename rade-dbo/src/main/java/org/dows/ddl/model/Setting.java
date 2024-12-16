package org.dows.ddl.model;


import lombok.Data;

import static org.dows.ddl.enums.SqlTypeAndJavaTypeEnum.*;

public class Setting {
    public SettingProperties properties = new SettingProperties();

    public static Setting getInstance() {
        return new Setting();
    }

    public SettingProperties getState() {
        return properties;
    }

    public void loadState(SettingProperties state) {
        properties = state;
    }

    @Data
    public static class SettingProperties {
        /**
         * 是否开启自动翻译
         */
        private final Boolean autoTranslationRadio = false;
        /**
         * 翻译组件
         */
        private final String translationAppComboBox = "";
        /**
         * appid
         */
        private final String appIdText = "";
        /**
         * secret
         */
        private final String secretText = "";
        /**
         * 腾讯云翻译secretId
         */
        private final String secretId = "";
        /**
         * 腾讯云翻译secretKey
         */
        private final String secretKey = "";
        /**
         * 表名使用的注解
         */
        private final String tableAnnotation = "javax.persistence.Table,jakarta.persistence.Table,com.baomidou.mybatisplus.annotation.TableName";
        /**
         * 表名使用的注解属性
         */
        private final String tableAnnotationProperty = "name,value";
        /**
         * id使用的注解
         */
        private final String idAnnotation = "javax.persistence.Id,jakarta.persistence.Id,com.baomidou.mybatisplus.annotation.TableId";
        /**
         * 注释
         */
        private final String commentAnnotation = "comment";
        private String intType = INT.getSqlType();
        private String longType = BIGINT.getSqlType();
        private String stringType = VARCHAR.getSqlType();
        private String booleanType = TINYINT.getSqlType();
        private String dateType = DATETIME.getSqlType();
        private String doubleType = DOUBLE.getSqlType();
        private String floatType = DOUBLE.getSqlType();
        private String bigDecimalType = DECIMAL.getSqlType();
        private String intDefaultLength = INT.getDefaultLength();
        private String longDefaultLength = BIGINT.getDefaultLength();
        private String stringDefaultLength = VARCHAR.getDefaultLength();
        private String doubleDefaultLength = DOUBLE.getDefaultLength();
        private String floatDefaultLength = DOUBLE.getDefaultLength();
        private String booleanDefaultLength = TINYINT.getDefaultLength();
        private String dateDefaultLength = DATETIME.getDefaultLength();
        private String bigDecimalDefaultLength = DECIMAL.getDefaultLength();
    }
}