package interpolation;

import java.util.Scanner;

import matrix.Matrix;
import point.Point;

public class Bicubic {
  public double bicubic(Matrix m, Point p, Scanner scanner) {
    Matrix x = new Matrix(16, 16, true, scanner);
    Matrix f = new Matrix(16, 1, true, scanner);
    Matrix a = new Matrix(16, 1, true, scanner);

    /*
     * Bangun matriks f
     */
    int mRowLength = m.getRowLength(), mColLength = m.getColLength(), rowCnt = 0;
    for (int i = 0; i < mRowLength; i++) {
      for (int j = 0; j < mColLength; j++) {
        f.setMatrixElement(rowCnt, 0, m.getMatrixElement(i, j));
        rowCnt++;
      }
    }

    /*
     * Bangun matriks x
     */
    rowCnt = 0;
    for (int i = -1; i <= 2; i++) {
      for (int j = -1; j <= 2; j++) {
        int supI = 0, supJ = 0;
        for (int k = 0; k < 16; k++) {
          double value = (Math.pow(j, supJ) * Math.pow(i, supI));
          x.setMatrixElement(rowCnt, k, (Double.compare(value, -0.0) == 0) ? Math.abs(value) : value);
          supJ++;
          if (supJ == 4) {
            supJ = 0;
            supI++;
          }
        }
        rowCnt++;
      }
    }

    /*
     * Menentukan matriks yang berisi nilai a_ij (vektor a)
     */
    a = x.getInverseUnsafe().multiplyMatrix(f);

    /*
     * Menentukan nilai interpolasi bicubic untuk titik p
     */
    rowCnt = 0;
    double intpResult = 0;
    for (int j = 0; j <= 3; j++) {
      for (int i = 0; i <= 3; i++) {
        double tempRes = (a.getMatrixElement(rowCnt, 0) * Math.pow(p.getAbsis(), i) * Math.pow(p.getOrdinat(), j));
        intpResult += (Double.compare(tempRes, -0.0) == 0 ? Math.abs(tempRes) : tempRes);
        rowCnt++;
      }
    }

    return intpResult;
  }
}
