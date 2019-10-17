package com.endare.adhese.sdk.parameters;

import android.text.TextUtils;

public class Device implements URLParameter {

    private String deviceInfo;
    private String deviceBrand;
    private String deviceType;

    @Override
    public String getAsURL() {
        StringBuilder builder = new StringBuilder();

        if (!TextUtils.isEmpty(deviceInfo)) {
            builder.append(String.format("/%s%s", deviceInfo, AdheseParameter.DEVICE_ID.getKey()));
        }

        if (!TextUtils.isEmpty(deviceType)) {
            builder.append(String.format("/%s%s", deviceType, AdheseParameter.DEVICE_TYPE.getKey()));
        }

        if (!TextUtils.isEmpty(deviceBrand)) {
            builder.append(String.format("/%s%s", deviceBrand, AdheseParameter.DEVICE_BRAND.getKey()));
        }

        return builder.toString();
    }
}
