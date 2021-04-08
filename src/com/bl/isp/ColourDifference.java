package com.bl.isp;

public class ColourDifference {

    public static double deltaLPrime(double L1, double L2){
        return L2 - L1;
    }

    public static double lBarPrime(double L1, double L2){
        return (L1 + L2)/2;
    }

    public static double cStar(double a, double b){
        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    public static double aPrime(double a, double cBar){
        return a + (a/2) * (1 - Math.sqrt(Math.pow(cBar, 7)/(Math.pow(cBar, 7) + Math.pow(25, 7))));
    }

    public static double cPrime(double aPrime, double b){
        return Math.sqrt(Math.pow(aPrime, 2) + Math.pow(b, 2));
    }

    public static double hPrime(double aPrime, double b){
        if(aPrime == 0 && b == 0){
            return 0;
        }
        else{
            double angle = Math.toDegrees(Math.atan2(b, aPrime));
            if(angle < 0){
                angle += 360;
            }
            return angle;
        }
    }

    public static double deltahPrime(double hPrime1, double hPrime2, double cPrime1, double cPrime2){
        if(cPrime1 * cPrime2 == 0){
            return 0;
        }
        if (Math.abs(hPrime1 - hPrime2) <= 180){
            return hPrime2 - hPrime1;
        }
        if (Math.abs(hPrime1 - hPrime2) > 180 && hPrime2 <= hPrime1){
            return hPrime2 - hPrime1 + 360;
        }

        if (Math.abs(hPrime1 - hPrime2) > 180 && hPrime2 > hPrime1){
            return hPrime2 - hPrime1 - 360;
        }
        else {
            return 0;
        }
    }

    public static double hBarPrime(double hPrime1, double hPrime2, double cPrime1, double cPrime2){
        if(cPrime1 * cPrime2 == 0){
            return hPrime1 + hPrime2;
        }
        if(Math.abs(hPrime1 - hPrime2) <= 180){
            return (hPrime1 + hPrime2)/2;
        }
        if(Math.abs(hPrime1 - hPrime2) > 180 && hPrime1 + hPrime2 < 360){
            return (hPrime1 + hPrime2 + 360)/2;
        }
        if(Math.abs(hPrime1 - hPrime2) > 180 && hPrime1 + hPrime2 >= 360){
            return (hPrime1 + hPrime2 - 360)/2;
        }
        else {
            return 0;
        }
    }

    public static double deltaHPrime(double deltahPrime, double cPrime1, double cPrime2){
        return 2 * Math.sqrt(cPrime1 * cPrime2) * Math.sin(Math.toRadians(deltahPrime/2));
    }

    public static double t(double hBarPrime){
        return 1 - 0.17 * Math.cos(Math.toRadians(hBarPrime - 30)) +
                0.24 * Math.cos(Math.toRadians(2 * hBarPrime))
                + 0.32 * Math.cos(Math.toRadians(3 * hBarPrime + 6)) -
                0.2 * Math.cos(Math.toRadians(4 * hBarPrime - 63));
    }

    public static double sL(double lBarPrime){
        return 1 + (0.015 * Math.pow(lBarPrime - 50, 2))/Math.sqrt(20 + Math.pow(lBarPrime - 50, 2));
    }

    public static double sC(double cBarPrime){
        return 1 + 0.045 * cBarPrime;
    }

    public static double sH(double cBarPrime, double t){
        return 1 + 0.015 * cBarPrime * t;
    }

    public static double rT(double cBarPrime, double hBarPrime){
        return -2 * Math.sqrt(Math.pow(cBarPrime, 7)/(Math.pow(cBarPrime, 7) + Math.pow(25, 7))) *
                Math.sin(Math.toRadians(60 * Math.exp(-Math.pow(((hBarPrime - 275)/25), 2))));
    }

    //Using kL = kC = kH = 1
    public static double deltaE(double L1, double a1, double b1, double L2, double a2, double b2){

        double deltaLPrime = deltaLPrime(L1, L2);

        double cBar = (cStar(a1, b1) + cStar(a2, b2))/2;

        double aPrime1 = aPrime(a1, cBar);
        double aPrime2 = aPrime(a2, cBar);

        double cPrime1 = cPrime(aPrime1, b1);
        double cPrime2 = cPrime(aPrime2, b2);

        double deltaCPrime = cPrime2 - cPrime1;
        double cBarPrime = (cPrime1 + cPrime2)/2;

        double hPrime1 = hPrime(aPrime1, b1);
        double hPrime2 = hPrime(aPrime2, b2);

        double deltaHPrime = deltaHPrime(deltahPrime(hPrime1, hPrime2, cPrime1, cPrime2), cPrime1, cPrime2);
        double hBarPrime = hBarPrime(hPrime1, hPrime2, cPrime1, cPrime2);

        double sL = sL(lBarPrime(L1, L2));
        double sC = sC(cBarPrime);
        double sH = sH(cBarPrime, t(hBarPrime));
        double rT = rT(cBarPrime, hBarPrime);

        return Math.sqrt(Math.pow(deltaLPrime/sL, 2) + Math.pow(deltaCPrime/sC, 2) + Math.pow(deltaHPrime/sH, 2) +
                rT * (deltaCPrime/sC) * (deltaHPrime/sH));
    }

    public static void main(String[] args) {

        double d = deltaE(50,  2.5, 0, 61, -5, 29);
        System.out.println(d);

    }

}
