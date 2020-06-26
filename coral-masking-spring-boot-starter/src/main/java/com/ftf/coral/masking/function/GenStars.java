package com.ftf.coral.masking.function;

public class GenStars {

    public static String genStars(int length) {

        char[] stars = new char[length];
        for (int i = 0; i < length; i++) {
            stars[i] = '*';
        }

        String result = new String(stars);
        return result;
    }
}
