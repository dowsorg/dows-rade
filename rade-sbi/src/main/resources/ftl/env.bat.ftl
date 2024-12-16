@echo off
set regpath=HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\Session Manager\Environment
:: 注意文件换行符是windows系统下的(CR LF),文件编码是ANSI
:: path变量追加这个可以拓展到tomcat,mysql等使用

<#if java??>
set java=${java.dir!"c:/program files/java/jdk-17.0.10"}
</#if>

<#if git??>
set git=${git.dir!"c:/program files/git/2.43.0"}
</#if>

<#if maven??>
set maven=${maven.dir!"c:/program files/maven/apache-maven-3.9.6"}
</#if>

<#if go??>
set go=${go.dir!"c:/program files/go1.19"}
set gopath=${go.gopath!"c:/program files/go1.19/workspace"}
</#if>

<#if node??>
set nvm=E:/workspaces/nvm
set nvm_symlink=E:/workspaces/nvm/nodejs
set npm = E:/workspaces/nvm/nodejs/npm
</#if>

echo.
echo ************************************************************
echo *                                                          *
echo *                   系统环境变量设置                          *
echo *                                                          *
echo ************************************************************
echo.

<#if java?? >
echo === 准备设置环境变量: JAVA_HOME=%java%
echo === 准备设置环境变量: CLASSPATH=.;%%JAVA_HOME%%\lib;%%JAVA_HOME%%\lib\tools.jar;%%JAVA_HOME%%%\lib\dt.jar;
echo === 准备设置环境变量: PATH=%%JAVA_HOME%%\bin;%%JAVA_HOME%%\jre\bin
</#if>
<#if git?? >
echo === 准备设置环境变量: "GIT_HOME" "%git%"
</#if>
<#if mysql?? >
echo === 准备设置环境变量: "MYSQL_HOME" "%mysql%"
</#if>
echo.
set /P EN=请确认后按回车键开始设置环境变量。如果路径设置不正确，请关闭此窗口重新编辑后再执行。
echo.
echo.

<#if java??>
:: setx "JAVA_HOME" "%java%"
setx /M "JAVA_HOME" "%java%" >nul 2>&1 && (echo JAVA_HOME设置成功) || (echo JAVA_HOME设置失败，请检查权限或路径是否正确)
:: setx "CLASSPATH" ".;%%JAVA_HOME%%\lib;%%JAVA_HOME%%\lib\tools.jar;%%JAVA_HOME%%\lib\dt.jar;"
setx /M "CLASSPATH" ".;%%JAVA_HOME%%\lib;%%JAVA_HOME%%\lib\tools.jar;%%JAVA_HOME%%\lib\dt.jar;" >nul 2>&1 && (echo CLASSPATH设置成功) || (echo CLASSPATH设置失败，请检查权限或路径是否正确)
echo === 完成设置环境变量 CLASSPATH=%%JAVA_HOME%%\lib;%%JAVA_HOME%%\lib\tools.jar;%%JAVA_HOME%%\lib\dt.jar;
</#if>

<#if git??>
:: setx "GIT_HOME" "%git%"
setx /M "GIT_HOME" "%git%" >nul 2>&1 && (echo GIT_HOME设置成功) || (echo GIT_HOME设置失败，请检查权限或路径是否正确)
echo === 完成设置环境变量: GIT_HOME=%git%
</#if>

<#if mysql??>
:: setx "MYSQL_HOME" "%mysql%"
setx /M "MYSQL_HOME" "%mysql%" >nul 2>&1 && (echo MYSQL_HOME设置成功) || (echo MYSQL_HOME设置失败，请检查权限或路径是否正确)
echo === 完成设置环境变量: MYSQL_HOME=%mysql%
</#if>

<#if maven??>
:: setx "MAVEN_HOME" "%maven%"
setx /M "MAVEN_HOME" "%maven%" >nul 2>&1 && (echo MAVEN_HOME设置成功) || (echo MAVEN_HOME设置失败，请检查权限或路径是否正确)
echo === 完成设置环境变量: MAVEN_HOME=%maven%
</#if>

<#if go??>
:: setx "GO_HOME" "%go%"
setx /M "GO_HOME" "%go%" >nul 2>&1 && (echo GO_HOME设置成功) || (echo GO_HOME设置失败，请检查权限或路径是否正确)
:: setx "GOPATH" "%gopath%"
setx /M "GOPATH" "%gopath%" >nul 2>&1 && (echo GOPATH设置成功) || (echo GOPATH设置失败，请检查权限或路径是否正确)
echo === 完成设置环境变量: GO_HOME=%go%;"GOPATH" "%gopath%"
</#if>

<#if node??>
:: setx "NVM_HOME" %nvm%
:: setx "NVM_SYMLINK" %nvm_symlink%
:: setx "NPM_HOME" %npm%
setx /M "NVM_HOME" "%nvm%" >nul 2>&1 && (echo NVM_HOME设置成功) || (echo NVM_HOME设置失败，请检查权限或路径是否正确)
setx /M "NVM_SYMLINK" "%nvm_symlink%" >nul 2>&1 && (echo NVM_SYMLINK设置成功) || (echo NVM_SYMLINK设置失败，请检查权限或路径是否正确)
setx /M "NPM_HOME" "%npm%" >nul 2>&1 && (echo NPM_HOME设置成功) || (echo NPM_HOME设置失败，请检查权限或路径是否正确)
echo === 完成设置环境变量: NVM_HOME=%nvm%
</#if>
echo.
echo.

:: 追加PATH环境变量，注意避免重复添加路径
echo === 新追加环境变量(追加到最前面) PATH=%%JAVA_HOME%%\bin;%%JAVA_HOME%%\jre\bin;%%GIT_HOME%%\bin;%%MYSQL_HOME%%\bin;%%MAVEN_HOME%%\bin;%%GO_HOME%%\bin;%%NVM_HOME%%;%%NPM_HOME%%;%%NVM_SYMLINK%%
set "new_path=%%JAVA_HOME%%\bin;%%JAVA_HOME%%\jre\bin;%%GIT_HOME%%\bin;%%MYSQL_HOME%%\bin;%%MAVEN_HOME%%\bin;%%GO_HOME%%\bin;%%NVM_HOME%%;%%NPM_HOME%%;%%NVM_SYMLINK%%;%path%"
setx /M "PATH" "%new_path%" >nul 2>&1 && (echo PATH设置成功) || (echo PATH设置失败，请检查权限或路径是否正确)

echo.
echo === 环境变量设置完成，请按任意键退出!
pause>nul