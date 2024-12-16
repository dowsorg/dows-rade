<#if applicationDepends??>
dows:
  depends:
    datasource:
      inventory:
        driverClassName: org.mariadb.jdbc.Driver
        username: ${applicationDepends.username!"root"}
        password: ${applicationDepends.password!"shdy123!"}
       # type: com.zaxxer.hikari.HikariDataSource
        #    schema: classpath:form-schema.sql
        #    data: classpath:form-data.sql
        maximum-pool-size: 100
        minimum-idle: 50
        connectionTimeout: 6000
        jdbc-url: jdbc:mariadb://${applicationDepends.host!"192.168.111.103"}:${applicationDepends.port!"13306"}/${applicationDepends.databaseName!"wes_all"}?serverTimezone=GMT%2B8&autoReconnect=true&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false

      eqpt:
        driverClassName: org.mariadb.jdbc.Driver
        username: ${applicationDepends.username!"root"}
        password: ${applicationDepends.password!"shdy123!"}
        # type: com.zaxxer.hikari.HikariDataSource
        #    schema: classpath:form-schema.sql
        #    data: classpath:form-data.sql
        maximum-pool-size: 100
        minimum-idle: 50
        connectionTimeout: 6000
        jdbc-url: jdbc:mariadb://${applicationDepends.host!"192.168.111.103"}:${applicationDepends.port!"13306"}/${applicationDepends.databaseName!"wes_all"}?serverTimezone=GMT%2B8&autoReconnect=true&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false
</#if>