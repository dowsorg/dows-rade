package org.dows.ddl.init;

import org.dows.ddl.actuator.ibuilder.*;
import org.dows.ddl.api.ddl.*;
import org.dows.ddl.builder.mysql.*;
import org.dows.framework.ddl.actuator.ibuilder.*;
import org.dows.framework.ddl.api.ddl.*;
import org.dows.ddl.builder.db2.AlterDb2Builder;
import org.dows.ddl.builder.db2.CreateDb2Builder;
import org.dows.ddl.builder.db2.DropDb2Builder;
import org.dows.ddl.builder.db2.TruncateDb2Builder;
import org.dows.framework.ddl.builder.mysql.*;
import org.dows.ddl.builder.oracle.AlterOracleBuilder;
import org.dows.ddl.builder.oracle.CreateOracleBuilder;
import org.dows.ddl.builder.oracle.DropOracleBuilder;
import org.dows.ddl.builder.oracle.TruncateOracleBuilder;
import org.dows.ddl.builder.postgresql.AlterPostgresqlBuilder;
import org.dows.ddl.builder.postgresql.CreatePostgresqlBuilder;
import org.dows.ddl.builder.postgresql.DropPostgresqlBuilder;
import org.dows.ddl.builder.postgresql.TruncatePostgresqlBuilder;
import org.dows.ddl.enums.DbTypeEnum;
import org.dows.ddl.proxy.ProxyMethod;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class FactoryBuilder {

    private static Object getBean(Object current, Class sign) throws InstantiationException, IllegalAccessException {
        for (Class obj : Context.hashMap.get(current)) {
            if (Arrays.stream(obj.getInterfaces()).anyMatch(v -> v.equals(sign))) {
                return Proxy.newProxyInstance(           // return obj.newInstance();
                        obj.getClassLoader(),
                        new Class[]{sign(sign)},
                        new ProxyMethod(obj.newInstance())
                );
            }

        }
        return null;
    }

    public static HashMap<Object, List<Class>> initBuilder() {
        HashMap<Object, List<Class>> hashMap = new LinkedHashMap<>();
        hashMap.put(DbTypeEnum.MYSQL.getDb(), Arrays.asList(
                AlterMysqlBuilder.class, CreateMysqlBuilder.class, SnapshotMysqlBuilder.class, DropMysqlBuilder.class, TruncateMysqlBuilder.class)
        );
        hashMap.put(DbTypeEnum.DB2.getDb(), Arrays.asList(
                AlterDb2Builder.class, CreateDb2Builder.class, SnapshotMysqlBuilder.class, DropDb2Builder.class, TruncateDb2Builder.class)
        );
        hashMap.put(DbTypeEnum.ORAClE.getDb(), Arrays.asList(
                AlterOracleBuilder.class, CreateOracleBuilder.class, SnapshotMysqlBuilder.class, DropOracleBuilder.class, TruncateOracleBuilder.class)
        );
        hashMap.put(DbTypeEnum.POSTGRESQL.getDb(), Arrays.asList(
                AlterPostgresqlBuilder.class, CreatePostgresqlBuilder.class, SnapshotMysqlBuilder.class, DropPostgresqlBuilder.class, TruncatePostgresqlBuilder.class)
        );
        return hashMap;
    }

    private static Class sign(Class sign) {
        Class[] list = {Alter.class, Create.class, Snapshot.class, Drop.class, Truncate.class};
        for (Class c : list) {
            if (sign.getSimpleName().contains(c.getSimpleName())) {
                return c;
            }
        }
        throw new RuntimeException();
    }

    public static class Builder$Alter {
        public static <T> T getBean(Object current) {
            try {
                return (T) FactoryBuilder.getBean(current, AlterBuilder.class);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class Builder$Create {
        public static <T> T getBean(Object current) {
            try {
                return (T) FactoryBuilder.getBean(current, CreateBuilder.class);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class Builder$Snapshot {
        public static <T> T getBean(Object current) {
            try {
                return (T) FactoryBuilder.getBean(current, SnapshotBuilder.class);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class Builder$Drop {
        public static <T> T getBean(Object current) {
            try {
                return (T) FactoryBuilder.getBean(current, DropBuilder.class);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class Builder$Truncate {
        public static <T> T getBean(Object current) {
            try {
                return (T) FactoryBuilder.getBean(current, TruncateBuilder.class);
            } catch (Exception e) {
                return null;
            }
        }
    }
}
