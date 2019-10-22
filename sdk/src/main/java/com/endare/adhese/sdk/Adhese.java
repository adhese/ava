package com.endare.adhese.sdk;

import android.content.Context;

import com.endare.adhese.sdk.api.APICallback;
import com.endare.adhese.sdk.api.AdheseAPI;
import com.endare.adhese.sdk.logging.AdheseLogger;
import com.endare.adhese.sdk.parameters.Device;
import com.endare.adhese.sdk.utils.DeviceUtils;

import java.util.List;

import androidx.annotation.NonNull;

public final class Adhese {

    private static final String TAG = Adhese.class.getSimpleName();
    private static final String OS_NAME = "Android";

    private static boolean isInitialised;
    private static AdheseAPI adheseAPI;
    private static Device device;

    private static AdheseOptions options;

    public static void initialise(@NonNull Context context, AdheseOptions adheseOptions) {

        if (isInitialised) {
            AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, "Tried initialising the SDK but it was already initialised.");
            return;
        }

        options = adheseOptions;
        adheseAPI = new AdheseAPI(context, adheseOptions);
        isInitialised = true;

        device = determineDevice(context);

        AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT,"Initialised the SDK.");
    }

    public static void loadAds(@NonNull final APICallback<List<Ad>> callback) {

        if (!isInitialised) {
            throw new IllegalStateException("Tried loading ads but Adhese has not initialised yet.");
        }

        adheseAPI.getAds(callback);
    }

    public static Device determineDevice(@NonNull Context context) {
        String brand = DeviceUtils.getDeviceName();
        String deviceInfo = DeviceUtils.determineDeviceType(context).getName();

        return new Device(brand, OS_NAME, deviceInfo);
    }

}
