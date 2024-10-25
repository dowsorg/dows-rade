# 使用 GraalVM 17 作为基础镜像
FROM ghcr.io/graalvm/graalvm-ce:latest

# 设置容器内的工作目录
WORKDIR /application

# 将可执行的jar文件复制到容器内
COPY rade-app/target/rade-admin-1.0.241001.jar /application/rade-admin-1.0.241001.jar

# 暴露Spring Boot应用程序运行的端口
EXPOSE 8080

# 运行Spring Boot应用程序的命令
ENTRYPOINT ["java", "-jar", "/application/rade-admin-1.0.241001.jar", "--spring.profiles.active=prd"]
