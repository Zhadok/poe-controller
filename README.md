# poe-controller
Software for playing Path of Exile with a controller








# Development setup
## IDE setup
Run the following command once if you want to run the application via an IDE. This will unpack the neccessary native files (.dll) from the Maven dependency.  
```
mvn nativedependencies:copy
```
Then, run the class `org.zhadok.poe.controller.App`.


## Packaging and starting the application
Run the following commands to package and start the jar file (remember to insert the correct version): 
```
mvn clean nativedependencies:copy package
java -D"java.library.path"="./poe-controller-files/lib" -jar target/poe-controller-{version}.jar
```

## Command line parameters
You can set a parameter when starting the jar file: 
```
-D"{parameter}"="{value}"
```
For example, you can increase the verbosity by starting the jar file with `-Dverbosity=2`. 

Available parameters: 
- `verbosity` (Default 1): Increase or decrease the logging output. A higher verbosity means more output. 
- `java.library.path`: Required for locating the native files


# License
Apache License Version 2.0

# Open source software and attributions
- [JInput](https://jinput.github.io/jinput/): Copyright 2019 jinput
- Controller icon made by [Good Ware](https://www.flaticon.com/authors/good-ware") from [Flaticon](https://www.flaticon.com/)


