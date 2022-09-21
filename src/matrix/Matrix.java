package matrix;

import java.util.*;
import matrix.coordinate.*;

public class Matrix {
  public int MAX_DIMENSION = 1000;
  public double VAL_UNDEF = -99999999;
  public double EPSILON_IMPRECISION = 0.0000001;
  /**
   * private artinya tidak bisa diakses dari luar
   * segala interaksi dengan field harus dilakukan dengan fungsi di kelas matriks
   * (this.)
   */
  private double[][] mat;
  private int rowEff = 0;
  private int colEff = 0;
  private boolean isValid;
  private Scanner scanner;

  /**
   * 
   * @param row     Dimensi baris yang ingin dimiliki matriks
   * @param col     Dimensi kolom yang ingin dimiliki matriks
   * @param isValid 
   * @param scanner Scanner u/matriks
   */
  public Matrix(int row, int col, boolean isValid, Scanner scanner) {
    this.rowEff = row;
    this.colEff = col;
    this.isValid = isValid;
    this.scanner = scanner;
    this.mat = new double[row][col];
  }

  /**
   * @return Index baris terakhir di matriks
   */
  public int getRowLastIdx() {
    return this.rowEff - 1;
  }

  /**
   * @return Index kolom terakhir di matriks
   */
  public int getColLastIdx() {
    return this.colEff - 1;
  }

  /**
   * 
   * @return Banyak baris efektif
   */
  public int getRowLength() {
    return this.rowEff;
  }

  /**
   * 
   * @return Banyak kolom efektif
   */
  public int getColLength() {
    return this.colEff;
  }

  /**
   * Menimpa element pada index [row][col] dengan value
   * @param row
   * @param col
   * @param value
   */
  public void setMatrixElement(int row, int col, double value) {
    this.mat[row][col] = value;
  }

  /**
   * Menimpa element pada index [row][col] dengan value
   * @param row
   * @param col
   */
  public double getMatrixElement(int row, int col) {
    return this.mat[row][col];
  }

  public void changeMatrixValidity(boolean isValid) {
    this.isValid = isValid;
  }

  public boolean isSquare() {
    return getColLength() == getRowLength();
  }

  /**
   * Mengisi matriks sesuai input
   */
  public void readMatrix() {
    System.out.println("Silahkan masukkan matriks " + getRowLength() + "x" + getColLength() + ":");
    for (int i = 0; i < this.rowEff; i++) {
      for (int j = 0; j < this.colEff; j++) {
        this.mat[i][j] = this.scanner.nextDouble();
      }
    }
  }
  /**
   * Menulis matriks ke layar
   */
  public void writeMatrix() {
    if (this.isValid) {
      for (int i = 0; i < getRowLength(); i++) {
        for (int j = 0; j < this.colEff; j++) {
          /** Mencegah adanya -0 dan 0 akibat doubleing point imprecision */
          double val = this.mat[i][j];
          if (val > (-this.EPSILON_IMPRECISION) && val < this.EPSILON_IMPRECISION) {
            val = this.EPSILON_IMPRECISION;
          }
          System.out.printf("%.2f ", val, val);
        }
        System.out.print("\n");
      }
    }
  }

  public Matrix copyMatrix() {
    Matrix baru = new Matrix(this.rowEff, this.colEff, true, this.scanner);
    for (int i = 0; i < this.rowEff; i++) {
      for (int j = 0; j < this.colEff; j++) {
        baru.setMatrixElement(i, j, this.mat[i][j]);
      }
    }
    baru.writeMatrix();
    return baru;
  }

  /**
   * 
   * @param fromRow    baris yang akan menjadi acuan OBE
   * @param toRow      baris yang akan dilakukan OBE
   * @param multiplier konstanta pengali di dalam OBE
   */
  public void doRowOperation(int fromRow, int toRow, double multiplier) {
    for (int col = 0; col < this.colEff; col++) {
      this.mat[toRow][col] += this.mat[fromRow][col] * multiplier;
    }
  }

  /**
   * 
   * @param fromCol    kolom yang akan menjadi acuan OBE
   * @param toCol      kolom yang akan dilakukan OBE
   * @param multiplier konstanta pengali di dalam OBE
   */
  public void doColOperation(int fromCol, int toCol, double multiplier) {
    for (int row = 0; row < this.rowEff; row++) {
      this.mat[row][toCol] += this.mat[row][fromCol] * multiplier;
    }
  }

  /**
   * 
   * @param row        indeks baris yang akan dikalikan oleh konstanta
   * @param multiplier konstanta pengali di dalam OBE
   */
  public void multiplyRow(int row, double multiplier) {
    for (int col = 0; col < this.colEff; col++) {
      this.mat[row][col] *= multiplier;
    }
  }

  /**
   * @param firstRow  indeks baris yang akan ditukar dengan baris kedua
   * @param secondRow indeks baris yang akan ditukar dengan baris pertama
   */
  public void swapRow(int firstRow, int secondRow) {
    double temp;
    for (int col = 0; col < getColLength(); col++) {
      temp = this.mat[firstRow][col];
      this.mat[firstRow][col] = this.mat[secondRow][col];
      this.mat[secondRow][col] = temp;
    }
  }

