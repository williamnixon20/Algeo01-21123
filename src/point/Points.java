package point;

import java.util.Scanner;

public class Points {
  private double[][] points;
  private int rowEff = 0;
  private int colEff = 2;

  private double[] samples;

  private boolean isValid;
  private String type;
  private Scanner scanner;

  public Points(int row, String type, boolean isValid, Scanner scanner) {
    this.rowEff = row;
    this.type = type;
    this.isValid = isValid;
    this.points = new double[row][2];
    this.scanner = scanner;
  }

  public void readPoints() {
    System.out.println("Masukkan titik:");
    for (int i = 0; i < this.rowEff; i++) {
      for (int j = 0; j < this.colEff; j++) {
        this.points[i][j] = scanner.nextDouble();
      }
    }
    if (this.type == "REG") {
      System.out.print("Masukkan jumlah sampel: ");
      this.readSamples(scanner.nextInt());
    }
  }

  public void setPointsElement(int row, int col, double value) {
    this.points[row][col] = value;
  }

  public int getRowEff() {
    return this.rowEff;
  }

  public int getColEff() {
    return this.colEff;
  }

  public void changePointsValidty(boolean isValid) {
    this.isValid = isValid;
  }

  /**
   * Untuk keperluan testing
   * 
   * @param size
   */
  public void writePoints() {
    if (this.isValid) {
      for (int i = 0; i < this.rowEff; i++) {
        System.out.printf("(%.2f,%.2f)\n", this.points[i][0], this.points[i][1]);
      }
      if (this.type == "REG") {
        for (int i = 0; i < this.samples.length; i++) {
          System.out.printf("%.2f ", this.samples[i]);
        }
      }
    }
  }

  public void setSamples(int size) {
    this.samples = new double[size];
  }

  public void readSamples(int size) {
    this.setSamples(size);
    for (int i = 0; i < size; i++) {
      this.samples[i] = this.scanner.nextDouble();
    }
  }

}
