package com.endare.adhese.sdk.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.endare.adhese.sdk.Ad;
import com.endare.adhese.sdk.Adhese;
import com.endare.adhese.sdk.api.APICallback;
import com.endare.adhese.sdk.api.APIError;
import com.endare.adhese.sdk.logging.AdheseLogger;

import androidx.annotation.NonNull;

/**
 * A view used to display an ad. The view handles the calling of tracker URLs and view impressions
 * on its own.
 */
public class AdView extends WebView {

    public static final String TAG = AdView.class.getSimpleName();

    protected Ad ad;

    private boolean isViewImpressionCallInProgress;
    private boolean hasViewImpressionBeenCalled;
    private boolean isContentLoaded;
    private boolean shouldOpenAd = true;

    private OnAdLoadedListener adLoadedListener;
    private OnTrackerNotifiedListener trackingNotifiedListener;
    private OnViewImpressionNotifiedListener viewImpressionNotifiedListener;
    private OnErrorListener errorListener;
    private OnAdClickListener onAdClickListener;

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

    /**
     * Returns whether or not the AdView should handle opening the URL by itself or not when the
     * ad is clicked.
     */
    public boolean shouldOpenAd() {
        return shouldOpenAd;
    }

    public void setShouldOpenAd(boolean shouldOpenAd) {
        this.shouldOpenAd = shouldOpenAd;
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

    public void setErrorListener(OnErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public void setOnAdClickListener(OnAdClickListener onAdClickListener) {
        this.onAdClickListener = onAdClickListener;
    }

    private void init() {
        applySettings();
        registerListeners();

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
                triggerViewImpressionWhenVisible();
            }

        });

        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                if (onAdClickListener != null) {
                    onAdClickListener.onAdClicked(AdView.this);
                }

                if (!shouldOpenAd) {
                    return true;
                }

                openInBrowser(request.getUrl().toString());

                return true;
            }
        });
    }

    protected void loadAd() {

        if (!Adhese.isIsInitialised()) {
            throw new IllegalStateException(String.format("Tried creating an %s but the Adhese SDK has not been initialised yet.", AdView.class.getSimpleName()));
        }

        if (ad == null) {
            return;
        }

        loadDataWithBaseURL(null, wrapInHtmlWrapper(determineContentScale(), ad), null, null, null);
    }

    /**
     * @deprecated use wrapInHtmlWrapper(double scale, Ad ad) instead (includes better content scaling)
     */
    @Deprecated
    public static String wrapInHtmlWrapper(double scale, String content) {
        return String.format(Adhese.getHtmlWrapper(), Double.toString(scale), Double.toString(scale), content);
    }

    public static String wrapInHtmlWrapper(double scale, Ad ad) {
        return String.format(Adhese.getHtmlWrapper(), Double.toString(scale), Double.toString(scale), Math.round(ad.getWidth() * scale), Math.round(ad.getHeight() * scale), ad.getContent());
    }

    private double determineContentScale() {
        if (ad == null) { return 1; }

        if (ad.getWidth() < this.getHeight()) {
            return (double) this.getHeight() / ad.getHeight();
        } else {
            return (double) this.getWidth() / ad.getWidth();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void applySettings() {
        getSettings().setJavaScriptEnabled(true);
        getSettings().setLoadWithOverviewMode(true);
        getSettings().setUseWideViewPort(true);
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        setScrollbarFadingEnabled(false);
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

    private void registerListeners() {
        getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                triggerViewImpressionWhenVisible();
            }
        });
    }

    private void openInBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        getContext().startActivity(browserIntent);
    }

    private void notifyTracker() {
        AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, String.format("Will notify the tracker for slot %s", ad.getSlotName()));

        Adhese.getAPI().get(ad.getTrackerUrl(), new APICallback<Void>() {
            @Override
            public void onResponse(Void data, APIError error) {

                if (error != null) {
                    AdheseLogger.log(TAG, AdheseLogger.SDK_ERROR, String.format("Failed to notify the tracker: %s", error.getErrorTypeName()));

                    if (errorListener != null) {
                        errorListener.onError(AdView.this, error);
                    }

                    return;
                }

                AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, String.format("Notified tracker for slot %s", ad.getSlotName()));

                if (trackingNotifiedListener != null) {
                    trackingNotifiedListener.onTrackerNotified(AdView.this);
                }

            }
        });
    }

    private void notifyViewImpression() {
        isViewImpressionCallInProgress = true;
        AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, String.format("Will notify the view impression for slot %s", ad.getSlotName()));

        Adhese.getAPI().get(ad.getViewableImpressionUrl(), new APICallback<Void>() {
            @Override
            public void onResponse(Void data, APIError error) {

                if (error != null) {
                    AdheseLogger.log(TAG, AdheseLogger.SDK_ERROR, String.format("Failed to send view impression for slot: %s", error.getErrorTypeName()));
                    hasViewImpressionBeenCalled = false;
                    isViewImpressionCallInProgress = false;

                    if (errorListener != null) {
                        errorListener.onError(AdView.this, error);
                    }

                    return;
                }


                AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, String.format("Notified tracker for slot %s", ad.getSlotName()));

                if (viewImpressionNotifiedListener != null) {
                    viewImpressionNotifiedListener.onViewImpressionNotified(AdView.this);
                }

                hasViewImpressionBeenCalled = true;
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

    public interface OnAdClickListener {
        void onAdClicked(@NonNull AdView adView);
    }

    /**
     * A listener that can be implemented to be notified when an error occurs with the ad.
     */
    public interface OnErrorListener {
        void onError(@NonNull AdView adView, APIError error);
    }
}
