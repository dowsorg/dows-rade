package org.dows.ddl.model;

import org.dows.ddl.enums.DbTypeEnum;

public class DbWrappers<T> {
    private final Class<T> type;

    public DbWrappers(Class<T> type) {
        this.type = type;
    }

    public T mysql() {
        return DbTypeEnum.MYSQL.getDbBase().getIndex(type);
    }

    public T oracle() {
        return DbTypeEnum.ORAClE.getDbBase().getIndex(type);
    }

    public T db2() {
        return DbTypeEnum.DB2.getDbBase().getIndex(type);
    }

    public T postgreSql() {
        return DbTypeEnum.POSTGRESQL.getDbBase().getIndex(type);
    }
}
