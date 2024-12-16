package org.dows.cdc;//package org.dows.framework.cdc;
//
//import io.debezium.data.Envelope;
//import io.debezium.embedded.Connect;
//import io.debezium.engine.ChangeEvent;
//import io.debezium.engine.DebeziumEngine;
//import io.debezium.engine.RecordChangeEvent;
//import io.debezium.engine.format.ChangeEventFormat;
//import io.debezium.engine.format.Json;
//import jakarta.annotation.PostConstruct;
//import org.apache.commons.lang3.tuple.Pair;
//import org.apache.kafka.connect.data.Field;
//import org.apache.kafka.connect.data.Struct;
//import org.apache.kafka.connect.source.SourceRecord;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static io.debezium.data.Envelope.FieldName.*;
//import static java.util.stream.Collectors.toMap;
//
//
///**
// * The type Debezium configuration.
// *
// * @author n1
// * @since 2021 /6/1 17:01
// */
////@Configuration
//public class DebeziumMysqlConfiguration {
//
//
////
////    @PostConstruct
////    public void buildMysql() {
////        final Properties props = new Properties();
////        props.setProperty("name", "mysql-engine");
////        props.setProperty("connector.class", "io.debezium.connector.mysql.MySqlConnector");
////        props.setProperty("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore");
////        props.setProperty("offset.storage.file.filename", "c:/storage/offsets.dat");
////        props.setProperty("offset.flush.interval.ms", "60000");
////        /* begin connector properties */
////        props.setProperty("database.hostname", "localhost");
////        props.setProperty("database.port", "13306");
////        props.setProperty("database.user", "root");
////        props.setProperty("database.password", "123456");
////        props.setProperty("database.server.sequence", "1");
////        props.setProperty("topic.prefix", "my-app-connector");
////        props.setProperty("schema.history.internal", "io.debezium.storage.file.history.FileSchemaHistory");
////        props.setProperty("schema.history.internal.file.filename", "c:/storage/schemahistory.dat");
////
////        DebeziumEngine<ChangeEvent<String, String>> engine = DebeziumEngine.create(Json.class)
////                .using(props)
////                .notifying(record -> {
////                    System.out.println(record);
////                }).build();
////        // Run the engine asynchronously ...
////        ExecutorService executor = Executors.newSingleThreadExecutor();
////        executor.execute(engine);
////
////        // Do something else or wait for a signal or an event
////    }
//
//
//    /**
//     * Debezium 配置.
//     *
//     * @return configuration
//     */
////    @Bean
//    io.debezium.config.Configuration debeziumConfig(CdcJobProperties cdcJobProperties) {
//        return io.debezium.config.Configuration.create()
////            连接器的Java类名称
//                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
////            偏移量持久化，用来容错 默认值
//                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
////                偏移量持久化文件路径 默认/tmp/offsets.dat  如果路径配置不正确可能导致无法存储偏移量 可能会导致重复消费变更
////                如果连接器重新启动，它将使用最后记录的偏移量来知道它应该恢复读取源信息中的哪个位置。
//                .with("offset.storage.file.filename", "C:/tmp1/offsets.dat")
////                捕获偏移量的周期
//                .with("offset.flush.interval.ms", "6000")
////               连接器的唯一名称
//                .with("name", "mysql-connector")
////                数据库的hostname
//                .with("database.hostname", "localhost")
////                端口
//                .with("database.port", "13306")
////                用户名
//                .with("database.user", "root")
////                密码
//                .with("database.password", "123456")
////                 包含的数据库列表
////                .with("database.include.list", "coadmin")
////                .with("table.include.list", "coadmin.sys_dict_detail")
//                .with("database.include.list", "coadmin")
//                .with("table.include.list", "coadmin.sys_dict_detail")
////                是否包含数据库表结构层面的变更，建议使用默认值true
//                .with("include.schema.changes", "false")
////                mysql.cnf 配置的 server-sequence
//                .with("database.server.sequence", "1")
////                	MySQL 服务器或集群的逻辑名称
//                .with("database.server.name", "customer-mysql-db-server")
////                历史变更记录
//                .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
////                历史变更记录存储位置，存储DDL
//                .with("database.history.file.filename", "C:/tmp1/dbhistory.dat")
//                .build();
//    }
//
//    /**
//     * Debezium server bootstrap debezium server bootstrap.
//     *
//     * @param cdcJobProperties the configuration
//     * @return the debezium server bootstrap
//     */
//    @Bean
//    DebeziumServerBootstrap debeziumServerBootstrap(CdcJobProperties cdcJobProperties) {
//
//        io.debezium.config.Configuration configuration = debeziumConfig(cdcJobProperties);
//        DebeziumServerBootstrap debeziumServerBootstrap = new DebeziumServerBootstrap();
//        DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
//                .using(configuration.asProperties())
//                .notifying(this::handlePayload)
//                .build();
//
//        debeziumServerBootstrap.setDebeziumEngine(debeziumEngine);
//        return debeziumServerBootstrap;
//    }
//
//
//    private void handlePayload(List<RecordChangeEvent<SourceRecord>> recordChangeEvents, DebeziumEngine.RecordCommitter<RecordChangeEvent<SourceRecord>> recordCommitter) {
//        recordChangeEvents.forEach(r -> {
//            SourceRecord sourceRecord = r.record();
//            Struct sourceRecordChangeValue = (Struct) sourceRecord.value();
//
//            if (sourceRecordChangeValue != null) {
//                // 判断操作的类型 过滤掉读 只处理增删改   这个其实可以在配置中设置
//                Envelope.Operation operation = Envelope.Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));
//
//                if (operation != Envelope.Operation.READ) {
//                    String record = operation == Envelope.Operation.DELETE ? BEFORE : AFTER;
//                    // 获取增删改对应的结构体数据
//                    Struct struct = (Struct) sourceRecordChangeValue.get(record);
//                    // 将变更的行封装为Map
//                    Map<String, Object> payload = struct.schema().fields().stream()
//                            .map(Field::name)
//                            .filter(fieldName -> struct.get(fieldName) != null)
//                            .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
//                            .collect(toMap(Pair::getKey, Pair::getValue));
//                    // 这里简单打印一下
//                    System.out.println("payload = " + payload);
//                }
//            }
//        });
//    }
//
//}
