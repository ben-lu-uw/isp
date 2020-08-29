package com.bl.isp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GaussianBlur {
    public double gaussianFunction(int x, int y, int sigma){
        double euler = Math.exp(1);
        double power = -(x*x + y*y)/(2.0 * sigma*sigma);
        double weight = 1/(2 * Math.PI * sigma*sigma) * Math.pow(euler, power);
        return weight;
    }

    public void applyBlur(int sigma, String inputPath, String outputPath) throws IOException {
        ReadImg readImg = new ReadImg();
        BufferedImage img = readImg.read(inputPath);
        File file;

        int width = img.getWidth();
        int height = img.getHeight();

        Kernel k = new Kernel();

        int limit = k.kernelMidpoint(sigma);
        int size = k.kernelSize(sigma);

        RowMatrix<Weight> weights = Matrix.weightMatrix(sigma);
        RowMatrix<Pixel> pixels = Matrix.getAll(inputPath);

        int cutWidth = k.kernelFit(sigma, width);
        int cutHeight = k.kernelFit(sigma, height);

        BufferedImage outputImg = new BufferedImage(cutWidth, cutHeight, BufferedImage.TYPE_3BYTE_BGR);

        for (int y = 0; y < cutHeight; y++){
            for (int x = 0; x < cutWidth; x++){
                int centerX = limit + x;
                int centerY = limit + y;

                int rSum = 0;
                int gSum = 0;
                int bSum = 0;
                int newPixel;

                ArrayList<Row<Weight>> weightRows = weights.getRows();

                for(int yk = 0; yk < size ; yk++){
                    Row<Weight> weightRow = weightRows.get(yk);
                    ArrayList<Weight> cells = weightRow.getCells();
                    ArrayList<Row<Pixel>> pixelRows = pixels.getRows();
                    for (int xk = 0; xk < size; xk++){
                        Weight cell = cells.get(xk);

                        Pixel p = pixelRows.get(centerY + cell.getY()).getCells().get(centerX - cell.getX());
                        rSum += p.getR() * cell.getWeight();
                        gSum += p.getG() * cell.getWeight();
                        bSum += p.getB() * cell.getWeight();
                    }
                }

                newPixel = (24 << 24)|(rSum << 16)|(gSum << 8)|(bSum);
                outputImg.setRGB(x, y, newPixel);
            }
        }
        file = new File(outputPath);
        ImageIO.write(outputImg, "png", file);
    }

    public static void main(String[] args) {
        GaussianBlur blur = new GaussianBlur();
        try{
            blur.applyBlur(3, "C:\\Users\\avtea\\Desktop\\Input\\g.png", "C:\\Users\\avtea\\Desktop\\Output\\b.png");
        }catch (Exception e){
            System.out.println(e);
        }

    }

}
