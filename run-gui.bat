@echo off
echo ===================================================
echo Starting FitPro Desktop Admin GUI (Java Swing)...
echo ===================================================
cd /d "%~dp0"
call "..\tools\apache-maven-3.9.6\bin\mvn.cmd" compile exec:java
pause
