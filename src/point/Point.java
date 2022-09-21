package point;

public class Point {
  private float x;
  private float y;

  /**
   * Constructor
   * 
   * @param x
   * @param y
   */
  public Point(float x, float y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Untuk mengubah point yang telah terdefinisi
   * 
   * @param x
   * @param y
   */
  public void changePoint(float x, float y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Mendapatkan nilai x (absis)
   * 
   * @return
   */
  public float getAbsis() {
    return this.x;
  }

  /**
   * Mendapatkan nilai y (ordinat)
   * 
   * @return
   */
  public float getOrdinat() {
    return this.y;
  }
}
