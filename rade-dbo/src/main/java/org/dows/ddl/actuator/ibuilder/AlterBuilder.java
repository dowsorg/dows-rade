package org.dows.ddl.actuator.ibuilder;

public interface AlterBuilder<T> extends Builder<T> {
    T alter();

    T tableName(String tableName);

    T renameTo(String nweTableName);

    T change();

    T modify();

    T drop();

    T add();

    T index();

    T primaryKey();

    T autokey(String key);

    T notNull();

    default T repeat(StringBuffer ddl) {  //大家对重复Alter定义进行名称复用
        return (T) this;
    }
}
