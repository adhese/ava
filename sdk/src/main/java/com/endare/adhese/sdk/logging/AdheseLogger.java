package com.endare.adhese.sdk.logging;

import android.util.Log;

public final class AdheseLogger {

    private static final String GLOBAL_TAG = "Adhese";

    public static String SDK_EVENT = "SDK EVENT";
    public static String NETWORK_REQUEST = "NETWORK - REQUEST";
    public static String NETWORK_RESPONSE = "NETWORK - RESPONSE";

    public static void log(String message) {

        if (!isLoggingEnabled()) {
            return;
        }

        Log.d(GLOBAL_TAG, message);
    }

    public static void log(String tag, String message) {
        log( tag, "", message);
    }

    public static void log(String tag, String event, String message) {
        log(String.format("%s | %s | %s", tag, event, message));
    }

    private static boolean isLoggingEnabled() {
        return true;
    }

}
