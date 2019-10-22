package com.endare.adhese.sdk.utils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;

public class JSONUtils {

    public static boolean isTagNonExistentEmptyOrNull(@NonNull JSONObject data, @NonNull String tag) throws JSONException {
        return !data.has(tag) || data.getString(tag).equalsIgnoreCase("null") || TextUtils.isEmpty(data.getString(tag));
    }

}
