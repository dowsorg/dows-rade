[mysqld]
#prot
port=${my.port!"3306"}

# default timezone
default-time_zone='+8:00'

#datadir
datadir=${my.dataDir}

#character-set
character-set-server=utf8

#poolsize
innodb_buffer_pool_size=${my.bufferPoolSize!"2029M"}

# ignore lowercase
lower_case_table_names=1

<#if my.binlog??>
#binlog config
log_bin=mysql-bin
binlog_format=ROW
</#if>

<#if my.slowlogDir??>
# slow log config
slow_query_log=1
slow_query_log_file=${my.slowlogDir}
long_query_time=0.02
</#if>

<#if my.expireLogsDays??>
# expire_logs_days,auto delete expired binlog files
expire_logs_days = ${my.expireLogsDays!"7"}
</#if>

<#if my.maxConnections??>
# max connections size
max_connections=${my.maxConnectErrors!"500"}
</#if>
<#if my.maxConnectErrors??>
# max connect errors
max_connect_errors=${my.maxConnectErrors!"10"}
</#if>
# character-set-client
#character-set-client-handshake=FALSE

# mysql_native_password plugin
default_authentication_plugin=mysql_native_password

[client]
plugin-dir=${my.pluginDir}
#prot
port=${my.port!"3306"}
