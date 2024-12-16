package org.dows.ddl.builder;

public class DdlBuilder<T> {
    protected StringBuffer ddl = new StringBuffer();
    protected T current = (T) this;

    public T tableName(String tableName) {
        ddl.append("TABLE").append(" ").append(tableName);
        return this.wrap();
    }

    public T LeftParenthesis() {
        ddl.append("(");
        return current;
    }

    public T rightParenthesis() {
        ddl.append(")");
        return current;
    }

    public T addColumn(String field) {
        ddl.append(field);
        return current;
    }


    public T addType(String type) {
        ddl.append(type);
        return current;
    }

    public T def(String def) {
        ddl.append("default").append(" ").append(def);
        return this.space();
    }

    public T addComment(String commend) {
        ddl.append("COMMENT ").append("'").append(commend).append("'");
        return current;
    }

    public T addComma() {
        ddl.append(",");
        return current;
    }

    public T space() {
        ddl.append(" ");
        return current;
    }

    public T space(int size) {
        if (size <= 0) {
            size = 1;
        }
        for (int i = 0; i < size; i++) {
            space();
        }
        return current;
    }

    public T remove() {
        ddl.deleteCharAt(ddl.length() - 1);
        return current;
    }

    public T wrap() {
        ddl.append("\n");
        return current;
    }

    public String end() {
        try {
            return ddl.append(";").toString();
        } finally {
            ddl.setLength(0);
        }

    }
}
