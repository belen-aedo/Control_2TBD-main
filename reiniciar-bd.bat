@echo off
echo ======================================
echo  REINICIANDO BASE DE DATOS
echo ======================================
echo.
echo Este script ejecutara test.sql en PostgreSQL
echo.

set PGPASSWORD=1234

echo Intentando encontrar psql.exe...
echo.

REM Intentar varias rutas comunes de PostgreSQL
if exist "C:\Program Files\PostgreSQL\16\bin\psql.exe" (
    set PSQL_PATH="C:\Program Files\PostgreSQL\16\bin\psql.exe"
) else if exist "C:\Program Files\PostgreSQL\15\bin\psql.exe" (
    set PSQL_PATH="C:\Program Files\PostgreSQL\15\bin\psql.exe"
) else if exist "C:\Program Files\PostgreSQL\14\bin\psql.exe" (
    set PSQL_PATH="C:\Program Files\PostgreSQL\14\bin\psql.exe"
) else if exist "C:\Program Files\PostgreSQL\13\bin\psql.exe" (
    set PSQL_PATH="C:\Program Files\PostgreSQL\13\bin\psql.exe"
) else if exist "C:\Program Files (x86)\PostgreSQL\16\bin\psql.exe" (
    set PSQL_PATH="C:\Program Files (x86)\PostgreSQL\16\bin\psql.exe"
) else (
    echo ERROR: No se pudo encontrar psql.exe
    echo Por favor, ejecuta manualmente el script test.sql en pgAdmin o psql
    echo.
    echo Ubicacion del script: gestion-tareas-backend\src\main\resources\test.sql
    echo Base de datos: gestion_tareas_db
    echo Usuario: postgres
    echo.
    pause
    exit /b 1
)

echo Usando: %PSQL_PATH%
echo.
echo Ejecutando script SQL...
%PSQL_PATH% -U postgres -d gestion_tareas_db -f "gestion-tareas-backend\src\main\resources\test.sql"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ======================================
    echo  BASE DE DATOS REINICIADA CON EXITO
    echo ======================================
    echo.
    echo Usuario de prueba creado:
    echo   Usuario: admin
    echo   Contrasena: 1234
    echo.
) else (
    echo.
    echo ======================================
    echo  ERROR AL REINICIAR BASE DE DATOS
    echo ======================================
    echo.
    echo Verifica que:
    echo 1. PostgreSQL este instalado y corriendo
    echo 2. La base de datos 'gestion_tareas_db' exista
    echo 3. El usuario 'postgres' tenga contrasena '1234'
    echo.
    echo Si PostgreSQL no esta en las rutas comunes,
    echo ejecuta manualmente el script test.sql usando pgAdmin
    echo.
)

pause

