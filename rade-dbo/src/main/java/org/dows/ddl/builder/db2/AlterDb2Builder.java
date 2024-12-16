package org.dows.ddl.builder.db2;

import org.dows.ddl.actuator.ExecuteAlter;
import org.dows.ddl.actuator.ibuilder.AlterBuilder;
import org.dows.ddl.actuator.sign.Db2Sign;

public class AlterDb2Builder extends ExecuteAlter<AlterDb2Builder> implements Db2Sign, AlterBuilder<AlterDb2Builder> {

    @Override
    public AlterDb2Builder alter() {
        return null;
    }

    @Override
    public AlterDb2Builder renameTo(String nweTableName) {
        return null;
    }

    @Override
    public AlterDb2Builder change() {
        return null;
    }

    @Override
    public AlterDb2Builder modify() {
        return null;
    }

    @Override
    public AlterDb2Builder drop() {
        return null;
    }

    @Override
    public AlterDb2Builder add() {
        return null;
    }

    @Override
    public AlterDb2Builder index() {
        return null;
    }

    @Override
    public AlterDb2Builder primaryKey() {
        return null;
    }

    @Override
    public AlterDb2Builder autokey(String key) {
        return null;
    }

    @Override
    public AlterDb2Builder notNull() {
        return null;
    }


}
