package org.dows.ddl.actuator.ibuilder;

public interface Builder<T> {
    T tableName(String tableName);

    T LeftParenthesis();

    T rightParenthesis();

    T addColumn(String field);

    T addType(String type);

    T addComma();

    T space();

    T space(int size);

    T wrap();

    String end();

    T def(String def);

    T addComment(String commend);
}
