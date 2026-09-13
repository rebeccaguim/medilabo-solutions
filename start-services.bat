@echo off

echo Starting MediLabo services...

start "Patient Service" cmd /k "cd /d %~dp0patient-service && mvn spring-boot:run"

start "Notes Service" cmd /k "cd /d %~dp0notes-service && mvn spring-boot:run"

start "Risk Service" cmd /k "cd /d %~dp0risk-service && mvn spring-boot:run"

start "Gateway Service" cmd /k "cd /d %~dp0gateway-service && mvn spring-boot:run"

start "Front Service" cmd /k "cd /d %~dp0front-service && mvn spring-boot:run"

echo All services are starting...