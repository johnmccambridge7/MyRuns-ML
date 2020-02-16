package com.example.myruns;

// Generated with Weka 3.8.4
//
// This code is public domain and comes with no warranty.
//
// Timestamp: Sat Feb 15 16:55:25 EST 2020

class WekaClassifier {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier.N25f7c5d50(i);
        return p;
    }
    static double N25f7c5d50(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 11.076863) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 11.076863) {
            p = WekaClassifier.Nb0c02a51(i);
        }
        return p;
    }
    static double Nb0c02a51(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 1;
        } else if (((Double) i[9]).doubleValue() <= 11.424991) {
            p = WekaClassifier.N3a31e4ce2(i);
        } else if (((Double) i[9]).doubleValue() > 11.424991) {
            p = 2;
        }
        return p;
    }
    static double N3a31e4ce2(Object []i) {
        double p = Double.NaN;
        if (i[25] == null) {
            p = 1;
        } else if (((Double) i[25]).doubleValue() <= 3.665618) {
            p = 1;
        } else if (((Double) i[25]).doubleValue() > 3.665618) {
            p = 2;
        }

        return p;
    }
}