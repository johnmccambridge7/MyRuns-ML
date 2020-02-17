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
        p = WekaClassifier.N7abebd300(i);
        return p;
    }
    static double N7abebd300(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 58.593059) {
            p = WekaClassifier.N640885591(i);
        } else if (((Double) i[2]).doubleValue() > 58.593059) {
            p = 2;
        }
        return p;
    }
    static double N640885591(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 128.121602) {
            p = WekaClassifier.N84635d62(i);
        } else if (((Double) i[0]).doubleValue() > 128.121602) {
            p = 1;
        }
        return p;
    }
    static double N84635d62(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 0;
        } else if (((Double) i[11]).doubleValue() <= 2.138015) {
            p = WekaClassifier.N57add6e53(i);
        } else if (((Double) i[11]).doubleValue() > 2.138015) {
            p = WekaClassifier.N78f7c2075(i);
        }
        return p;
    }
    static double N57add6e53(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 0;
        } else if (((Double) i[4]).doubleValue() <= 0.815825) {
            p = 0;
        } else if (((Double) i[4]).doubleValue() > 0.815825) {
            p = WekaClassifier.N5875b61d4(i);
        }
        return p;
    }
    static double N5875b61d4(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 0.792774) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() > 0.792774) {
            p = 0;
        }
        return p;
    }
    static double N78f7c2075(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() <= 7.363358) {
            p = 2;
        } else if (((Double) i[3]).doubleValue() > 7.363358) {
            p = 1;
        }
        return p;
    }
}