package org.dows.ddl.builder.mysql;

import org.dows.ddl.actuator.ExecuteAlter;
import org.dows.ddl.actuator.ibuilder.AlterBuilder;
import org.dows.ddl.actuator.sign.MysqlSign;

public class AlterMysqlBuilder extends ExecuteAlter<AlterMysqlBuilder> implements MysqlSign, AlterBuilder<AlterMysqlBuilder> {

    public AlterMysqlBuilder alter() {
        ddl.append("ALTER");
        return this.space();
    }

    public AlterMysqlBuilder tableName(String tableName) {
        ddl.append("TABLE").append(" ").append(tableName);
        return this.space();
    }

    public AlterMysqlBuilder renameTo(String nweTableName) {
        ddl.append("RENAME TO").append(" ").append(nweTableName);
        return this;
    }

    public AlterMysqlBuilder change() {
        ddl.append("CHANGE");
        return this.space();
    }

    public AlterMysqlBuilder modify() {
        ddl.append("MODIFY");
        return this.space();
    }

    public AlterMysqlBuilder drop() {
        ddl.append("DROP");
        return this.space();
    }

    public AlterMysqlBuilder add() {
        ddl.append("ADD");
        return this.space();
    }

    public AlterMysqlBuilder primaryKey() {
        ddl.append("primary key");
        return this.space();
    }


    public AlterMysqlBuilder autokey(String key) {
        ddl.append(key).append(" int auto_increment primary key ");
        return this;
    }


    public AlterMysqlBuilder index() {
        ddl.append("INDEX");
        return this.space();
    }


    public AlterMysqlBuilder notNull() {
        ddl.append("NOT NULL");
        return this.space();
    }


}
