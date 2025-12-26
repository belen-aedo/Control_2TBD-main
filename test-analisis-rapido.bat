@echo off
echo ========================================
echo   PRUEBA RAPIDA - Analisis View
echo ========================================
echo.

echo [1/3] Verificando archivos modificados...
if exist "gestion-tareas-frontend\src\core\api\axios.js" (
    echo   [OK] axios.js encontrado
) else (
    echo   [ERROR] axios.js no encontrado
)

if exist "gestion-tareas-frontend\src\modules\analisis\views\AnalisisView.vue" (
    echo   [OK] AnalisisView.vue encontrado
) else (
    echo   [ERROR] AnalisisView.vue no encontrado
)
echo.

echo [2/3] Instrucciones de prueba:
echo   1. Asegurate de que el backend este corriendo en http://localhost:8080
echo   2. Asegurate de que el frontend este corriendo en http://localhost:5173
echo   3. Inicia sesion en la aplicacion
echo   4. Navega a la seccion "Analisis"
echo   5. Abre la consola del navegador (F12 ^> Console)
echo.

echo [3/3] Que deberias ver en la consola:
echo   - "Token disponible: Si"
echo   - "Iniciando carga de datos de analisis..."
echo   - "Resultados de peticiones:"
echo   - "Carga de analisis completada"
echo.

echo ========================================
echo   RESULTADO ESPERADO
echo ========================================
echo.
echo [SI] La pagina de analisis carga sin cerrar sesion
echo [SI] Se muestran datos o mensaje "Sin datos disponibles"
echo [NO] NO debe redirigir a /login (a menos que el token sea invalido)
echo.

echo ========================================
echo   CODIGOS HTTP ESPERADOS
echo ========================================
echo.
echo 200 OK      = Datos cargados (NO cierra sesion)
echo 401 Unauth  = Token invalido (SI cierra sesion)
echo 403 Forbid  = Sin permisos (NO cierra sesion)
echo 404 NotFnd  = Sin datos (NO cierra sesion)
echo.

echo Presiona cualquier tecla para ver la documentacion completa...
pause > nul

start "" "SOLUCION-ANALISIS.md"

echo.
echo Documentacion abierta. Revisa SOLUCION-ANALISIS.md para mas detalles.
echo.
pause

