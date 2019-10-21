package com.endare.adhese.sdk.views;

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.Nullable;

class ViewVisibilityHelper {

    public static boolean isVisible(final @Nullable View view) {

        if (view == null) {
            return false;
        }

        if (!view.isShown()) {
            return false;
        }

        final Rect actualPosition = new Rect();
        view.getGlobalVisibleRect(actualPosition);
        final Rect screen = new Rect(0, 0, getScreenWidth(), getScreenHeight());

        return actualPosition.intersect(screen);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}
