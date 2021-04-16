package com.bl.isp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class isp {
    public static void main(String[] args) throws IOException {
        String inputPath = "FULL INPUT PATH";
        String outputPath = "FULL OUTPUT PATH";

        RowMatrix<Pixel> rgbMatrix = ReadImage.rgbMatrix(inputPath);
        RowMatrix<Pixel> blurredMatrix = GaussianBlur.apply5x5Filter(rgbMatrix);
        RowMatrix<Gradient> gradientMatrix = Edge.generate(blurredMatrix, 50, 20);
        RowMatrix<Gradient> suppressedMatrix = Edge.suppress(gradientMatrix);
        RowMatrix<Gradient> finalMatrix = Edge.trackConnection(suppressedMatrix);

        int height = finalMatrix.getRows().size();
        int width = finalMatrix.getRows().get(0).getCells().size();

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        for (int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int g = finalMatrix.getRows().get(y).getCells().get(x).getG();
                int bit = (g << 16) | (g << 8) | g;

                bufferedImage.setRGB(x, y, bit);
            }
        }

        ImageIO.write(bufferedImage, "png", new File(outputPath));
    }
}
