package notRight;

import com.bl.bimp.ReadImg;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PixelMatrix {
    public ArrayList imgToBitMatrix(String inputPath) throws IOException{

        ReadImg readImg = new ReadImg();
        BufferedImage img = readImg.read(inputPath);

        int width = img.getWidth();
        int height = img.getHeight();

        ArrayList<ArrayList<Integer>> arrayList = new ArrayList<>(width);

        for (int x = 0; x < width; x++){
            arrayList.add(new ArrayList());
        }

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                int pixel = img.getRGB(x, y);

                arrayList.get(y).add(pixel);

            }
        }

        return arrayList;
    }

    public ArrayList bitToRGBMatrix(String inputPath) throws IOException{
        ArrayList bitMatrix = imgToBitMatrix(inputPath);
        int outerSize = bitMatrix.size();

        int c = 3;
        ArrayList<ArrayList<ArrayList<Integer>>> rgbMatrix = new ArrayList<>(outerSize);

        for (int y = 0; y < outerSize; y++){
            int innerSize = ((ArrayList) bitMatrix.get(y)).size();
            rgbMatrix.add(new ArrayList<ArrayList<Integer>>(innerSize));

            for (int x = 0; x < innerSize; x++){
                rgbMatrix.get(y).add(new ArrayList<Integer>(c));
                int r = (((int)((ArrayList)bitMatrix.get(y)).get(x)) >> 16)&0xff;
                int g = (((int)((ArrayList)bitMatrix.get(y)).get(x)) >> 8)&0xff;
                int b = ((int)((ArrayList)bitMatrix.get(y)).get(x))&0xff;
                ArrayList<Integer> rgbGroup = new ArrayList<>(Arrays.asList(r, g, b));

                for (int z = 0; z < c; z++){
                    rgbMatrix.get(y).get(x).add(z, rgbGroup.get(z));
                }
            }
        }

        return rgbMatrix;
    }

    public ArrayList avgRGBMatrix(String inputPath) throws IOException{
        ArrayList rgbMatrix = bitToRGBMatrix(inputPath);
        int outerSize = rgbMatrix.size();
        int c = 3;
        ArrayList<ArrayList<Integer>> avgMatrix = new ArrayList<>(outerSize);

        for (int y = 0; y < outerSize; y++){
            int innerSize = ((ArrayList) rgbMatrix.get(y)).size();
            avgMatrix.add(new ArrayList<>());

            for (int x = 0; x < innerSize; x++){
                int total = 0;

                for (int z = 0; z < c; z++){
                    total += ((int)((ArrayList)((ArrayList)rgbMatrix.get(y)).get(x)).get(z));

                    if (z == (c-1)){
                        int mean = total/3;
                        avgMatrix.get(y).add(x, mean);
                    }
                }
            }
        }

        return avgMatrix;
    }

}
