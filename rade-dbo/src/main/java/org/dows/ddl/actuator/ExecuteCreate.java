//package org.dows.ddl.actuator;
//
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import io.swagger.v3.oas.annotations.media.Schema;
//import org.dows.ddl.actuator.ibuilder.CreateBuilder;
//import org.dows.ddl.api.ddl.Create;
//import org.dows.ddl.builder.DdlBuilder;
//import org.dows.ddl.model.Column;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class ExecuteCreate<T extends CreateBuilder<T>> extends DdlBuilder<T> implements Create {
//
//    public String createTable(String tableName, List<Column> columnList) {
//        current.create()
//                .tableName(tableName)
//                .LeftParenthesis()
//                .wrap();
//
//        int maxFieldStringLength = 0;
//        int maxFieldSqlTypeStringLength = 0;
//        for (Column column : columnList) {
//            if (maxFieldStringLength <= column.getTableColumn().length()) {
//                maxFieldStringLength = column.getTableColumn().length();
//            }
//            if (maxFieldSqlTypeStringLength <= column.getSqlType().length()) {
//                maxFieldSqlTypeStringLength = column.getSqlType().length();
//            }
//        }
//        maxFieldStringLength++;
//        maxFieldSqlTypeStringLength++;
//
//        for (Column column : columnList) {
//            String tableColumn = column.getTableColumn();
//            current = current.space(4)
//                    .addColumn(String.format("%-" + maxFieldStringLength + "s", tableColumn))
//                    .addType(String.format("%-" + maxFieldSqlTypeStringLength + "s", column.getSqlType()))
//                    .isPrimaryKey(column.getPrimaryKey());
//            if (null != column.getComment()) {
//                current.space().addComment(column.getComment());
//            }
//            current.addComma().wrap();
//        }
//        current = current.remove(2).wrap().rightParenthesis();
//        return current.end();
//    }
//
//    @Override
//    public String creteTable(Class<? extends CrudEntity> entityClass) {
//        java.lang.reflect.Field[] declaredFields = entityClass.getDeclaredFields();
//        TableName tableName = entityClass.getAnnotation(TableName.class);
//        if (tableName == null) {
//            throw new RuntimeException("this class ：" + entityClass.getName() + " not support create table ,please add annotation @TableName");
//        }
//        List<Column> columnList = new ArrayList<>();
//        for (java.lang.reflect.Field declaredField : declaredFields) {
//            Column column = new Column();
//            // sequence
//            TableId tableId = declaredField.getAnnotation(TableId.class);
//            if (tableId != null || declaredField.getName().equals("id")) {
//                column.setPrimaryKey(true);
//            } else {
//                column.setPrimaryKey(false);
//            }
//            // column
//            TableField annotation = declaredField.getAnnotation(TableField.class);
//            if (annotation != null && !StrUtil.isBlank(annotation.value())) {
//                column.setName(annotation.value());
//            } else {
//                column.setName(StrUtil.toUnderlineCase(declaredField.getName()));
//            }
//            // type
//            String simpleName = declaredField.getType().getSimpleName();
//            column.setType(simpleName);
//            // comment
//            Schema comment = declaredField.getAnnotation(Schema.class);
//            if (comment != null) {
//                column.setComment(comment.title());
//            }
//            columnList.add(column);
//        }
//        return createTable(tableName.value(), columnList);
//    }
//
////    @Override
////    public String creteTableAndSnapshot(Class<? extends CrudEntity> entityClass, String sql) {
////        creteTable(entityClass);
////
////        return null;
////    }
//
//    public String createDataBase(String database) {
//        return current.create().dataBase(database).end();
//    }
//
//}
