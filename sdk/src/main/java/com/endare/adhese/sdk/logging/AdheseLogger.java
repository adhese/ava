package com.endare.adhese.sdk.logging;

import android.util.Log;

public class AdheseLogger {

    public static String SDK_EVENT = "SDK EVENT";

    public static void log(String tag, String message) {

        if (!isLoggingEnabled()) {
            return;
        }

        Log.d(tag, message);
    }

    private static boolean isLoggingEnabled() {
        return true;
    }

}
