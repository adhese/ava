package com.endare.adhese.sdk.parameters;

public enum DeviceType {
    APPLIANCE("appliance"),
    CAR("car"),
    PHONE("phone"),
    SMART_WATCH("smartwatch"),
    TABLET("tablet"),
    TV("tv");

    private final String name;

    DeviceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
