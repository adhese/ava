package com.endare.adhese.sdk.api;

public interface APICallback<T> {

    void onResponse(T data, APIError error);

}
