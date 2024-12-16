<#if applicationMongo??>
spring:
  data:
    mongodb:
      database: ${applicationMongo.databaseName!"acre"}
      host: ${applicationMongo.host!"192.168.111.103"}
      port: ${applicationMongo.port!"17017"}
      username: ${applicationMongo.username!"shdy"}
      password: ${applicationMongo.password!"shdy123!"}
</#if>
