<service>
    <!-- 唯一服务ID -->
    <id>${nginx.serviceName}</id>
    <!-- 显示服务的名称 -->
    <name>Nginx Service</name>
    <!-- 服务描述 -->
    <description>Nginx Web Server</description>
    <!-- 可执行文件的命令 -->
    <executable>${nginx.baseDir}/nginx.exe</executable>
    <!-- 启动参数 -->
    <!-- <startarguments>-p C:/program files/nginx-1.26.2</startarguments> -->
    <!-- 停止可执行文件的命令 -->
    <stopexecutable>${nginx.baseDir}/nginx.exe -s stop</stopexecutable>
    <!-- 停止参数 -->
    <!-- <stoparguments>-p C:\Application\nginx-1.26.2 -s stop</stoparguments> -->
    <!-- 日志路径 -->
    <#--<logpath>${nginx.logDir!"c:/program files/nginx1.26.2/logs"}</logpath>-->
    <logpath>%BASE%\logs</logpath>
    <!-- 日志模式-->
    <log mode="roll-by-size">
        <sizeThreshold>10240</sizeThreshold>
        <keepFiles>8</keepFiles>
    </log>
    <logmode>roll</logmode>
</service>