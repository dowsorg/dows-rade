package org.dows.ddl.init;

import org.dows.ddl.actuator.distribute.*;
import org.dows.ddl.utils.SqlTypeMapUtil;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Context {
    public static final AbstractDbHandler.Builder dbHandler = new AbstractDbHandler.Builder();
    public static final Init manager = new Init();
    public static ConcurrentHashMap<String, SqlTypeMapUtil.ConvertBean> convertMap;
    public static HashMap<Object, List<Class>> hashMap;

    static class Init {

        static {
            convertMap = SqlTypeMapUtil.getInstance().convertMapInit();
            hashMap = FactoryBuilder.initBuilder();
            dbHandler.addHandler(new Db2Handler()).
                    addHandler(new MysqlHandler()).
                    addHandler(new OracleHandler()).
                    addHandler(new PostgreSqlHandler()).build();


        }


    }


}
