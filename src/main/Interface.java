package main;

import java.util.Scanner;
import matrix.*;
public class Interface {
    private Scanner scanner; 

    private int mainMenu() {
        int result = 0;

        System.out.println("\nMenu");
        System.out.println("1. Input & Display Matriks");
        System.out.println("2. Keluar\n==============");

        System.out.print("Masukan: ");

        result = this.scanner.nextInt();

        return result;
    }
    public void start(Scanner scanner) {
        this.scanner = scanner;
        System.out.println("Hai! Selamat datang di program matriks HaDeW.");
        
        while (true) {
            int menuChoice = this.mainMenu();
            if (menuChoice == 1) {
                Matrix baru = new Matrix(3,3,scanner);
                baru.readMatrix();
                baru.writeMatrix();
            } else {
                break;
            }
        }

    }
}
