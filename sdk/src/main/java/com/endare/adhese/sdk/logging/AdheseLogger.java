package com.endare.adhese.sdk.logging;

import android.util.Log;

public final class AdheseLogger {

    public static String SDK_EVENT = "SDK EVENT";
    public static String NETWORK_REQUEST = "NETWORK - REQUEST";
    public static String NETWORK_RESPONSE = "NETWORK - RESPONSE";

    public static void log(String tag, String event, String message) {
        log(tag, String.format("%s %s", event, message));
    }

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
