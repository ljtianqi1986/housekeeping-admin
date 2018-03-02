package com.biz.test;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Created by liujiajia on 16/8/8.
 */
public class ImageTest {
    public static String path = "/Users/liujiajia/Desktop/images/";


    public static void main(String[] args) throws IOException {
        File file1 = new File(path, "1.png");
        File file2 = new File(path, "2.jpg");
        mergeImage(file1, file2);
    }
    public static void mergeImage(File file1, File file2) throws IOException {
        BufferedImage image1 = ImageIO.read(file1);
        BufferedImage image2 = ImageIO.read(file2);

        BufferedImage combined = new BufferedImage(image1.getWidth() * 2, image1.getHeight(), BufferedImage.TYPE_INT_RGB);

        // paint both images, preserving the alpha channels
        Graphics g = combined.getGraphics();
        g.drawImage(image1, 0, 0, null);
        g.drawImage(image2, image1.getWidth(), 0, null);

        // Save as new image
        ImageIO.write(combined, "JPG", new File(path, "3.jpg"));
    }

}
