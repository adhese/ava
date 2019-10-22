package com.endare.adhese.sdk;

import android.content.Context;

import com.endare.adhese.sdk.api.APICallback;
import com.endare.adhese.sdk.api.AdheseAPI;
import com.endare.adhese.sdk.logging.AdheseLogger;
import com.endare.adhese.sdk.parameters.Device;
import com.endare.adhese.sdk.utils.DeviceUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import androidx.annotation.NonNull;

public final class Adhese {

    private static final String TAG = Adhese.class.getSimpleName();
    private static final String OS_NAME = "Android";

    private static boolean isInitialised;
    private static String htmlWrapper;
    private static AdheseAPI adheseAPI;
    private static Device device;

    public static boolean isIsInitialised() {
        return isInitialised;
    }

    public static String getHtmlWrapper() {
        return htmlWrapper;
    }

    public static void initialise(@NonNull Context context, AdheseOptions adheseOptions) {

        if (isInitialised) {
            AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, "Tried initialising the SDK but it was already initialised.");
            return;
        }

        adheseAPI = new AdheseAPI(context);
        isInitialised = true;

        device = determineDevice(context);
        loadHtmlWrapper(context);

        AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT,"Initialised the SDK.");
    }

    public static void loadAds(@NonNull AdheseOptions options, @NonNull final APICallback<List<Ad>> callback) {

        if (!isInitialised) {
            throw new IllegalStateException("Tried loading ads but Adhese has not initialised yet.");
        }

        if (options.getDevice() == null) {
            options.setDevice(device);
        }

        adheseAPI.getAds(options, callback);
    }

    public static Device determineDevice(@NonNull Context context) {
        String brand = DeviceUtils.getDeviceName();
        String deviceInfo = DeviceUtils.determineDeviceType(context).getName();

        return new Device(brand, OS_NAME, deviceInfo);
    }

    private static void loadHtmlWrapper(@NonNull Context context) {
        StringBuilder sb = new StringBuilder();
        InputStream is;

        try {
            is = context.getAssets().open("adhese/adhese_ad_wrapper.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8 ));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {

        }

        htmlWrapper = sb.toString();
    }

}
