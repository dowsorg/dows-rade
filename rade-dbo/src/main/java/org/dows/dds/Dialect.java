package org.dows.dds;

import cn.hutool.core.util.StrUtil;

/**
 * 数据库 - 驱动和连接示例
 */
public enum Dialect {
    DB2("com.ibm.db2.jcc.DB2Driver", "jdbc:db2://%s:%s/%s"),
    MYSQL("com.mysql.jdbc.Driver", "jdbc:mysql://%s:%s/%s"),
    MARIADB("org.mariadb.jdbc.Driver", "jdbc:mariadb://%s:%s/%s"),
    POSTGRESQL("org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s"),
    SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://%s:%s;databaseName=%s"),
    ORACLE("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:@//%s:%s/%s");
    //    HSQLDB("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:sample"),
    //    MYSQL5(2, "mysql", "MySQL数据库", "com.mysql.jdbc.Driver", "jdbc:mysql://%s:%s/%s", false),
//    MARIADB(3, "mariadb", "MariaDB数据库", "org.mariadb.jdbc.Driver", "jdbc:mariadb://%s:%s/%s", true),
//    SQLSERVER2005(4, "sqlserver2005", "SQLServer2005数据库", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://%s:%s;DatabaseName=%s", true),
//    SQLSERVER2012(5, "sqlserver2012", "SQLServer2012数据库", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://%s:%s;DatabaseName=%s", true),
//    POSTGRESQL(6, "postgresql", "Postgre数据库", "org.postgresql.Driver", "jdbc:postgresql://%s:%s/%s", true),
//    HSQLDB(7, "hsqldb", "HSQL数据库", "org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost", true),
//    DB2(8, "db2", "DB2数据库", "com.ibm.db2.jcc.DB2Driver", "jdbc:db2://%s:%s/%s:progressiveStreaming=2", true),
//    SQLITE(9, "sqlite", "SQLite数据库", "org.sqlite.JDBC", "jdbc:sqlite:/path/db-file-name (MAC以及Linux) jdbc:sqlite://path/db-file-name (Windows)", true),
//    H2(10, "h2", "H2数据库", "org.h2.Driver", "jdbc:h2:db-file-name/%s", true),
//    DM(11, "dm", "达梦数据库", "dm.jdbc.driver.DmDriver", "jdbc:dm://%s:%s/%s", true),
//    XUGU(12, "xugu", "虚谷数据库", "com.xugu.cloudjdbc.Driver", "jdbc:xugu://%s:%s/%s", true),
//    KINGBASE(13, "kingbase", "人大金仓数据库", "com.kingbase8.Driver", "jdbc:kingbase8://%s:%s/%s", true),
//    PHOENIX(14, "phoenix", "Phoenix HBase数据库", "org.apache.phoenix.jdbc.PhoenixDriver", "jdbc:phoenix", true),
//    HIGHGO(15, "highgo", "瀚高数据库", "com.highgo.jdbc.Driver", "jdbc:highgo://%s:%s/%s", true),
    private String clazz;
    private String sample;

    Dialect(String clazz, String sample) {
        this.clazz = clazz;
        this.sample = sample;
    }

    public String getSample() {
        return sample;
    }

    public String getDriverClass() {
        return clazz;
    }

    /**
     * 驱动是否存在
     *
     * @return
     */
    public boolean exists() {
        try {
            Class.forName(clazz);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public String buildJdbcUrl(DatasourceProperties dsp) {
        StringBuilder jdbcUrl = new StringBuilder();
        if (this == SQLSERVER) {
            jdbcUrl.append(String.format(this.getSample(), dsp.getHost(), dsp.getPort(), dsp.getDatabase())).append(";");
            dsp.getProperties().forEach((k, v) -> {
                jdbcUrl.append(k).append("=").append(v).append(";");
            });
        } else if (this == MARIADB || this == MYSQL) {
            String database = dsp.getDatabase();
            if (StrUtil.isBlank(dsp.getDatabase())) {
                database = "";
            }
            jdbcUrl.append(String.format(this.getSample(), dsp.getHost(), dsp.getPort(), database)).append("?");
            dsp.getProperties().forEach((k, v) -> {
                jdbcUrl.append(k).append("=").append(v).append("&");
            });
        }
        return jdbcUrl.deleteCharAt(jdbcUrl.length() - 1).toString();
    }
}
