## How to run
Please be on root directory /TUBESALGEO !
### VSCODE
1. Go to src/main/Main.java
2. Click run (top right) if you are in VSCode. It's already setup so that it will automatically build (make files in bin) and run.
3. See https://www.youtube.com/watch?v=1vY5eNw5OdE (15:00 - end)

### NOT VSCODE (Not Using Jar)
1. Go to your root directory. Usually this will be ./TUBESALGEO1 (Not /src or /test or /bin)
2. Execute main program by typing "java -cp bin Main" to run the compiled class.
3. (NOT RECOMMENDED) compile by using the command "javac -d bin -sourcepath src src/Main.java"

### I wanna delete all src and just use the .jar at lib!
Ok, be at root folder and do this.
1. javac -cp lib/TubesAlgeo.jar -sourcepath src src/Main.java -d bin
2. java -cp "lib/TubesAlgeo.jar;bin*" Main

### I wanna execute jar as an executable
1. Ok, be at root directory and type java -jar TubesAlgeo.jar