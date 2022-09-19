package matrix;
import java.util.*;

public class Matrix {
    static final int MAX_DIMENSION = 1000;
    /**
     * private artinya tidak bisa diakses dari luar
     * segala interaksi dengan field harus dilakukan dengan fungsi di kelas matriks (this.)
     */
    private double[][] mat; 
    private int rowEff = 0;
    private int colEff = 0;
    private Scanner scanner; 

    /**
     * 
     * @param row Dimensi baris yang ingin dimiliki matriks
     * @param col Dimensi kolom yang ingin dimiliki matriks
     * @param scanner Scanner u/matriks
     */
    public Matrix(int row, int col, Scanner scanner) {
        this.rowEff = row;
        this.colEff = col;
        this.scanner = scanner;
        this.mat = new double[row][col];
    }

    /**
     * @return Index baris terakhir di matriks
     */
    public int getRowLastIdx() {
      return this.rowEff-1;
    }

    /**
     * @return Index kolom terakhir di matriks
     */
    public int getColLastIdx() {
      return this.colEff-1;
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
        for (int i = 0; i < this.rowEff; i++) {
            for (int j = 0; j < this.colEff; j++) {
                System.out.printf("%.2f ", this.mat[i][j]);
            }
            System.out.print("\n");
        }
    }

    /**
     * 
     * @param fromRow baris yang akan menjadi acuan OBE
     * @param toRow baris yang akan dilakukan OBE
     * @param multiplier konstanta pengali di dalam OBE
     */
    public void doRowOperation(int fromRow, int toRow, float multiplier) {
      for (int col = 0; col < this.colEff; col++) {
        this.mat[toRow][col] += this.mat[fromRow][col] * multiplier;
      }
    }

    /**
     * 
     * @param fromCol kolom yang akan menjadi acuan OBE
     * @param toCol kolom yang akan dilakukan OBE
     * @param multiplier konstanta pengali di dalam OBE
     */
    public void doColOperation(int fromCol, int toCol, float multiplier) {
      for (int row = 0; row < this.rowEff; row++) {
        this.mat[row][toCol] += this.mat[row][fromCol] * multiplier;
      }
    }
}