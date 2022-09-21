package menu;

import java.io.IOException;
import java.util.Scanner;

import io.FileReader;
import matrix.*;

public class Interface {
    private Scanner scanner;

    private int mainMenu() {
        int result = 0;

        System.out.println("\nMenu");
        System.out.println("1. Input dari keyboard & Display Matriks");
        System.out.println("2. Input dari file & Display Matriks");
        System.out.println("3. Keluar\n==============");

        System.out.print("Masukan: ");

        result = this.scanner.nextInt();

        return result;
    }

    public void start(Scanner scanner) throws IOException {
        this.scanner = scanner;
        System.out.println("Hai! Selamat datang di program matriks HaDeW.");

        boolean active = true;
        while (active) {
            int menuChoice = this.mainMenu();
            switch (menuChoice) {
                case 1:
                    int row, col;
                    System.out.print("Masukkan panjang baris: ");
                    row = this.scanner.nextInt();
                    System.out.print("Masukkan panjang kolom: ");
                    col = this.scanner.nextShort();

                    Matrix baru = new Matrix(row, col, true, scanner);
                    baru.readMatrix();
                    baru.writeMatrix();
                    System.out.printf("Determinan matriks: %.2f\n", baru.getDetWithCofactor());
                    System.out.println("Matriks dalam bentuk baris eselon tereduksi:");
                    baru.toRREF();
                    baru.writeMatrix();
                    break;
                case 2:
                    FileReader fileReader = new FileReader();
                    if (fileReader.setFileName(scanner)) {
                        Matrix m = fileReader.readMatrix();
                        m.writeMatrix();
                    } else {
                        System.out.println("File tidak ditemukan.");
                    }
                    break;
                default:
                    active = false;
                    break;
            }
        }
    }
}
