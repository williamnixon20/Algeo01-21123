package menu;

import java.io.IOException;

import java.util.Scanner;

import bonus.ScaleImage;
import interpolation.Bicubic;
import interpolation.PolinomInterpolation;
import io.FileReader;
import io.FileTulis;
import lineq.Lineq;
import regression.MultiLinearReg;
import point.*;
import matrix.*;

public class Interface {
    private Scanner scanner;
    private FileTulis fileWriter;

    private int mainMenu() {
        int result = 0;

        System.out.println("\nMenu");
        System.out.println("1. SPL");
        System.out.println("2. Regresi Linier Berganda");
        System.out.println("3. Interpolasi Polinom");
        System.out.println("4. Interpolasi Bicubic");
        System.out.println("5. Image Scaling");
        System.out.println("6: Matriks Balikan (Invers)");
        System.out.println("7: Determinan");
        System.out.println("8. Keluar\n==============");

        System.out.print("Masukan: ");

        result = this.scanner.nextInt();

        return result;
    }

    private int detMenu() {
        int result = 0;

        System.out.println("\nIngin mencari determinan dengan metode apa?");
        System.out.println("1. Kofaktor");
        System.out.println("2. Segitiga Atas");
        System.out.println("3. Segitiga Bawah");
        System.out.println("4. Keluar\n==============");

        System.out.print("Masukan: ");

        result = this.scanner.nextInt();

        return result;
    }

    private int inversMenu() {
        int result = 0;

        System.out.println("\nIngin mencari invers dengan metode apa?");
        System.out.println("1. Adjoin");
        System.out.println("2. OBE");
        System.out.println("3. OBE (UNSAFE, determinant menuju 0, matriks TIDAK SINGULAR");
        System.out.println("3. Keluar\n==============");

        System.out.print("Masukan: ");

        result = this.scanner.nextInt();

        return result;
    }

