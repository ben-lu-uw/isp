package com.bl.bimp;

public class Kernel {
    public int kernelSize(int sigma){
        return sigma * 3;
    }

    public int kernelMidpoint(int sigma){
        int size = kernelSize(sigma);
        return ((size - 1)/2);
    }

    public int kernelFit(int sigma, int imgDimension){
        int shift = kernelMidpoint(sigma);
        return imgDimension - (2 * shift);
    }

}
