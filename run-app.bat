@echo off
echo ===================================================
echo Packaging and Starting FitPro Web Application...
echo Accessible at http://localhost:8080/fitpro/
echo ===================================================
cd /d "%~dp0"
call "..\tools\apache-maven-3.9.6\bin\mvn.cmd" package cargo:run
pause
