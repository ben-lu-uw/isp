package com.bl.isp;

public class Weight implements Combinable<Weight>{
    private int x;
    private int y;
    private double weight;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Weight(int x, int y, double weight) {
        this.x = x;
        this.y = y;
        this.weight = weight;
    }

    public Weight(double weight){
        this.weight = weight;
    }

    public Weight combine(Weight other){
        return new Weight(this.x, this.y, (this.weight + other.weight)/2);
    }

    @Override
    public String toString() {
        return "imageProcessor.Weight{" +
                "x=" + x +
                ", y=" + y +
                ", weight=" + weight +
                '}';
    }
}