  /**
   * @param row indeks baris yang akan dicari indeks letak leading koefisiennya
   * @return indeks kolom dari leading koefisiennya, 1000 (MAX_DIMENSION) jika
   *         baris nol semua
   */
  public int getLeadingCoeffIdx(int row) {
    for (int col = 0; col < getColLength(); col++) {
      if (this.mat[row][col] != 0) {
        return col;
      }
    }
    return MAX_DIMENSION;
  }

  /**
   * 
   * @param startRow baris awal mulai pengecekan pivot
   * @return Koordinat matriks dari pivot, elemen dengan leading koefisien paling
   *         kiri
   */
  public Coordinate getPivot(int startRow) {
    Coordinate pivot = new Coordinate(startRow, getLeadingCoeffIdx(startRow));
    for (int row = startRow + 1; row < getRowLength(); row++) {
      int leadCoeff = getLeadingCoeffIdx(row);

      if (leadCoeff < pivot.getCol()) {
        pivot.setRow(row);
        pivot.setCol(leadCoeff);
      }
    }

    return pivot;
  }


  public void toREF() {
    Coordinate pivot = getPivot(0);
    int curLead = pivot.getCol();
    for (int row = 0; row < getRowLength() - 1; row++) {
      if (curLead < MAX_DIMENSION) {
        if (pivot.getRow() != row) {
          swapRow(pivot.getRow(), row);
        }

        if (this.mat[row][curLead] != 1) {
          multiplyRow(row, 1 / this.mat[row][curLead]);
        }

        for (int xrow = row + 1; xrow < getRowLength(); xrow++) {
          int nextLead = getLeadingCoeffIdx(xrow);
          if (curLead == nextLead) {
            double multiplier = (-1) * this.mat[xrow][curLead] / this.mat[row][curLead];
            doRowOperation(row, xrow, multiplier);
          }
        }
      }
      pivot = getPivot(row + 1);
      curLead = pivot.getCol();
    }

    if (curLead < MAX_DIMENSION && this.mat[getRowLastIdx()][curLead] != 1) {
      multiplyRow(getRowLastIdx(), 1 / this.mat[getRowLastIdx()][curLead]);
    }
  }

  public void toUpperTriangle() {
    for (int row = 0; row < getRowLength(); row++) {
      for (int rowBelow = row + 1; rowBelow < getRowLength(); rowBelow++) {
        if (this.mat[rowBelow][row] > this.EPSILON_IMPRECISION) {
          double multiplier = (-1) * this.mat[rowBelow][row] / this.mat[row][row];
          doRowOperation(row, rowBelow, multiplier);
        }
      }
    }
  }

  /**
   * Mengubah matriks menjadi bentuk Eselon Baris Tereduksi
   */
  public void toRREF() {
    toREF();
    for (int row = getRowLastIdx(); row > 0; row--) {
      int curLead = getLeadingCoeffIdx(row);
      if (curLead < MAX_DIMENSION) {
        for (int xrow = row - 1; xrow >= 0; xrow--) {
          if (this.mat[xrow][curLead] != 0) {
            double multiplier = (-1) * this.mat[xrow][curLead] / this.mat[row][curLead];
            doRowOperation(row, xrow, multiplier);
          }
        }
      }
    }
  }

  public double getDeterminantWithTriangle() {
    if (!this.isSquare()) {
      return this.VAL_UNDEF;
    }
    Matrix copy = this.copyMatrix();
    copy.writeMatrix();
    copy.toUpperTriangle();
    copy.writeMatrix();
    float determinant = 1;
    for (int row = 0; row < getRowLength(); row++) {
      determinant *= copy.getMatrixElement(row, row);
    }   
    return determinant;
  }
  /**
   * 
   * @param refRow baris yang akan dihilangkan dalam perhitungan
   * @param refCol kolom yang akan dihilangkan dalam perhitungan
   * @return kofaktor dari refRow dan refCol
   */
  public double getCofactor(int refRow, int refCol) {
    Scanner scanner = new Scanner(System.in);
    Matrix m = new Matrix(getRowLength() - 1, getColLength() - 1, true, scanner);

    for (int row = 0; row <= m.getRowLastIdx(); row++) {
      for (int col = 0; col <= m.getColLastIdx(); col++) {
        m.setMatrixElement(row, col, this.mat[row + (row >= refRow ? 1 : 0)][col + (col >= refCol ? 1 : 0)]);
      }
    }

    if ((refRow + refCol) % 2 == 0) {
      return m.getDetWithCofactor();
    } else {
      return (-1) * m.getDetWithCofactor();
    }
  }

  /**
   * 
   * @return determinan Matriks dengan metode kofaktor
   */
  public double getDetWithCofactor() {
    double det = 0;
    int row = 0;
    if (getRowLength() == 1) {
      return this.mat[0][0];
    } else if (getRowLength() == 2) {
      return ((this.mat[0][0] * this.mat[1][1]) - (this.mat[0][1] * this.mat[1][0]));
    } else {
      for (int col = 0; col <= getColLastIdx(); col++) {
        det += this.mat[row][col] * getCofactor(row, col);
      }

      return det;
    }
  }
}