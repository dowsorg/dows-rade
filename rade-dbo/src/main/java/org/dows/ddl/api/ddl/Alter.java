package org.dows.ddl.api.ddl;

import org.dows.ddl.annotation.NotCamel;
import org.dows.ddl.annotation.Type;
import org.dows.ddl.model.Column;

public interface Alter {
    String renameTable(String tableName, String newTableName);

    String renameColumn(String tableName,
                        String column,
                        String newColumn,
                        @NotCamel @Type String newColumnType);

    String modifyColumn(String tableName,
                        @NotCamel Column column);

    String addColumn(String tableName,
                     @NotCamel Column column);


    String delColumn(String tableName,
                     String column);


    String addPrimaryKey(String tableName, String... column);

    String setAutoPrimaryKey(String tableName, String keyColumn);

    String delPrimaryKey(String tableName);

    String addIndex(String tableName, String column);


    String delIndex(String tableName, String column);

}
