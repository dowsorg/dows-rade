package org.dows.ddl.builder.mysql;

import org.dows.ddl.actuator.ExecuteDrop;
import org.dows.ddl.actuator.ibuilder.DropBuilder;
import org.dows.ddl.actuator.sign.MysqlSign;

public class DropMysqlBuilder extends ExecuteDrop<DropMysqlBuilder> implements MysqlSign, DropBuilder<DropMysqlBuilder> {

    public DropMysqlBuilder drop() {
        ddl.append("DROP");
        return this.space();
    }

    public DropMysqlBuilder dataBase(String dataBase) {
        ddl.append(" DATABASE ").append(dataBase);
        return this.space();
    }
}
