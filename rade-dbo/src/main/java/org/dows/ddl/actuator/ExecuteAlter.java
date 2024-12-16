package org.dows.ddl.actuator;

import org.dows.ddl.actuator.ibuilder.AlterBuilder;
import org.dows.ddl.api.ddl.Alter;
import org.dows.ddl.builder.DdlBuilder;
import org.dows.ddl.model.Column;

public abstract class ExecuteAlter<T extends AlterBuilder<T>> extends DdlBuilder<T> implements Alter {

    public String renameTable(String tableName, String newTableName) {
        return current.alter().tableName(tableName).renameTo(newTableName).end();

    }

    public String renameColumn(String tableName, String column, String newColumn, String newColumnType) {
        return current.alter().tableName(tableName).change().
                addColumn(column).space().addColumn(newColumn).space().
                addType(newColumnType).end();

    }

    public String modifyColumn(String tableName, Column column) {
        current.alter().tableName(tableName).modify().addColumn(column.getTableColumn()).space().addType(column.getType()).space();
        if (column.getNotNull())
            current.notNull();
        if (null != column.getDefaultValue())
            current.def(column.getDefaultValue());
        if (null != column.getComment())
            current.addComment(column.getComment());
        return current.end();
    }

    public String addColumn(String tableName, Column column) {
        current.alter().tableName(tableName).add().addColumn(column.getTableColumn()).space().addType(column.getType()).space();
        if (column.getNotNull())
            current.notNull();
        if (null != column.getDefaultValue())
            current.def(column.getDefaultValue());
        if (null != column.getComment())
            current.addComment(column.getComment());
        return current.end();
    }


    public String delColumn(String tableName, String column) {
        return current.alter().tableName(tableName).drop().addColumn(column).end();
    }


    public String addPrimaryKey(String tableName, String... column) {
        current.alter().tableName(tableName).add().primaryKey().LeftParenthesis();
        for (String k : column) {
            current.addColumn(k).addComma();
        }
        this.remove();
        current.rightParenthesis();
        return current.end();
    }

    public String setAutoPrimaryKey(String tableName, String keyColumn) {
        return current.alter().tableName(tableName).autokey(keyColumn).end();
    }

    public String delPrimaryKey(String tableName) {
        return current.alter().tableName(tableName).drop().primaryKey().end();
    }


    public String addIndex(String tableName, String column) {
        return current.alter().tableName(tableName).add().index().LeftParenthesis().addColumn(column).rightParenthesis().end();
    }


    public String delIndex(String tableName, String column) {
        return current.alter().tableName(tableName).drop().index().addColumn(column).end();
    }

}
