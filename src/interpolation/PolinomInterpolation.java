package interpolation;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import io.FileTulis;
import lineq.Lineq;
import point.Points;
import matrix.Matrix;
import matrix.expression.ExpressionList;

public class PolinomInterpolation {
    private int degree;
    private Points points;
    private Scanner scanner;
    private double max = -9999999;
    private double min = 9999999;
    private Matrix augmented;
    private HashMap<Integer, Double> solusi;
    
    public PolinomInterpolation(Scanner scanner) {
      this.scanner = scanner;
    }

    public void setPoints(Points read) {
      this.points = read;
      this.degree = points.getRowEff()-1;
    }

    public Points readPointsKeyboard() {
      System.out.println("Silahkan masukan banyak titik sampel (n):");
      this.degree = this.scanner.nextInt()-1; 
      this.points = new Points(degree+1, true, this.scanner);
      this.points.readPoints();
      return this.points;
    }    


    public void setAugmented() {
      Matrix augmented = new Matrix(this.degree + 1, this.degree + 2, true, this.scanner);
      for (int baris = 0; baris < points.getRowEff(); baris++) {
        ArrayList<Double> row = new ArrayList<Double>();
        row.add(1.0);
        double[] currPoint = this.points.getPoint(baris);
        if (currPoint[0] > this.max) {
          this.max = currPoint[0];
        }
        if (currPoint[0] < this.min) {
          this.min = currPoint[0];
        }
        for (int deg = 1; deg <= this.degree; deg++) {
          row.add(Math.pow(currPoint[0], deg));
        }
        row.add(currPoint[1]);
        augmented.setRow(baris, row);
      }
      this.augmented = augmented;
    }

    public void solve() {
      Lineq splSolver = new Lineq();
      HashMap<Integer, ExpressionList> solusi = splSolver.GaussJordan(this.augmented, 2, new FileTulis(""));
      this.solusi = turnExpressionToDouble(solusi);
      displayAsFunction();
      interpolateKeyboard();
    }

    private HashMap<Integer, Double> turnExpressionToDouble(HashMap<Integer, ExpressionList> expresi) {
      HashMap<Integer, Double> valueMap = new HashMap<Integer, Double>();
      expresi.entrySet().forEach(entry -> {
          valueMap.put(entry.getKey(), entry.getValue().getFirstDouble());
        }
      ); 
      return valueMap;
    }

    private void displayAsFunction() {
      System.out.printf("f(x) = %.4f", this.solusi.get(0));
      this.solusi.entrySet().forEach(entry -> {
          if (entry.getKey() != 0) {
            System.out.printf(" + %.4fx^%d",entry.getValue(), entry.getKey());
          }
        }
      ); 
      System.out.println("");
    }

    private void interpolateKeyboard() {
      System.out.println("Berapa banyak sampel interpolasi anda?");
      int samples = this.scanner.nextInt();
      System.out.println("Silahkan masukan " + samples + " buah sampel (x).");
      System.out.printf("Gunakan range x di selang [%.2f, %.2f] agar hasil akurat.\n", this.min, this.max);
      for (int i = 0; i < samples; i++) {
          double input = this.scanner.nextDouble();
          double result = evaluateFunction(input);
          System.out.printf("f(%.2f) = %.3f\n", input, result);
      }
    }

    private double evaluateFunction(double x) {
      double result = 0;
      for (HashMap.Entry<Integer, Double> entry : this.solusi.entrySet()) {
        double res = entry.getValue()*Math.pow(x, entry.getKey());
        result += res;
      }
      return result;
    }

}
