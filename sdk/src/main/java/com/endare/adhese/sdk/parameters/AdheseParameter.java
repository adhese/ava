package com.endare.adhese.sdk.parameters;

public enum AdheseParameter {

    /**
     * Position (or the combination of a location and template, slot is used as backend name)
     * example: _demo_ster_a_
     */
    SLOT("sl"),

    /**
     * The type of device the ad is showing on
     * Handset
     * Tablet
     * Tv
     * unknown
     */
    DEVICE_TYPE("dt"),

    /**
     * Brand code (collection) browser and OS data
     * example: Chrome;Chrome7;Mac
     */
    DEVICE_BRAND("br"),

    /**
     * The parameter is called device ID but it's more about the current OS version.
     * iOS: "iPhone7,2"
     * Android: Lollipop
     * Windows: ?
     */
    DEVICE_ID("de"),

    /**
     * Use request information for the delivery of ads.
     * possible values:
     * - none
     * - all
     */
    COOKIE_MODE("tl");

    private final String key;

    AdheseParameter(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
