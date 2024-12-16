package org.dows.ddl.actuator.ibuilder;

public interface CreateBuilder<T> extends Builder<T> {
    T create();

    T addField(String field, String type, boolean isPrimaryKey);

    T addField(String field, String type);

    T addType(String type);

    T isPrimaryKey(boolean isPrimaryKey);

    T remove();

    T remove(int size);

    T addComment(String commend);

    T dataBase(String dataBase);

    default T repeat(StringBuffer ddl) {
        return (T) this;
    }
}
