package org.dows.ddl.builder.postgresql;

import org.dows.ddl.actuator.ExecuteTruncate;
import org.dows.ddl.actuator.ibuilder.TruncateBuilder;
import org.dows.ddl.actuator.sign.PostgreSqlSign;

public class TruncatePostgresqlBuilder extends ExecuteTruncate<TruncatePostgresqlBuilder> implements PostgreSqlSign, TruncateBuilder<TruncatePostgresqlBuilder> {
    @Override
    public TruncatePostgresqlBuilder truncate() {
        return null;
    }
}
