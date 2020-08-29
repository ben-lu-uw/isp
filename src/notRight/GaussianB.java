package notRight;

import com.bl.isp.ReadImg;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GaussianB {
    public float avg(ArrayList population){
        int populationSize = population.size();
        float total = 0;

        for(int i = 0; i < populationSize; i++){
            total += (int) population.get(i);
        }

        return total/populationSize;
    }

    public double stnDev(ArrayList population){
        int populationSize = population.size();
        float mean = avg(population);
        double deviationTotal = 0;

        for(int i = 0; i < populationSize; i++){
            deviationTotal += Math.pow((int)population.get(i) - mean, 2);
        }

        return Math.sqrt(deviationTotal/populationSize);
    }

    public ArrayList gaussianFunction(ArrayList population){

        ArrayList<Double> arrayList = new ArrayList<Double>();

        float mean = avg(population);
        double euler = Math.exp(1);

        double denominatorExp = (2 * Math.pow(stnDev(population), 2));
        double multiplier = 1/Math.sqrt(2 * Math.PI * Math.pow(stnDev(population), 2));

        for(int i = 0; i < population.size(); i++){
            double exponent = -Math.pow((int) population.get(i) - mean, 2)/denominatorExp;
            double Gx = multiplier * Math.pow(euler, exponent);
            arrayList.add(Gx);
        }

        return arrayList;
    }

    public void applyBlur(String inputPath, String outputPath) throws IOException {

        ReadImg readImg = new ReadImg();
        BufferedImage img = readImg.read(inputPath);
        File file;

        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage outputImg = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

        PixelMatrix matrix = new PixelMatrix();

        try{


            ArrayList fullBitMatrix = matrix.imgToBitMatrix(inputPath);
            ArrayList fullRGBAvgMatrix = matrix. avgRGBMatrix(inputPath);

                for(int y = 0; y < height; y++){

                    for(int x = 0; x < width; x++){
                        int r = 0;
                        int g = 0;
                        int b = 0;
                        int pixel;

                        ArrayList bitMatrixPortion = (ArrayList) (fullBitMatrix.get(y));
                        ArrayList rgbAvgMatrixPortion = (ArrayList) (fullRGBAvgMatrix.get(y));

                        /*
                        int[] avgArray = new int[rgbAvgMatrixPortion.size()];
                        for (int i = 0; i < avgArray.length; i++){
                            avgArray[i] = (int)rgbAvgMatrixPortion.get(i);
                        }

                        int sum = IntStream.of(avgArray).sum();

                         */
                        ArrayList weightList = gaussianFunction(rgbAvgMatrixPortion);

                        double weight = (double)weightList.get(x);

                        r += (((int)(bitMatrixPortion.get(x)) >>> 16)&0xff) * weight;
                        g += (((int)(bitMatrixPortion.get(x)) >>> 8)&0xff) * weight;
                        b += ((int)(bitMatrixPortion.get(x))&0xff) * weight;

                        /*
                        r /= sum;
                        g /= sum;
                        b /= sum;
                        */
                        pixel = (24 << 24) | (r << 16) | (g << 8) | b;

                        outputImg.setRGB(x, y, pixel);
                        
                    }

                }


        }catch (Exception e){
            System.out.println(e);
        }

        file = new File(outputPath);
        ImageIO.write(outputImg, "png", file);

    }

    public static void main(String[] args) throws Exception{
        GaussianB blur = new GaussianB();
        blur.applyBlur("C:\\Users\\avtea\\Desktop\\Input\\b.png", "C:\\Users\\avtea\\Desktop\\Output\\a.png");
    }

}

