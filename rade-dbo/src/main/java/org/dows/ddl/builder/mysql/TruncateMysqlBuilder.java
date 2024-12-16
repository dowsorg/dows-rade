package org.dows.ddl.builder.mysql;

import org.dows.ddl.actuator.ExecuteTruncate;
import org.dows.ddl.actuator.ibuilder.TruncateBuilder;
import org.dows.ddl.actuator.sign.MysqlSign;

public class TruncateMysqlBuilder extends ExecuteTruncate<TruncateMysqlBuilder> implements MysqlSign, TruncateBuilder<TruncateMysqlBuilder> {

    public TruncateMysqlBuilder truncate() {
        ddl.append("TRUNCATE");
        return this.space();
    }

}
