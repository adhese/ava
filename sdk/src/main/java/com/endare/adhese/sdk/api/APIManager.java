package com.endare.adhese.sdk.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.endare.adhese.sdk.logging.AdheseLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class APIManager {

    private static final String TAG = APIManager.class.getSimpleName();

    private String baseUrl;

    private static RequestQueue queue;

    public APIManager(@NonNull Context context) {
        this(context, "");
    }

    public APIManager(@NonNull Context context, String baseUrl) {
        this.baseUrl = baseUrl;
        initQueue(context);
    }

    public void getObject(String url, final Response.Listener<JSONObject> callback, final Response.ErrorListener errorCallback) {
        String fullUrl = url.startsWith("http") ? url : buildUrl(url);

        AdheseLogger.log(TAG, AdheseLogger.NETWORK_REQUEST, String.format("Performing GET for url %s", fullUrl));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                AdheseLogger.log(TAG, AdheseLogger.NETWORK_RESPONSE, String.format("Success: %s", response.toString()));
                callback.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AdheseLogger.log(TAG, AdheseLogger.NETWORK_RESPONSE, String.format("Error: %s", error.toString()));
                errorCallback.onErrorResponse(error);
            }
        });

        queue.add(request);
    }

    public void getArray(String url, final Response.Listener<JSONArray> callback, final Response.ErrorListener errorCallback) {
        String fullUrl = url.startsWith("http") ? url : buildUrl(url);

        AdheseLogger.log(TAG, AdheseLogger.NETWORK_REQUEST, String.format("Performing GET for url %s", fullUrl));

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, fullUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                AdheseLogger.log(TAG, AdheseLogger.NETWORK_RESPONSE, String.format("Success: %s", response.toString()));
                callback.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AdheseLogger.log(TAG, AdheseLogger.NETWORK_RESPONSE, String.format("Error: %s", error.toString()));
                errorCallback.onErrorResponse(error);
            }
        });

        queue.add(request);
    }

    public void get(String url, @Nullable final Response.Listener<String> callback, @Nullable final Response.ErrorListener errorCallback) {
        String fullUrl = url.startsWith("http") ? url : buildUrl(url);

        AdheseLogger.log(TAG, AdheseLogger.NETWORK_REQUEST, String.format("Performing GET for url %s", fullUrl));

        StringRequest request = new StringRequest(Request.Method.GET, fullUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AdheseLogger.log(TAG, AdheseLogger.NETWORK_RESPONSE, String.format("Success: %s", response));

                if (callback != null) {
                    callback.onResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AdheseLogger.log(TAG, AdheseLogger.NETWORK_RESPONSE, String.format("Error: %s", error.toString()));

                if (errorCallback != null) {
                    errorCallback.onErrorResponse(error);
                }
            }
        });

        queue.add(request);
    }

    private String buildUrl(String url) {
        return String.format("%s%s", baseUrl, url);
    }

    private void initQueue(@NonNull Context context) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
    }

}
