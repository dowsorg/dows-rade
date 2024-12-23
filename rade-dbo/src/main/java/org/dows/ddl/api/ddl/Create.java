package org.dows.ddl.api.ddl;

import org.dows.ddl.annotation.NotCamel;
import org.dows.ddl.model.Column;

import java.util.List;

public interface Create {
    String createTable(String tableName, @NotCamel List<Column> columnList);

    //String creteTable(Class<? extends CrudEntity> entityClass);

    //String creteTableAndSnapshot(Class<? extends CrudEntity> entityClass, String sql);

}
