package com.endare.adhese.sdk.api;

import android.content.Context;

import com.endare.adhese.sdk.AdheseOptions;

import androidx.annotation.NonNull;

public class AdheseAPI {

    private static final String BASE_URL = "https://ads-demo.adhese.com/";
    private final APIManager apiManager;
    private final AdheseOptions adheseOptions;

    public AdheseAPI(@NonNull Context context, AdheseOptions options) {
        apiManager = new APIManager(context, BASE_URL);
        adheseOptions = options;
    }

    public void getAds() {
        apiManager.getArray(String.format("json%s", adheseOptions.getAsURL()));
    }
}
