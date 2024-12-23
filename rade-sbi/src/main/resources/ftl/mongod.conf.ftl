<#if mongod??>
# 存储相关配置
storage:
  # 数据库文件存储路径
  dbPath: ${mongod.dataDir}
  syncPeriodSecs: 60
  # 老板本配置方式，是否启用日志（推荐开启，用于数据恢复）
  #journal:
  #  enabled: true
  # 老版本配置，每个数据库使用独立目录存储数据，可选配置
  #directoryperdb: true

# 系统日志相关配置
systemLog:
  # 日志输出方式，file表示输出到文件，还可以是syslog
  destination: file
  # 日志文件路径
  path: ${mongod.logDir!"./log"}
  # 以追加方式写入日志
  logAppend: true

  #<#if mongod.authorization??>
  #security:
  #authorization: ${mongod.authorization!"enabled"}
  #</#if>

# 网络相关配置
net:
  # 服务监听端口，默认是27017
  port: ${mongod.port!"27017"}
  # 服务绑定的IP地址，127.0.0.1表示仅本地访问，0.0.0.0表示所有IP都可访问（有安全风险）
  bindIp: ${mongod.bindIp!"0.0.0.0"}
</#if>