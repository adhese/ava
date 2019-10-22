package com.endare.adhese.sdk;

import com.endare.adhese.sdk.parameters.AdheseParameter;
import com.endare.adhese.sdk.parameters.CookieMode;
import com.endare.adhese.sdk.parameters.Device;
import com.endare.adhese.sdk.parameters.URLParameter;

import java.util.ArrayList;
import java.util.List;

public class AdheseOptions implements URLParameter {

    private String location;
    private boolean debugEnabled;
    private String account;
    private List<String> slots;
    private CookieMode cookieMode = CookieMode.NONE;
    private Device device;

    private AdheseOptions() {
        slots = new ArrayList<>();
    }

    public String getLocation() {
        return location;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public String getAccount() {
        return account;
    }

    public List<String> getSlots() {
        return slots;
    }

    public CookieMode getCookieMode() {
        return cookieMode;
    }

    public Device getDevice() {
        return device;
    }

    void setDevice(Device device) {
        this.device = device;
    }

    @Override
    public String getAsURL() {
        StringBuilder builder = new StringBuilder();

        for (String slot : slots) {
            builder.append(String.format("/%s%s-%s", AdheseParameter.SLOT.getKey(), account, slot));
        }

        builder.append(String.format("/%s%s", AdheseParameter.COOKIE_MODE.getKey(), cookieMode.getValue()));

        if (device != null) {
            builder.append(device.getAsURL());
        }

        return builder.toString();
    }

    public static class Builder {

        private AdheseOptions options;

        public Builder() {
            options = new AdheseOptions();
        }

        public Builder forLocation(String location) {
            options.location = location;
            return this;
        }

        public Builder withAccount(String account) {
            options.account = account;
            return this;
        }

        public Builder addSlot(String slot) {
            options.slots.add(slot);
            return this;
        }

        public Builder addSlots(List<String> slots) {
            options.slots.addAll(slots);
            return this;
        }

        public Builder withCookieMode(CookieMode cookieMode) {
            this.options.cookieMode = cookieMode;
            return this;
        }

        public Builder enableDebugMode() {
            this.options.debugEnabled = true;
            return this;
        }

        public AdheseOptions build() {

            // TODO: check if all required parameters have been set

            return options;
        }
    }

}
