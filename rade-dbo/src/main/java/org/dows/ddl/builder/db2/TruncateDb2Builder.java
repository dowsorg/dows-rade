package org.dows.ddl.builder.db2;

import org.dows.ddl.actuator.ExecuteTruncate;
import org.dows.ddl.actuator.ibuilder.TruncateBuilder;
import org.dows.ddl.actuator.sign.Db2Sign;

public class TruncateDb2Builder extends ExecuteTruncate<TruncateDb2Builder> implements Db2Sign, TruncateBuilder<TruncateDb2Builder> {
    @Override
    public TruncateDb2Builder truncate() {
        return null;
    }
}
