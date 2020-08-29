package com.bl.isp;

public class Pixel implements Combinable<Pixel>{
    private int bit;
    private int a;
    private int r;
    private int g;
    private int b;

    public int getBit() {
        return bit;
    }

    public int getA() {
        return a;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public void setBit(int bit) {
        this.bit = bit;
    }

    public void setA(int a) {
        this.a = a;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setG(int g) {
        this.g = g;
    }

    public void setB(int b) {
        this.b = b;
    }

    public Pixel(int bit) {
        this.bit = bit;
    }

    public Pixel combine(Pixel other){
        int thisBit = this.getBit();
        int otherBit = other.getBit();
        int finalBit = thisBit | otherBit;
        Pixel p = new Pixel(finalBit);
        return p;
    }

    @Override
    public String toString() {
        return "imageProcessor.Pixel{" +
                "bit=" + bit +
                ", a=" + a +
                ", r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }


}
