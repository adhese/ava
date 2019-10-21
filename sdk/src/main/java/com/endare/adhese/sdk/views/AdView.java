package com.endare.adhese.sdk.views;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.endare.adhese.sdk.Ad;
import com.endare.adhese.sdk.api.APIManager;
import com.endare.adhese.sdk.logging.AdheseLogger;

import androidx.annotation.NonNull;

public class AdView extends WebView {

    public static final String TAG = AdView.class.getSimpleName();

    protected Ad ad;

    private APIManager apiManager;
    private boolean isViewImpressionCallInProgress;
    private boolean hasViewImpressionBeenCalled;
    private boolean isContentLoaded;

    private OnAdLoadedListener adLoadedListener;
    private OnTrackerNotifiedListener trackingNotifiedListener;
    private OnViewImpressionNotifiedListener viewImpressionNotifiedListener;

    public AdView(Context context) {
        super(context);
        init();
    }

    public AdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public AdView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
        init();
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
        this.loadAd();
    }

    public void setAdLoadedListener(OnAdLoadedListener adLoadedListener) {
        this.adLoadedListener = adLoadedListener;
    }

    public void setTrackingNotifiedListener(OnTrackerNotifiedListener trackingNotifiedListener) {
        this.trackingNotifiedListener = trackingNotifiedListener;
    }

    public void setViewImpressionNotifiedListener(OnViewImpressionNotifiedListener viewImpressionNotifiedListener) {
        this.viewImpressionNotifiedListener = viewImpressionNotifiedListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        triggerViewImpressionWhenVisible();
    }

    private void loadAd() {
        getSettings().setJavaScriptEnabled(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setUseWideViewPort(true);
        loadDataWithBaseURL(null, ad.getContent(), null, null, null);
    }

    private void init() {
        apiManager = new APIManager(getContext());
        this.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                if (progress != 100 || isContentLoaded) {
                    return;
                }

                isContentLoaded = true;

                if (adLoadedListener != null) {
                    adLoadedListener.onAdLoaded(AdView.this);
                }

                AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, String.format("Finished loading slot %s", ad.getSlotName()));

                notifyTracker();
            }
        });
    }

    private void triggerViewImpressionWhenVisible() {
        if (!hasViewImpressionBeenCalled
                && !isViewImpressionCallInProgress
                && ad != null
                && !TextUtils.isEmpty(ad.getViewableImpressionUrl())
                && getVisibility() == View.VISIBLE
                && ViewVisibilityHelper.isVisible(this)
        ) {
            AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, String.format("%s is visible.", getAd().getSlotName()));
            notifyViewImpression();
        }
    }

    private void notifyTracker() {
        AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, String.format("Will notify the tracker for slot %s", ad.getSlotName()));

        apiManager.get(ad.getTrackerUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, String.format("Notified tracker for slot %s", ad.getSlotName()));

                if (trackingNotifiedListener != null) {
                    trackingNotifiedListener.onTrackerNotified(AdView.this);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AdheseLogger.log(TAG, AdheseLogger.SDK_ERROR, String.format("Failed to notify the tracker: %s", error.networkResponse));
            }
        });
    }

    private void notifyViewImpression() {
        isViewImpressionCallInProgress = true;
        AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, String.format("Will notify the view impression for slot %s", ad.getSlotName()));

        apiManager.get(ad.getViewableImpressionUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, String.format("Notified tracker for slot %s", ad.getSlotName()));

                if (viewImpressionNotifiedListener != null) {
                    viewImpressionNotifiedListener.onViewImpressionNotified(AdView.this);
                }

                hasViewImpressionBeenCalled = true;
                isViewImpressionCallInProgress = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AdheseLogger.log(TAG, AdheseLogger.SDK_ERROR, String.format("Failed to notify the tracker: %s", error.networkResponse));
                hasViewImpressionBeenCalled = false;
                isViewImpressionCallInProgress = false;
            }
        });
    }

    /**
     * A listener that can be implemented to be notified when an advertisement has been loaded as HTML content.
     */
    public interface OnAdLoadedListener {
        void onAdLoaded(@NonNull AdView adView);
    }

    /**
     * A listener that can be implemented to be notified when the tracker URL was called.
     */
    public interface OnTrackerNotifiedListener {
        void onTrackerNotified(@NonNull AdView adView);
    }

    /**
     * A listener that can be implemented to be notified when the viewableImpressionCounter URL was called.
     */
    public interface OnViewImpressionNotifiedListener {
        void onViewImpressionNotified(@NonNull AdView adView);
    }
}
