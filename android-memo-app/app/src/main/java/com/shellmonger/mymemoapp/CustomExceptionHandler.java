package com.shellmonger.mymemoapp;

import android.app.Activity;
import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

public class CustomExceptionHandler implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler defaultHandler;
    private Activity activity;

    public CustomExceptionHandler(Activity activity) {
        this.defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.activity = activity;
    }

    public void uncaughtException(Thread t, Throwable e) {
        final Writer stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String stacktrace = stringWriter.toString();
        printWriter.close();

        Log.e("CRASH", stacktrace);
        Log.e("CRASH", e.toString());

        defaultHandler.uncaughtException(t, e);
    }
}
