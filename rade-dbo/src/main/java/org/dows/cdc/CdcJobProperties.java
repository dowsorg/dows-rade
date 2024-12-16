package org.dows.cdc;

import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 7/12/2024 6:11 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */

@Data
public class CdcJobProperties {
    private String dbType;
    private boolean enable;
    private String handleClass;
    private String descr;
    private String datasourceName;

//    private Map<String,String> config;

    private String name;
    private Integer jobWebPort;
    private boolean encrypt;
    private boolean schemaChanges;
    private int offsetInterval;
    private String offsetFile;
    private String historyFile;
    private String topicPrefix;
    private String serverId;
    private String connectorClass;



    private Properties properties = new Properties();

    private Set<String> databases = new HashSet<>();
    private Set<String> tables = new HashSet<>();


    public CdcJobProperties addDatabase(String database) {
        databases.add(database);
        return this;
    }

    public CdcJobProperties addDatabases(List<String> database) {
        databases.addAll(database);
        return this;
    }

    public CdcJobProperties addTable(String table) {
        tables.add(table);
        return this;
    }

    public CdcJobProperties addTables(List<String> table) {
        tables.addAll(table);
        return this;
    }
}

