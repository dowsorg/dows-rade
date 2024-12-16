dows:
  cdc:
    jobs:
<#if applicationCdc.mssql??>
      - handle-class: org.dows.task.cdc.MssqlMessageHandler
        enable: true
        datasource-name: acre-mssql
        dbType: mssql
        descr: 描述
        properties:
          "connector.class": "io.debezium.connector.sqlserver.SqlServerConnector"
          "name": "MSSQL_ORDER"
          "database.hostname": "${applicationCdc.mssql.host!"127.0.0.1"}"
          "database.port": "${applicationCdc.mssql.port!"1433"}"
          "database.user": "${applicationCdc.mssql.username!"sa"}"
          "database.password": "${applicationCdc.mssql.password!"shdy123!"}"
          "database.names": "${applicationCdc.mssql.database!"sks"}"
          "database.encrypt": "${applicationCdc.mssql.encrypt!"false"}"
          #          "database.ssl.truststore": "path/to/trust-store",
          #          "database.ssl.truststore.password": "password-for-trust-store"
          "topic.prefix": "work-order"
          #          "table.include.list": "dbo.customers",
          "offset.storage.file.filename": "mssql-offset.dat"
          "offset.flush.interval.ms": "1000"
          "event.processing.failure.handling.mode": "warn"
          "snapshot.mode": "when_needed"
        #          "snapshot.mode": "initial"
        #          "schema.history.internal.kafka.bootstrap.servers": "kafka:9092",
        #          "schema.history.internal.kafka.topic": "schemahistory.fullfillment",

        history-file: mssql-history.dat
        tables:
          - dbo.work_order
          - dbo.AllZone_ScanValue
          - dbo.agent_control
          - dbo.zone_info
          - dbo.eqpt_coord
</#if>
<#if applicationCdc.mysql??>
      - name: MYSQL_LOG
        datasource-name: acre-mysql
        enable: true
        descr: 描述
        dbType: mysql
        properties:
          "offset.flush.interval.ms": "${applicationCdc.mysql.interval!"3000"}"
          #          "event.deserialization.failure.handling.mode": "warn"
          #          "event.converting.failure.handling.mode": "warn"
          "event.processing.failure.handling.mode": "warn"
        #        properties:
        #          "connector.class": "io.debezium.connector.mysql.MysqlConnector"
        #          "name": "MYSQL_ORDER"
        #          "database.hostname": "192.168.111.103"
        #          "database.port": "13306"
        #          "database.user": "root"
        #          "database.password": "shdy123!"
        #          "database.names": "wes_all"
        #          "database.encrypt": "false"
        #          "topic.prefix": "wes_binlog"
        #          "offset.storage.file.filename": "mysql-offset.dat"
        #          "offset.flush.interval.ms": "3000"

        topicPrefix: ${applicationCdc.mysql.topicPrefix!"mysql-log"}
        serverId: ${applicationCdc.mysql.serverId!"110003"}
        offsetInterval: "${applicationCdc.mysql.offsetInterval!"3000"}"
        offset-file: mysql-offset.dat
        history-file: mysql-history.dat
        connector-class: io.debezium.connector.mysql.MySqlConnector
        handle-class: org.dows.log.handler.LogSinkHandler
        databases:
          - ${applicationCdc.mysql.databaseName!"wes_all"}
        tables:
          - wes_all.log_column
</#if>
