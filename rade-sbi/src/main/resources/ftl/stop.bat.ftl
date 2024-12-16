@echo off
>nul 2>&1 "%SYSTEMROOT%\system32\cacls.exe" "%SYSTEMROOT%\system32\config\system"

if '%errorlevel%' NEQ '0' (
goto UACPrompt
) else (goto gotAdmin)

:UACPrompt
echo 设置BatFile=%~f0
(set "BatchPath=%~dp0")
set "BatchFile=%BatchPath%%~n0%~x0"
(
echo Set UAC = CreateObject^("Shell.Application"^)
echo UAC.ShellExecute "%BatchFile%", "", "", "runas", 1
) > "%temp%\getadmin.vbs"
"%temp%\getadmin.vbs"
exit /B

:gotAdmin
if exist "%temp%\getadmin.vbs" (del "%temp%\getadmin.vbs")
pushd "%CD%"
CD /D "%~dp0"

<#if services??>
    <#list services as service>
net stop ${service}
sc delete ${service}
    </#list>
</#if>