package org.dows.cdc;//package org.dows.framework.cdc;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONUtil;
////import com.ververica.cdc.connectors.base.options.StartupOptions;
//import com.ververica.cdc.connectors.mysql.source.MySqlSource;
//import com.ververica.cdc.connectors.mysql.table.StartupOptions;
//import com.ververica.cdc.connectors.shaded.org.apache.kafka.connect.json.DecimalFormat;
//import com.ververica.cdc.connectors.shaded.org.apache.kafka.connect.json.JsonConverterConfig;
////import com.ververica.cdc.connectors.sqlserver.SqlServerSource;
////import com.ververica.cdc.connectors.sqlserver.source.SqlServerSourceBuilder;
//import com.ververica.cdc.debezium.DebeziumSourceFunction;
//import com.ververica.cdc.debezium.JsonDebeziumDeserializationSchema;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.flink.wrapper.common.ExecutionConfig;
//import org.apache.flink.wrapper.common.eventtime.WatermarkStrategy;
//import org.apache.flink.configuration.Configuration;
//import org.apache.flink.configuration.RestOptions;
//import org.apache.flink.streaming.wrapper.CheckpointingMode;
//import org.apache.flink.streaming.wrapper.environment.StreamExecutionEnvironment;
//import org.apache.flink.streaming.wrapper.functions.sink.SinkFunction;
//import org.dows.framework.datasource.DatasourceProperties;
//
//import java.util.*;
//
///**
// * @description: SQL server CDC变更监听器
// * @author: lait.zhang@gmail.com
// * @date: 7/2/2024 5:20 PM
// * @history: </br>
// * <author>      <time>      <version>    <desc>
// * 修改人姓名      修改时间        版本号       描述
// */
////@RequiredArgsConstructor
////@Configuration
////@EnableConfigurationProperties(CdcConfiguration.class)
//@Slf4j
//public class CdcInitializer /*implements ApplicationRunner, Serializable */ {
//
//    public static Map<String, StreamExecutionEnvironment> genExecution(CdcConfiguration cdcConfiguration) throws Exception {
//        Map<String, StreamExecutionEnvironment> streamExecutionEnvironments = new HashMap<>();
//        List<CdcJobProperties> jobs = cdcConfiguration.getJobs();
//        for (CdcJobProperties job : jobs) {
//            if (job.isEnable()) {
//                DatasourceProperties datasourceProperties = cdcConfiguration.getByDatasourceName(job.getDatasourceName());
//                String driverClassName = datasourceProperties.getDriverClassName();
//                if (driverClassName.contains("sqlserver")) {
//                    streamExecutionEnvironments.put(job.getName(), buildMssqlJob(job, datasourceProperties));
//                } else if (driverClassName.contains("maria")) {
//                    streamExecutionEnvironments.put(job.getName(), buildMysqlJob(job, datasourceProperties));
//                }
//            }
//        }
//        return streamExecutionEnvironments;
//    }
//
//
//    private static StreamExecutionEnvironment buildMssqlJob(CdcJobProperties job, DatasourceProperties datasourceProperties) {
//        log.info("开始启动Flink CDC获取{}变更数据......", JSONUtil.toJsonStr(job));
//        //DebeziumSourceFunction<String> source = buildMssqlDataChangeSource(job, datasourceProperties);
//
////        SqlServerSourceBuilder.SqlServerIncrementalSource<String> source = buildMssqlSource(job, datasourceProperties);
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        ExecutionConfig config = env.getConfig();
//        ClassLoader classLoader0 = Thread.currentThread().getContextClassLoader();
//        log.info("===========================class loader:{}", classLoader0);
//        log.info("===========================parent class loader:{}", classLoader0.getParent());
//        log.info("===========================ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();:{}", ClassLoader.getSystemClassLoader());
//
//        config.configure(new Configuration(), ClassLoader.getSystemClassLoader());
//        //StreamExecutionEnvironment env = new StreamExecutionEnvironment(new Configuration(),classLoader);
//
//        // 设置全局并行度
//        env.setParallelism(1);
//        // 设置时间语义为ProcessingTime
//        env.getConfig().setAutoWatermarkInterval(0);
//        // 每隔60s启动一个检查点
//        env.enableCheckpointing(60000, CheckpointingMode.EXACTLY_ONCE);
//        // checkpoint最小间隔
//        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
//        // checkpoint超时时间
//        env.getCheckpointConfig().setCheckpointTimeout(60000);
//        // 同一时间只允许一个checkpoint
//        // env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
//        // Flink处理程序被cancel后，会保留Checkpoint数据
//        //   env.getCheckpointConfig().setExternalizedCheckpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//        //Class<? extends Function> sinkClass = job.getFuncClass();
//        String funcClass = job.getFuncClass();
//        if (StrUtil.isNotBlank(funcClass)) {
//            try {
//                Class<?> aClass = Class.forName(funcClass);
//                Object function = aClass.getDeclaredConstructor().newInstance();
//
////                Class<?> aClass1 = Class.forName("org.apache.flink.wrapper.common.ExecutionConfig");
////                ClassLoader classLoader = aClass1.getClassLoader();
////                log.info("===========current class loader :{}", classLoader);
//                if (function instanceof SinkFunction) {
////                    env.addSource(source, job.getDatasourceName())
////                            .addSink((SinkFunction<String>) function);
////                    env.addSource(source, job.getDatasourceName())
////                            .addSink((SinkFunction<String>) function);
//                    return env;
////                    env.execute(job.getName());
//                } else {
//                    log.info("配置错误");
//                }
//                // web console
//        /*org.apache.flink.configuration.Configuration configuration = new org.apache.flink.configuration.Configuration();
//        configuration.set(RestOptions.PORT, 8081);*/
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        return null;
//    }
//
//
////    private static SqlServerSourceBuilder.SqlServerIncrementalSource<String> buildMssqlSource(CdcJobProperties job, DatasourceProperties datasourceProperties) {
////        Set<String> tableList = job.getTables();
////        if (tableList.isEmpty()) {
////            log.info("表列表为空!");
////            return null;
////        }
////        String[] tables = new String[tableList.size()];
////        tableList.toArray(tables);
////
////        Properties properties = job.getProperties();
////        properties.setProperty("characterEncoding", "UTF-8");
////        properties.setProperty("characterSetResult", "UTF-8");
////        //encrypt=true;trustServerCertificate=true
////        properties.setProperty("encrypt", "true");
////        properties.setProperty("trustServerCertificate", "true");
////        // 自定义格式，可选
////        properties.put("sqlserverDebeziumConverter.format.datetime", "yyyy-MM-dd HH:mm:ss");
////        properties.put("sqlserverDebeziumConverter.format.date", "yyyy-MM-dd");
////        properties.put("sqlserverDebeziumConverter.format.time", "HH:mm:ss");
////
////
////        SqlServerSourceBuilder.SqlServerIncrementalSource<String> build = SqlServerSourceBuilder.SqlServerIncrementalSource.<String>builder()
////                .hostname(datasourceProperties.getHost())
////                .port(datasourceProperties.getPort())
////                .databaseList(datasourceProperties.getDatabase())
////                .username(datasourceProperties.getUsername())
////                .password(datasourceProperties.getPassword())
////                .debeziumProperties(properties)
////                .tableList(tables)
////
////                /*
////                 *initial初始化快照,即全量导入后增量导入(检测更新数据写入)
////                 * latest:只进行增量导入(不读取历史变化)
////                 */
////                .startupOptions(StartupOptions.latest())
////                .deserializer(new JsonDebeziumDeserializationSchema())
////                .build();
////        return build;
////    }
//
//
//    /**
//     * 构造CDC数据源
//     */
////    private static DebeziumSourceFunction<String> buildMssqlDataChangeSource(CdcJobProperties job, DatasourceProperties datasourceProperties) {
////
////        Set<String> tableList = job.getTables();
////        if (tableList.isEmpty()) {
////            log.info("表列表为空!");
////            return null;
////        }
////        String[] tables = new String[tableList.size()];
////        tableList.toArray(tables);
////
////        Properties properties = job.getProperties();
////        properties.setProperty("characterEncoding", "UTF-8");
////        properties.setProperty("characterSetResult", "UTF-8");
////        //encrypt=true;trustServerCertificate=true
////        properties.setProperty("encrypt", "true");
////        properties.setProperty("trustServerCertificate", "true");
////        // 自定义格式，可选
////        properties.put("sqlserverDebeziumConverter.format.datetime", "yyyy-MM-dd HH:mm:ss");
////        properties.put("sqlserverDebeziumConverter.format.date", "yyyy-MM-dd");
////        properties.put("sqlserverDebeziumConverter.format.time", "HH:mm:ss");
////
////        return SqlServerSource.<String>builder()
////                .hostname(datasourceProperties.getHost())
////                .port(datasourceProperties.getPort())
////                .database(datasourceProperties.getDatabase())
////                .username(datasourceProperties.getUsername())
////                .password(datasourceProperties.getPassword())
////                .debeziumProperties(properties)
////                .tableList(tables)
////                //initial初始化快照,即全量导入后增量导入(检测更新数据写入)latest:只进行增量导入(不读取历史变化)
////                .startupOptions(StartupOptions.latest())
////                .deserializer(new JsonDebeziumDeserializationSchema())
////                .build();
////    }
//
//
//    /**
//     * public  void init(DataSourceProperties dataSourceProperties, List<String> tables) throws Exception {
//     *
//     * @param job
//     * @param datasourceProperties
//     */
//    private static StreamExecutionEnvironment buildMysqlJob(CdcJobProperties job, DatasourceProperties datasourceProperties) {
//        log.info("开始启动Flink CDC获取{}变更数据......", JSONUtil.toJsonStr(job));
//        Set<String> tableList = job.getTables();
//        if (tableList.isEmpty()) {
//            log.info("表列表为空!");
//            return null;
//        }
//        String[] tables = new String[tableList.size()];
//        tableList.toArray(tables);
//
//
//        Map<String, Object> configs = new HashMap<>();
//        //声明decimal转换类型
//        configs.put(JsonConverterConfig.DECIMAL_FORMAT_CONFIG, DecimalFormat.NUMERIC.name());
//
//        MySqlSource<String> mySqlSource = MySqlSource.<String>builder()
//                .hostname(datasourceProperties.getHost())
//                .port(datasourceProperties.getPort())
//                .username(datasourceProperties.getUsername())
//                .password(datasourceProperties.getPassword())
//                .databaseList(datasourceProperties.getDatabase())
//                .tableList(tables)
//                .deserializer(new JsonDebeziumDeserializationSchema(false, configs))
//                //.startupOptions(StartupOptions.initial())
//                .serverTimeZone("Asia/Shanghai")
//                .includeSchemaChanges(true)
//                .build();
//        log.info("init data source");
//
//        // web console
//        if (job.getPort() != null) {
//            Configuration configuration = new Configuration();
//            configuration.set(RestOptions.PORT, job.getPort());
//        }
//
//
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        env.enableCheckpointing(5000);
//
////        Class<? extends Function> sinkClass = job.getFuncClass();
////        if (sinkClass != null) {
//        String funcClass = job.getFuncClass();
//        if (StrUtil.isNotBlank(funcClass)) {
//            try {
//                Class<?> aClass = Class.forName(funcClass);
//                Object function = aClass.getDeclaredConstructor().newInstance();
//                if (function instanceof SinkFunction) {
////                    env.addSource(dataChangeInfoMySqlSource, job.getDatasourceName()).addSink((SinkFunction<String>) function);
//                    env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), job.getDatasourceName()).addSink((SinkFunction<String>) function);
//                    //env.execute(job.getName());
//                    return env;
//                } else {
//                    log.info("配置错误");
//                    return null;
//                }
////                Function function = sinkClass.getDeclaredConstructor().newInstance();
////                env.fromSource(mySqlSource, WatermarkStrategy.noWatermarks(), job.getDatasourceName()).addSink((SinkFunction<String>) function);
//                //env.addSource(dataChangeInfoMySqlSource, job.getDatasourceName()).addSink((SinkFunction<String>) function);
////                env.execute(job.getName());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            log.info("没有配置func-class");
//        }
////        log.info("{} job start", JSONUtil.toJsonStr(job));
//        return null;
//    }
//}
//
//
