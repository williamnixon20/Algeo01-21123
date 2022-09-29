package bonus;

import java.io.File;
import java.io.IOException;
// import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.Color;
import javax.imageio.ImageIO;

import interpolation.Bicubic;
import matrix.Matrix;
import point.Point;

import java.io.IOException;

public class ScaleImage {
  public Color c(BufferedImage img, int x, int y) {
    Color c = new Color(img.getRGB(x, y));
    int c_r = (int) Math.round((c.getRed()));
    int c_g = (int) Math.round((c.getGreen()));
    int c_b = (int) Math.round((c.getBlue()));
    int cSum = c_r + c_g + c_b;
    Color cGray = new Color(c_r, c_g, c_b);
    // Color cGray = new Color(c.getRed(), c.getGreen(), c.getBlue());
    return cGray;
  }

  public Point getPoint(int x, int y) {
    if (x % 2 == 0) {
      if (y % 2 == 0)
        return new Point(0.75, 0.75);
      return new Point(0.25, 0.75);
    } else {
      if (y % 2 == 0)
        return new Point(0.75, 0.25);
      return new Point(0.25, 0.25);
    }
  }

  public void scaleImage() throws IOException {
    BufferedImage img = null;
    File f = null;

    /**
     * Baca gambar
     */
    String cwd = System.getProperty("user.dir");
    try {
      cwd += "/src/bonus/mawar.jpg";
      f = new File(cwd);
      img = ImageIO.read(f);
    } catch (IOException e) {
      System.out.println(e);
    }

    BufferedImage padImg = new BufferedImage(img.getWidth() + 4, img.getHeight() + 4, BufferedImage.TYPE_INT_RGB);
    BufferedImage newImg = new BufferedImage(img.getWidth() * 2, img.getHeight() * 2, BufferedImage.TYPE_INT_RGB);

    /**
     * Isi pojok kiri atas
     */
    for (int x = 0; x < 3; x++) {
      for (int y = 0; y < 3; y++) {
        padImg.setRGB(x, y, c(img, 0, 0).getRGB());
      }
    }

    /**
     * Isi pojok kiri bawah
     */
    for (int x = 0; x < 3; x++) {
      for (int y = padImg.getHeight() - 4; y < padImg.getHeight(); y++) {
        padImg.setRGB(x, y, c(img, 0, img.getHeight() - 1).getRGB());
      }
    }

    /**
     * Isi pojok kanan atas
     */
    for (int x = padImg.getWidth() - 4; x < padImg.getWidth(); x++) {
      for (int y = 0; y < 3; y++) {
        padImg.setRGB(x, y, c(img, img.getWidth() - 1, 0).getRGB());
      }
    }

    /**
     * Isi pojok kanan bawah
     */
    for (int x = padImg.getWidth() - 4; x < padImg.getWidth(); x++) {
      for (int y = padImg.getHeight() - 4; y < padImg.getHeight(); y++) {
        padImg.setRGB(x, y, c(img, img.getWidth() - 1, img.getHeight() - 1).getRGB());
      }
    }

    /**
     * Isi sisi kiri
     */
    for (int y = 1; y < img.getHeight() - 1; y++) {
      for (int x = 0; x < 3; x++) {
        padImg.setRGB(x, y + 2, c(img, 0, y).getRGB());
      }
    }

    /**
     * Isi sisi kanan
     */
    for (int y = 1; y < img.getHeight() - 1; y++) {
      for (int x = padImg.getWidth() - 3; x < padImg.getWidth(); x++) {
        padImg.setRGB(x, y + 2, c(img, img.getWidth() - 1, y).getRGB());
      }
    }

    /**
     * Isi sisi atas
     */
    for (int x = 1; x < img.getWidth() - 1; x++) {
      for (int y = 0; y < 3; y++) {
        padImg.setRGB(x + 2, y, c(img, x, 0).getRGB());
      }
    }

    /**
     * Isi sisi bawah
     */
    for (int x = 1; x < img.getWidth() - 1; x++) {
      for (int y = padImg.getHeight() - 3; y < padImg.getHeight(); y++) {
        padImg.setRGB(x + 2, y, c(img, x, img.getHeight() - 1).getRGB());
      }
    }

    /**
     * Isi bagian tengah
     */
    for (int x = 1; x < img.getWidth() - 1; x++) {
      for (int y = 1; y < img.getHeight() - 1; y++) {
        padImg.setRGB(x + 2, y + 2, c(img, x, y).getRGB());
      }
    }

    /**
     * Hitung bicubic interpolasi sekaligus membangun gambar hasil scaling
     */
    Bicubic bcb = new Bicubic();
    int trackX = -1, trackY = -1;
    for (int x = 0; x < padImg.getWidth() - 3; x++) {
      for (int y = 0; y < padImg.getHeight() - 3; y++) {
        Matrix subImg = new Matrix(4, 4, true, null);
        for (int x1 = x; x1 < x + 4; x1++) {
          for (int y1 = y; y1 < y + 4; y1++) {
            subImg.setMatrixElement(x1 - x, y1 - y, padImg.getRGB(x, y));
          }
        }
        if (x == 0 && y == 0) {
          trackX = 0;
          trackY = 0;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
        } else if (x == 0 && y == padImg.getHeight() - 4) {
          trackX = 0;
          trackY = img.getHeight() * 2 - 1;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
        } else if (x == padImg.getWidth() - 4 && y == 0) {
          trackX = img.getWidth() * 2 - 1;
          trackY = 0;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
        } else if (x == padImg.getWidth() - 4 && y == padImg.getHeight() - 4) {
          trackX = img.getWidth() * 2 - 1;
          trackY = img.getHeight() * 2 - 1;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
        } else if (x == 0) {
          trackX = 0;
          trackY++;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
          trackY++;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
        } else if (x == padImg.getWidth() - 4) {
          trackX = img.getWidth() * 2 - 1;
          trackY++;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
          trackY++;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
        } else if (y == 0) {
          trackY = 0;
          trackX++;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
          trackX++;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
        } else if (y == padImg.getHeight() - 4) {
          trackY = img.getHeight() * 2 - 1;
          trackX--;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
          trackX++;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
        } else {
          trackX--;
          trackY++;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
          trackY++;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
          trackX++;
          trackY--;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
          trackY++;
          // System.out.println(trackX + ", " + trackY);
          newImg.setRGB(trackX, trackY, (int) Math.round(bcb.bicubic(subImg,
              getPoint(trackX, trackY), null)));
        }
      }
    }

    /**
     * Tulis gambar
     */
    cwd = System.getProperty("user.dir");
    try {
      cwd += "/src/bonus/mawar-out.jpg";
      f = new File(cwd);
      ImageIO.write(newImg, "jpg", f);
    } catch (IOException e) {
      System.out.println(e);
    }
  }

}
