package regression;

import java.util.Scanner;
import java.util.HashMap;

import io.FileTulis;
import matrix.Matrix;
import matrix.expression.ExpressionList;
import lineq.Lineq;

/**
 * Kelas untuk multi variabel linear regression.
 */
public class MultiLinearReg {
    /**
     * Melakukan multiple linear regression untuk mencari estimasi nilai dari
     * keyboard, menerima input banyak sampel yang akan diestimasi dan nilai-nilai
     * yang akan diestimasi.
     * 
     * Menggunakan fungsi getEstimatedValue untuk mencari estimasi nilai dari suatu
     * data
     * 
     * @param data        Data yang akan diregresi dalam bentuk matriks augmented [A
     *                    | b] dengan A merupakan nilai-nilai xk dan b merupakan
     *                    nilai y
     * @param scanner     Scanner untuk matriks dan estimasi nilai
     * @param writeChoice Bentuk output. 1 jika ingin ditulis pada FILE
     * @param fileWriter  FileTulis untuk menuliskan output pada FILE
     */
    public void doMultiLinearRegKey(Matrix data, Scanner scanner, int writeChoice, FileTulis fileWriter) {
        HashMap<Integer, ExpressionList> solution = getSolution(data, scanner);
        writeRegEquation(writeChoice, fileWriter, solution);
        String choice;
        System.out.print("\nApakah Anda ingin melakukan estimasi nilai? [Y/N]: ");
        choice = scanner.next();
        while (!choice.toLowerCase().equals("y") && !choice.toLowerCase().equals("n")) {
            System.out.println("Masukkan tidak valid.");
            System.out.print("\nApakah Anda ingin melakukan estimasi nilai? [Y/N]: ");
            choice = scanner.next();
        }

        if (choice.toLowerCase().equals("y")) {
            double[] refData = new double[data.getColLength() - 1];
            System.out.print("Masukkan jumlah sampel yang ingin diestimasi: ");
            int sample = scanner.nextInt();

            for (int count = 0; count < sample; count++) {
                System.out.printf("\nSampel ke-%d \n\n", count + 1);
                for (int i = 0; i < refData.length; i++) {
                    System.out.printf("Masukkan x%d: ", i + 1);
                    refData[i] = scanner.nextDouble();
                }
                double estimatedValue = getEstimatedValue(solution, refData, scanner);
                writeEstimate(writeChoice, fileWriter, refData, estimatedValue);
            }

        }
    }

    /**
     * Melakukan multiple linear regression untuk mencari estimasi nilai dari data
     * yang dibaca dari File
     * 
     * Menggunakan fungsi getEstimatedValue untuk mencari estimasi nilai dari suatu
     * data
     * 
     * @param data        Data yang akan diregresi dalam bentuk matriks augmented [A
     *                    | b] dengan A merupakan nilai-nilai xk dan b merupakan
     *                    nilai y
     * @param estimates   Data yang akan diestimasi nilainya. Data bisa lebih dari 1
     * @param scanner     Scanner untuk matriks dan estimasi nilai
     * @param writeChoice Bentuk output. 1 jika ingin ditulis pada FILE
     * @param fileWriter  FileTulis untuk menuliskan output pada FILE
     */
    public void doMultiLinearRegFile(Matrix data, Matrix estimates, Scanner scanner, int writeChoice,
            FileTulis fileWriter) {
        HashMap<Integer, ExpressionList> solution = getSolution(data, scanner);
        writeRegEquation(writeChoice, fileWriter, solution);
        for (int i = 0; i < estimates.getRowLength(); i++) {
            double[] refData = new double[estimates.getColLength()];
            for (int j = 0; j < estimates.getColLength(); j++) {
                refData[j] = estimates.getMatrixElement(i, j);
            }
            double estimatedValue = getEstimatedValue(solution, refData, scanner);
            writeEstimate(writeChoice, fileWriter, refData, estimatedValue);
        }
    }

    /**
     * Mendapatkan solusi dari SPL Normal Estimated Equation untuk Regresi Linier
     * Berganda. Menggunakan fungsi getNEE untuk mendapatkan Normal Estimated
     * Equation
     * 
     * @param data    Data yang akan diregresi dalam bentuk matriks augmented [A
     *                | b] dengan A merupakan nilai-nilai xk dan b merupakan
     *                nilai y
     * @param scanner Scanner untuk matriks
     * @return Solusi dari SPL Normal Estimated Equation untuk Regresi Linier
     *         Berganda
     */
    private HashMap<Integer, ExpressionList> getSolution(Matrix data, Scanner scanner) {
        Matrix NEE;
        HashMap<Integer, ExpressionList> solution;
        Lineq lineq = new Lineq();
        NEE = getNEE(data, scanner);

        solution = lineq.GaussJordan(NEE);
        return solution;
    }