    private int splMenu() {
        int result = 0;

        System.out.println("\nSilakan pilih metode yang anda inginkan");
        System.out.println("1. Gauss");
        System.out.println("2. Gauss-Jordan");
        System.out.println("3. Cramer Safe (RECOMMENDED)");
        System.out.println("4. Cramer UNSAFE (Determinan menuju 0, matriks hilbert, MATRIX MUST NOT BE SINGULAR!)");
        System.out.println("5. Invers Safe (RECOMMENDED)");
        System.out.println("6: Invers UNSAFE (Determinan menuju 0, matriks hilbert, MATRIX MUST NOT BE SINGULAR!)");
        System.out.println("7: Kembali\n==============");

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

        System.out.println("Nama filenya apa (tuliskan juga .txt)? (akan ditulis di /test/output)");
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

            if (menuChoice < 8 && menuChoice != 8 && menuChoice != 5) {
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
                    int splChoice = this.splMenu();
                    if (splChoice == 7)
                        break;
                    Matrix m;
                    int inputChoice2 = inputChoiceMenu();
                    if (inputChoice2 == 1) {
                        System.out.print("Masukkan panjang baris matriks AUGMENTED: ");
                        int row2 = this.scanner.nextInt();
                        System.out.print("Masukkan panjang kolom matriks AUGMENTED: ");
                        int col2 = this.scanner.nextShort();
                        m = new Matrix(row2, col2, true, this.scanner);
                        m.readMatrix();
                    } else {
                        if (fileReader.setFileName(scanner)) {
                            m = fileReader.readMatrix();
                            if (m.getValidity() == false) {
                                break;
                            }
                        } else {
                            System.out.println("File tidak ditemukan.");
                            break;
                        }
                    }
                    Lineq leq = new Lineq();
                    System.out.println();
                    switch (splChoice) {
                        case 1:
                            leq.displaySolution(leq.Gauss(m), writeChoice, fileWriter);
                            break;
                        case 2:
                            leq.displaySolution(leq.GaussJordan(m), writeChoice, fileWriter);
                            break;
                        case 3:
                            leq.doCramer(m, writeChoice, fileWriter);
                            break;
                        case 4:
                            leq.doCramerUnsafe(m, writeChoice, fileWriter);
                            break;
                        case 5:
                            leq.doInverse(m, writeChoice, fileWriter);
                            break;
                        case 6:
                            System.out.println("hi");
                            leq.doInverseUnsafe(m, writeChoice, fileWriter);
                            break;
                    }
                    break;
                case 2:
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
                case 3:
                    inputChoice = inputChoiceMenu();
                    Points p = new Points(1, false, scanner);
                    PolinomInterpolation polinom = new PolinomInterpolation(scanner);
                    Boolean keluar = false;
                    switch (inputChoice) {
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
                    if (keluar)
                        break;
                    polinom.setPoints(p);
                    polinom.setAugmented();
                    polinom.solve(inputChoice, writeChoice, fileWriter);
                    break;
                case 4:
                    inputChoice = inputChoiceMenu();
                    keluar = false;
                    Matrix mBic;
                    Point pBic;
                    Bicubic intpBcb = new Bicubic();
                    double intpBcbRes, xBic, yBic;
                    String output = "";
                    switch (inputChoice) {
                        case 1:
                            // Input matrix dari keyboard
                            int rowBic = 4;
                            int colBic = 4;
                            mBic = new Matrix(rowBic, colBic, true, scanner);
                            mBic.readMatrix();

                            // Input point dari keyboard
                            System.out.println("Rentang nilai x dan y adalah [0,1].");
                            System.out.print("Masukkan nilai x: ");
                            xBic = this.scanner.nextDouble();
                            System.out.print("Masukkan nilai y: ");
                            yBic = this.scanner.nextDouble();
                            pBic = new Point(xBic, yBic);

                            // Hitung interpolasi bicubic dan cetak ke layar
                            intpBcbRes = intpBcb.bicubic(mBic, pBic, scanner);
                            output += String.format("f(%.2f,%.2f) = %.2f\n", xBic, yBic, intpBcbRes);
                            break;
                        case 2:
                            if (fileReader.setFileName(scanner)) {
                                // Bangun matrix dari file
                                mBic = new Matrix(4, 4, true, scanner);
                                mBic = fileReader.readBicubicFromFile().get(0);

                                // Bangun point dari file
                                xBic = fileReader.readBicubicFromFile().get(1).getMatrixElement(0, 0);
                                yBic = fileReader.readBicubicFromFile().get(1).getMatrixElement(0, 1);
                                pBic = new Point(xBic, yBic);

                                // Hitung interpolasi bicubic dan cetak ke layar
                                intpBcbRes = intpBcb.bicubic(mBic, pBic, scanner);
                                output += String.format("f(%.2f,%.2f) = %.2f\n", xBic, yBic, intpBcbRes);
                            } else {
                                output += String.format("File tidak ditemukan.");
                            }
                            break;
                        default:
                            keluar = true;
                    }
                    if (keluar)
                        break;

                    System.out.print(output);
                    if (writeChoice == 1) {
                        fileWriter.writeFile(output);
                    }

                    break;
                case 5:
                    System.out.println(
                            "Rasakan sendiri dahsyatnya perbesaran citra dengan algoritma kami.");
                    System.out.println("====!!====");
                    System.out.println(
                            "Format citra yang diterima hanya .jpg atau .png.\nDisarankan dalam .jpg.\nJika .png memiliki latar transparan, latar akan berubah menjadi putih.");
                    System.out.println("====!!====");
                    System.out.print("Silakan masukkan nama citra di /test/bonus/images-in (Ex. anya.jpg): ");
                    String fileName = scanner.next();
                    ScaleImage scaleImg = new ScaleImage();
                    scaleImg.scaleImage(fileName);
                    break;
                case 6:
                    int inversChoice = inversMenu();
                    inputChoice = inputChoiceMenu();
                    keluar = false;
                    Matrix m2 = new Matrix(0, 0, false, this.scanner);
                    switch (inputChoice) {
                        case 1:
                            System.out.print("Masukkan dimensi matriks persegi (nxn): ");
                            int row2 = this.scanner.nextInt();
                            m2 = new Matrix(row2, row2, true, this.scanner);
                            m2.readMatrix();
                            break;
                        case 2:
                            if (fileReader.setFileName(scanner)) {
                                m2 = fileReader.readMatrix();
                                if (m2.getValidity() == false) {
                                    break;
                                }
                            } else {
                                System.out.println("File tidak ditemukan.");
                                break;
                            }
                            break;
                        default:
                            keluar = true;
                            break;
                    }
                    if (keluar)
                        break;

                    switch (inversChoice) {
                        case 1:
                            m2.getInverseWithAdjoin().writeMatrix(writeChoice, fileWriter);
                            break;
                        case 2:
                            m2.getInverse().writeMatrix(writeChoice, fileWriter);
                            break;
                        case 3:
                            m2.getInverseUnsafe().writeMatrix(writeChoice, fileWriter);
                            break;
                    }
                    break;
                case 7:
                    int detChoice = detMenu();
                    inputChoice = inputChoiceMenu();
                    keluar = false;
                    Matrix m3 = new Matrix(0, 0, false, this.scanner);
                    switch (inputChoice) {
                        case 1:
                            System.out.print("Masukkan dimensi matriks persegi (nxn): ");
                            int row2 = this.scanner.nextInt();
                            m3 = new Matrix(row2, row2, true, this.scanner);
                            m3.readMatrix();
                            break;
                        case 2:
                            if (fileReader.setFileName(scanner)) {
                                m3 = fileReader.readMatrix();
                                if (m3.getValidity() == false) {
                                    break;
                                }
                            } else {
                                System.out.println("File tidak ditemukan.");
                                break;
                            }
                            break;
                        default:
                            keluar = true;
                            break;
                    }
                    if (keluar)
                        break;
                    String row = "Determinan matriks anda adalah: ";
                    switch (detChoice) {
                        case 1:
                            row += m3.getDetWithCofactor();
                            break;
                        case 2:
                            row += m3.getDeterminantWithTriangle(true);
                            break;
                        case 3:
                            row += m3.getDeterminantWithTriangle(false);
                            break;
                    }
                    System.out.println(row);
                    if (writeChoice == 1) {
                        fileWriter.writeFile(row);
                    }
                    break;
                case 8:
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