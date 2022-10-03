Halo! Ini adalah program yang dibuat untuk memenuhi tugas besar aljabar linier.
Berikut merupakan beberapa cara yang dapat anda lakukan untuk menjalankan program.

## How to run
Please be on root directory /TUBESALGEO. Ini sangat penting, karena jika tidak di root directory
maka pembacaan/penulisan file akan kacau.

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

### Having Trouble Running The Program?
Mungkin bisa cek versi Java kalian, karena program dicompile menggunakan versi java terbaru (kami baru download dan belajar Java hehe). Atau mungkin jika lebih berpengalaman, kalian dapat mengcompile dan menjalankan programnya sendiri, berhubung source code telah tersedia.

## Link Github
https://github.com/williamnixon20/Algeo01-21123