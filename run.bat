@echo off
echo Compiling Java files...
javac -cp "lib/mysql-connector-java-8.0.33.jar" -d build src\Main.java src\handle\*.java

if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b
)

echo Running server...
java -cp "build;lib/mysql-connector-java-8.0.33.jar" Main
pause
