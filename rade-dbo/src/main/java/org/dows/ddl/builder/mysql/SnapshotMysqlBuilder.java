package org.dows.ddl.builder.mysql;

import cn.hutool.core.util.StrUtil;
import org.dows.ddl.actuator.ExecuteSnapshot;
import org.dows.ddl.actuator.ibuilder.SnapshotBuilder;
import org.dows.ddl.actuator.sign.MysqlSign;

import java.util.List;

public class SnapshotMysqlBuilder extends ExecuteSnapshot<SnapshotMysqlBuilder> implements MysqlSign, SnapshotBuilder<SnapshotMysqlBuilder> {

    @Override
    public SnapshotMysqlBuilder paste(String toTableName) {
        ddl.append("CREATE TABLE ")
                .append(toTableName)
                .append(" AS ");
        return current;
    }

    @Override
    public SnapshotMysqlBuilder select(List<String> columnList) {
        ddl.append("SELECT ");
        for (String s : columnList) {
            ddl.append("`" + s + "`,");
        }
        return this.remove();
    }

    @Override
    public SnapshotMysqlBuilder copy(String fromTableName) {
        ddl.append(" FROM ").append(fromTableName).append(" ");
        return current;
    }

    @Override
    public SnapshotMysqlBuilder where(String where) {
        if (!StrUtil.isBlank(where)) {
            ddl.append("WHERE ").append(where);
        }
        return current;
    }

    @Override
    public SnapshotMysqlBuilder dataBase(String dataBase) {
        return current;
    }

}
