package org.dows.ddl.builder.oracle;

import org.dows.ddl.actuator.ExecuteDrop;
import org.dows.ddl.actuator.ibuilder.DropBuilder;
import org.dows.ddl.actuator.sign.OracleSign;
import org.dows.ddl.builder.db2.DropDb2Builder;

public class DropOracleBuilder extends ExecuteDrop<DropDb2Builder> implements OracleSign, DropBuilder<DropDb2Builder> {
    @Override
    public DropDb2Builder drop() {
        return null;
    }

    @Override
    public DropDb2Builder dataBase(String dataBase) {
        return null;
    }
}
