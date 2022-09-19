package main;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);

        Interface display = new Interface();
        display.start(scanner);

        scanner.close();
    }
}
