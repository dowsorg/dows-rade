@echo off
title 关闭指定端口对应的进程

REM 判断是否有传入参数
if "%~1"=="" (
    echo 请输入要关闭的端口号，多个端口号之间用空格隔开。
    pause
    exit /b
)

REM 获取当前用户目录路径
for /f "tokens=2 delims==" %%a in ('wmic useraccount get name /value ^| findstr "Name="') do set "user_dir=%USERPROFILE%"

REM 设置输出结果的文件路径到当前用户目录
set "result_file=%user_dir%\port_close_results.txt"

REM 自动清空文件内容
type nul > %result_file%

REM 循环处理传入的每个端口号
:loop
if not "%~1"=="" (
    set port=%~1
    echo 正在处理端口: %port% >> %result_file%

    REM 执行关闭端口对应的进程操作，并将结果输出到文件
    for /f "tokens=1-5" %%i in ('netstat -ano^|findstr ":%port%"') do (
        taskkill /f /pid %%m 2>&1 >> %result_file%
    )

    echo 端口 %port% 处理完成 >> %result_file%
    shift
    goto loop
)