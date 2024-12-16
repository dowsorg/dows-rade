package org.dows.ddl.builder.db2;

import org.dows.ddl.actuator.ExecuteCreate;
import org.dows.ddl.actuator.ibuilder.CreateBuilder;
import org.dows.ddl.actuator.sign.Db2Sign;

public class CreateDb2Builder extends ExecuteCreate<CreateDb2Builder> implements Db2Sign, CreateBuilder<CreateDb2Builder> {
    @Override
    public CreateDb2Builder create() {
        return null;
    }

    @Override
    public CreateDb2Builder addField(String field, String type, boolean isPrimaryKey) {
        return null;
    }

    @Override
    public CreateDb2Builder addField(String field, String type) {
        return null;
    }

    @Override
    public CreateDb2Builder addType(String type) {
        return null;
    }

    @Override
    public CreateDb2Builder isPrimaryKey(boolean isPrimaryKey) {
        return null;
    }

    @Override
    public CreateDb2Builder remove() {
        return null;
    }

    @Override
    public CreateDb2Builder remove(int size) {
        return null;
    }

    @Override
    public CreateDb2Builder addComment(String commend) {
        return null;
    }

    @Override
    public CreateDb2Builder dataBase(String dataBase) {
        return null;
    }
}
