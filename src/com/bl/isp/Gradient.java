package com.bl.isp;

import java.util.ArrayList;
import java.util.Arrays;

public class Gradient implements Combinable<Gradient> {
    private int x;
    private int y;
    private int g;
    private double direction;
    private boolean strength;

    public void threshold(Gradient gradient, int upper, int lower){
        int value = gradient.getG();

        if(value >= upper){
            gradient.setStrength(true);
        }

        else if(value >= lower){
            gradient.setStrength(false);
        }

        else{
            gradient.setG(0);
            gradient.setStrength(false);
        }

    }


    public int roundedDirection(double direction){
        direction = Math.toDegrees(direction);

        if(direction < 0){
            direction += 180;
        }

        ArrayList<Integer> arrayList = new ArrayList<Integer>(Arrays.asList(0, 45, 90, 135, 180));
        double difference = 180;
        int angle = 1;

        for (Integer integer : arrayList) {
            double d = Math.abs(direction - integer);
            if (d < difference) {
                difference = d;
                angle = integer;
            }
        }

        if(angle == 180){
            angle = 0;
        }
        return angle;

    }

    @Override
    public String toString() {
        return "Gradient{" +
                "x=" + x +
                ", y=" + y +
                ", g=" + g +
                ", direction=" + direction +
                ", strength=" + strength +
                '}';
    }

    public boolean isStrong() {
        return strength;
    }

    public double getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getG() {
        return g;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setG(int g) {
        this.g = g;
    }


    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void setStrength(boolean strength) {
        this.strength = strength;
    }


    @Override
    public Gradient combine(Gradient o) {
        return null;
    }
}
