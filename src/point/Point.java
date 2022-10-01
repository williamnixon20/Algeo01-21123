package point;


public class Point {
  private double x;
  private double y;

  /**
   * Constructor
   * 
   * @param x
   * @param y
   */
  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Untuk mengubah point yang telah terdefinisi
   * 
   * @param x
   * @param y
   */
  public void changePoint(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Mendapatkan nilai x (absis)
   * 
   * @return
   */
  public double getAbsis() {
    return this.x;
  }

  /**
   * Mendapatkan nilai y (ordinat)
   * 
   * @return
   */
  public double getOrdinat() {
    return this.y;
  }
}
