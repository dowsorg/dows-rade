package org.dows.ddl.builder.oracle;

import org.dows.ddl.actuator.ExecuteAlter;
import org.dows.ddl.actuator.ibuilder.AlterBuilder;
import org.dows.ddl.actuator.sign.OracleSign;

public class AlterOracleBuilder extends ExecuteAlter<AlterOracleBuilder> implements OracleSign, AlterBuilder<AlterOracleBuilder> {
    @Override
    public AlterOracleBuilder alter() {
        return null;
    }

    @Override
    public AlterOracleBuilder renameTo(String nweTableName) {
        return null;
    }

    @Override
    public AlterOracleBuilder change() {
        return null;
    }

    @Override
    public AlterOracleBuilder modify() {
        return null;
    }

    @Override
    public AlterOracleBuilder drop() {
        return null;
    }

    @Override
    public AlterOracleBuilder add() {
        return null;
    }

    @Override
    public AlterOracleBuilder index() {
        return null;
    }

    @Override
    public AlterOracleBuilder primaryKey() {
        return null;
    }

    @Override
    public AlterOracleBuilder autokey(String key) {
        return null;
    }

    @Override
    public AlterOracleBuilder notNull() {
        return null;
    }


}
