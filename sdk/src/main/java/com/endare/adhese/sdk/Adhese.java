package com.endare.adhese.sdk;

import android.content.Context;
import android.text.TextUtils;

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
    private static String adheseAccount;

    public static boolean isIsInitialised() {
        return isInitialised;
    }

    public static String getHtmlWrapper() {
        return htmlWrapper;
    }

    /**
     * Initialises the Adhese SDK, should be called once on application start-up or in the main activity.
     *
     * @param context      The current context when for when the method is called.
     * @param debugEnabled Determines whether Adhese logging should show in Logcat or not.
     */
    public static void initialise(@NonNull Context context, @NonNull String account, boolean debugEnabled) {

        if (isInitialised) {
            AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, "Tried initialising the SDK but it was already initialised.");
            return;
        }

        if (TextUtils.isEmpty(account)) {
            throw new IllegalArgumentException("The account parameter cannot be empty");
        }

        AdheseLogger.setIsLoggingEnabled(debugEnabled);
        adheseAPI = new AdheseAPI(context, account);
        isInitialised = true;
        adheseAccount = account;
        device = determineDevice(context);
        loadHtmlWrapper(context);

        AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, "Initialised the SDK.");
    }

    /**
     * Initialises the Adhese SDK, should be called once on application start-up or in the main activity.
     *
     * @param context The current context when for when the method is called.
     */
    public static void initialise(@NonNull Context context, @NonNull String account) {
        initialise(context, account, false);
    }

    /**
     * Gets the Adhese API instance.
     *
     * @return AdheseAPI
     */
    @NonNull
    public static AdheseAPI getAPI() {

        if (!isInitialised) {
            throw new IllegalStateException("Tried getting the AdheseAPI but Adhese has not been initialised yet.");
        }

        return adheseAPI;
    }

    /**
     * Gets the Adhese account
     *
     * @return String
     */
    @NonNull
    public static String getAdheseAccount() {

        if (!isInitialised) {
            throw new IllegalStateException("Tried fetching the Adhese Account but Adhese has not been initialised yet.");
        }

        return adheseAccount;
    }

    /**
     * Loads the ads with a given set of AdheseOptions and returns the result async with a callback.
     *
     * @param options  The options to send to the parameter.
     * @param callback The callback that will return the ads data.
     */
    public static void loadAds(@NonNull AdheseOptions options, @NonNull final APICallback<List<Ad>> callback) {

        if (!isInitialised) {
            throw new IllegalStateException("Tried loading ads but Adhese has not been initialised yet.");
        }

        if (options.getDevice() == null) {
            options.setDevice(device);
        }

        adheseAPI.getAds(options, callback);
    }

    /**
     * Performs some magic to determine the device properties where the SDK is running on.
     *
     * @param context The context used to fetch some metadata.
     * @return A Device instance
     */
    public static Device determineDevice(@NonNull Context context) {
        String brand = DeviceUtils.getDeviceName();
        String deviceInfo = DeviceUtils.determineDeviceType(context).getName();

        return new Device(brand, OS_NAME, deviceInfo);
    }

    /**
     * Validates the Device the SDK is running on for compatibility
     *
     * @return True when the device is compatible
     */
    public static boolean validateDevice() {
        return (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.M);
    }

    /**
     * Loads the static HTML page stored in the assets. This is required to display the ads optimally in a webview.
     *
     * @param context The context used to fetch the asset.
     */
    private static void loadHtmlWrapper(@NonNull Context context) {
        StringBuilder sb = new StringBuilder();
        InputStream is;

        try {
            is = context.getAssets().open("adhese/adhese_ad_wrapper.html");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            AdheseLogger.log(TAG, AdheseLogger.SDK_ERROR, "Something went wrong loading the HTML asset, ads will not display properly!");
        }

        htmlWrapper = sb.toString();
    }

}
