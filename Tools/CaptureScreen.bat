ECHO off
cls
set buildFile=CaptureScreen.build
IF EXIST "C:\Program Files\Nant\bin\nant.exe" SET nant_exe="C:\Program Files\Nant\bin\nant.exe"
IF EXIST "C:\Program Files (x86)\Nant\bin\nant.exe" SET nant_exe="C:\Program Files (x86)\Nant\bin\nant.exe"
set nant=%nant_exe% -buildfile:%buildFile%

set target=capture

%nant% %target%
echo 

pause
