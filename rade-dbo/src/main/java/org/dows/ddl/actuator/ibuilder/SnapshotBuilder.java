package org.dows.ddl.actuator.ibuilder;

import java.util.List;

/**
 * create table ${md5}_xxx
 * as
 * select * from xxx where ...
 *
 * @param <T>
 */
public interface SnapshotBuilder<T> extends Builder<T> {
    T paste(String toTableName);

    T select(List<String> columnList);

    T copy(String formTableName);

    T where(String where);

    T dataBase(String dataBase);

    default T repeat(StringBuffer ddl) {
        return (T) this;
    }
}
