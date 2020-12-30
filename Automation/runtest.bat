echo off
set spath=%~dp0%
cd %spath%
call mvn clean install -DsuiteFile=%1

@echo off
for /f "tokens=3" %%a in ('find/c ">Passed<" ./target/surefire-reports/html/results.csv') do set Pcount=%%a
echo %Pcount%
for /f "tokens=3" %%a in ('find/c ">Failed<" ./target/surefire-reports/html/results.csv') do set Fcount=%%a
echo %Fcount%
for /f "tokens=3" %%a in ('find/c ">Skipped<" ./target/surefire-reports/html/results.csv') do set Scount=%%a
echo %Scount%
set sStatus="Failed"
IF %Fcount% == 0 set sStatus="Passed"
for /f "delims=" %%i in (./build.txt) do set buildno=%%i
set /a iTCnt=%Pcount%+%Fcount%+%Scount%
echo %iTCnt%
echo y | "./utilities/plink.exe" -ssh <ipaddress> -l <username> -pw <password> exit
call "./utilities/pscp.exe" -batch -pw l00nie ./target/surefire-reports/html/results.csv <username>@<ipaddress>:<path>.
call "./utilities/plink.exe" <username>@<ipaddress> -pw <password> <path of .sh file> '%sStatus% - [Build - %buildno%] - Automation result [Total-%iTCnt%, Passed-%Pcount%, Failed-%Fcount%, Skipped-%Scount%]' results.csv <emailaddress> '-'

