package org.dows.ddl.store;

import org.dows.ddl.api.DdlApi;
import org.dows.ddl.enums.DbTypeEnum;

public class DdlBuildrRepository {

    public DdlApi mysql() {
        return DbTypeEnum.MYSQL.getDb();
    }

    public DdlApi oracle() {
        return DbTypeEnum.ORAClE.getDb();
    }

    public DdlApi db2() {
        return DbTypeEnum.DB2.getDb();
    }

    public DdlApi postgre() {
        return DbTypeEnum.POSTGRESQL.getDb();
    }

}
