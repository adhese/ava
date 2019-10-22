package com.endare.adhese.sdk.parameters;

import android.text.TextUtils;

public class Device implements URLParameter {

    private String deviceInfo;
    private String deviceBrand;
    private String deviceType;

    public Device(String deviceBrand, String deviceType) {
        this(deviceBrand, deviceType, null);
    }

    public Device(String deviceBrand, String deviceType, String deviceInfo) {
        this.deviceInfo = deviceInfo;
        this.deviceBrand = deviceBrand;
        this.deviceType = deviceType;
    }

    @Override
    public String getAsURL() {
        StringBuilder builder = new StringBuilder();

        if (!TextUtils.isEmpty(deviceInfo)) {
            builder.append(String.format("/%s%s", AdheseParameter.DEVICE_ID.getKey(), deviceInfo));
        }

        if (!TextUtils.isEmpty(deviceType)) {
            // TODO: adding this parameter seems to disable loading ads?
//            builder.append(String.format("/%s%s", AdheseParameter.DEVICE_TYPE.getKey(), deviceType));
        }

        if (!TextUtils.isEmpty(deviceBrand)) {
            builder.append(String.format("/%s%s", AdheseParameter.DEVICE_BRAND.getKey(), deviceBrand).replaceAll(" ", "_"));
        }

        return builder.toString();
    }
}