    /**
     * Menuliskan persamaan regresi dari solusi yang telah diperoleh
     * 
     * @param writeChoice Bentuk output. 1 jika ingin ditulis pada FILE
     * @param fileWriter  FileTulis untuk menuliskan output pada FILE
     * @param solution    Solusi dari SPL Normal Estimated Equation
     */
    private void writeRegEquation(int writeChoice, FileTulis fileWriter, HashMap<Integer, ExpressionList> solution) {

        String result;

        result = "f(x) = ";

        for (int i = 0; i < solution.size(); i++) {
            solution.get(i).simplify();
            double coeff = solution.get(i).getVariable(0).getNumber();
            if (i == 0) {
                result += String.format("%.4f", coeff);
            } else {
                result += String.format("(%.4f)x%d", coeff, i);
            }

            if (i != solution.size() - 1) {
                result += " + ";
            }
        }

        System.out.println("\nHasil Regresi Linier:");
        System.out.print(result);
        System.out.println();

        if (writeChoice == 1) {
            fileWriter.writeFile(result + "\n");
        }
    }

    /**
     * Menuliskan nilai estimasi sesuai dengan format ke layar/file
     * 
     * @param writeChoice    Bentuk output. 1 jika ingin ditulis pada File
     * @param fileWriter     FileTulis untuk menuliskan output pada FILE
     * @param refData        Data (xk) yang diestimasi nilainya
     * @param estimatedValue Estimasi nilai dari data tersebut
     */
    private void writeEstimate(int writeChoice, FileTulis fileWriter, double[] refData, double estimatedValue) {
        String temp = "Estimasi nilai ";
        for (int i = 0; i < refData.length; i++) {
            temp += String.format("x%d = %.2f", i + 1, refData[i]);

            if (i != refData.length - 1) {
                temp += ", ";
            }
        }

        System.out.println();
        System.out.println(temp);
        System.out.printf("f(xk) = %.4f\n", estimatedValue);

        if (writeChoice == 1) {

            fileWriter.writeFile(temp);
            fileWriter.writeFile(String.format("f(xk) = %.4f\n", estimatedValue));
        }
    }

    /**
     * Mendapatkan sum of product yaitu sigma dari xpi * xqi
     * 
     * @param data         Data yang akan diregresi dalam bentuk matriks augmented
     *                     [A | b] dengan A merupakan nilai-nilai xk dan b merupakan
     *                     nilai y
     * @param firstVarIdx  Variabel pertama p [0..k]. Jika p = 0, maka xpi = 1
     * @param secondVarIdx Variabel kedua q [0..k]. Jika q = 0, maka xqi = 1
     * @return Sigma dari xpi * xqi
     */
    private double getSOP(Matrix data, int firstVarIdx, int secondVarIdx) {
        double sum = 0;
        if (firstVarIdx == 0) {
            if (secondVarIdx == 0) {
                sum = data.getRowLength();
            } else {
                for (int row = 0; row < data.getRowLength(); row++) {
                    sum += data.getMatrixElement(row, secondVarIdx - 1);
                }
            }
        } else {
            if (secondVarIdx == 0) {
                for (int row = 0; row < data.getRowLength(); row++) {
                    sum += data.getMatrixElement(row, firstVarIdx - 1);
                }
            } else {
                for (int row = 0; row < data.getRowLength(); row++) {
                    sum += data.getMatrixElement(row, firstVarIdx - 1) * data.getMatrixElement(row, secondVarIdx - 1);
                }
            }
        }

        return sum;
    }

    /**
     * Mendapatkan Normal Estimation Equation untuk Multiple Linear Regression dari
     * data
     * 
     * @param data    Data yang akan dilakukan regresi
     * @param scanner Scanner untuk matriks
     * @return Matriks Normal Estimation Equation untuk Multiple Linear Regression
     */
    private Matrix getNEE(Matrix data, Scanner scanner) {

        Matrix NEE = new Matrix(data.getColLength(), data.getColLength() + 1, true, scanner);

        for (int firstVarIdx = 0; firstVarIdx < NEE.getRowLength(); firstVarIdx++) {
            for (int secondVarIdx = 0; secondVarIdx < NEE.getColLength(); secondVarIdx++) {
                NEE.setMatrixElement(firstVarIdx, secondVarIdx, getSOP(data, firstVarIdx, secondVarIdx));
            }
        }

        return NEE;
    }

    /**
     * Mendapatkan estimasi nilai dari refData
     * 
     * @param solution Solusi dari multi linear regression yang
     *                 merupakan Matrix eselon baris tereduksi dari
     *                 Normal Estimation Equation
     * @param refData  data yang akan dicari estimasi nilainya. Prekondisi: banyak
     *                 peubah (xk) sama dengan banyak peubah (xk) pada data
     * @param scanner` Scanner untuk matrix
     * @return Estimasi nilai dari refData
     */
    private double getEstimatedValue(HashMap<Integer, ExpressionList> solution, double[] refData, Scanner scanner) {
        double estimatedValue = solution.get(0).getVariable(0).getNumber();
        for (int idx = 1; idx < solution.size(); idx++) {
            estimatedValue += refData[idx - 1] * solution.get(idx).getVariable(0).getNumber();
        }

        return estimatedValue;
    }

}
