<service>
    <id>${application.serviceName}</id>
    <name>${application.name!""}</name>
    <description>${application.description!""}</description>
    <env name="JRE_VERSION" value="jre" />
    <env name="JRE_HOME" value="%BASE%\%JRE_VERSION%" />
    <executable>%JAVA_HOME%\bin\java.exe</executable>
    <arguments>-jar %BASE%\bin\${application.name}.jar -Dspring.config.location=%BASE%\configs\ -Dspring.profiles.active=dev</arguments>
    <logpath>%BASE%\logs</logpath>
</service>