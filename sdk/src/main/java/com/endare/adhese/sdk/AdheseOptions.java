package com.endare.adhese.sdk;

import android.text.TextUtils;

import com.endare.adhese.sdk.parameters.AdheseParameter;
import com.endare.adhese.sdk.parameters.CookieMode;
import com.endare.adhese.sdk.parameters.Device;
import com.endare.adhese.sdk.parameters.URLParameter;

import java.util.*;

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

    public Map<String, Set<String>> getCustomParameters() {
        return customParameters;
    }

    @Override
    public String getAsURL() {
        StringBuilder builder = new StringBuilder();

        for (String slot : slots) {
            builder.append(String.format("/%s%s-%s", AdheseParameter.SLOT.getKey(), location, slot));
        }

        builder.append(String.format("/%s%s", AdheseParameter.COOKIE_MODE.getKey(), cookieMode.getValue()));
        TreeSet<String> sortedKeys = new TreeSet<>(customParameters.keySet());
        for (String key: sortedKeys) {
            StringBuilder values = new StringBuilder();
            List<String> valueList = new ArrayList(customParameters.get(key));
            Collections.sort(valueList);

            for (int i = 0; i < valueList.size(); i++) {
                if (i < valueList.size() - 1) {
                    values.append(String.format("%s%s", valueList.get(i), ";"));
                } else {
                    values.append(valueList.get(i));
                }
            }
            String valueString = values.toString();
            builder.append(String.format("/%s%s", key, valueString));
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
            options.cookieMode = cookieMode;
            return this;
        }

        public Builder addCustomParameterRaw(String key, String value) {
            return addCustomParameterRaw(key, Arrays.asList(value));
        }

        public Builder addCustomParameterRaw(String key, Collection<String> values) {
            if (key == null || values == null || key.length() != 2 || values.isEmpty()) {
                throw new IllegalArgumentException("To add a valid custom parameter, your key must be two chars long and have at least one value.");
            }

            if (options.customParameters.containsKey(key)) {
                options.customParameters.get(key).addAll(values);
            } else {
                options.customParameters.put(key, new HashSet<>(values));
            }

            return this;
        }

        public Builder addCustomParametersRaw(Map<String, ? extends Collection<String>> map) {
            for (Map.Entry<String, ? extends Collection<String>> entry: map.entrySet()) {
                addCustomParameterRaw(entry.getKey(), entry.getValue());
            }

            return this;
        }

        public Builder removeCustomParameters() {
            options.customParameters.clear();
            return this;
        }

        public Builder removeCustomParameter(String key) {
            options.customParameters.remove(key);
            return this;
        }

        public AdheseOptions build() {

            if (TextUtils.isEmpty(options.location) || options.slots.size() == 0) {
                throw new IllegalArgumentException("To create AdheseOptions you need at least a location and one or more slots.");
            }

            return options;
        }
    }

    public Builder clone() {
        AdheseOptions.Builder clone = new AdheseOptions.Builder();
        clone.addSlots(getSlots());
        clone.addCustomParametersRaw(getCustomParameters());
        clone.forLocation(getLocation());
        clone.withCookieMode(getCookieMode());
        clone.withAccount(getAccount());

        return clone;
    }
}
