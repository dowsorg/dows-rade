package org.dows.cdc;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dows.dds.DatasourceContext;
import org.dows.dds.DatasourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Slf4j
@Component
@Data
@ConfigurationProperties("dows.cdc")
public class CdcConfiguration {
    @Getter
    private List<CdcJobProperties> jobs;
    @Resource
    private DatasourceContext datasourceContext;

    private static Map<String, String> map = new HashMap<>();
    private static Map<String, CdcJobProperties> cdcJobPropertiesMap = new HashMap<>();

    @PostConstruct
    public void init() {
        //todo
        for (CdcJobProperties job : jobs) {
            map.put(job.getName(), job.getDatasourceName());
            cdcJobPropertiesMap.put(job.getName(), job);
        }
    }


    /**
     * public io.debezium.config.Configuration debeziumConfig(CdcJobProperties cdcJobProperties) {}
     */
    public Properties debeziumConfig(CdcJobProperties cdcJobProperties) {
        DatasourceProperties datasourceProperties =
                datasourceContext.getDatasourceProperties(cdcJobProperties.getDatasourceName());
        /*String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            path = System.getProperty("user.dir");
        } else if (osName.startsWith("Linux")) {
            path = "/opt/";
        }*/
        if (!datasourceProperties.isEnable()) {
            log.info("配置没有开启CDC:{}", JSONUtil.toJsonStr(cdcJobProperties));
            return null;
            //throw new RuntimeException("没有开启对应数据源~");
        }
        String path = System.getProperty("user.dir");
        Properties props = new Properties();

        //连接器的唯一名称
        if (cdcJobProperties.getName() != null) {
            props.put("name", cdcJobProperties.getName());
        }
        //连接器的Java类名称
        if (cdcJobProperties.getConnectorClass() != null) {
            props.put("connector.class", cdcJobProperties.getConnectorClass());
        }
        //偏移量持久化，用来容错 默认值
        props.put("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
        //偏移量持久化文件路径 默认/tmp/offsets.dat  如果路径配置不正确可能导致无法存储偏移量 可能会导致重复消费变更
        //如果连接器重新启动，它将使用最后记录的偏移量来知道它应该恢复读取源信息中的哪个位置。
        if (cdcJobProperties.getOffsetFile() != null) {
            props.put("offset.storage.file.filename", path + File.separator + cdcJobProperties.getOffsetFile());
        }
        //捕获偏移量的周期
        if (cdcJobProperties.getOffsetFile() != null) {
            props.put("offset.flush.interval.ms", cdcJobProperties.getOffsetFile() + "");
        }
        //数据库的hostname
        if (datasourceProperties.getHost() != null) {
            props.put("database.hostname", datasourceProperties.getHost());
        }
        //端口
        if (datasourceProperties.getPort() != null) {
            props.put("database.port", datasourceProperties.getPort() + "");
        }
        //用户名
        if (datasourceProperties.getUsername() != null) {
            props.put("database.user", datasourceProperties.getUsername());
        }
        //密码
        if (datasourceProperties.getPassword() != null) {
            props.put("database.password", datasourceProperties.getPassword());
        }
        //历史变更记录
        //props.put("database.history", "io.debezium.relational.history.FileDatabaseHistory");
        //历史变更记录存储位置，存储DDL:"C:/tmp1/dbhistory.dat"
        //props.put("database.history.file.filename", path + File.separator + cdcJobProperties.getHistoryFile());
        props.put("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory");
        if (cdcJobProperties.getHistoryFile() != null) {
            props.put("schema.history.internal.file.filename", cdcJobProperties.getHistoryFile());
        }
        // 表
        if (cdcJobProperties.getTables() != null) {
            props.put("table.include.list", String.join(",", cdcJobProperties.getTables()));
        }
        //是否包含数据库表结构层面的变更，建议使用默认值true
        //props.put("include.schema.changes", cdcJobProperties.isSchemaChanges());
        if (cdcJobProperties.getTopicPrefix() != null) {
            props.put("topic.prefix", cdcJobProperties.getTopicPrefix());
        }

        if (cdcJobProperties.getDbType().equals("mssql")) {
            // 不加密
            props.put("database.encrypt", cdcJobProperties.isEncrypt() ? "ture" : "false");
            //包含的数据库列表
            if (cdcJobProperties.getDatabases() != null) {
                props.put("database.names", String.join(",", cdcJobProperties.getDatabases()));
            }
        } else if (cdcJobProperties.getDbType().equals("mysql")) {
            //包含的数据库列表
            if (cdcJobProperties.getDatabases() != null) {
                props.put("database.include.list", String.join(",", cdcJobProperties.getDatabases()));
            }
            //mysql.cnf 配置的 server-sequence
            if (cdcJobProperties.getServerId() != null) {
                props.put("database.server.sequence", cdcJobProperties.getServerId());
            }
            props.put("include.schema.changes", cdcJobProperties.isSchemaChanges());
            //MySQL 服务器或集群的逻辑名称
            //props.put("database.server.name", "customer-mysql-db-server");
        }
        // 以debezium配置为主，覆盖
        props.putAll(cdcJobProperties.getProperties());
        return props;
    }


    public CdcConfiguration putTable(String jobName, String... table) {
        Set<String> tables = cdcJobPropertiesMap.get(jobName).getTables();
        tables.addAll(Arrays.asList(table));
        return this;
    }


    public DatasourceProperties getByDatasourceName(String datasourceName) {
        DatasourceProperties datasourceProperties = datasourceContext.getDatasourceProperties(datasourceName);
        if (datasourceProperties == null) {
            throw new RuntimeException("不存在的数据源");
        }
        return datasourceProperties;
    }

    public String getDatasourceNameByJonName(String jobName) {
        String s = map.get(jobName);
        if (s == null) {
            throw new RuntimeException("不存在的数据源");
        }
        return s;
    }
}