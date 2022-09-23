package lineq;

import java.util.ArrayList;
import java.util.HashMap;

import matrix.Matrix;
import matrix.expression.Expression;

public class Lineq {

  /**
   * Gauss*
   */
  public void Gauss(Matrix m) {
    HashMap<Integer, ArrayList<Expression>> temp = new HashMap<Integer, ArrayList<Expression>>();
    boolean isNoSol = false;

    m.toREF();

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

    if (isNoSol)
      System.out.println("SPL tidak memiliki solusi.");
    else {
      for (int i = 0; i < temp.size(); i++) {
        System.out.printf("%d\n", i);
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
        System.out.printf("x%d: ", i + 1);
        for (int j = 0; j < temp.get(i).size(); j++) {
          if (temp.get(i).get(j).getNumber() < 0) {
            System.out.print("- ");
            temp.get(i).get(j).setNumber((-1) * temp.get(i).get(j).getNumber());
          } else if (j > 0 && !(Double.compare(temp.get(i).get(j - 1).getNumber(), 0) == 0 && j == 1)) {
            System.out.print("+ ");
          }
          if ((temp.get(i).get(j).getIsNumber()
              && ((Double.compare(temp.get(i).get(j).getNumber(), 0) == 0 && temp.get(i).size() == 1)
                  || temp.get(i).get(j).getNumber() != 0))
              || (!temp.get(i).get(j).getIsNumber() && !(Double.compare(temp.get(i).get(j).getNumber(), 1) == 0))) {
            System.out.printf("%.2f", temp.get(i).get(j).getNumber());
            if (temp.get(i).get(j).getIsNumber())
              System.out.print(" ");
          }
          if (!temp.get(i).get(j).getIsNumber()) {
            System.out.print(temp.get(i).get(j).getVar() + " ");
          }
        }
        System.out.println();
      }
    }
  }
}
