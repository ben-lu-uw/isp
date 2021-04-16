package com.bl.isp;

import java.io.IOException;

public class Edge {

    //https://en.wikipedia.org/wiki/Sobel_operator#Formulation
    public static RowMatrix<Gradient> generate(RowMatrix<Pixel> rgbMatrix, int upperThreshold, int lowerThreshold) throws IOException {
        RowMatrix<Gradient> gradientRowMatrix = new RowMatrix<Gradient>();
        int height = rgbMatrix.getRows().size() - 1;
        int width = rgbMatrix.getRows().get(0).getCells().size() - 1;

        int maxG = -1;

        for(int y = 1; y < height; y++){
            Row<Gradient> gradientRow = new Row<Gradient>();
            for(int x = 1; x < width; x++){
                RowMatrix<Pixel> rowMatrix = new RowMatrix<Pixel>();
                for(int i = y - 1; i <= y + 1; i++){
                    Row<Pixel> row = new Row<Pixel>();
                    for(int j = x - 1; j <= x + 1; j++){
                        Pixel p = new Pixel(rgbMatrix.getRows().get(i).getCells().get(j).getBit());
                        int gray = ConvertColourSpace.rgbToGrayscale(p.getBit());
                        p.setR(gray);
                        row.addCell(p);
                    }
                    rowMatrix.addRow(row);
                }

                int gx = -1 * (rowMatrix.getRows().get(0).getCells().get(0).getR())
                        + (rowMatrix.getRows().get(0).getCells().get(2).getR())
                        + -2 * (rowMatrix.getRows().get(1).getCells().get(0).getR())
                        + 2 * (rowMatrix.getRows().get(1).getCells().get(2).getR())
                        + -1 * (rowMatrix.getRows().get(2).getCells().get(0).getR())
                        + (rowMatrix.getRows().get(2).getCells().get(2).getR());

                int gy = -1 * (rowMatrix.getRows().get(0).getCells().get(0).getR())
                        + -2 * (rowMatrix.getRows().get(0).getCells().get(1).getR())
                        + -1 * (rowMatrix.getRows().get(0).getCells().get(2).getR())
                        + (rowMatrix.getRows().get(2).getCells().get(0).getR())
                        + 2 * (rowMatrix.getRows().get(2).getCells().get(1).getR())
                        + (rowMatrix.getRows().get(2).getCells().get(2).getR());

                int g = (int) (Math.sqrt(gx * gx + gy * gy));
                double direction = Math.atan2(gy, gx);
                int roundedDirection = new Gradient().roundedDirection(direction);

                if(maxG < g){
                    maxG = g;
                }

                Gradient gradient = new Gradient();
                gradient.setG(g);
                gradient.setDirection(roundedDirection);
                gradientRow.addCell(gradient);

            }

            gradientRowMatrix.addRow(gradientRow);
        }

        double factor = 255.0 / maxG;

        int newHeight = gradientRowMatrix.getRows().size();
        int newWidth = gradientRowMatrix.getRows().get(0).getCells().size();

        for(int y = 0; y < newHeight; y++){
            for (int x = 0; x < newWidth; x++){
                Gradient gradient = gradientRowMatrix.getRows().get(y).getCells().get(x);
                int normalizedGradient = (int) (gradient.getG() * factor);
                gradient.setG(normalizedGradient);

                gradient.threshold(gradient, upperThreshold, lowerThreshold);
            }
        }

        return gradientRowMatrix;
    }

    public static RowMatrix<Gradient> suppress(RowMatrix<Gradient> rowMatrix){
        int height = rowMatrix.getRows().size() - 1;
        int width = rowMatrix.getRows().get(0).getCells().size() - 1;

        RowMatrix<Gradient> suppressedGradientMatrix = new RowMatrix<>();

        for(int y = 1; y < height; y++){
            Row<Gradient> row = new Row<>();
            for (int x = 1; x < width; x++){
                Gradient gradient = new Gradient();

                int direction = (int) rowMatrix.getRows().get(y).getCells().get(x).getDirection();
                Gradient g = rowMatrix.getRows().get(y).getCells().get(x);
                gradient.setStrength(g.isStrong());

                if(direction == 0 && (rowMatrix.getRows().get(y).getCells().get(x - 1).getG() > g.getG()
                        || rowMatrix.getRows().get(y).getCells().get(x + 1).getG() > g.getG())){
                    gradient.setG(0);
                }

                else if(direction == 45 && (rowMatrix.getRows().get(y - 1).getCells().get(x + 1).getG() > g.getG()
                        || rowMatrix.getRows().get(y + 1).getCells().get(x - 1).getG() > g.getG())){
                    gradient.setG(0);
                }

                else if(direction == 90 && (rowMatrix.getRows().get(y - 1).getCells().get(x).getG() > g.getG()
                        || rowMatrix.getRows().get(y+ 1).getCells().get(x).getG() > g.getG())){
                    gradient.setG(0);
                }

                else if(direction == 135 && (rowMatrix.getRows().get(y - 1).getCells().get(x - 1).getG() > g.getG()
                        || rowMatrix.getRows().get(y + 1).getCells().get(x + 1).getG() > g.getG())){
                    gradient.setG(0);
                }

                else {
                    gradient.setG(g.getG());
                }

                row.addCell(gradient);

            }

            suppressedGradientMatrix.addRow(row);
        }

        return suppressedGradientMatrix;

    }

    public static RowMatrix<Gradient> trackConnection(RowMatrix<Gradient> rowMatrix){
        int height = rowMatrix.getRows().size() - 1;
        int width = rowMatrix.getRows().get(0).getCells().size() - 1;

        RowMatrix<Gradient> newRowMatrix = new RowMatrix<Gradient>();

        for(int y = 1; y < height; y++){
            Row<Gradient> row = new Row<>();

            for (int x = 1; x < width; x++){
                Gradient gradient = rowMatrix.getRows().get(y).getCells().get(x);
                Gradient newGradient = gradient;
                if(!(gradient.isStrong())){
                    boolean strongNeighbor = false;
                    for(int i = y - 1; i < y + 1; i++){
                        for(int j = x - 1; j < x + 1; j++){
                            if (rowMatrix.getRows().get(i).getCells().get(j).isStrong()) {
                                strongNeighbor = true;
                                break;
                            }
                        }
                    }

                    if(!strongNeighbor){
                        newGradient.setG(0);
                    }
                }

                row.addCell(newGradient);

            }

            newRowMatrix.addRow(row);
        }
        return newRowMatrix;

    }

}
