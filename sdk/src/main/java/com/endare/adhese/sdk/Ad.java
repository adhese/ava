package com.endare.adhese.sdk;

import com.endare.adhese.sdk.logging.AdheseLogger;
import com.endare.adhese.sdk.utils.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Ad {
    static final String TAG = Ad.class.getSimpleName();

    /**
     * The actual HTML content to display
     */
    private String content;

    /**
     * Should be called to indicate the ad was visible to the user
     */
    private String viewableImpressionUrl;

    /**
     * Should be called to indicate the ad has been loaded
     */
    private String trackerUrl;

    /**
     * The type of ad
     */
    private String adType;

    /**
     * The unique identifier of the slot ad
     */
    private String slotName;

    /**
     * The preferred width of the ad
     */
    private int width;

    /**
     * The preferred height of the ad
     */
    private int height;

    /**
     * The raw data
     */
    private JSONObject raw;

    private Ad() {
    }

    public String getContent() {
        return content;
    }

    public String getViewableImpressionUrl() {
        return viewableImpressionUrl;
    }

    public String getTrackerUrl() {
        return trackerUrl;
    }

    public String getAdType() {
        return adType;
    }

    public String getSlotName() {
        return slotName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static Ad fromJSON(JSONObject data) throws JSONException {
        Ad ad = new Ad();

        if (!JSONUtils.isTagNonExistentEmptyOrNull(data, Keys.TAG)) {
            ad.content = data.getString(Keys.TAG);
        } else if (!JSONUtils.isTagNonExistentEmptyOrNull(data, Keys.BODY)) {
            ad.content = data.getString(Keys.BODY);
        } else {
            throw new JSONException("The payload contains neither a tag or body property.");
        }

        ad.adType = data.getString(Keys.AD_TYPE);
        ad.slotName = data.getString(Keys.SLOT_NAME);
        ad.width = data.getInt(Keys.WIDTH);
        ad.height = data.getInt(Keys.HEIGHT);
        ad.raw = data;

        if (data.has(Keys.TRACKER)) {
            ad.trackerUrl = data.getString(Keys.TRACKER);
        } else {
            ad.trackerUrl = null;
            AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, String.format("%s is loaded without tracker url.", ad.getSlotName()));
        }

        if (data.has(Keys.VIEWABLE_IMPRESSION_URL)) {
            ad.viewableImpressionUrl = data.getString(Keys.VIEWABLE_IMPRESSION_URL);
        } else {
            ad.viewableImpressionUrl = null;
            AdheseLogger.log(TAG, AdheseLogger.SDK_EVENT, String.format("%s is loaded without viewable impression url.", ad.getSlotName()));
        }


        return ad;
    }

    public static class Keys {
        static final String AD_TYPE = "adType";
        static final String SLOT_NAME = "slotName";
        static final String TAG = "tag";
        static final String BODY = "body";
        static final String TRACKER = "tracker";
        static final String VIEWABLE_IMPRESSION_URL = "viewableImpressionCounter";
        static final String WIDTH = "width";
        static final String HEIGHT = "height";
    }

}
