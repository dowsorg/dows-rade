package org.dows.cdc;//package org.dows.framework.cdc;
//
//
//import io.debezium.engine.ChangeEvent;
//import io.debezium.engine.DebeziumEngine;
//import io.debezium.engine.format.Json;
//import jakarta.annotation.PostConstruct;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Properties;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * {
// *     "name": "inventory-connector",
// *     "config": {
// *         "connector.class": "io.debezium.connector.sqlserver.SqlServerConnector",
// *         "database.hostname": "192.168.99.100",
// *         "database.port": "1433",
// *         "database.user": "sa",
// *         "database.password": "Password!",
// *         "database.names": "testDB1,testDB2",
// *         "topic.prefix": "fullfillment",
// *         "table.include.list": "dbo.customers",
// *         "schema.history.internal.kafka.bootstrap.servers": "kafka:9092",
// *         "schema.history.internal.kafka.topic": "schemahistory.fullfillment",
// *         "database.ssl.truststore": "path/to/trust-store",
// *         "database.ssl.truststore.password": "password-for-trust-store"
// *     }
// * }
// */
//@Configuration
//public class DebeziumMssqlConfiguration {
//    @PostConstruct
//    public void buildMssql() {
//        final Properties props = new Properties();
//        props.setProperty("name", "mssql-engine");
//        props.setProperty("connector.class", "io.debezium.connector.sqlserver.SqlServerConnector");
//        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
//        props.setProperty("offset.storage.file.filename", "c:/storage/mssql_offsets.dat");
//        props.setProperty("offset.flush.interval.ms", "60000");
//        /* begin connector properties */
//        props.setProperty("database.encrypt", "false");
//        props.setProperty("database.hostname", "localhost");
//        props.setProperty("database.port", "11433");
//        props.setProperty("database.user", "sa");
//        props.setProperty("database.password", "12345678!");
//        props.setProperty("database.names", "demo");
//        props.setProperty("table.include.list", "dbo.t1");
////        props.setProperty("database.server.sequence", "1");
//        props.setProperty("topic.prefix", "my-app-connector");
//        props.setProperty("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory");
//        props.setProperty("schema.history.internal.file.filename", "c:/storage/mssql_schemahistory.dat");
//
//        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
//                .using(props)
//                .notifying(record -> {
//                    System.out.println(record);
//                }).build();
//        // Run the engine asynchronously ...
//        ExecutorService executor = Executors.newSingleThreadExecutor();
//        executor.execute(engine);
//
//        // Do something else or wait for a signal or an event
//    }
//
//}
