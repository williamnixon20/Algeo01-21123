package regression;

import java.util.Scanner;
import matrix.Matrix;

public class MultiLinearReg {

    /**
     * Melakukan multiple linear regression untuk mencari estimasi nilai
     */
    public void doMultiLinearReg(Matrix data, Scanner scanner) {
        double[] refData = new double[data.getColLength() - 1];

        System.out.println("\nMasukkan data yang akan diestimasi nilainya: ");
        for (int i = 0; i < refData.length; i++) {
            refData[i] = scanner.nextDouble();
        }

        double estimatedValue = getEstimatedValue(data, refData, scanner);

        System.out.printf("Estimasi nilai: %.2f\n", estimatedValue);
    }

    /**
     * Mendapatkan sigma dari xk * xi
     * 
     * @param data         data yang akan dilakukan regresi
     * @param firstVarIdx  variabel pertama
     * @param secondVarIdx variabel kedua
     * @return sigma dari xk * xi
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
     * @param data    data yang akan dilakukan regresi
     * @param scanner
     * @return Matriks Normal Estimation Equation untuk Multiple Linear Regression
     */
    public Matrix getNEE(Matrix data, Scanner scanner) {

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
     * @param data    data yang akan dilakukan regresi dengan format x1i x2i ... xki
     *                yi
     * @param refData data yang akan dicari estimasi nilainya. Banyak peubah sama
     *                dengan banyak peubah pada data
     * @param scanner
     * @return Estimasi nilai dari refData
     */
    public double getEstimatedValue(Matrix data, double[] refData, Scanner scanner) {
        Matrix NEE;
        NEE = getNEE(data, scanner);

        NEE.toRREF();

        // System.out.println("Normal Estimated Eq: \n");
        // NEE.writeMatrix();

        double estimatedValue = NEE.getMatrixElement(0, NEE.getColLastIdx());
        for (int idx = 1; idx < NEE.getRowLength(); idx++) {
            estimatedValue += refData[idx - 1] * NEE.getMatrixElement(idx, NEE.getColLastIdx());
        }

        return estimatedValue;
    }

}
