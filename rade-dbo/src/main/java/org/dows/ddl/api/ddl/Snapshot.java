package org.dows.ddl.api.ddl;

import java.util.List;

public interface Snapshot {
    String snapshotTable(String formTableName, String toTableName, List<String> columnList, String where);

    String snapshotTable(Class<?> formClass, String toTableName, String where);
}
