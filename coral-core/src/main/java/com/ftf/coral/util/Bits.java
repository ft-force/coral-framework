package com.ftf.coral.util;

/**
 * Part a copy of <code>java.io.Bits</code>, which is for unknown reason package local. Utility methods for
 * packing/unpacking primitive values in/out of byte arrays using big-endian byte ordering.
 */
public class Bits {

    public static boolean getBoolean(final byte[] b, final int off) {
        return b[off] != 0;
    }

    public static char getChar(final byte[] b, final int off) {
        return (char)(((b[off + 1] & 0xFF)) + ((b[off]) << 8));
    }

    public static short getShort(final byte[] b, final int off) {
        return (short)(((b[off + 1] & 0xFF)) + ((b[off]) << 8));
    }

    public static int getInt(final byte[] b, final int off) {
        return ((b[off + 3] & 0xFF)) + ((b[off + 2] & 0xFF) << 8) + ((b[off + 1] & 0xFF) << 16) + ((b[off]) << 24);
    }

    public static float getFloat(final byte[] b, final int off) {
        int i = getInt(b, off);
        return Float.intBitsToFloat(i);
    }

    public static long getLong(final byte[] b, final int off) {
        return ((b[off + 7] & 0xFFL)) + ((b[off + 6] & 0xFFL) << 8) + ((b[off + 5] & 0xFFL) << 16)
            + ((b[off + 4] & 0xFFL) << 24) + ((b[off + 3] & 0xFFL) << 32) + ((b[off + 2] & 0xFFL) << 40)
            + ((b[off + 1] & 0xFFL) << 48) + (((long)b[off]) << 56);
    }

    public static double getDouble(final byte[] b, final int off) {
        long j = getLong(b, off);
        return Double.longBitsToDouble(j);
    }

    public static void putBoolean(final byte[] b, final int off, final boolean val) {
        b[off] = (byte)(val ? 1 : 0);
    }

    public static void putChar(final byte[] b, final int off, final char val) {
        b[off + 1] = (byte)(val);
        b[off] = (byte)(val >>> 8);
    }

    public static void putShort(final byte[] b, final int off, final short val) {
        b[off + 1] = (byte)(val);
        b[off] = (byte)(val >>> 8);
    }

    public static void putInt(final byte[] b, final int off, final int val) {
        b[off + 3] = (byte)(val);
        b[off + 2] = (byte)(val >>> 8);
        b[off + 1] = (byte)(val >>> 16);
        b[off] = (byte)(val >>> 24);
    }

    public static void putFloat(final byte[] b, final int off, final float val) {
        int i = Float.floatToIntBits(val);
        putInt(b, off, i);
    }

    public static void putLong(final byte[] b, final int off, final long val) {
        b[off + 7] = (byte)(val);
        b[off + 6] = (byte)(val >>> 8);
        b[off + 5] = (byte)(val >>> 16);
        b[off + 4] = (byte)(val >>> 24);
        b[off + 3] = (byte)(val >>> 32);
        b[off + 2] = (byte)(val >>> 40);
        b[off + 1] = (byte)(val >>> 48);
        b[off] = (byte)(val >>> 56);
    }

    public static void putDouble(final byte[] b, final int off, final double val) {
        long j = Double.doubleToLongBits(val);
        putLong(b, off, j);
    }

    // ---------------------------------------------------------------- mask

    public static boolean isSet(final byte value, final byte mask) {
        return (value & mask) == mask;
    }

    public static boolean isSet(final int value, final int mask) {
        return (value & mask) == mask;
    }

    public static boolean notSet(final int value, final int mask) {
        return (value & mask) != mask;
    }

    /**
     * Returns value with the bit corresponding to the mask set (if setBit is true) or cleared (if setBit is false).
     */
    public static int set(final int value, final int mask, final boolean setBit) {
        return setBit ? value | mask : value & ~mask;
    }

    /**
     * Returns value with the bit corresponding to the mask set (if setBit is true) or cleared (if setBit is false).
     */
    public static byte set(final byte value, final byte mask, final boolean setBit) {
        return (byte)(setBit ? value | mask : value & ~mask);
    }
}