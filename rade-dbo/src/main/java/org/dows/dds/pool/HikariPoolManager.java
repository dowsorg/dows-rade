package org.dows.dds.pool;//package org.dows.framework.datasource.pool;
//
//import cn.hutool.json.JSONUtil;
//import com.zaxxer.hikari.HikariConfig;
//import com.zaxxer.hikari.HikariDataSource;
//import lombok.extern.slf4j.Slf4j;
//
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @description: </br>
// * @author: lait.zhang@gmail.com
// * @date: 6/19/2024 11:43 AM
// * @history: </br>
// * <author>      <time>      <version>    <desc>
// * 修改人姓名      修改时间        版本号       描述
// */
//
//
//@Slf4j
//public class HikariPoolManager implements PoolManager {
//
//    //所有数据源的连接池存在map里
//    static ConcurrentHashMap<Long, HikariDataSource> map = new ConcurrentHashMap<>();
//
//    public static HikariDataSource createDatasource(AppDependonEntity appDependonEntity) {
//        if (map.containsKey(appDependonEntity.getAppDependonId())) {
//            return map.get(appDependonEntity.getAppDependonId());
//        } else {
//
//            String configJson = appDependonEntity.getConfigJson();
//            DependOnMysql mysqlDatasource = JSONUtil.toBean(configJson, DependOnMysql.class);
//            HikariConfig hikariConfig = new HikariConfig();
//            hikariConfig.setDriverClassName(mysqlDatasource.getDriver());
//            // executor:mysql://localhost:3306/test
//            hikariConfig.setJdbcUrl(mysqlDatasource.getUrl());
//            hikariConfig.setUsername(mysqlDatasource.getUsername());
//            // todo setting properties
//            // 设置连接池其他属性
//            // 设置最大连接数
//            hikariConfig.setMaximumPoolSize(30);
//            // 设置最小空闲连接数
//            hikariConfig.setMinimumIdle(15);
//            // 设置连接超时时间
//            hikariConfig.setConnectionTimeout(6000);
//            hikariConfig.setInitializationFailTimeout(-1000);
//
//            HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
//            try {
//                // todo 使用密文 解密
//                hikariDataSource.setPassword(mysqlDatasource.getPassword());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            map.put(appDependonEntity.getAppDependonId(), hikariDataSource);
//            log.info("create hikari datasource：{}", mysqlDatasource.getName());
//            return map.get(appDependonEntity.getAppDependonId());
//
//        }
//    }
//
//
//
//    /*public static JdbcClient createJdbcClient(AppDependonEntity appDependonEntity) {
//        if (!map.containsKey(appDependonEntity.getAppDependonId())) {
//            //return map.get(appDependonEntity.getAppDependonId());
//        } else {
//            String configJson = appDependonEntity.getConfigJson();
//            DependOnMysql mysqlDatasource = JSONUtil.toBean(configJson, DependOnMysql.class);
//            HikariConfig hikariConfig = new HikariConfig();
//            hikariConfig.setDriverClassName(mysqlDatasource.getDriver());
//            // executor:mysql://localhost:3306/test
//            hikariConfig.setJdbcUrl(mysqlDatasource.getUrl());
//            hikariConfig.setUsername(mysqlDatasource.getUsername());
//            // todo setting properties
//            // 设置连接池其他属性
//            // 设置最大连接数
//            hikariConfig.setMaximumPoolSize(10);
//            // 设置最小空闲连接数
//            hikariConfig.setMinimumIdle(5);
//            // 设置连接超时时间
//            hikariConfig.setConnectionTimeout(30000);
//            hikariConfig.setInitializationFailTimeout(-1000);
//
//            HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
//            try {
//                // todo 使用密文 解密
//                hikariDataSource.setPassword(mysqlDatasource.getPassword());
//                JdbcClient jdbcClient = JdbcClient.create(hikariDataSource);
//                map.put(appDependonEntity.getAppDependonId(), jdbcClient);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            log.info("create hikari datasource：{}", mysqlDatasource.getName());
//            //return map.get(appDependonEntity.getAppDependonId());
//        }
//    }*/
//
//
//    //删除数据库连接池
//    public static void removeJdbcConnectionPool(Long sequence) {
//        if (map.containsKey(sequence)) {
//            HikariDataSource old = map.get(sequence);
//            map.remove(sequence);
//            old.close();
//        }
//    }
//
//
//    public static Connection getPooledConnection(Long dsDependId) throws SQLException {
//        HikariDataSource pool = map.get(dsDependId);
//        return pool.getConnection();
//    }
//}
