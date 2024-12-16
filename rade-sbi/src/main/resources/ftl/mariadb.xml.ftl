<#--<service>
    <!-- 唯一服务ID &ndash;&gt;
    <id>${mariadb.serviceName}</id>
    <!-- 显示服务的名称 &ndash;&gt;
    <name>Mysql Service</name>
    <!-- 服务描述 &ndash;&gt;
    <description>Mysql Server</description>
    <!-- 可执行文件的命令 &ndash;&gt;
    &lt;#&ndash;<executable>${mariadb.baseDir!"c:/program files/nginx1.26.2"}bin/mysql_install_db.exe --basedir=${mariadb.baseDir} --datadir=${mariadb.dataDir} --service=Mysql --password=${mariadb.password!"root"}</executable>&ndash;&gt;
    <executable>${mariadb.baseDir!"c:/program files/nginx1.26.2"}bin/mysql_install_db.exe --datadir=${mariadb.dataDir} --service=Mysql --password=${mariadb.password!"root"}</executable>
    <!-- 启动参数 &ndash;&gt;
    <!-- <startarguments>-p C:/program files/nginx-1.26.2</startarguments> &ndash;&gt;
    <!-- 停止可执行文件的命令 &ndash;&gt;
    <stopexecutable>${mariadb.baseDir!"c:/program files/nginx1.26.2"}nginx.exe -s stop</stopexecutable>
    <!-- 停止参数 &ndash;&gt;
    <!-- <stoparguments>-p C:\Application\nginx-1.26.2 -s stop</stoparguments> &ndash;&gt;
    <!-- 日志路径 &ndash;&gt;
    <logpath>${mariadb.logDir!"c:/program files/nginx1.26.2/logs"}logs</logpath>
    <!-- 日志模式&ndash;&gt;
    <log mode="roll-by-size">
        <sizeThreshold>10240</sizeThreshold>
        <keepFiles>8</keepFiles>
    </log>
    <logmode>roll</logmode>
</service>-->


<service>
    <id>${mariadb.serviceName}</id>
    <name>MariaDB</name>
    <description>MariaDB Server</description>
    <executable>%BASE%\bin\mysqld.exe</executable>
    <arguments>--defaults-file=%BASE%\my.ini</arguments>
    <logpath>%BASE%\logs</logpath>
    <log mode="roll-by-size" size="10240"/>
</service>