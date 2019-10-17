package com.endare.adhese.sdk.parameters;

public enum CookieMode {
    ALL("all"),
    NONE("none");

    private final String value;

    CookieMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
