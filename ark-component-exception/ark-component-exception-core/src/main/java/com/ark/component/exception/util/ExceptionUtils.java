package com.ark.component.exception.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class ExceptionUtils {

    public static String stackTraceToString(Throwable cause) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        cause.printStackTrace(pout);
        pout.flush();
        try {
            return out.toString();
        } finally {
            try {
                out.close();
            } catch (IOException ignore) {
                // ignore as should never happen
            }
        }
    }
}
