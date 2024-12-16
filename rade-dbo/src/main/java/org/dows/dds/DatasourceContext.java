package org.dows.dds;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 7/2/2024 2:26 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "dows.datasource")
public class DatasourceContext implements Serializable {

    private Map<String, DatasourceProperties> mssql = new HashMap<>();
    private Map<String, DatasourceProperties> mysql = new HashMap<>();

    private final static Map<String, DataSource> dataSourceMap = new HashMap<>();
    private final static Map<String, DatasourceProperties> dataSourcePropertiesMap = new HashMap<>();
    private final static Map<String, JdbcTemplate> jdbcTemplateMap = new HashMap<>();
    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        Map<String, DatasourceProperties> datasource = new HashMap<>();
        datasource.putAll(mssql);
        datasource.putAll(mysql);
        datasource.forEach((k, dataSourceProperties) -> {
            String type = dataSourceProperties.getPool().getType();
            if (dataSourceProperties.isEnable()) {
                if (type.equals("hikari")) {
                    buildHikariDataSource(k, dataSourceProperties);
                }
            }
        });
    }


    public DataSource getDefault() {
        DatasourceProperties datasourceProperties = dataSourcePropertiesMap.values()
                .stream()
                .filter(DatasourceProperties::isDefault)
                .findFirst()
                .orElse(null);
        if (datasourceProperties != null) {
            return dataSourceMap.get(datasourceProperties.getName());
        }
        throw new RuntimeException("not found datasource");
    }

    public DataSource getDataSource(String key) {
        DataSource dataSource = dataSourceMap.get(key);
        if (dataSource == null) {
            throw new RuntimeException("datasource not found");
        }
        return dataSource;
    }


    public JdbcTemplate getJdbcTemplate(String key) {
        JdbcTemplate dataSource = jdbcTemplateMap.get(key);
        if (dataSource == null) {
            throw new RuntimeException("jdbc template not found");
        }
        return dataSource;
    }


    public DatasourceProperties getDatasourceProperties(String key) {
        DatasourceProperties dsp = dataSourcePropertiesMap.get(key);
        if (dsp == null) {
            throw new RuntimeException("datasource properties not found");
        }
        return dsp;
    }


    /**
     * BindResult<HikariConfig> bind = Binder.get(env).bind("dows.task.datasource." + k + ".hikari", HikariConfig.class);
     * //        HikariConfig configuration = bind.get();
     *
     * @param name
     * @param dsp
     */
    private void buildHikariDataSource(String name, DatasourceProperties dsp) {
        PoolSettings pool = dsp.getPool();
        HikariConfig configuration = new HikariConfig();
        BeanUtil.copyProperties(pool, configuration);
        dsp.setName(name);
        Dialect dialect = dsp.getDialect();
        if (!dialect.exists()) {
            throw new RuntimeException("驱动不存在");
        }
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName(dialect.getDriverClass())
                .url(dialect.buildJdbcUrl(dsp))
                .username(dsp.getUsername())
                .password(dsp.getPassword())
                .build();
        String dataSourceType = dsp.getDataSourceType();
        try {
            if (StrUtil.isNotBlank(dataSourceType)) {
                Class aClass = Class.forName(dataSourceType);
                dataSourceBuilder.type(aClass);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        HikariDataSource hikariDataSource = (HikariDataSource) dataSourceBuilder.build();
        hikariDataSource.copyStateTo(configuration);
        dataSourcePropertiesMap.put(name, dsp);
        dataSourceMap.put(name, hikariDataSource);
        jdbcTemplateMap.put(name, new JdbcTemplate(hikariDataSource));
    }

}
