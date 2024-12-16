package org.dows.dds;

import lombok.Data;

import java.util.Properties;

/**
 * @description: </br>
 * //jdbc:sqlserver://%s:%s;%s;encrypt=true;trustServerCertificate=true;characterEncoding=UTF-8
 * //String mssqlJdbcUrl = "jdbc:sqlserver://%s:%s;databaseName=%s";
 * //?serverTimezone=GMT%2B8&autoReconnect=true&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false
 * //String mysqlJdbcUrl = "jdbc:mariadb://%s:%s/%s";
 * @author: lait.zhang@gmail.com
 * @date: 7/12/2024 5:45 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class DatasourceProperties {


    private PoolSettings pool;

    private String serverId;
    private String name;

    private boolean enable;
    private boolean isDefault;

    /**
     * Fully qualified name of the connection pool implementation to use. By default, it
     * is auto-detected from the classpath.
     * Class<? extends DataSource> type;
     */
    private String dataSourceType;

    private Dialect dialect;
    /**
     * Fully qualified name of the JDBC driver. Auto-detected based on the URL by default.
     */
//    private String driverClassName;

    /**
     * JDBC URL of the database.
     */
    private String host;

    private Integer port;

    private String database;


    /**
     * Login username of the database.
     */
    private String username;

    /**
     * Login password of the database.
     */
    private String password;

    private boolean forceBigDecimals;

    private boolean useCamelCase;

    /**
     * encrypt: true
     * trustServerCertificate: true
     * characterEncoding: UTF-8
     */
    private Properties properties;

    //    private List<String> tables = new ArrayList<>();
//    public DatasourceProperties addTable(String table) {
//        tables.add(table);
//        return this;
//    }
//
//
//    public DatasourceProperties addTables(List<String> table) {
//        tables.addAll(table);
//        return this;
//    }
}

