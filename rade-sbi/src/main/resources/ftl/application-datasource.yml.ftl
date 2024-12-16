
dows:
  datasource:
<#if applicationDatasource.mysql??>
    mysql:
      acre-mysql:
        enable: true
        pool:
          type: hikari
          maximum-pool-size: ${applicationDatasource.mysql.maximumPoolSize!"100"}
          minimum-idle: ${applicationDatasource.mysql.minimumIdle!"50"}
          connectionTimeout: ${applicationDatasource.mysql.connectionTimeout!"6000"}
#        driverClassName: org.mariadb.jdbc.Driver
        dialect: MARIADB
        host: ${applicationDatasource.mysql.host}
        port: ${applicationDatasource.mysql.port}
        username: ${applicationDatasource.mysql.username!"root"}
        password: ${applicationDatasource.mysql.password!"shdy123!"}
        database: ${applicationDatasource.mysql.databaseName}
        properties:
          serverTimezone: GMT%2B8
          autoReconnect: true
          allowMultiQueries: true
          useUnicode: true
          characterEncoding: utf8
          zeroDateTimeBehavior: convertToNull
          useSSL: false
</#if>
<#if applicationDatasource.mssql??>
    mssql:
      acre-mssql:
        enable: true
        pool:
          type: hikari
          maximum-pool-size: ${applicationDatasource.mssql.maximumPoolSize!"100"}
          minimum-idle: ${applicationDatasource.mssql.minimumIdle!"50"}
          connectionTimeout: ${applicationDatasource.mssql.connectionTimeout!"6000"}
#        driverClassName: com.microsoft.sqlserver.jdbc.SQLServerDriver
        dialect: SQLSERVER
        host: ${applicationDatasource.mssql.host}
        port: ${applicationDatasource.mssql.port}
        username: ${applicationDatasource.mssql.username!"sa"}
        password: ${applicationDatasource.mssql.password!"shdy123!"}
        database: ${applicationDatasource.mssql.databaseName}
        properties:
          encrypt: true
          trustServerCertificate: true
          characterEncoding: UTF-8
</#if>
