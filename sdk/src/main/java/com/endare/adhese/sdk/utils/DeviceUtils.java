package com.endare.adhese.sdk.utils;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.endare.adhese.sdk.R;
import com.endare.adhese.sdk.parameters.DeviceType;

import androidx.annotation.NonNull;

import static android.content.Context.UI_MODE_SERVICE;

public final class DeviceUtils {

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return StringUtils.capitalize(model);
        }
        return StringUtils.capitalize(manufacturer) + " " + model;
    }

    public static @NonNull DeviceType determineDeviceType(@NonNull Context context) {

        if (isTV(context)) {
            return DeviceType.TV;
        } else if (isSmartWatch(context)) {
            return DeviceType.SMART_WATCH;
        } else if (isAppliance(context)) {
            return DeviceType.APPLIANCE;
        } else if (isCar(context)) {
            return DeviceType.CAR;
        } else if (isTablet(context)) {
            return DeviceType.TABLET;
        }

        return DeviceType.PHONE;
    }

    private static boolean isTV(@NonNull Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);

        return uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION;
    }

    private static boolean isSmartWatch(@NonNull Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);

        return uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_WATCH;
    }

    private static boolean isAppliance(@NonNull Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);

        return uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_APPLIANCE;
    }

    private static boolean isCar(@NonNull Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);

        return uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_CAR;
    }

    /**
     * See https://stackoverflow.com/a/9308284/4614197
     */
    private static boolean isTablet(@NonNull Context context) {
        return context.getResources().getBoolean(R.bool.adhese_is_tablet);
    }

}
