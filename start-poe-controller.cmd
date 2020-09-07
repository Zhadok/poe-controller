
@echo off
echo Java version: 
java -version


REM Get name of jar file
REM https://stackoverflow.com/questions/6359820/how-to-set-commands-output-as-a-variable-in-a-batch-file
FOR /F "tokens=* USEBACKQ" %%F IN (`dir  /s /b *.jar`) DO ( 
SET jarFileName=%%F
)

echo --------------------------
echo Starting poe-controller...
echo Starting jar file "%jarFileName%"
echo --------------------------

java -D"java.library.path"="./poe-controller-files/lib" -Dverbosity=1 -jar "%jarFileName%"

if errorlevel 1 set /p DUMMY=Hit ENTER to continue...

