package com.bl.isp;

public class GaussianBlur {
    public double gaussianFunction(int x, int y, int sigma){
        double euler = Math.exp(1);
        double power = -(x*x + y*y)/(2.0 * sigma*sigma);
        double weight = 1/(2 * Math.PI * sigma*sigma) * Math.pow(euler, power);
        return weight;
    }

    public static RowMatrix<Weight> weightMatrix(int sigma){
        RowMatrix<Weight> rowMatrix = new RowMatrix<Weight>();
        GaussianBlur blur = new GaussianBlur();
        Kernel kernel = new Kernel();
        int limit = kernel.kernelMidpoint(sigma);

        double sum = 0;

        for (int y = -limit; y <= limit; y++){
            Row<Weight> row = new Row<Weight>();
            for (int x = -limit; x<= limit; x++){
                double weight = blur.gaussianFunction(x, y, sigma);
                Weight c = new Weight(x, y, weight);
                c.setX(x);
                c.setY(y);
                c.setWeight(weight);
                row.addCell(c);

                sum += weight;
            }
            rowMatrix.addRow(row);
        }

        for(int y = 0; y <= limit * 2; y++){
            for(int x = 0; x<= limit * 2; x++){
                double normalizedWeight = rowMatrix.getRows().get(y).getCells().get(x).getWeight() / sum;
                rowMatrix.getRows().get(y).getCells().get(x).setWeight(normalizedWeight);
            }
        }
        return rowMatrix;
    }

    public static RowMatrix<Pixel> applyBlur(int sigma, RowMatrix<Pixel> rgbMatrix){

        RowMatrix<Pixel> rowMatrix = new RowMatrix<>();

        int width = rgbMatrix.getRows().get(0).getCells().size();
        int height = rgbMatrix.getRows().size();

        Kernel k = new Kernel();

        int limit = k.kernelMidpoint(sigma);
        int size = k.kernelSize(sigma);

        RowMatrix<Weight> weightMatrix = GaussianBlur.weightMatrix(sigma);

        int cutWidth = k.kernelFit(sigma, width);
        int cutHeight = k.kernelFit(sigma, height);

        for (int y = 0; y < cutHeight; y++){
            Row<Pixel> row = new Row<>();
            for (int x = 0; x < cutWidth; x++){
                int centerX = limit + x;
                int centerY = limit + y;

                int rSum = 0;
                int gSum = 0;
                int bSum = 0;

                for(int yk = 0; yk < size ; yk++){
                    for (int xk = 0; xk < size; xk++){
                        Weight cell = weightMatrix.getRows().get(yk).getCells().get(xk);

                        Pixel p = rgbMatrix.getRows().get(centerY + cell.getY()).getCells().get(centerX - cell.getX());
                        rSum += p.getR() * cell.getWeight();
                        gSum += p.getG() * cell.getWeight();
                        bSum += p.getB() * cell.getWeight();
                    }
                }
                Pixel pixel = new Pixel(0);
                pixel.setBit((24 << 24)|(rSum << 16)|(gSum << 8)|(bSum));
                pixel.setR(rSum);
                pixel.setG(gSum);
                pixel.setB(bSum);
                row.addCell(pixel);
            }

            rowMatrix.addRow(row);
        }

        return rowMatrix;
    }


}
