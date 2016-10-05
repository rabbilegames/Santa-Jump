ECHO off
cls
set buildFile=PrepareImages.build
IF EXIST "C:\Program Files\Nant\bin\nant.exe" SET nant_exe="C:\Program Files\Nant\bin\nant.exe"
IF EXIST "C:\Program Files (x86)\Nant\bin\nant.exe" SET nant_exe="C:\Program Files (x86)\Nant\bin\nant.exe"
set nant=%nant_exe% -buildfile:%buildFile%


:start

ECHO.
ECHO 1. Prepare Images
ECHO 2. Prepare Launcher Images
ECHO 3. Exit
ECHO .

set choice=
set /p choice=Type your choice:
set target=empty

if '%choice%'=='1' set target=prepareImages
if '%choice%'=='2' set target=prepareLauncherImages
if '%choice%'=='3' goto end

if '%target%'=='empty' goto start

%nant% %target%
echo 
goto start

pause

:end