@echo off
echo [INFO] Compiling all Java files...

set LIB=libs\mysql-connector-j-8.0.33.jar
set SRC=src

javac -cp ".;%LIB%" %SRC%\services\*.java %SRC%\auth\*.java %SRC%\handle\*.java %SRC%\server\*.java

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Compilation failed.
    pause
    exit /b
) else (
    echo [SUCCESS] Compilation completed.
)

pause
