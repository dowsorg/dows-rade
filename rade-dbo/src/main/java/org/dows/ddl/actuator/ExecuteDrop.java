package org.dows.ddl.actuator;

import org.dows.ddl.actuator.ibuilder.DropBuilder;
import org.dows.ddl.api.ddl.Drop;
import org.dows.ddl.builder.DdlBuilder;

public abstract class ExecuteDrop<T extends DropBuilder<T>> extends DdlBuilder<T> implements Drop {

    public String dropTable(String table) {
        return current.drop().tableName(table).end();

    }

    public String dropDataBase(String database) {
        return current.drop().dataBase(database).end();
    }

}
