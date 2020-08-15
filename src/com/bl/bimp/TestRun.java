package com.bl.bimp;

public class TestRun {
    public static void main(String[] args) {
        String inputDirectory = "C:\\Users\\avtea\\Desktop\\Input";
        String outputDirectory = "C:\\Users\\avtea\\Desktop\\Output\\img";

        ChromaKey removeColor = new ChromaKey();

        try{
            removeColor.multiProcess(inputDirectory, outputDirectory);
        }catch (Exception e){
            System.out.println(e);
        }

    }
}
