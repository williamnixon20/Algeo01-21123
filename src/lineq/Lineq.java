package lineq;

import java.util.ArrayList;
import java.util.HashMap;

import io.FileTulis;
import matrix.Matrix;
import matrix.expression.Expression;

public class Lineq {

  public void substituteAndDisplaySolution(Matrix m, int writeChoice, FileTulis fileWriter) {
    HashMap<Integer, ArrayList<Expression>> temp = new HashMap<Integer, ArrayList<Expression>>();
    boolean isNoSol = false;

    int rowLength = m.getRowLastIdx(), colLength = m.getColLastIdx(), cntParam = 97;
    for (int i = rowLength; i >= 0; i--) {
      boolean isFound = false;
      int target = i;
      for (int j = 0; j < colLength; j++) {
        if (!isFound && Double.compare(m.getMatrixElement(i, j), 1) == 0) {
          isFound = true;
          target = j;
          ArrayList<Expression> tempArrOfExp = new ArrayList<Expression>();
          Expression tempExp = new Expression(true, m.getMatrixElement(i, colLength), null);
          tempArrOfExp.add(tempExp);
          temp.put(j, tempArrOfExp);
        } else if (m.getMatrixElement(i, j) != 0.000000 && isFound) {
          if (temp.get(j) == null) {
            ArrayList<Expression> tempArrOfExp = new ArrayList<Expression>();
            Expression parametrik = new Expression(false, 1, String.valueOf((char) cntParam));
            Expression parametrikTarget = new Expression(false, (-1) * m.getMatrixElement(i, j),
                String.valueOf((char) cntParam));
            tempArrOfExp.add(parametrik);
            cntParam++;
            temp.get(target).add(parametrikTarget);
            temp.put(j, tempArrOfExp);
          } else {
            for (int k = 0; k < temp.get(j).size(); k++) {
              Expression newExp = new Expression(temp.get(j).get(k).getIsNumber(), temp.get(j).get(k).getNumber(),
                  temp.get(j).get(k).getVar());
              // Expression tempExp = temp.get(j).get(k);
              newExp.multiplyExpression((-1) * m.getMatrixElement(i, j));
              temp.get(target).add(newExp);
            }
          }
        } else if (m.getMatrixElement(i, j) == 0 && j == colLength - 1 && m.getMatrixElement(i, colLength) != 0
            && !isFound) {
          isNoSol = true;
        }
      }
    }

    if (isNoSol){
      System.out.println("SPL tidak memiliki solusi.");
      if (writeChoice == 1) {
        fileWriter.writeFile("SPL tidak memiliki solusi.");
      }
    }
    else {
      String row = "";
      for (int i = 0; i < temp.size(); i++) {
        if (temp.get(i) == null) {
          ArrayList<Expression> newTemp = new ArrayList<Expression>();
          Expression newExp = new Expression(false, 1, String.valueOf((char) cntParam));
          cntParam++;
          newTemp.add(newExp);
          temp.put(i, newTemp);
        }
        for (int j = 0; j < temp.get(i).size(); j++) {
          for (int k = j + 1; k < temp.get(i).size(); k++) {
            if (temp.get(i).get(j).getIsNumber()
                && temp.get(i).get(j).getIsNumber() == temp.get(i).get(k).getIsNumber()) {
              temp.get(i).get(j).setNumber(temp.get(i).get(j).getNumber() + temp.get(i).get(k).getNumber());
              temp.get(i).remove(k);
              k--;
            } else if (!temp.get(i).get(j).getIsNumber()
                && (temp.get(i).get(j).getVar().equals(temp.get(i).get(k).getVar()))) {
              temp.get(i).get(j).setNumber(temp.get(i).get(j).getNumber() + temp.get(i).get(k).getNumber());
              if (Double.compare(temp.get(i).get(j).getNumber(), 0) == 0) {
                temp.get(i).remove(j);
                j--;
                k--;
              }
              temp.get(i).remove(k);
              k--;
            }
          }
        }
      }
      for (int i = 0; i < temp.size(); i++) {
        row += String.format("x%d: ", i + 1);
        for (int j = 0; j < temp.get(i).size(); j++) {
          if (temp.get(i).get(j).getNumber() < 0) {
            row += String.format("- ");
            temp.get(i).get(j).setNumber((-1) * temp.get(i).get(j).getNumber());
          } else if (j > 0 && !(Double.compare(temp.get(i).get(j - 1).getNumber(), 0) == 0 && j == 1)) {
            row += String.format("+ ");
          }
          if ((temp.get(i).get(j).getIsNumber()
              && ((Double.compare(temp.get(i).get(j).getNumber(), 0) == 0 && temp.get(i).size() == 1)
                  || temp.get(i).get(j).getNumber() != 0))
              || (!temp.get(i).get(j).getIsNumber() && !(Double.compare(temp.get(i).get(j).getNumber(), 1) == 0))) {
                row += String.format("%.2f", temp.get(i).get(j).getNumber());
            if (temp.get(i).get(j).getIsNumber())
              row += String.format(" ");
          }
          if (!temp.get(i).get(j).getIsNumber()) {
            row += String.format(temp.get(i).get(j).getVar() + " ");
          }
        }
        row += "\n";
      }
      if (writeChoice == 1) {
        fileWriter.writeFile(row);
      }
      System.out.println(row);
    }
  }
  public void Gauss(Matrix m, int writeChoice, FileTulis fileWriter) {
    m.toREF();
    substituteAndDisplaySolution(m, writeChoice, fileWriter);
  }

  public void GaussJordan(Matrix m, int writeChoice, FileTulis fileWriter) {
    m.toRREF();
    substituteAndDisplaySolution(m, writeChoice, fileWriter);
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
