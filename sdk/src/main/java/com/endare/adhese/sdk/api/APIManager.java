package com.endare.adhese.sdk.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.endare.adhese.sdk.logging.AdheseLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.annotation.NonNull;

public class APIManager {

    private static final String TAG = APIManager.class.getSimpleName();

    private RequestQueue queue;
    private String baseUrl;

    public APIManager(@NonNull Context context, String baseUrl) {
        this.queue = Volley.newRequestQueue(context);
        this.baseUrl = baseUrl;
    }

    public void getObject(String url) {
        String fullUrl = buildUrl(url);
        Log.d(TAG, fullUrl);
        AdheseLogger.log(TAG, AdheseLogger.NETWORK_REQUEST, String.format("Performing GET for url %s", fullUrl));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AdheseLogger.log(TAG, AdheseLogger.NETWORK_RESPONSE, String.format("Success: %s", response.toString()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AdheseLogger.log(TAG, AdheseLogger.NETWORK_RESPONSE, String.format("Error: %s", error.toString()));
            }
        });

        queue.add(request);
    }

    public void getArray(String url) {
        String fullUrl = buildUrl(url);
        AdheseLogger.log(TAG, AdheseLogger.NETWORK_REQUEST, String.format("Performing GET for url %s", fullUrl));

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                AdheseLogger.log(TAG, AdheseLogger.NETWORK_RESPONSE, String.format("Success: %s", response.toString()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AdheseLogger.log(TAG, AdheseLogger.NETWORK_RESPONSE, String.format("Error: %s", error.toString()));
            }
        });

        queue.add(request);
    }

    private String buildUrl(String url) {
        return String.format("%s%s", baseUrl, url);
    }


}
