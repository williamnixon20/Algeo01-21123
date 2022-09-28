package io;

import java.io.FileWriter;
import java.io.IOException;

public class FileTulis {
  private FileWriter fileWriter;
  private String cwd = System.getProperty("user.dir");

  private void changeCWD(String fileName) {
    this.cwd += "/test/output/" + fileName;
  }

  public FileTulis(String filename) {
    try {
      changeCWD(filename);
      this.fileWriter = new FileWriter(this.cwd);
    } catch (IOException e) {
      System.out.println("An error occurred.");
    }
  }

  public void writeFile(String string) {
    try {
      this.fileWriter.write(string + "\n");
    } catch (IOException e) {
      System.out.println("An error occurred." + e);
    }
  }

  public void closeFile() {
    try {
      this.fileWriter.close();
    } catch (IOException e) {
      System.out.println("An error occurred." + e);
    }
  }
}