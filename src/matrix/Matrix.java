package matrix;

import java.util.*;

import io.FileTulis;
import matrix.coordinate.*;

public class Matrix {
  public int MAX_DIMENSION = 1000;
  public int VAL_UNDEF = 99999999;
  public double EPSILON_IMPRECISION = 0.000000000000001;
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

  public boolean getValidity() {
    return this.isValid;
  }

  /**
   * Menimpa element pada index [row][col] dengan value
   * 
   * @param row
   * @param col
   * @param value
   */
  public void setMatrixElement(int row, int col, double value) {
    this.mat[row][col] = value;
  }

  /**
   * Menimpa element pada index [row][col] dengan value
   * 
   * @param row
   * @param col
   */
  public double getMatrixElement(int row, int col) {
    return this.mat[row][col];
  }
  /**
   * Rubah validitas matrix
   * @param isValid
   */
  public void changeMatrixValidity(boolean isValid) {
    this.isValid = isValid;
  }

  public void setRow(int row, ArrayList<Double> values) {
    for (int kolom = 0; kolom < getColLength(); kolom++) {
      this.setMatrixElement(row, kolom, values.get(kolom));
    }
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
  public void writeMatrix(int writeChoice, FileTulis fileWriter) {
    String row = "";
    if (this.isValid) {
      for (int i = 0; i < getRowLength(); i++) {
        for (int j = 0; j < this.colEff; j++) {
          /** Mencegah adanya -0 dan 0 akibat doubleing point imprecision */
          double val = this.mat[i][j];
          if (Math.abs(val) < this.EPSILON_IMPRECISION) {
            val = 0;
          }
          row += String.format("%.2f ", val, val);
        }
        row += "\n";
      }
    } else {
      row += "Matriks invalid";
    }
    System.out.println(row);
    if (writeChoice == 1) {
      fileWriter.writeFile(row);
    }
  }

  public Matrix copyMatrix() {
    Matrix baru = new Matrix(this.rowEff, this.colEff, true, this.scanner);
    for (int i = 0; i < this.rowEff; i++) {
      for (int j = 0; j < this.colEff; j++) {
        baru.setMatrixElement(i, j, this.mat[i][j]);
      }
    }
    return baru;
  }


  /**
   * Mendapatkan matriks A dari AX = B di dari bentuk matriks augmented (matriks kiri)
   * @return
   */
  public Matrix getMatrixAFromAugmented() {
    Matrix baru = new Matrix(this.rowEff, this.colEff - 1, true, this.scanner);
    for (int i = 0; i < this.rowEff; i++) {
      for (int j = 0; j < this.colEff - 1; j++) {
        baru.setMatrixElement(i, j, this.mat[i][j]);
      }
    }
    return baru;
  }

  /**
   * Mendapatkan matriks B dari AX= B dari bentruk matriks augmented (matriks kanan)
   * @return
   */
  public Matrix getMatrixBFromAugmented() {
    Matrix baru = new Matrix(this.rowEff, 1, true, this.scanner);
    for (int i = 0; i < this.rowEff; i++) {
      baru.setMatrixElement(i, 0, this.mat[i][this.colEff - 1]);
    }
    return baru;
  }

  /**
   * Buat matriks identitas
   */
  public void makeIdentity() {
    for (int i = 0; i < this.rowEff; i++) {
      for (int j = 0; j < this.colEff; j++) {
        float value;
        if (i == j) {
          value = 1;
        } else {
          value = 0;
        }
        this.setMatrixElement(i, j, value);
      }
    }
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
   * Perkalian matriks this dan m2
   * @param m2
   * @return
   */
  public Matrix multiplyMatrix(Matrix m2) {
    // 2x3 x 3,2
    Matrix val = new Matrix(this.getRowLength(), m2.getColLength(), true, this.scanner);
    for (int i = 0; i < val.getRowLength(); i++) {
      for (int j = 0; j < val.getColLength(); j++) {
        for (int k = 0; k < m2.getRowLength(); k++) {
          double currVal = val.getMatrixElement(i, j);
          double addition = this.getMatrixElement(i, k) * m2.getMatrixElement(k, j);
          val.setMatrixElement(i, j, currVal + addition);
        }
      }
    }
    return val;
  };

  /**
   * @param row indeks baris yang akan dicari indeks letak leading koefisiennya
   * @return indeks kolom dari leading koefisiennya, jika
   *         baris nol semua
   */
  public int getLeadingCoeffIdx(int row) {
    for (int col = 0; col < getColLength(); col++) {
      if (Math.abs(this.mat[row][col]) > EPSILON_IMPRECISION) {
        return col;
      }
    }
    return VAL_UNDEF;
  }

  /**
   * Mendapat koefisien 1 dilihat dari paling belakang (kanan)
   * Merupakan kebalikan getLeadingCoeffIdx
   */
  public int getTrailingCoeffIdx(int row) {
    for (int col = getColLastIdx(); col >= 0; col--) {
      if (this.mat[row][col] != 0) {
        return col;
      }
    }
    return VAL_UNDEF;
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

  /**
   * Mendapatkan pivot dari belakang (kanan)
   * Merupakan kebalikan getPivot
   */
  public Coordinate getPivotReverse(int startRow) {
    Coordinate pivot = new Coordinate(startRow, getTrailingCoeffIdx(startRow));
    for (int row = startRow - 1; row >= 0; row--) {
      int leadCoeff = getTrailingCoeffIdx(row);

      if (leadCoeff < pivot.getCol()) {
        pivot.setRow(row);
        pivot.setCol(leadCoeff);
      }
    }

    return pivot;
  }


  /**
   * Mengubah matriks ke dalam row echelon form
   * Menggunakan eliminasi gauss dan OBE.
   */
  public void toREF() {
    Coordinate pivot = getPivot(0);
    int curLead = pivot.getCol();
    for (int row = 0; row < getRowLength() - 1; row++) {
      if (curLead < getColLastIdx()) {
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
      } else if (curLead == getColLastIdx()) {
        break;
      }
      pivot = getPivot(row + 1);
      curLead = pivot.getCol();
    }
    if (curLead < getColLastIdx() && this.mat[getRowLastIdx()][curLead] != 1) {
      multiplyRow(getRowLastIdx(), 1 / this.mat[getRowLastIdx()][curLead]);
    }
  }

  /**
   * Mengubah matriks menjadi bentuk Eselon Baris Tereduksi
   */
  public void toRREF() {
    toREF();
    for (int row = getRowLastIdx(); row > 0; row--) {
      int curLead = getLeadingCoeffIdx(row);
      if (curLead < getColLastIdx()) {
        for (int xrow = row - 1; xrow >= 0; xrow--) {
          if (this.mat[xrow][curLead] != 0) {
            double multiplier = (-1) * this.mat[xrow][curLead] / this.mat[row][curLead];
            doRowOperation(row, xrow, multiplier);
          }
        }
      }
    }
  }

  /**
   * Mengubah matriks menjadi REF, memanipulasi matriks inverse juga
   * @param inverse
   */
  public void toREFWithInverse(Matrix inverse) {
    Coordinate pivot = getPivot(0);
    int curLead = pivot.getCol();
    for (int row = 0; row < getRowLength() - 1; row++) {
      if (curLead < VAL_UNDEF) {
        if (pivot.getRow() != row) {
          int pivotRow = pivot.getRow();
          swapRow(pivotRow, row);
          inverse.swapRow(pivotRow, row);
        }

        if (this.mat[row][curLead] != 1) {
          double value = 1 / this.mat[row][curLead];
          multiplyRow(row, value);
          inverse.multiplyRow(row, value);
        }

        for (int xrow = row + 1; xrow < getRowLength(); xrow++) {
          int nextLead = getLeadingCoeffIdx(xrow);
          if (curLead == nextLead) {
            double multiplier = (-1) * this.mat[xrow][curLead] / this.mat[row][curLead];
            doRowOperation(row, xrow, multiplier);
            inverse.doRowOperation(row, xrow, multiplier);
          }
        }
      }
      pivot = getPivot(row + 1);
      curLead = pivot.getCol();
    }
    if (curLead < VAL_UNDEF && this.mat[getRowLastIdx()][curLead] != 1) {
      double multiplier = 1 / this.mat[getRowLastIdx()][curLead];
      multiplyRow(getRowLastIdx(), multiplier);
      inverse.multiplyRow(getRowLastIdx(), multiplier);
    }

  }

  /**
   * Mengubah matriks menjadi RREF, memanipulasi invers juga
   * @param inverse
   */
  public void toRREFWithInverse(Matrix inverse) {
    toREFWithInverse(inverse);
    for (int row = getRowLastIdx(); row > 0; row--) {
      int curLead = getLeadingCoeffIdx(row);
      if (curLead < VAL_UNDEF) {
        for (int xrow = row - 1; xrow >= 0; xrow--) {
          if (this.mat[xrow][curLead] != 0) {
            double multiplier = (-1) * this.mat[xrow][curLead] / this.mat[row][curLead];
            doRowOperation(row, xrow, multiplier);
            inverse.doRowOperation(row, xrow, multiplier);
          }
        }
      }
    }
  }

  /**
   * Converts to upper triangle, returns the number of swaps done
   * 
   * @return
   */
  public int toUpperTriangle() {
    Coordinate pivot = getPivot(0);
    int curLead = pivot.getCol();
    int swaps = 0;
    for (int row = 0; row < getRowLength() - 1; row++) {
      if (curLead < VAL_UNDEF) {
        if (pivot.getRow() != row) {
          swapRow(pivot.getRow(), row);
          swaps += 1;
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
    return swaps;
  }

  /**
   * Convert matrix to lower triangle, returns the number of swaps done
   * @return
   */
  public int toLowerTriangle() {
    Coordinate pivot = getPivotReverse(getRowLastIdx());
    int curLead = pivot.getCol();
    int swaps = 0;
    for (int row = getRowLastIdx(); row > 0; row--) {
      if (curLead < VAL_UNDEF) {
        if (pivot.getRow() != row) {
          swapRow(pivot.getRow(), row);
          swaps += 1;
        }

        for (int xrow = row - 1; xrow >= 0; xrow--) {
          int nextLead = getTrailingCoeffIdx(xrow);
          if (curLead == nextLead) {
            double multiplier = (-1) * this.mat[xrow][curLead] / this.mat[row][curLead];
            doRowOperation(row, xrow, multiplier);
          }
        }
      }
      pivot = getPivotReverse(row - 1);
      curLead = pivot.getCol();
    }
    return swaps;
  }

  /**
   * Mencari determinant dengan metode segitiga atas/bawah
   * @param isUpper
   * @return
   */
  public double getDeterminantWithTriangle(boolean isUpper) {
    if (!this.isSquare()) {
      return this.VAL_UNDEF;
    }
    Matrix copy = this.copyMatrix();
    int swaps;
    if (isUpper) {
      swaps = copy.toUpperTriangle();
    } else {
      swaps = copy.toLowerTriangle();
    }
    double determinant = Math.pow(-1, swaps);
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
    Matrix m = new Matrix(getRowLength() - 1, getColLength() - 1, true, this.scanner);

    for (int row = 0; row <= m.getRowLastIdx(); row++) {
      for (int col = 0; col <= m.getColLastIdx(); col++) {
        m.setMatrixElement(row, col, this.mat[row + (row >= refRow ? 1 : 0)][col + (col >= refCol ? 1 : 0)]);
      }
    }

    if ((refRow + refCol) % 2 == 0) {
      return m.getDeterminantWithTriangle(true);
    } else {
      return (-1) * m.getDeterminantWithTriangle(true);
    }
  }

  /**
   * Note: menggunakan rekursi sehingga mungkin lambat
   * @return determinan Matriks dengan metode kofaktor
   */
  public double getDetWithCofactor() {
    if (!this.isSquare()) {
      return this.VAL_UNDEF;
    }
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

  /**
   * Mendapatkan inverse dengan metode OBE
   * @return
   */
  public Matrix getInverse() {
    if (!isSquare() || Math.abs(getDeterminantWithTriangle(true)) < EPSILON_IMPRECISION) {
      System.out.println("Matriks tidak mempunyai invers!");
      return new Matrix(0, 0, false, this.scanner);
    }
    Matrix copy = this.copyMatrix();
    Matrix Inverse = new Matrix(this.rowEff, this.colEff, true, this.scanner);
    Inverse.makeIdentity();
    copy.toRREFWithInverse(Inverse);
    return Inverse;
  }

  /**
   * Mendapatkan inverse dengan metode OBE, tanpa pengecekan determinan terlebih dahulu.
   * Dilakukan untuk matriks yang determinannya menuju 0 (matriks hilbert)
   * atau jika memerlukan komputasi cepat (soal bonus)
   */
  public Matrix getInverseUnsafe() {
    if (!isSquare()) {
      System.out.println("Matriks tidak mempunyai invers!");
      return new Matrix(0, 0, false, this.scanner);
    }
    Matrix copy = this.copyMatrix();
    Matrix Inverse = new Matrix(this.rowEff, this.colEff, true, this.scanner);
    Inverse.makeIdentity();
    copy.toRREFWithInverse(Inverse);
    return Inverse;
  }

  /**
   * Mendapat matriks kofaktor
   * @return
   */
  public Matrix getMatrixCofactor() {
    Matrix adj = new Matrix(this.rowEff, this.colEff, true, this.scanner);
    for (int i = 0; i < this.rowEff; i++) {
      for (int j = 0; j < this.colEff; j++) {
        double value = getCofactor(i, j);
        adj.setMatrixElement(i, j, value);
      }
    }
    return adj;
  }

  public Matrix tranpose() {
    Matrix mOut = new Matrix(this.colEff, this.rowEff, true, this.scanner);
    int i, j;
    for (i = 0; i < mOut.getRowLength(); i++) {
      for (j = 0; j < mOut.getColLength(); j++) {
        mOut.setMatrixElement(i, j, this.getMatrixElement(j, i));
      }
    }
    return mOut;
  }

  public Matrix getAdjoin() {
    Matrix kofaktor = this.getMatrixCofactor();
    Matrix adjoin = kofaktor.tranpose();
    return adjoin;
  }

  /**
   * Inverse dengan adjoin
   * @return
   */
  public Matrix getInverseWithAdjoin() {
    if (!isSquare() ||  Math.abs(getDeterminantWithTriangle(true)) < EPSILON_IMPRECISION) {
      System.out.println("Matriks tidak mempunyai invers!");
      return new Matrix(0, 0, false, this.scanner);
    }
    double determinant = this.getDeterminantWithTriangle(true);
    Matrix adjoin = this.getAdjoin();
    for (int i = 0; i < adjoin.getRowLength(); i++) {
      adjoin.multiplyRow(i, 1 / determinant);
    }
    return adjoin;
  }

  /**
   * Substitusi matriks untuk metode cramer
   */
  public Matrix substituteCramer(Matrix vector, int kolom) {
    Matrix baru = this.copyMatrix();
    for (int baris = 0; baris < this.rowEff; baris++) {
      baru.setMatrixElement(baris, kolom, vector.getMatrixElement(baris, 0));
    }
    return baru;
  }
}