package com.bl.bimp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Outline {
    public static RowMatrix<Pixel> drawOutline(RowMatrix<Pixel> pixels, int width, int height, Boolean isX) throws IOException {

        int dimension1 = height;
        int dimension2 = width;

        if(!isX){
            dimension1 = width;
            dimension2 = height;
        }

        RowMatrix<Pixel> output = new RowMatrix<Pixel>();
        for(int i = 0; i < height; i++){
            output.addRow(new Row<Pixel>());
        }

        if(!isX){
            for(int i = 0; i < width; i++){
                output.getRows().get(height - 1).addCell(new Pixel(0));
            }
        }

        for (int direction1 = 0; direction1 < dimension1; direction1++){
            for (int direction2 = 1; direction2 < dimension2; direction2++){
                //System.out.println("1: " + direction1 + " 2: " + direction2);
                ArrayList<Row<Pixel>> pixelRows = pixels.getRows();

                int xCurrent = direction2;
                int yCurrent = direction1;

                if(!isX){
                    xCurrent = direction1;
                    yCurrent = direction2;
                }
                Pixel p = pixelRows.get(yCurrent).getCells().get(xCurrent);

                int xPrevious = direction2 - 1;
                int yPrevious = direction1;

                if(!isX){
                    xPrevious = direction1;
                    yPrevious = direction2 - 1;
                }
                Pixel pPrevious = pixelRows.get(yPrevious).getCells().get(xPrevious);

                int pixel;
                int a2 = p.getA();
                int r2 = p.getR();
                int g2 = p.getG();
                int b2 = p.getB();

                int a1 = pPrevious.getA();
                int r1 = pPrevious.getR();
                int g1 = pPrevious.getG();
                int b1 = pPrevious.getB();

                double d = Math.sqrt(Math.pow(r2 - r1, 2) + Math.pow(g2 - g1, 2) + Math.pow(b2 - b1, 2));

                if(d > 10){
                    pixel = (a1 << 24)|(0 << 16)|(0 << 8)|0;
                }
                else{
                    pixel = (0 << 24)|(0 << 16)|(0 << 8)|0;
                }
                //System.out.println(yPrevious);
                Pixel outputPixel = new Pixel(pixel);
                output.getRows().get(yPrevious).addCell(outputPixel);

            }

        }
        if(isX){
            for(int i = 0; i < height; i++){
                output.getRows().get(i).addCell(new Pixel(0));
            }
        }

        return output;
    }

    public static void combineOutline(String inputPath, String outputPath) throws IOException {
        ReadImg readImg = new ReadImg();
        BufferedImage img = readImg.read(inputPath);

        final int width = img.getWidth();
        final int height = img.getHeight();

        File file;

        RowMatrix<Pixel> pixels = Matrix.getAll(inputPath);
        RowMatrix<Pixel> outlineX = drawOutline(pixels, width, height, true);
        RowMatrix<Pixel> outlineY = drawOutline(pixels, width, height, false);

        RowMatrix<Pixel> combined = outlineX.combine(outlineY);
        BufferedImage outputImg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Pixel pixel = combined.getRows().get(y).getCells().get(x);
                int p = pixel.getBit();
                //System.out.println(p);
                outputImg.setRGB(x, y, p);
            }
        }
        file = new File(outputPath);
        ImageIO.write(outputImg, "png", file);
    }

    public static void main(String[] args) {
        try{

            Outline.combineOutline("C:\\Users\\avtea\\Desktop\\Output\\a.png",
                    "C:\\Users\\avtea\\Desktop\\Output\\g.png");
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
