package lineq;

import java.util.HashMap;
import java.lang.Math;

import io.FileTulis;
import matrix.Matrix;
import matrix.expression.ExpressionList;

public class Lineq {
  private static final double EPSILON_IMPRECISION = 0.00001; // impresisi epsilon

  /**
   * Substitusi balik REF / RREF hasil Gauss / Gauss Jordan.
   * Hashmap[1] mengandung expr list untuk x1, hashmap[2] x2, dst.
   * 
   * @param m matriks augmented dari SPL
   * @return solusi dari SPL
   */
  private HashMap<Integer, ExpressionList> getSolution(Matrix m) {
    HashMap<Integer, ExpressionList> columnToExpression = new HashMap<Integer, ExpressionList>();

    int rowLength = m.getRowLastIdx(), colLength = m.getColLastIdx(), cntParam = (int) 'a';
    for (int baris = rowLength; baris >= 0; baris--) {
      /** isFound = telah leading 1 di baris */
      boolean isFound = false;
      int target = baris;

      for (int kolom = 0; kolom < colLength; kolom++) {
        /** Menemukan leading 1 di baris ini, buat List baru untuk kolom ini */
        if (!isFound && Math.abs(m.getMatrixElement(baris, kolom) - 1) < EPSILON_IMPRECISION) {
          isFound = true;
          target = kolom;
          ExpressionList newList = new ExpressionList();
          newList.addExpression(true, m.getMatrixElement(baris, colLength), null);
          columnToExpression.put(kolom, newList);
        }
        /** Menemukan nilai bukan leading 1 di baris ini */
        else if (isFound && Double.compare(m.getMatrixElement(baris, kolom), 0) != 0) {
          /**
           * Nilai tak 0 belum memiliki nilai di hashmap, buat agar menjadi parametrik.
           * masukan -1*m[i][j] ke expression leading 1 (target)
           */
          if (columnToExpression.get(kolom) == null) {
            ExpressionList newList = new ExpressionList();
            newList.addExpression(false, 1, generateParametricVariable(cntParam));
            double multiplier = -1 * m.getMatrixElement(baris, kolom);
            columnToExpression.get(target).addExpression(false, multiplier, generateParametricVariable(cntParam));
            cntParam++;
            columnToExpression.put(kolom, newList);
          } else {
            /**
             * Nilai tak 0 sudah ada di hashmap, kita substitusi.
             */
            ExpressionList ekspresiVariabelLeading1 = columnToExpression.get(target);
            ExpressionList ekspresiVariabelSubstitusi = columnToExpression.get(kolom);
            double multiplier = (-1) * m.getMatrixElement(baris, kolom);
            ekspresiVariabelLeading1.addAndSubstitute(multiplier, ekspresiVariabelSubstitusi);
          }
        } else if (Math.abs(m.getMatrixElement(baris, kolom)) < EPSILON_IMPRECISION && kolom == colLength - 1
            && Math.abs(m.getMatrixElement(baris, colLength) - 0) > EPSILON_IMPRECISION && !isFound) {
          columnToExpression.clear();
          return columnToExpression;
        } else if (!isFound && kolom == colLength - 1) {
          /** Baris 0 semua, jadikan parametrik */
          ExpressionList newList = new ExpressionList();
          newList.addExpression(false, 1, generateParametricVariable(cntParam));
          cntParam++;
          columnToExpression.put(kolom, newList);
        }
      }
    }
    /**
     * Last run, see if any variables are still null. if so, set as parametric
     */
    for (int kolom = 0; kolom < colLength; kolom++) {
      if (columnToExpression.get(kolom) == null) {
        ExpressionList newList = new ExpressionList();
        newList.addExpression(false, 1, generateParametricVariable(cntParam));
        cntParam++;
        columnToExpression.put(kolom, newList);
      }
    }

    return columnToExpression;
  }

  /**
   * Mendisplay solution dari hasil substitusi dengan mengiterasi hashmap
   * 
   * @param expression  solusi dari SPL
   * @param writeChoice Bentuk output. 1 untuk output pada CLI dan 2 untuk
   *                    menuliskan output pada FILE
   * @param fileWriter  FileTulis untuk menuliskan output pada FILE
   */
  public void displaySolution(
      HashMap<Integer, ExpressionList> expression,
      int writeChoice,
      FileTulis fileWriter) {

    if (expression.size() == 0) {
      System.out.println("SPL tidak memiliki solusi.");
      if (writeChoice == 1) {
        fileWriter.writeFile("SPL tidak memiliki solusi.");
      }
    } else {
      String row = "";
      for (HashMap.Entry<Integer, ExpressionList> entry : expression.entrySet()) {
        entry.getValue().simplify();
        row += String.format("x%d : %s\n", (entry.getKey() + 1), entry.getValue().getStringPrint());
      }
      System.out.print(row);
      if (writeChoice == 1) {
        fileWriter.writeFile(row);
      }
    }
  }

