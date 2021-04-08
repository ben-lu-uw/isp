package com.bl.isp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ReadImage {
    public BufferedImage read(String inputPath) throws IOException{
        BufferedImage img;
        File file;
        file = new File(inputPath);
        img = ImageIO.read(file);
        return img;
    }

    public static RowMatrix<Pixel> rgbMatrix(String inputPath) throws IOException {
        RowMatrix<Pixel> rowMatrix = new RowMatrix<Pixel>();
        ReadImage readImage = new ReadImage();
        BufferedImage img = readImage.read(inputPath);

        int width = img.getWidth();
        int height = img.getHeight();

        for(int y = 0; y < height; y++){
            Row<Pixel> row = new Row<Pixel>();
            for(int x = 0; x < width; x++){

                int p = img.getRGB(x, y);
                int a = (p >> 24)&0xff;
                int r = (p >> 16)&0xff;
                int g = (p >> 8)&0xff;
                int b = p&0xff;

                Pixel pixel = new Pixel(p);
                pixel.setA(a);
                pixel.setR(r);
                pixel.setG(g);
                pixel.setB(b);
                row.addCell(pixel);

            }
            rowMatrix.addRow(row);
        }

        return rowMatrix;
    }
}

