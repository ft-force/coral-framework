package com.ftf.coral.core.io;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ftf.coral.CoralCore;

public class StreamUtil {

    private static final int ZERO = 0;
    private static final int NEGATIVE_ONE = -1;

    public static void close(final Closeable closeable) {
        if (closeable != null) {
            if (closeable instanceof Flushable) {
                try {
                    ((Flushable)closeable).flush();
                } catch (IOException ignored) {
                }
            }

            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        int numToRead = CoralCore.ioBufferSize;
        byte[] buffer = new byte[numToRead];

        int totalRead = ZERO;
        int read;

        while ((read = input.read(buffer, ZERO, numToRead)) >= ZERO) {
            output.write(buffer, ZERO, read);
            totalRead = totalRead + read;
        }

        output.flush();
        return totalRead;
    }

    public static byte[] readAvailableBytes(final InputStream input) throws IOException {
        int numToRead = input.available();
        byte[] buffer = new byte[numToRead];

        int totalRead = ZERO;
        int read;

        while ((totalRead < numToRead) && (read = input.read(buffer, totalRead, numToRead - totalRead)) >= ZERO) {
            totalRead = totalRead + read;
        }

        if (totalRead < numToRead) {
            throw new IOException("Failed to completely read InputStream");
        }

        return buffer;
    }

    public static boolean compare(InputStream input1, InputStream input2) throws IOException {
        if (!(input1 instanceof BufferedInputStream)) {
            input1 = new BufferedInputStream(input1);
        }
        if (!(input2 instanceof BufferedInputStream)) {
            input2 = new BufferedInputStream(input2);
        }
        int ch = input1.read();
        while (ch != NEGATIVE_ONE) {
            int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }
        int ch2 = input2.read();
        return (ch2 == NEGATIVE_ONE);
    }
}
