//package org.dows.ddl.actuator;
//
//import cn.hutool.core.util.StrUtil;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import io.swagger.v3.oas.annotations.media.Schema;
//import org.dows.ddl.actuator.ibuilder.SnapshotBuilder;
//import org.dows.ddl.api.ddl.Snapshot;
//import org.dows.ddl.builder.DdlBuilder;
//import org.dows.ddl.model.Column;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public abstract class ExecuteSnapshot<T extends SnapshotBuilder<T>> extends DdlBuilder<T> implements Snapshot {
//
//    public String snapshotTable(String formTableName, String toTableName, List<String> columnList, String where) {
//        current.paste(toTableName)
//                .select(columnList)
//                .copy(formTableName)
//                .where(where);
//        return current.end();
//    }
//
//    @Override
//    public String snapshotTable(Class<?> formClass, String toTableName, String where) {
//
//        java.lang.reflect.Field[] declaredFields = formClass.getDeclaredFields();
//        TableName tableName = formClass.getAnnotation(TableName.class);
//        if (tableName == null) {
//            throw new RuntimeException("this class ：" + formClass.getName() + " not support create table ,please add annotation @TableName");
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
//        return snapshotTable(tableName.value(), toTableName, columnList.stream().map(Column::getName).toList(), where);
//    }
//
//}
