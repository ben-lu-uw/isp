package com.bl.isp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ReadImg {
    public BufferedImage read(String inputPath) throws IOException{
        BufferedImage img;
        File file;
        file = new File(inputPath);
        img = ImageIO.read(file);
        return img;
    }
}

