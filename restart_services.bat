@echo off
echo Stopping all Java and Node processes...
taskkill /F /IM java.exe 2>nul
taskkill /F /IM node.exe 2>nul
timeout /t 3 /nobreak >nul
echo Done stopping processes.
echo.
echo Checking Docker containers...
docker ps --format "table {{.Names}}\t{{.Status}}"
echo.
echo Starting backend services...
