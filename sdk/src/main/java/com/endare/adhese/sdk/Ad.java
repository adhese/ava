package com.endare.adhese.sdk;

import com.endare.adhese.sdk.utils.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Ad {

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

        ad.trackerUrl = data.getString(Keys.TRACKER);
        ad.viewableImpressionUrl = data.getString(Keys.VIEWABLE_IMPRESSION_URL);
        ad.adType = data.getString(Keys.AD_TYPE);
        ad.slotName = data.getString(Keys.SLOT_NAME);
        ad.width = data.getInt(Keys.WIDTH);
        ad.height = data.getInt(Keys.HEIGHT);
        ad.raw = data;

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
