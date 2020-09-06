
@echo off
echo Java version: 
java -version


echo --------------------------
echo Starting poe-controller...
echo --------------------------

java -D"java.library.path"="./poe-controller-files/lib" -Dverbosity=1 -jar poe-controller-0.0.1-SNAPSHOT.jar

if errorlevel 1 set /p DUMMY=Hit ENTER to continue...

