package menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import io.FileReader;
import io.FileTulis;
import lineq.Lineq;
import regression.MultiLinearReg;
import point.*;
import matrix.*;
import matrix.expression.Expression;

public class Interface {
    private Scanner scanner;
    private FileTulis fileWriter;

    private int mainMenu() {
        int result = 0;

        System.out.println("\nMenu");
        System.out.println("1. Input dari keyboard & Display Matriks");
        System.out.println("2. Input dari file & Display Matriks");
        System.out.println("3. Input points dari file");
        System.out.println("4. SPL");
        System.out.println("5. Regresi Linier Berganda");
        System.out.println("6. Keluar\n==============");

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

    private int writeMenu() {
        int result = 0;

        System.out.println("Apakah anda ingin menuliskan hasil ke file?");
        System.out.println("1. Ya");
        System.out.println("2. Tidak");

        System.out.print("Masukan: ");

        result = this.scanner.nextInt();

        return result;
    }

    private int mlrMenu() {
        int result = 0;

        System.out.println("\nSilahkan pilih metode input data:");
        System.out.println("1. Input dari keyboard");
        System.out.println("2. Input dari file");

        System.out.println("3. Kembali\n==============");

        System.out.print("Masukan: ");

        result = this.scanner.nextInt();

        return result;
    }

    private String writeNameMenu() {
        String result;

        System.out.println("Nama filenya apa (xxxx.txt)? (akan ditulis di /test/output)");
        System.out.print("Masukan: ");

        result = this.scanner.next();

        return result;
    }

    public void start(Scanner scanner) throws IOException {
        this.scanner = scanner;
        System.out.println("Hai! Selamat datang di program matriks HaDeW.");

        boolean active = true;
        while (active) {
            FileReader fileReader = new FileReader();
            int menuChoice = this.mainMenu();
            int writeChoice = 2;
            if (menuChoice != 5) {
                writeChoice = this.writeMenu();
            }
            if (writeChoice == 1 && menuChoice != 5) {
                String fileName = this.writeNameMenu();
                this.fileWriter = new FileTulis(fileName);
            }
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
                            baru.getDetWithCofactor(), baru.getDeterminantWithTriangle(true),
                            baru.getDeterminantWithTriangle(false));
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
                    if (splChoice == 5)
                        break;

                    Matrix m;
                    if (fileReader.setFileName(scanner)) {
                        m = fileReader.readMatrix();
                    } else {
                        System.out.println("File tidak ditemukan.");
                        break;
                    }
                    Lineq leq = new Lineq();
                    switch (splChoice) {
                        case 1:
                            leq.Gauss(m, writeChoice, this.fileWriter);
                            break;
                        case 2:
                            leq.GaussJordan(m, writeChoice, this.fileWriter);
                            break;
                        case 3:
                            leq.doCramer(m);
                            break;
                        case 4:
                            leq.doInverse(m);
                            break;
                    }
                    break;
                case 5:
                    Matrix data;
                    MultiLinearReg mlr = new MultiLinearReg();

                    int mlrChoice = mlrMenu();

                    switch (mlrChoice) {
                        case 1:
                            int varCount, sampleCount;

                            System.out.print("Masukkan banyak variabel: "); // variabel x
                            varCount = scanner.nextInt();
                            System.out.print("Masukkan banyak sample: ");
                            sampleCount = scanner.nextInt();

                            data = new Matrix(sampleCount, varCount + 1, true, scanner);

                            data.readMatrix();

                            mlr.doMultiLinearReg(data, scanner);

                            break;
                        case 2:
                            if (fileReader.setFileName(scanner)) {
                                data = fileReader.readMatrix();
                            } else {
                                System.out.println("File tidak ditemukan.");
                                break;
                            }

                            mlr.doMultiLinearReg(data, scanner);
                            break;
                        case 3:
                            break;
                    }

                    break;
                case 6:
                    active = false;
                    break;
                default:
                    System.out.println("Opsi tidak tersedia.");
                    break;
            }
            if (writeChoice == 1) {
                System.out.println("Output telah tertulis di file.");
                this.fileWriter.closeFile();
            }
        }
    }
}
