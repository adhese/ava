package com.endare.adhese.sdk;

import android.text.TextUtils;

import com.endare.adhese.sdk.parameters.AdheseParameter;
import com.endare.adhese.sdk.parameters.CookieMode;
import com.endare.adhese.sdk.parameters.Device;
import com.endare.adhese.sdk.parameters.URLParameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdheseOptions implements URLParameter {

    private String location;

    @Deprecated
    private String account;

    private List<String> slots;
    private CookieMode cookieMode = CookieMode.NONE;
    private Device device;
    private Map<String, Set<String>> customParameters;

    private AdheseOptions() {
        slots = new ArrayList<>();
        customParameters = new HashMap<>();
    }

    public String getLocation() {
        return location;
    }

    @Deprecated
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
            builder.append(String.format("/%s%s-%s", AdheseParameter.SLOT.getKey(), location, slot));
        }

        builder.append(String.format("/%s%s", AdheseParameter.COOKIE_MODE.getKey(), cookieMode.getValue()));
        for (Map.Entry<String, Set<String>> customParameter: customParameters.entrySet()) {
            StringBuilder values = new StringBuilder();
            for (String value: customParameter.getValue()) {
                values.append(String.format("%s%s", value, ";"));
            }
            String valueString = values.toString();
            builder.append(String.format("/%s%s", customParameter.getKey(), valueString.substring(0, valueString.length() - 1)));
        }

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

        @Deprecated
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

        public Builder addCustomParameter(String key, String... values) {
            return this.addCustomParameter(key, Arrays.asList(values));
        }

        public Builder addCustomParameter(String key, Collection<String> values) {
            if (key == null || values == null || key.length() != 2 || values.isEmpty()) {
                throw new IllegalArgumentException("To add a valid custom parameter, your key must be two chars long and have at least one value.");
            }

            if (this.options.customParameters.containsKey(key)) {
                this.options.customParameters.get(key).addAll(values);
            } else {
                this.options.customParameters.put(key, new HashSet<>(values));
            }

            return this;
        }

        public AdheseOptions build() {

            if (TextUtils.isEmpty(options.location) || options.slots.size() == 0) {
                throw new IllegalArgumentException("To create AdheseOptions you need at least a location and one or more slots.");
            }

            return options;
        }
    }

}
