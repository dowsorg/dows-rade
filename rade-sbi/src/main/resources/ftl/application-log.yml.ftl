<#if applicationLog??>
dows:
  log:
    cdc: enable
    actLogScan: false
    appLogCollectionNames:
      ${applicationLog.application.name}:
        actlog: shdy:${applicationLog.application.name}:actlog
        binlog: shdy:${applicationLog.application.name}:biglog
</#if>
<#if applicationLog.mysql??>
log:
  binlog:
    hosts:
      - name: ${applicationLog.mysql.databaseName}
        host: ${applicationLog.mysql.host}
        port: ${applicationLog.mysql.port}
        username: ${applicationLog.mysql.username}
        password: ${applicationLog.mysql.password}
        timeOffset: ${applicationLog.mysql.timeOffset!"1000"}
</#if>


