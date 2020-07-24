package com.ftf.coral.util;

import java.io.UnsupportedEncodingException;

import com.ftf.coral.CoralCore;

public class StringUtils {

    public static void trimAll(final String... strings) {
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            if (string != null) {
                strings[i] = string.trim();
            }
        }
    }

    /**
     * 判断字符串s是否以c开头
     * 
     * @param s
     * @param c
     * @return
     */
    public static boolean startsWithChar(String s, char c) {
        if (s.length() == 0) {
            return false;
        }
        return s.charAt(0) == c;
    }

    /**
     * 判断字符串是否只包含数字
     * 
     * @param string
     * @return
     */
    public static boolean containsOnlyDigits(final CharSequence string) {
        int size = string.length();
        for (int i = 0; i < size; i++) {
            char c = string.charAt(i);
            if (!(CharUtils.isDigit(c))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将src字符串根据指定的字符串d进行拆分，拆分为几个字符串数组
     * 
     * @param src
     * @param d
     * @return
     */
    public static String[] splitc(final String src, final String d) {
        if ((d.length() == 0) || (src.length() == 0)) {
            return new String[] { src };
        }
        return splitc(src, d.toCharArray());
    }

    /**
     * 将source字符串根据delimiters进行拆分
     *
     * @param src
     *            source to examine
     * @param delimiters
     *            char array with delimiter characters
     *
     * @return array of tokens
     */
    public static String[] splitc(final String src, final char[] delimiters) {
        if ((delimiters.length == 0) || (src.length() == 0)) {
            return new String[] { src };
        }
        char[] srcc = src.toCharArray();

        int maxparts = srcc.length + 1;
        int[] start = new int[maxparts];
        int[] end = new int[maxparts];

        int count = 0;

        start[0] = 0;
        int s = 0, e;
        if (CharUtils.equalsOne(srcc[0], delimiters)) { // string starts with
                                                        // delimiter
            end[0] = 0;
            count++;
            s = CharUtils.findFirstDiff(srcc, 1, delimiters);
            if (s == -1) { // nothing after delimiters
                return new String[] { "", "" };
            }
            start[1] = s; // new start
        }
        while (true) {
            // find new end
            e = CharUtils.findFirstEqual(srcc, s, delimiters);
            if (e == -1) {
                end[count] = srcc.length;
                break;
            }
            end[count] = e;

            // find new start
            count++;
            s = CharUtils.findFirstDiff(srcc, e, delimiters);
            if (s == -1) {
                start[count] = end[count] = srcc.length;
                break;
            }
            start[count] = s;
        }
        count++;
        String[] result = new String[count];
        for (int i = 0; i < count; i++) {
            result[i] = src.substring(start[i], end[i]);
        }
        return result;
    }

    /**
     * 判断字符串是否为空
     * 
     * @param string
     * @return
     */
    public static boolean isEmpty(final CharSequence string) {
        return ((string == null) || (string.length() == 0));
    }

    /**
     * 判断字符串包含空格外的字符
     * 
     * @param string
     * @return
     */
    public static boolean isBlank(final String string) {
        return !isNotBlank(string);
    }

    /**
     * 判断字符串包含空格外的字符
     * 
     * @param string
     * @return
     */
    public static boolean isNotBlank(final String string) {
        return ((string != null) && !containsOnlyWhitespaces(string));
    }

    /**
     * 判断字符串是否只有空格
     * 
     * @param string
     * @return
     */
    public static boolean containsOnlyWhitespaces(final CharSequence string) {
        int size = string.length();
        for (int i = 0; i < size; i++) {
            char c = string.charAt(i);
            if (!CharUtils.isWhitespace(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 忽略大小写，找出subS字符串在给定的src字符串中第一次出现的位置
     * 
     * @param src
     * @param subS
     * @return
     */
    public static int indexOfIgnoreCase(final String src, final String subS) {
        return indexOfIgnoreCase(src, subS, 0, src.length());
    }

    public static int indexOfIgnoreCase(final String src, String sub, int startIndex, int endIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        int srclen = src.length();
        if (endIndex > srclen) {
            endIndex = srclen;
        }

        int sublen = sub.length();
        if (sublen == 0) {
            return startIndex > srclen ? srclen : startIndex;
        }
        sub = sub.toLowerCase();
        int total = endIndex - sublen + 1;
        char c = sub.charAt(0);
        mainloop: for (int i = startIndex; i < total; i++) {
            if (Character.toLowerCase(src.charAt(i)) != c) {
                continue;
            }
            int j = 1;
            int k = i + 1;
            while (j < sublen) {
                char source = Character.toLowerCase(src.charAt(k));
                if (sub.charAt(j) != source) {
                    continue mainloop;
                }
                j++;
                k++;
            }
            return i;
        }
        return -1;
    }

    public static int indexOf(final String src, final char c) {
        return indexOf(src, c, 0, src.length());
    }

    public static int indexOf(final String src, final char c, int startIndex, int endIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        int srclen = src.length();
        if (endIndex > srclen) {
            endIndex = srclen;
        }
        for (int i = startIndex; i < endIndex; i++) {
            if (src.charAt(i) == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 在指定range内查找指定char在指定src字符串中第一次出现的位置，忽略char的大小写
     */
    public static int indexOfIgnoreCase(final String src, char c, int startIndex, int endIndex) {
        if (startIndex < 0) {
            startIndex = 0;
        }
        int srclen = src.length();
        if (endIndex > srclen) {
            endIndex = srclen;
        }
        c = Character.toLowerCase(c);
        for (int i = startIndex; i < endIndex; i++) {
            if (Character.toLowerCase(src.charAt(i)) == c) {
                return i;
            }
        }
        return -1;
    }

    public static int indexOfChars(final String string, final char[] chars) {
        return indexOfChars(string, chars, 0);
    }

    public static int indexOfChars(final String string, final char[] chars, final int startindex) {
        int stringLen = string.length();
        int charsLen = chars.length;
        for (int i = startindex; i < stringLen; i++) {
            char c = string.charAt(i);
            for (int j = 0; j < charsLen; j++) {
                if (c == chars[j]) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static boolean equals(String str1, String str2) {
        return str1 == null ? str2 == null : str1.equals(str2);
    }

    public static boolean isEmpty(Object str) {
        return (str == null || "".equals(str));
    }

    public static String cutPrefix(String string, final String prefix) {
        if (string.startsWith(prefix)) {
            string = string.substring(prefix.length());
        }
        return string;
    }

    public static String cutSuffix(String string, final String suffix) {
        if (string.endsWith(suffix)) {
            string = string.substring(0, string.length() - suffix.length());
        }
        return string;
    }

    public static String cutSurrounding(final String string, final String prefix, final String suffix) {
        int start = 0;
        int end = string.length();

        if (string.startsWith(prefix)) {
            start = prefix.length();
        }
        if (string.endsWith(suffix)) {
            end -= suffix.length();
        }

        if (end <= start) {
            return StringPool.EMPTY;
        }

        return string.substring(start, end);
    }

    public static byte[] getBytes(final String string) {
        try {
            return string.getBytes(CoralCore.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getBytes(final String string, final String charsetName) {
        try {
            return string.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String newString(final byte[] bytes) {
        try {
            return new String(bytes, CoralCore.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String newString(final byte[] bytes, final String charsetName) {
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
