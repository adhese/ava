package com.endare.adhese.sdk.api;

import android.content.Context;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
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

    private static final String BASE_URL = "https://ads-%s.adhese.com/";
    private final APIManager apiManager;

    public AdheseAPI(@NonNull Context context, String account) {
        apiManager = new APIManager(context, String.format(BASE_URL, account));
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
                    callback.onResponse(null, new APIError(APIError.Type.PARSE_ERROR, e));
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onResponse(null, createException(error));
            }
        });
    }

    public void get(@NonNull String url, @NonNull final APICallback<Void> callback) {
        apiManager.get(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onResponse(null, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onResponse(null, createException(error));
            }
        });
    }

    private APIError createException(VolleyError error) {

        if (error instanceof NetworkError) {
            return new APIError(APIError.Type.NETWORK_ERROR, error);
        }

        if (error instanceof ParseError) {
            return new APIError(APIError.Type.PARSE_ERROR, error);
        }

        return new APIError(APIError.Type.UNKNOWN_ERROR, error);
    }
}
