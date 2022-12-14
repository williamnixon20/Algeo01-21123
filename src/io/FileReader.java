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

  public String getFileName() {
    return this.fileName;
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

  public Points readPointsFromFile() throws FileNotFoundException {
    ArrayList<ArrayList<Double>> arrOfPoints = new ArrayList<ArrayList<Double>>();
    ArrayList<Double> samples = new ArrayList<Double>();
    boolean isDataValid = true;
    File file = new File(this.cwd);
    Scanner scanner = new Scanner(file);

    while (isDataValid && scanner.hasNextLine()) {
      ArrayList<Double> tempArr = new ArrayList<Double>();
      String input = scanner.nextLine();
      String[] arrOfInput = input.split(" ", -2);

      if (arrOfInput.length != 2) {
        while (arrOfInput.length == 1) {
          try {
            double estimate = Double.parseDouble(arrOfInput[0]);
            samples.add(estimate);
            input = scanner.nextLine();
            arrOfInput = input.split(" ", -2);
          } catch (Exception e) {
            break;
          }
        }
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
    Points p = new Points(rowSize, false, scanner);

    if (isDataValid) {
      p.changePointsValidty(true);
      for (int i = 0; i < p.getRowEff(); i++) {
        p.setPointsElement(i, 0, arrOfPoints.get(i).get(0));
        p.setPointsElement(i, 1, arrOfPoints.get(i).get(1));
      }
    }

    p.setSamples(samples);
    return p;
  }

  public ArrayList<Matrix> readBicubicFromFile() throws FileNotFoundException {
    ArrayList<Matrix> bcbData = new ArrayList<Matrix>();

    ArrayList<ArrayList<Double>> arr = new ArrayList<ArrayList<Double>>();
    ArrayList<Double> p = new ArrayList<Double>();
    boolean isDataValid = true;
    File file = new File(this.cwd);
    Scanner scanner = new Scanner(file);
    boolean endOfFile = false;

    while (isDataValid && scanner.hasNextLine()) {
      String input = scanner.nextLine();
      String[] arrOfInput = input.split(" ", -2);
      ArrayList<Double> tempArr = new ArrayList<Double>();

      if (arrOfInput.length == 4 && !endOfFile) {
        for (int i = 0; i < arrOfInput.length; i++) {
          try {
            tempArr.add(Double.parseDouble(arrOfInput[i]));
          } catch (NumberFormatException e) {
            System.out.println("Harap masukkan data yang valid.");
            isDataValid = false;
            break;
          }
        }
      } else if (arrOfInput.length == 2 && !endOfFile) {
        for (int i = 0; i < arrOfInput.length; i++) {
          try {
            p.add(Double.parseDouble(arrOfInput[i]));
            endOfFile = true;
          } catch (NumberFormatException e) {
            System.out.println("Harap masukkan data yang valid.");
            isDataValid = false;
            break;
          }
        }
      } else {
        isDataValid = false;
      }

      if (arrOfInput.length == 4)
        arr.add(tempArr);
    }

    int fRowSize = 4, fColSize = 4, pointRowSize = 1, pointColSize = 2;
    Matrix f = new Matrix(fRowSize, fColSize, false, scanner);
    Matrix point = new Matrix(pointRowSize, pointColSize, false, scanner);

    if (isDataValid) {
      f.changeMatrixValidity(true);
      for (int row = 0; row < fRowSize; row++) {
        for (int col = 0; col < fColSize; col++) {
          f.setMatrixElement(row, col, arr.get(row).get(col));
        }
      }
      point.changeMatrixValidity(true);
      point.setMatrixElement(0, 0, p.get(0));
      point.setMatrixElement(0, 1, p.get(1));

      bcbData.add(f);
      bcbData.add(point);
    }

    return bcbData;
  }

  /**
   * Membaca data untuk Regresi Linier Berganda dari File dengan format data yang
   * diregresi diikuti oleh data yang akan diestimasi nilainya
   * 
   * @return Array dari Matrix dengan index 0 data yang akan diregresi dan index 1
   *         data yang akan diestimasi nilainya
   * @throws FileNotFoundException
   */
  public ArrayList<Matrix> readMLRFromFile() throws FileNotFoundException {
    ArrayList<Matrix> MLRData = new ArrayList<Matrix>();

    ArrayList<ArrayList<Double>> arr = new ArrayList<ArrayList<Double>>();
    ArrayList<ArrayList<Double>> samples = new ArrayList<ArrayList<Double>>();
    boolean isDataValid = true;
    boolean isReadSample = false;
    File file = new File(this.cwd);
    Scanner scanner = new Scanner(file);
    int lastRowLength = -1;

    // membaca data yang akan diregresi dan data yang akan diestimasi nilainya
    while (isDataValid && scanner.hasNextLine()) {
      String input = scanner.nextLine();
      String[] arrOfInput = input.split(" ", -2);
      ArrayList<Double> tempArr = new ArrayList<Double>();

      if (lastRowLength == -1) {
        lastRowLength = arrOfInput.length;
      } else if (!isReadSample && (arrOfInput.length == lastRowLength - 1)) {
        isReadSample = true;
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

      if (!isReadSample) {
        arr.add(tempArr);
      } else {
        samples.add(tempArr);
      }
    }

    int mRowSize = arr.size(), mColSize = arr.get(0).size();
    int sRowSize = samples.size();
    int sColSize;
    if (sRowSize > 0) {
      sColSize = samples.get(0).size();
    } else {
      sColSize = 0;
    }

    Matrix m = new Matrix(mRowSize, mColSize, false, scanner);
    Matrix s = new Matrix(sRowSize, sColSize, false, scanner);

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

      s.changeMatrixValidity(true);
      rowLength = s.getRowLastIdx();
      colLength = s.getColLastIdx();
      for (int row = 0; row <= rowLength; row++) {
        for (int col = 0; col <= colLength; col++) {
          s.setMatrixElement(row, col, samples.get(row).get(col));
        }
      }

    }

    MLRData.add(m);
    MLRData.add(s);
    return MLRData;
  }
}