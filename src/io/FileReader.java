package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import matrix.Matrix;
import point.Points;

public class FileReader {
  private String fileName = "defaultMatrix.txt";
  /**
   * Get the current working directory
   */
  private String cwd = System.getProperty("user.dir");

  public boolean setFileName(Scanner scanner) {
    System.out.print("Masukkan nama file: ");
    String fileName = scanner.next();

    /**
     * Checking file name
     */
    changeCWD(fileName);
    File temp = new File(cwd);
    if (temp.exists()) {
      this.fileName = fileName;
      return true;
    }

    changeCWD(this.fileName);
    return false;
  }

  /**
   * Add the file name at the end of the cwd
   */
  private void changeCWD(String fileName) {
    // this.cwd = this.cwd.substring(0, cwd.length() - 3);
    this.cwd += "/test/" + fileName;
  }

  public Matrix readMatrix() throws IOException {
    /**
     * Do the task
     */
    ArrayList<ArrayList<Double>> arr = new ArrayList<ArrayList<Double>>();
    boolean isDataValid = true;
    File file = new File(this.cwd);
    Scanner scanner = new Scanner(file);
    int lastRowLength = -1;

    while (isDataValid && scanner.hasNextLine()) {
      String input = scanner.nextLine();
      String[] arrOfInput = input.split(" ", -2);
      ArrayList<Double> tempArr = new ArrayList<Double>();

      if (lastRowLength == -1) {
        lastRowLength = arrOfInput.length;
      } else if (lastRowLength != arrOfInput.length) {
        System.out.println("Harap masukkan data yang valid.");
        isDataValid = false;
        break;
      }

      lastRowLength = arrOfInput.length;

      for (int i = 0; i < arrOfInput.length; i++) {
        try {
          tempArr.add(Double.parseDouble(arrOfInput[i]));
        } catch (NumberFormatException e) {
          System.out.println("Harap masukkan data yang valid.");
          isDataValid = false;
          break;
        }
      }
      arr.add(tempArr);
    }

    int rowSize = arr.size(), colSize = arr.get(0).size();
    Matrix m = new Matrix(rowSize, colSize, false, scanner);

    if (isDataValid) {
      /**
       * Assign matrix m value
       */
      m.changeMatrixValidity(true);
      int rowLength = m.getRowLastIdx(), colLength = m.getColLastIdx();
      for (int row = 0; row <= rowLength; row++) {
        for (int col = 0; col <= colLength; col++) {
          m.setMatrixElement(row, col, arr.get(row).get(col));
        }
      }
    }

    return m;
  }

  public Points readPoints(String type) throws FileNotFoundException {
    ArrayList<ArrayList<Double>> arrOfPoints = new ArrayList<ArrayList<Double>>();
    boolean isDataValid = true;
    File file = new File(this.cwd);
    Scanner scanner = new Scanner(file);

    while (isDataValid && scanner.hasNextLine()) {
      ArrayList<Double> tempArr = new ArrayList<Double>();
      String input = scanner.nextLine();
      String[] arrOfInput = input.split(" ", -2);

      if (arrOfInput.length != 2) {
        System.out.println("Harap masukkan data yang valid.");
        isDataValid = false;
        break;
      }

      for (int i = 0; i < arrOfInput.length; i++) {
        try {
          tempArr.add(Double.parseDouble(arrOfInput[i]));
        } catch (NumberFormatException e) {
          System.out.println("Harap masukkan data yang valid.");
          isDataValid = false;
          break;
        }
      }
      arrOfPoints.add(tempArr);
    }

    int rowSize = arrOfPoints.size();
    Points p = new Points(rowSize, type, false, scanner);

    if (isDataValid) {
      p.changePointsValidty(true);
      for (int i = 0; i < p.getRowEff(); i++) {
        p.setPointsElement(i, 0, arrOfPoints.get(i).get(0));
        p.setPointsElement(i, 1, arrOfPoints.get(i).get(1));
      }
    }

    return p;
  }
}