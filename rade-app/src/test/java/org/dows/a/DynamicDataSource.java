//package org.dows.app.config.a;
//
//import cn.hutool.extra.spring.SpringUtil;
//import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
//
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class DynamicDataSource extends AbstractRoutingDataSource {
//    /**
//     * 存储当前线程的数据源key
//     */
//    private static final ThreadLocal<String> DATA_SOURCE_KEY = ThreadLocal.withInitial(() -> "default");
//
//    /**
//     * 数据源map
//     */
//    public static Map<Object, Object> dataSourceMap = new ConcurrentHashMap<>(1000);
//
//    /**
//     * 获取数据源key
//     *
//     * @return
//     */
//    public static String getDataSourceKey() {
//        return DynamicDataSource.DATA_SOURCE_KEY.get();
//    }
//
//    /**
//     * 设置数据源key
//     *
//     * @param key
//     */
//    public static void setDataSourceKey(String key) {
//        DynamicDataSource.DATA_SOURCE_KEY.set(key);
//    }
//
//    /**
//     * 移除默认数据源key
//     */
//    public static void remove() {
//        DynamicDataSource.DATA_SOURCE_KEY.remove();
//    }
//
//    /**
//     * 切换成默认的数据源
//     */
//    public static void setDefaultDataSource() {
//        setDataSource("default");
//    }
//
//    /**
//     * 切换成指定数据源 前提是dataSourceMap中有该key
//     * 外层调用时需要判断下map是否有，可靠性交给外层维护
//     *
//     * @param dataSource
//     */
//    public static void setDataSource(String dataSource) {
//        setDataSourceKey(dataSource);
//        // InitializingBean.afterPropertiesSet()是，实例化后，bean的所有属性初始化后调用；但是如果该bean是直接从容器中拿的，并不需要实例化动作
//        // 这里直接拿到dataSource，手动触发一下，让AbstractRoutingDataSource.resolvedDataSources重新赋值，取到本类维护的map的值
//        DynamicDataSource dynamicDataSource = SpringUtil.getBean("dataSource");
//        dynamicDataSource.afterPropertiesSet();
//    }
//
//    /**
//     * 获取租户数据源配置
//     *
//     * @param tenantCode
//     * @return
//     */
//    public static Object getDataSourceMap(String tenantCode) {
//        return DynamicDataSource.dataSourceMap.get(tenantCode);
//    }
//
//    /**
//     * 设置map
//     *
//     * @param dataSourceName
//     * @return void
//     **/
//    public static void setDataSourceMap(String dataSourceName, Object dataSource) {
//        dataSourceMap.put(dataSourceName, dataSource);
//    }
//
//    /**
//     * 设置map
//     *
//     * @param dataSourceName
//     * @return void
//     **/
//    public static void setDataSourceMap(String dataSourceName) {
//        dataSourceMap.put(dataSourceName, SpringUtil.getBean(dataSourceName));
//    }
//
//    /**
//     * 设置租户数据源配置
//     *
//     * @param rdsConfig
//     * @return
//     */
//    public static void setDataSourceMap(RdsConfig rdsConfig) {
//        DynamicDataSource.dataSourceMap.put(rdsConfig.getTenantCode(), getDruidDataSource(rdsConfig));
//    }
//
//    /**
//     * 获取DruidDataSource
//     *
//     * @param rdsConfig
//     * @return
//     */
//    private static DruidDataSource getDruidDataSource(RdsConfig rdsConfig) {
//        DruidDataSource druidDataSource = new DruidDataSource();
//        druidDataSource.setUrl("jdbc:mysql://" + rdsConfig.getDbUrl() + ":" + rdsConfig.getDbPort() + "/" + rdsConfig.getDbName() + "?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=true&autoReconnect=true&serverTimezone=Asia/Shanghai");
//        druidDataSource.setUsername(rdsConfig.getDbAccount());
//        druidDataSource.setPassword(rdsConfig.getDbPassword());
//        return druidDataSource;
//    }
//
//    /**
//     * 重写determineCurrentLookupKey方法
//     *
//     * @return java.lang.Object
//     **/
//    @Override
//    protected Object determineCurrentLookupKey() {
//        return getDataSourceKey();
//    }
//
//}
