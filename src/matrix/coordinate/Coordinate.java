package matrix.coordinate;

/**
 * Kelas untuk koordinat matriks
 */
public class Coordinate {
    private int row; // indeks baris matriks
    private int col; // indeks kolom matriks

    /**
     * Constructor
     * 
     * @param row indeks baris dari matriks
     * @param col indeks kolom dari matriks
     */
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * 
     * @return indeks baris dari matriks
     */
    public int getRow() {
        return this.row;
    }

    /**
     * 
     * @return indeks kolom dari matriks
     */
    public int getCol() {
        return this.col;
    }

    /**
     *
     * @param newRow mengganti indeks baris
     */
    public void setRow(int newRow) {
        this.row = newRow;
    }

    /**
     * 
     * @param newCol mengganti indeks kolom
     */
    public void setCol(int newCol) {
        this.col = newCol;
    }
}
