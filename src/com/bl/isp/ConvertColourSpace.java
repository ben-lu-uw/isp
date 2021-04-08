package com.bl.isp;

import java.util.Arrays;

public class ConvertColourSpace {
    public static double[] rgbToXYZ(int r, int g, int b){
        double rv = r/255.0;
        double gv = g/255.0;
        double bv = b/255.0;

        //Inverse sRGB companding
        if(rv > 0.04045){
            rv = Math.pow(((rv + 0.055)/1.055), 2.4);
        }
        else{
            rv = rv / 12.92;
        }

        if(gv > 0.04045){
            gv = Math.pow(((gv + 0.055)/1.055), 2.4);
        }
        else{
            gv = gv / 12.92;
        }

        if(bv > 0.04045){
            bv = Math.pow(((bv + 0.055)/1.055), 2.4);
        }
        else{
            bv = bv / 12.92;
        }

        /*
        sRGB standardized matrix
        source: http://www.brucelindbloom.com/index.html?Eqn_RGB_XYZ_Matrix.html
        0.4124564  0.3575761  0.1804375
        0.2126729  0.7151522  0.0721750
        0.0193339  0.1191920  0.9503041
        */

        double x = rv * 0.4124564 + gv * 0.3575761 + bv * 0.1804375;
        double y = rv * 0.2126729 + gv * 0.7151522 + bv * 0.0721750;
        double z = rv * 0.0193339 + gv * 0.1191920 + bv * 0.9503041;

        return new double[] {x, y, z};
    }

    //https://en.wikipedia.org/wiki/Illuminant_D65#Definition
    //http://www.brucelindbloom.com/index.html?Eqn_XYZ_to_Lab.html
    //https://en.wikipedia.org/wiki/Color_difference#CIEDE2000
    public static double[] xyzToLAB(double x, double y, double z){

        //Using CIE standards
        double e = 0.008856;
        double k = 903.3;

        //Using D65 white point
        double xr = x/0.95047;
        double yr = y/1.00;
        double zr = z/1.08883;

        double fx;
        double fy;
        double fz;

        if(xr > e){
            fx = Math.cbrt(xr);
        }
        else {
            fx = (k * xr + 16)/116;
        }

        if(yr > e){
            fy = Math.cbrt(yr);
        }
        else {
            fy = (k * yr + 16)/116;
        }

        if(zr > e){
            fz = Math.cbrt(zr);
        }
        else {
            fz = (k * zr + 16)/116;
        }

        double L = 116 * fy - 16;
        double a = 500 * (fx - fy);
        double b = 200 * (fy - fz);

        return new double[] {L, a, b};
    }

    public static double[] rgbToLAB(int r, int g, int b){
        double[] xyz = rgbToXYZ(r, g, b);
        return xyzToLAB(xyz[0], xyz[1], xyz[2]);
    }

    public static int rgbToGrayscale(int p){
        int r = (p >> 16)&0xff;
        int g = (p >> 8)&0xff;
        int b = p&0xff;

        return (int)(0.2126 * r + 0.7152 * g + 0.0722 * b);
    }

}
