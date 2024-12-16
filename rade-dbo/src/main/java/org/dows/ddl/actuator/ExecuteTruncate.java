package org.dows.ddl.actuator;

import org.dows.ddl.actuator.ibuilder.TruncateBuilder;
import org.dows.ddl.api.ddl.Truncate;
import org.dows.ddl.builder.DdlBuilder;

public abstract class ExecuteTruncate<T extends TruncateBuilder<T>> extends DdlBuilder<T> implements Truncate {

    public String truncateTable(String table) {
        return current.truncate().tableName(table).end();
    }

}
