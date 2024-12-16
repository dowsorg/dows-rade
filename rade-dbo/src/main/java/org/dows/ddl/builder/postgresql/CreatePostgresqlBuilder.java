package org.dows.ddl.builder.postgresql;

import org.dows.ddl.actuator.ExecuteCreate;
import org.dows.ddl.actuator.ibuilder.CreateBuilder;
import org.dows.ddl.actuator.sign.PostgreSqlSign;

public class CreatePostgresqlBuilder extends ExecuteCreate<CreatePostgresqlBuilder> implements PostgreSqlSign, CreateBuilder<CreatePostgresqlBuilder> {
    @Override
    public CreatePostgresqlBuilder create() {
        return null;
    }

    @Override
    public CreatePostgresqlBuilder addField(String field, String type, boolean isPrimaryKey) {
        return null;
    }

    @Override
    public CreatePostgresqlBuilder addField(String field, String type) {
        return null;
    }

    @Override
    public CreatePostgresqlBuilder addType(String type) {
        return null;
    }

    @Override
    public CreatePostgresqlBuilder isPrimaryKey(boolean isPrimaryKey) {
        return null;
    }

    @Override
    public CreatePostgresqlBuilder remove() {
        return null;
    }

    @Override
    public CreatePostgresqlBuilder remove(int size) {
        return null;
    }

    @Override
    public CreatePostgresqlBuilder addComment(String commend) {
        return null;
    }

    @Override
    public CreatePostgresqlBuilder dataBase(String dataBase) {
        return null;
    }
}
