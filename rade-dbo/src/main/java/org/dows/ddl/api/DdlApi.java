package org.dows.ddl.api;

import org.dows.ddl.api.ddl.*;
import org.dows.framework.ddl.api.ddl.*;

public interface DdlApi {
    Alter builderAlter();

    Create builderCreate();

    Snapshot buildSnapshot();

    Drop builderDrop();

    Truncate builderTruncate();
}
