package menu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import interpolation.PolinomInterpolation;
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
        System.out.println("6. Interpolasi Polinom");
        System.out.println("7. Keluar\n==============");

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

    private int inputChoiceMenu() {
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
            if (menuChoice != 7) {
                writeChoice = this.writeMenu();
            }
            if (writeChoice == 1) {
                try {
                    String fileName = this.writeNameMenu();
                    this.fileWriter = new FileTulis(fileName);
                } catch (Exception e) {
                    writeChoice = 1;
                }
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
                    // System.out.printf("Determinan matriks: %.2f dan lewat metode segitiga %.2f
                    // %.2f\n",
                    // baru.getDetWithCofactor(), baru.getDeterminantWithTriangle(true),
                    // baru.getDeterminantWithTriangle(false));
                    System.out.println("Matriks inversnya");
                    baru.getInverse().writeMatrix();
                    // baru.getInverseWithAdjoin().writeMatrix();
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
                        Points p = fileReader.readPointsFromFile();
                        p.writePoints();
                        // PolinomInterpolation inter = new PolinomInterpolation(p, scanner);
                        // inter.setMatrix();
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
                            leq.displaySolution(leq.Gauss(m), writeChoice, fileWriter);
                            break;
                        case 2:
                            leq.displaySolution(leq.GaussJordan(m), writeChoice, fileWriter);
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

                    int inputChoice = inputChoiceMenu();

                    switch (inputChoice) {
                        case 1:
                            int varCount, sampleCount;

                            System.out.print("Masukkan banyak variabel: "); // variabel x
                            varCount = scanner.nextInt();
                            System.out.print("Masukkan banyak sample: ");
                            sampleCount = scanner.nextInt();

                            data = new Matrix(sampleCount, varCount + 1, true, scanner);

                            data.readMatrix();

                            mlr.doMultiLinearReg(data, scanner, writeChoice, this.fileWriter);

                            break;
                        case 2:
                            if (fileReader.setFileName(scanner)) {
                                data = fileReader.readMatrix();
                                mlr.doMultiLinearReg(data, scanner, writeChoice, this.fileWriter);
                            } else {
                                System.out.println("File tidak ditemukan.");
                                break;
                            }

                            break;
                        default:
                            break;
                    }

                    break;
                case 6:
                    inputChoice = inputChoiceMenu();
                    Points p = new Points(1, false, scanner);
                    PolinomInterpolation polinom = new PolinomInterpolation(scanner);
                    Boolean keluar = false;
                    switch(inputChoice) {
                        case 1:
                            p = polinom.readPointsKeyboard();
                            break;
                        case 2:
                            if (fileReader.setFileName(scanner)) {
                                p = fileReader.readPointsFromFile();
                            } else {
                                System.out.println("File tidak ditemukan.");
                            }
                            break;
                        default:
                            keluar = true;
                    }
                    if (keluar) break;
                    polinom.setPoints(p);
                    polinom.setAugmented();
                    polinom.solve(inputChoice, writeChoice, fileWriter);
                    break;
                case 7:
                    active = false;
                    break;
                default:
                    System.out.println("Opsi tidak tersedia.");
                    break;
            }
            if (writeChoice == 1) {
                System.out.println("\nOutput telah tertulis di file.");
                this.fileWriter.closeFile();
            }
        }
    }
}