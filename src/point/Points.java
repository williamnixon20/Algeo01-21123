package point;

import java.util.ArrayList;
import java.util.Scanner;

public class Points {
  private double[][] points;
  private int rowEff = 0;
  private int colEff = 2;
  private boolean isValid;
  private Scanner scanner;
  private ArrayList<Double> samples;

  public Points(int row, boolean isValid, Scanner scanner) {
    this.rowEff = row;
    this.isValid = isValid;
    this.points = new double[row][2];
    this.scanner = scanner;
  }

  public void readPoints() {
    System.out.println("Terimakasih! Silahkan masukan " + this.rowEff + " pasang poin.");
    for (int i = 0; i < this.rowEff; i++) {
      this.points[i][0] = this.scanner.nextFloat();
      this.points[i][1] = this.scanner.nextFloat();
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

  public ArrayList<Double> getSamples() {
    return this.samples;
  }

  public void changePointsValidty(boolean isValid) {
    this.isValid = isValid;
  }

  public void setSamples(ArrayList<Double> samples) {
    this.samples = samples;
  }

  public double[] getPoint(int row) {
    return this.points[row];
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
      for (int i = 0; i < this.samples.size(); i++) {
        System.out.printf("%.2f ", this.samples.get(i));
      }
    }
  }
}
