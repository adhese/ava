package com.endare.adhese.sdk;

public class AdheseOptions {

    private String location;
    private boolean debugEnabled;
    private String account;

    public AdheseOptions(String location, String account) {
        this(location, account, false);
    }

    public AdheseOptions(String location, String account, boolean debugEnabled) {
        this.location = location;
        this.account = account;
        this.debugEnabled = debugEnabled;
    }

    public String getLocation() {
        return location;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public String getAccount() {
        return account;
    }
}
