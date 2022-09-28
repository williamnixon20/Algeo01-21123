package lineq;

import java.util.HashMap;

import io.FileTulis;
import matrix.Matrix;
import matrix.expression.ExpressionList;

public class Lineq {
  private static final double EPSILON_IMPRECISION = 0.00001;
  
  public HashMap<Integer, ExpressionList> substituteAndDisplaySolution(Matrix m, boolean writeToLayar, int writeChoice, FileTulis fileWriter) {
    HashMap<Integer, ExpressionList> columnToExpression = new HashMap<Integer, ExpressionList>();
    boolean isNoSol = false;

    int rowLength = m.getRowLastIdx(), colLength = m.getColLastIdx(), cntParam = (int)'a';
    for (int baris = rowLength; baris >= 0; baris--) {
      /** isFound = telah leading 1 di baris*/
      boolean isFound = false;
      int target = baris;
      for (int kolom = 0; kolom < colLength; kolom++) {
        /** Menemukan leading 1 di baris ini, buat List baru untuk kolom ini*/
        if (!isFound && Double.compare(m.getMatrixElement(baris, kolom), 1) == 0) {
          isFound = true;
          target = kolom;
          ExpressionList newList = new ExpressionList();
          newList.addExpression(true, m.getMatrixElement(baris, colLength), null);
          columnToExpression.put(kolom, newList);
        } 
        /** Menemukan nilai bukan leading 1 di baris ini */
        else if (isFound && Double.compare(m.getMatrixElement(baris, kolom), 1) != 0) {
          /**
           * Nilai tak 0 belum memiliki nilai di hashmap, buat agar menjadi parametrik. 
           *  masukan -1*m[i][j] ke expression leading 1 (target)
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
        } else if (Math.abs(m.getMatrixElement(baris, kolom)) < EPSILON_IMPRECISION  && kolom == colLength - 1 && Double.compare(m.getMatrixElement(baris, colLength), 0)!= 0 && !isFound) {
          isNoSol = true;
        } else if (!isFound && kolom == colLength-1) {
          /** Baris 0 semua, jadikan parametrik */
            ExpressionList newList = new ExpressionList();
            newList.addExpression(false, 1, generateParametricVariable(cntParam));
            cntParam++;
            columnToExpression.put(kolom, newList);
        }
      }
    }

    if (isNoSol){
      System.out.println("SPL tidak memiliki solusi.");
      if (writeChoice == 1) {
        fileWriter.writeFile("SPL tidak memiliki solusi.");
      }
    } else {
      columnToExpression.entrySet().forEach(entry -> {
        entry.getValue().simplify();
        if (writeToLayar) {
          System.out.printf("x%d : ", (entry.getKey()+1));
          entry.getValue().print();
          System.out.println("");
        }
      });
    }

    return columnToExpression;
  }

  public String generateParametricVariable(int charCount) {
    int upperBound = (int) 'z';
    int lowerBound = (int) 'a';
    int range = upperBound - lowerBound;
    int decimalValue = charCount;
    String varName = "";
    do {
      int decimal = (decimalValue - lowerBound) % range;
      varName += Character.toString((char)(lowerBound += decimal));
      decimalValue -= decimalValue - lowerBound;
    } while( decimalValue > upperBound );

    return varName;
  }

  public HashMap<Integer, ExpressionList> Gauss(Matrix m, int writeChoice, FileTulis fileWriter) {
    m.toREF();
    return substituteAndDisplaySolution(m, true, writeChoice, fileWriter);
  }

  public HashMap<Integer, ExpressionList> GaussJordan(Matrix m, int writeChoice, FileTulis fileWriter) {
    m.toRREF();
    return substituteAndDisplaySolution(m, true, writeChoice, fileWriter);
  }

  public void doCramer(Matrix m) {
    Matrix matrixA = m.getMatrixAFromAugmented();
    Matrix matrixB = m.getMatrixBFromAugmented();
    double determinantA = matrixA.getDetWithCofactor();

    if (Math.abs(determinantA) < m.EPSILON_IMPRECISION) {
      System.out.println("Determinan matriks 0 sehingga tidak dapat diperoleh solusinya lewat metode Cramer.");
    } else if (!matrixA.isSquare()) {
      System.out.println("Determinan matriks u/ metode cramer tidak terdefinisi karena bukan persegi.");
    } else {
      for (int col = 0; col < matrixA.getColLength(); col++) {
        Matrix substitute = matrixA.substituteCramer(matrixB, col);
        double determinantX = substitute.getDetWithCofactor();
        System.out.printf("X%d: %.2f\n", col, determinantX/determinantA);
      }
    }
  }

  public void doInverse(Matrix m) {
    Matrix inverse = m.getMatrixAFromAugmented().getInverse();
    Matrix matrixB = m.getMatrixBFromAugmented();
    if (inverse.getValidity()) {
      Matrix solution = inverse.multiplyMatrix(matrixB);
      solution.displaySolution();      
    } else {
      System.out.println("Matrix tidak punya invers (singular) sehingga tidak bisa diperoleh solusinya lewat metode invers.");
    }
  }
}
