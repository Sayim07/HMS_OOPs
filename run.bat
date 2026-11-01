@echo off
echo ========================================================
echo Compiling Hospital Management System...
echo ========================================================
if not exist bin mkdir bin
javac -d bin src/hospital/*.java src/hospital/model/*.java src/hospital/exception/*.java src/hospital/service/*.java src/hospital/util/*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful! Starting application...
    echo.
    java -cp bin hospital.Main
) else (
    echo [ERROR] Compilation failed.
)
pause

