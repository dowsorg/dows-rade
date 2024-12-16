package org.dows.ddl.actuator.ibuilder;

public interface TruncateBuilder<T> extends Builder<T> {
    T truncate();

    default T repeat(StringBuffer ddl) {
        return (T) this;
    }
}
