package jchess.util;

import java.util.Arrays;

public class Logger {
    private int level;
    
    public static void log(String string) {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        String aboveFunc = ste[1].getMethodName();
        System.out.println("!!!\n  " + Arrays.toString(ste) + "\n" + aboveFunc + "\n!!!");
        // TODO do something cool with this
        //  log print to console the 'string' and the method above it from where
        //    Logger.log() was called
    }
}
