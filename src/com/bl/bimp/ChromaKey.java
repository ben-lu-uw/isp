package com.bl.bimp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ChromaKey {

    public void process(String inputPath, String outputPath) throws IOException{

        ReadImg readImg = new ReadImg();
        BufferedImage img = readImg.read(inputPath);
        File file;

        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage outputImg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){

                int pixel = img.getRGB(x, y);
                int a = (pixel >> 24)&0xff;
                int r = (pixel >> 16)&0xff;
                int g = (pixel >> 8)&0xff;
                int b = pixel&0xff;

                    if (a != 0 && r > 240 && g > 240 && b > 240){
                    pixel = (0 << 24) | (0 << 16) | (0 << 8) | 0;
                }

                outputImg.setRGB(x, y, pixel);
            }
        }

        file = new File(outputPath);
        ImageIO.write(outputImg, "png", file);

    }

    public void multiProcess(String inputDirectory, String outputDirectory) throws Exception{
        File folder = new File(inputDirectory);
        File[] files = folder.listFiles();

        for (int i = 0; i < files.length; i++){
            process(files[i].getPath(), outputDirectory + i + ".png");
        }
    }

}
