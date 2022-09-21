package menu;

import java.io.IOException;
import java.util.Scanner;

import io.FileReader;
import point.*;
import matrix.*;

public class Interface {
    private Scanner scanner;

    private int mainMenu() {
        int result = 0;

        System.out.println("\nMenu");
        System.out.println("1. Input dari keyboard & Display Matriks");
        System.out.println("2. Input dari file & Display Matriks");
        System.out.println("3. Input points dari file");
        System.out.println("4. Keluar\n==============");

        System.out.print("Masukan: ");

        result = this.scanner.nextInt();

        return result;
    }

    public void start(Scanner scanner) throws IOException {
        this.scanner = scanner;
        System.out.println("Hai! Selamat datang di program matriks HaDeW.");

        boolean active = true;
        while (active) {
            FileReader fileReader = new FileReader();
            int menuChoice = this.mainMenu();
            switch (menuChoice) {
                case 1:
                    Matrix baru = new Matrix(3, 3, true, scanner);
                    baru.readMatrix();
                    baru.writeMatrix();
                    break;
                case 2:
                    if (fileReader.setFileName(scanner)) {
                        Matrix m = fileReader.readMatrix();
                        m.writeMatrix();
                    } else {
                        System.out.println("File tidak ditemukan.");
                    }
                    break;
                case 3:
                    if (fileReader.setFileName(scanner)) {
                        Points p = fileReader.readPoints("ITPS");
                        p.writePoints();
                    } else {
                        System.out.println("File tidak ditemukan.");
                    }
                    break;
                case 4:
                    active = false;
                    break;
                default:
                    System.out.println("Opsi tidak tersedia.");
                    break;
            }
        }
    }
}
