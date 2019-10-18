package com.endare.adhese.sdk.api;

import androidx.annotation.Nullable;

public class APIError extends Exception {

    private final int errorType;
    private final Exception originalException;

    public APIError(int errorType, @Nullable Exception originalException) {
        this.errorType = errorType;
        this.originalException = originalException;
    }

    public final class Type {
        public static final int UNKNOWN_ERROR = 0;
        public static final int NETWORK_ERROR = 1;
        public static final int PARSE_ERROR = 2;
    }
}
