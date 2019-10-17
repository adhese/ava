package com.endare.adhese.sdk;

import android.content.Context;

import com.endare.adhese.sdk.api.AdheseAPI;
import com.endare.adhese.sdk.logging.AdheseLogger;

import androidx.annotation.NonNull;

public final class Adhese {

    private static final String TAG = Adhese.class.getSimpleName();

    private static boolean isInitialised;
    private static AdheseAPI adheseAPI;

    private static AdheseOptions options;

    public static void initialise(@NonNull Context context, AdheseOptions adheseOptions) {

        if (isInitialised) {
            AdheseLogger.log(AdheseLogger.SDK_EVENT, TAG, "Tried initialising the SDK but it was already initialised.");
            return;
        }

        options = adheseOptions;
        adheseAPI = new AdheseAPI(context, adheseOptions);
        isInitialised = true;

        AdheseLogger.log(AdheseLogger.SDK_EVENT, TAG, "Initialised the SDK.");
    }

    public static void loadAds() {

        if (!isInitialised) {
            throw new IllegalStateException("Tried loading ads but Adhese has not initialised yet.");
        }

        adheseAPI.getAds();
    }

}
