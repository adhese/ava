package com.endare.adhese.sdk;

public final class Adhese {

    private static boolean isInitialised;
    private static AdheseOptions options;

    public static void initialise(AdheseOptions adheseOptions) {

        if (isInitialised) {
            return;
        }

        options = adheseOptions;
        isInitialised = true;
    }




}
