package com.endare.adhese.sdk.parameters;

public enum AdheseParameter {
    SLOT("sl"),
    DEVICE_TYPE("dt"),
    DEVICE_BRAND("br"),
    DEVICE_ID("de"),
    COOKIE_MODE("tl");

    private final String key;

    AdheseParameter(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
