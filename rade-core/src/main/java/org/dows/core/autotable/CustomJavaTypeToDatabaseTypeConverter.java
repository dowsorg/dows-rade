package org.dows.core.autotable;

import com.tangzc.autotable.annotation.ColumnType;
import com.tangzc.autotable.core.constants.DatabaseDialect;
import com.tangzc.autotable.core.converter.DatabaseTypeAndLength;
import com.tangzc.autotable.core.converter.JavaTypeToDatabaseTypeConverter;
import com.tangzc.autotable.core.strategy.pgsql.data.PgsqlDefaultTypeEnum;
import com.tangzc.autotable.core.utils.StringUtils;
import com.tangzc.autotable.core.utils.TableBeanUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Component
public class CustomJavaTypeToDatabaseTypeConverter implements JavaTypeToDatabaseTypeConverter {

    /**
     * 重新 java转数据库类型
     * 兼容 mybatisflex 不兼容 postgresql 的json类型问题
     */
    @Override
    public DatabaseTypeAndLength convert(String databaseDialect, Class<?> clazz, Field field) {

        ColumnType column = TableBeanUtils.getColumnType(field);
        // 设置了类型
        if (column != null) {
            String type = column.value();
            Integer length = column.length() > -1 ? column.length() : null;
            Integer decimalLength = column.decimalLength() > -1 ? column.decimalLength() : null;
            List<String> values = Arrays.asList(column.values());
            // 如果明确指定了类型名，直接替换
            if (StringUtils.hasText(type)) {
                if (DatabaseDialect.PostgreSQL.equals(databaseDialect) &&
                        type.equalsIgnoreCase("json")) {
                    // json 类型在  postgresql 处理成 text
                    PgsqlDefaultTypeEnum typeEnum = PgsqlDefaultTypeEnum.TEXT;
                    return new DatabaseTypeAndLength(typeEnum.getTypeName(), typeEnum.getDefaultLength(), typeEnum.getDefaultLength(), values);
                }
                return new DatabaseTypeAndLength(type, length, decimalLength, values);
            }
            // 如果没有指定明确的类型名，但是却指定了长度。那么使用默认类型+指定长度
            if (length != null || decimalLength != null) {
                DatabaseTypeAndLength typeAndLength = getDatabaseTypeAndLength(databaseDialect, clazz, field);
                typeAndLength.setLength(length);
                typeAndLength.setDecimalLength(decimalLength);
                return typeAndLength;
            }
        }
        // 其他情况，使用默认类型
        return getDatabaseTypeAndLength(databaseDialect, clazz, field);
    }
}
