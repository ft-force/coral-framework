package com.ftf.coral.util;

public class CharUtils {

    /**
     * 判断指定字符是否在字符串中是否存在
     */
    public static boolean equalsOne(final char c, final char[] match) {
        for (char aMatch : match) {
            if (c == aMatch) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从指定的索引开始，在source中找到第一个在match中得不到匹配的字符的索引
     */
    public static int findFirstDiff(final char[] source, final int index, final char[] match) {
        for (int i = index; i < source.length; i++) {
            if (!equalsOne(source[i], match)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 从指定的索引开始，在source中找到第一个能在match中得到匹配的字符的索引
     */
    public static int findFirstEqual(final char[] source, final int index, final char match) {
        for (int i = index; i < source.length; i++) {
            if (source[i] == match) {
                return i;
            }
        }
        return -1;
    }

    public static int findFirstEqual(final char[] source, final int index, final char[] match) {
        for (int i = index; i < source.length; i++) {
            if (equalsOne(source[i], match)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 判断字符串是否是数字
     */
    public static boolean isDigit(final char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Returns <code>true</code> if character is a white space ({@code <= ' '}). White space definition is taken from
     * String class (see: <code>trim()</code>). This method has different results then
     * <code>Character#isWhitespace</code>."
     */
    public static boolean isWhitespace(final char c) {
        return c <= ' ';
    }

    public static char int2hex(final int i) {
        return HEX_CHARS[i];
    }

    public static final char[] HEX_CHARS =
        new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
}
