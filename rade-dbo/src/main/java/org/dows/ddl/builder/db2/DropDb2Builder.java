package org.dows.ddl.builder.db2;

import org.dows.ddl.actuator.ExecuteDrop;
import org.dows.ddl.actuator.ibuilder.DropBuilder;
import org.dows.ddl.actuator.sign.Db2Sign;

public class DropDb2Builder extends ExecuteDrop<DropDb2Builder> implements Db2Sign, DropBuilder<DropDb2Builder> {

    @Override
    public DropDb2Builder drop() {
        return null;
    }

    @Override
    public DropDb2Builder dataBase(String dataBase) {
        return null;
    }
}
