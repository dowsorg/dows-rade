package org.dows.ddl.api.db;

import org.dows.ddl.api.AbstractDdl;
import org.dows.ddl.api.ddl.*;
import org.dows.framework.ddl.api.ddl.*;
import org.dows.ddl.init.FactoryBuilder;

public class Db2Ddl extends AbstractDdl {
    @Override
    public Alter builderAlter() {
        return FactoryBuilder.Builder$Alter.getBean(this);
    }

    @Override
    public Create builderCreate() {
        return FactoryBuilder.Builder$Create.getBean(this);
    }

    @Override
    public Snapshot buildSnapshot() {
        return FactoryBuilder.Builder$Snapshot.getBean(this);
    }

    @Override
    public Drop builderDrop() {
        return FactoryBuilder.Builder$Drop.getBean(this);
    }

    @Override
    public Truncate builderTruncate() {
        return FactoryBuilder.Builder$Truncate.getBean(this);
    }
}
