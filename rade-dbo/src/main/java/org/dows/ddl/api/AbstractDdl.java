package org.dows.ddl.api;

import org.dows.ddl.api.ddl.*;
import org.dows.framework.ddl.api.ddl.*;

public abstract class AbstractDdl implements DdlApi {
    public <T> T getIndex(Class<T> finder) {
        if (finder == Alter.class) {
            return (T) builderAlter();
        }
        if (finder == Create.class) {
            return (T) builderCreate();
        }
        if (finder == Snapshot.class) {
            return (T) buildSnapshot();
        }
        if (finder == Drop.class) {
            return (T) builderDrop();
        }
        if (finder == Truncate.class) {
            return (T) builderTruncate();
        }
        throw new RuntimeException("no db operation");
    }
}
