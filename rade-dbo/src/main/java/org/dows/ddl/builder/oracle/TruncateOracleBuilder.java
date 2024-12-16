package org.dows.ddl.builder.oracle;

import org.dows.ddl.actuator.ExecuteTruncate;
import org.dows.ddl.actuator.ibuilder.TruncateBuilder;
import org.dows.ddl.actuator.sign.OracleSign;

public class TruncateOracleBuilder extends ExecuteTruncate<TruncateOracleBuilder> implements OracleSign, TruncateBuilder<TruncateOracleBuilder> {


    @Override
    public TruncateOracleBuilder truncate() {
        return null;
    }
}
