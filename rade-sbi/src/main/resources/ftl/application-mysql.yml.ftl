<#if applicationMysql??>
spring:
  datasource:
    driverClassName: org.mariadb.jdbc.Driver
    type: org.mariadb.jdbc.MariaDbDataSource
    #    schema: classpath:form-schema.sql
    #    data: classpath:form-data.sql
    hikari:
      connection-timeout: 60000
      minimum-idle: 5
      maximum-pool-size: 10
      idle-timeout: 300000
      max-lifetime: 1200000
      auto-commit: true
      connection-test-query: SELECT 1
      validation-timeout: 3000
      read-only: false
      login-timeout: 5

    url: jdbc:mariadb://${applicationMysql.host!"192.168.111.103"}:${applicationMysql.port!"13306"}/${applicationMysql.databaseName!"wes_all"}?serverTimezone=GMT%2B8&autoReconnect=true&allowMultiQueries=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false
    username: ${applicationMysql.username!"root"}
    password: ${applicationMysql.password!"shdy123!"}
    name: ${applicationMysql.databaseName!"wes_all"}
</#if>




