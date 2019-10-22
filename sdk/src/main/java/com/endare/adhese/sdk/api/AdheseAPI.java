package com.endare.adhese.sdk.api;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.endare.adhese.sdk.Ad;
import com.endare.adhese.sdk.AdheseOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public final class AdheseAPI {

    private static final String BASE_URL = "https://ads-demo.adhese.com/";
    private final APIManager apiManager;

    public AdheseAPI(@NonNull Context context) {
        apiManager = new APIManager(context, BASE_URL);
    }

    public void getAds(@NonNull AdheseOptions options, @NonNull final APICallback<List<Ad>> callback) {
        apiManager.getArray(String.format("json%s", options.getAsURL()), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                List<Ad> ads = new ArrayList<>();

                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        ads.add(Ad.fromJSON(object));
                    }

                    callback.onResponse(ads, null);

                } catch (JSONException e) {
                    callback.onResponse(null, e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onResponse(null, error);
            }
        });
    }
}
