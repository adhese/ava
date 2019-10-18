package com.endare.adhese.sdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.endare.adhese.sdk.Ad;

public class AdView extends WebView {

    protected Ad ad;

    public AdView(Context context) {
        super(context);
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
        this.loadAd();
    }

    private void loadAd() {
        getSettings().setJavaScriptEnabled(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setUseWideViewPort(true);
        loadDataWithBaseURL(null, ad.getContent(), null, null, null);
    }
}