  /**
   * Generator variabel parametrik
   * 
   * @param charCount
   * @return
   */
  public String generateParametricVariable(int charCount) {
    int upperBound = (int) 'z';
    int lowerBound = (int) 'a';
    int range = upperBound - lowerBound;
    int decimalValue = charCount;
    String varName = "";
    do {
      int decimal = (decimalValue - lowerBound) % range;
      varName += Character.toString((char) (lowerBound += decimal));
      decimalValue -= decimalValue - lowerBound;
    } while (decimalValue > upperBound);

    return varName;
  }

  /**
   * Mendapatkan solusi dari SPL dengan metode Gauss
   * 
   * @param m matriks augmented dari SPL
   * @return solusi SPL
   */
  public HashMap<Integer, ExpressionList> Gauss(Matrix m) {
    m.toREF();
    return getSolution(m);
  }

  /**
   * Mendapatkan solusi dari SPL dengan metode Gauss-Jordan
   * 
   * @param m matriks augmented dari SPL
   * @return solusi SPL
   */
  public HashMap<Integer, ExpressionList> GaussJordan(Matrix m) {
    m.toRREF();
    return getSolution(m);
  }

  public void doCramer(Matrix m, int writeChoice, FileTulis fileWriter) {
    Matrix matrixA = m.getMatrixAFromAugmented();
    Matrix matrixB = m.getMatrixBFromAugmented();
    double determinantA = matrixA.getDeterminantWithTriangle(true);

    String output = "";
    if (Math.abs(determinantA) < m.EPSILON_IMPRECISION) {
      output += ("Determinan matriks 0 sehingga tidak dapat diperoleh solusinya lewat metode Cramer.\n");
    } else if (!matrixA.isSquare()) {
      output += ("Determinan matriks u/ metode cramer tidak terdefinisi karena bukan persegi.\n");
    } else {
      for (int col = 0; col < matrixA.getColLength(); col++) {
        Matrix substitute = matrixA.substituteCramer(matrixB, col);
        double determinantX = substitute.getDeterminantWithTriangle(true);
        output += String.format("x%d : %.2f\n", col+1, determinantX / determinantA);
      }
    }
    System.out.print(output);
    if (writeChoice == 1) {
      fileWriter.writeFile(output);
    }
  }

  public void doCramerUnsafe(Matrix m, int writeChoice, FileTulis fileWriter) {
    Matrix matrixA = m.getMatrixAFromAugmented();
    Matrix matrixB = m.getMatrixBFromAugmented();
    double determinantA = matrixA.getDeterminantWithTriangle(true);

    String output = "";
    for (int col = 0; col < matrixA.getColLength(); col++) {
      Matrix substitute = matrixA.substituteCramer(matrixB, col);
      double determinantX = substitute.getDeterminantWithTriangle(true);
      output += String.format("x%d : %.2f\n", col+1, determinantX / determinantA);
    }
    System.out.print(output);
    if (writeChoice == 1) {
      fileWriter.writeFile(output);
    }
  }

  public void doInverse(Matrix m, int writeChoice, FileTulis fileWriter) {
    Matrix inverse = m.getMatrixAFromAugmented().getInverse();
    Matrix matrixB = m.getMatrixBFromAugmented();
    String output = "";
    if (inverse.getValidity()) {
      Matrix solution = inverse.multiplyMatrix(matrixB);
      for (int baris = 0; baris < solution.getRowLength(); baris++) {
        output += String.format("x%d : %.2f\n", baris+1, solution.getMatrixElement(baris, 0));
      }
    } else {
      output += ("Matrix tidak punya invers (singular) sehingga tidak bisa diperoleh solusinya lewat metode invers.\n");
    }
    System.out.print(output);
    if (writeChoice == 1) {
      fileWriter.writeFile(output);
    }
  }

  public void doInverseUnsafe(Matrix m, int writeChoice, FileTulis fileWriter) {
    Matrix inverse = m.getMatrixAFromAugmented().getInverseUnsafe();
    Matrix matrixB = m.getMatrixBFromAugmented();
    String output = "";
    Matrix solution = inverse.multiplyMatrix(matrixB);
    for (int baris = 0; baris < solution.getRowLength(); baris++) {
      output += String.format("x%d : %.2f\n", baris+1, solution.getMatrixElement(baris, 0));
    }
    System.out.print(output);
    if (writeChoice == 1) {
      fileWriter.writeFile(output);
    }
  }

}
