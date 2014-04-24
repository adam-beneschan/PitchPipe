package com.adambeneschan.pitchpipe;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;

/**
 * Static utility methods for PitchPipe.
 * @author Adam Beneschan
 */
public class Utilities {

    private Utilities() { 
    }

    /**
     * Determines the height and width of an image resource.
     * @param res The Resources object used to decode a resource ID.
     * @param resId The resource ID of an image.
     * @return A Point that contains the height and width of the image.
     */
    public static Point imageResourceDimensions(Resources res, int resId) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, opts);
        return new Point(opts.outWidth, opts.outHeight);
    }

    /**
     * Determines the width of an image resource.
     * @param res The Resources object used to decode a resource ID.
     * @param resId The resource ID of an image.
     * @return The width of the image.
     */
    public static int imageResourceWidth(Resources res, int resId) {
        return imageResourceDimensions(res, resId).x;
    }

    /**
     * Determines the height of an image resource.
     * @param res The Resources object used to decode a resource ID.
     * @param resId The resource ID of an image.
     * @return The height of the image.
     */
    public static int imageResourceHeight(Resources res, int resId) {
        return imageResourceDimensions(res, resId).y;
    }

    /**
     * Determines whether the app is currently in landscape mode.
     * @param a The current Activity.
     * @return {@true} if app is in landscape mode, false if in portrait mode.
     */
    public static boolean isLandscape(Activity a) {
        Display disp = a.getWindowManager().getDefaultDisplay();
        Point p = new Point();
        disp.getSize(p);
        return p.x > p.y;
    }

}
