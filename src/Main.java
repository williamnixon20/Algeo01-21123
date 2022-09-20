import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import menu.Interface;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner scanner = new Scanner(System.in);

        Interface display = new Interface();
        display.start(scanner);

        scanner.close();
    }
}
