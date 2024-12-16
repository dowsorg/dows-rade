package org.dows.ddl.actuator.ibuilder;

public interface DropBuilder<T> extends Builder<T> {
    T drop();

    T dataBase(String dataBase);

    default T repeat(StringBuffer ddl) {
        return (T) this;
    }
}
