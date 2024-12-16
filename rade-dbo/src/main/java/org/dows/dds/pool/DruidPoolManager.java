package org.dows.dds.pool;//package org.dows.framework.datasource.pool;
//
//import cn.hutool.json.JSONUtil;
//import com.alibaba.druid.pool.DruidDataSource;
//import com.alibaba.druid.pool.DruidPooledConnection;
//import lombok.extern.slf4j.Slf4j;
//
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
//@Slf4j
//public class DruidPoolManager {
//
//    static ConcurrentHashMap<Long, DruidDataSource> map = new ConcurrentHashMap<>();
//
//    public static DruidDataSource getJdbcConnectionPool(AppDependonEntity appDependonEntity) {
//        if (map.containsKey(appDependonEntity.getAppDependonId())) {
//            return map.get(appDependonEntity.getAppDependonId());
//        } else {
//            String configJson = appDependonEntity.getConfigJson();
//            DependOnMysql mysqlDatasource = JSONUtil.toBean(configJson, DependOnMysql.class);
//
//            DruidDataSource druidDataSource = new DruidDataSource();
//            druidDataSource.setName(mysqlDatasource.getName());
//            druidDataSource.setUrl(mysqlDatasource.getUrl());
//            druidDataSource.setUsername(mysqlDatasource.getUsername());
//            druidDataSource.setDriverClassName(mysqlDatasource.getDriver());
//            druidDataSource.setConnectionErrorRetryAttempts(3);
//            druidDataSource.setBreakAfterAcquireFailure(true);
//
//            try {
//                druidDataSource.setPassword(mysqlDatasource.getPassword());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//
//            map.put(appDependonEntity.getAppDependonId(), druidDataSource);
//            log.info("create druid datasource：{}", mysqlDatasource.getName());
//            return map.get(appDependonEntity.getAppDependonId());
//
//        }
//    }
//
//    //删除数据库连接池
//    public static void removeJdbcConnectionPool(Long sequence) {
//        if (map.containsKey(sequence)) {
//            DruidDataSource old = map.get(sequence);
//            map.remove(sequence);
//            old.close();
//            log.info("remove druid datasource: {}", old.getName());
//        }
//    }
//
//    public static DruidPooledConnection getPooledConnection(AppDependonEntity appDependonEntity) throws SQLException {
//        DruidDataSource pool = DruidPoolManager.getJdbcConnectionPool(appDependonEntity);
//        return pool.getConnection();
//    }
//}
