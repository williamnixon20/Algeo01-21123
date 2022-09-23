package menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import io.FileReader;
import lineq.Lineq;
import point.*;
import matrix.*;
import matrix.expression.Expression;

public class Interface {
    private Scanner scanner;

    private int mainMenu() {
        int result = 0;

        System.out.println("\nMenu");
        System.out.println("1. Input dari keyboard & Display Matriks");
        System.out.println("2. Input dari file & Display Matriks");
        System.out.println("3. Input points dari file");
        System.out.println("4. SPL");
        System.out.println("5. Keluar\n==============");

        System.out.print("Masukan: ");

        result = this.scanner.nextInt();

        return result;
    }
    
    private int splMenu() {
        int result = 0;

        System.out.println("\nSilakan pilih metode yang anda inginkan");
        System.out.println("1. Gauss");
        System.out.println("2. Gauss-Jordan");
        System.out.println("3. Cramer");
        System.out.println("4. Invers");
        System.out.println("5. Kembali\n==============");

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
                    int row, col;
                    System.out.print("Masukkan panjang baris: ");
                    row = this.scanner.nextInt();
                    System.out.print("Masukkan panjang kolom: ");
                    col = this.scanner.nextShort();

                    Matrix baru = new Matrix(row, col, true, scanner);
                    baru.readMatrix();
                    baru.writeMatrix();
                    System.out.printf("Determinan matriks: %.2f dan lewat metode segitiga %.2f %.2f\n",
                            baru.getDetWithCofactor(), baru.getDeterminantWithTriangle(true), baru.getDeterminantWithTriangle(false));
                    System.out.println("Matriks inversnya");
                    baru.getInverse().writeMatrix();
                    baru.getInverseWithAdjoin().writeMatrix();
                    System.out.print("Masukkan panjang baris: ");
                    row = this.scanner.nextInt();
                    System.out.print("Masukkan panjang kolom: ");
                    col = this.scanner.nextShort();

                    Matrix baru2 = new Matrix(row, col, true, scanner);
                    baru2.readMatrix();
                    baru.multiplyMatrix(baru2).writeMatrix();
                    // baru.toRREF();
                    // baru.writeMatrix();
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
                    int splChoice = this.splMenu();
                    if (splChoice == 5) break;

                    Matrix m;
                    if (fileReader.setFileName(scanner)) {
                        m = fileReader.readMatrix();
                    } else {
                        System.out.println("File tidak ditemukan.");
                        break;
                    }
                    Lineq leq = new Lineq();
                    switch (splChoice){
                        case 1:
                            leq.Gauss(m);
                            break;
                        case 2:
                        case 3:
                            leq.doCramer(m);
                            break;
                        case 4: 
                            leq.doInverse(m);
                            break;
                    }
                    break;
                case 5:
                    active = false;
                    break;
                default:
                    System.out.println("Opsi tidak tersedia.");
                    break;
            }
        }
    }
}
