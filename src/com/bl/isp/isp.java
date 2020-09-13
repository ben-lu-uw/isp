package com.bl.isp;

import java.io.IOException;

public class isp {
    public static void main(String[] args) {
        String inputPath = "ENTER IMAGE PATH";
        String bOutputPath = "ENTER MID OUTPUT PATH";
        String oOutputPath = "ENTER FINAL OUTPUT PATH";

        GaussianBlur blur = new GaussianBlur();
        try {
            blur.applyBlur(3, inputPath, bOutputPath);
            Outline.combineOutline(bOutputPath, oOutputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
