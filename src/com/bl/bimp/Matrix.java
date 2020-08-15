package com.bl.bimp;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class Matrix {

    public static RowMatrix<Pixel> getAll(String inputPath) throws IOException {
        RowMatrix<Pixel> rowMatrix = new RowMatrix<Pixel>();
        ReadImg readImg = new ReadImg();
        BufferedImage img = readImg.read(inputPath);

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


    public static RowMatrix<Weight> weightMatrix(int sigma){
        RowMatrix<Weight> rowMatrix = new RowMatrix<Weight>();
        GaussianBlur blur = new GaussianBlur();
        Kernel kernel = new Kernel();
        int limit = kernel.kernelMidpoint(sigma);

        for (int y = -limit; y <= limit; y++){
            Row<Weight> row = new Row<Weight>();
            for (int x = -limit; x<= limit; x++){
                double weight = blur.gaussianFunction(x, y, sigma);
                Weight c = new Weight(x, y, weight);
                c.setX(x);
                c.setY(y);
                c.setWeight(weight);
                row.addCell(c);
            }
            rowMatrix.addRow(row);
        }
        return rowMatrix;
    }

    public static void main(String[] args) {
        Kernel k = new Kernel();
        System.out.println(weightMatrix(1));
    }

    /*
    public static void main(String[] args) {
        try{
            System.out.println(imageProcessor.Matrix.getAll("C:\\Users\\avtea\\Desktop\\Input\\b.png"));
        }catch (Exception e){
            System.out.println(e);
        }
    }

     */

    public ArrayList rgbMatrix(String inputPath) throws IOException {

        ReadImg readImg = new ReadImg();
        BufferedImage img = readImg.read(inputPath);

        int width = img.getWidth();
        int height = img.getHeight();

        ArrayList<ArrayList<ArrayList<Integer>>> arrayList = new ArrayList<>(height);

        for(int y = 0; y < height; y++){
            arrayList.add(new ArrayList<ArrayList<Integer>>(width));

            for(int x = 0; x < width; x++){
                int pixel = img.getRGB(x, y);
                int r = (pixel >> 16)&0xff;
                int g = (pixel >> 8)&0xff;
                int b = pixel&0xff;
                ArrayList<Integer> rgb = new ArrayList<>(Arrays.asList(r, g, b));
                arrayList.get(y).add(new ArrayList<Integer>(rgb.size()));

                for (int z = 0; z < rgb.size(); z++){
                    arrayList.get(y).get(x).add(z, rgb.get(z));
                }
            }
        }

        return arrayList;
    }

}
