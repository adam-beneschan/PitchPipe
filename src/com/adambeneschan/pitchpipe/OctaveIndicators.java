/**
 * 
 */
package com.adambeneschan.pitchpipe;

import android.app.Fragment;
import android.graphics.Point;
import android.view.View;
import android.widget.ImageView;

/**
 * Supplies a set of four "octave indicators", images that are used to
 * show the user what octave a note button belongs to.  The four images must
 * all have the same dimensions.
 * @author Adam Beneschan
 *
 */
public class OctaveIndicators {

    private Fragment mFragment;
    private Point mOctaveDim;

    private static final int[] sOctavePicture = new int[] {
        R.drawable.c_1, R.drawable.c_2, R.drawable.c_3, R.drawable.c_4
    };

    /**
     * Constructs an OctaveIndicators object.
     * @param f The Fragment that will be using the object.
     */
    public OctaveIndicators(Fragment f) {
        this.mFragment = f;
        mOctaveDim = Utilities.imageResourceDimensions(f.getResources(), sOctavePicture[0]);
    }

    /**
     * Returns a View that holds the octave indicator image for an octave
     * @param octave The octave number, an integer from 0 to 3 inclusive
     * (0 = octave starting at low C, 1 = the octave above #0, 
     * 2 = octave starting at middle C, 3 = the octave above #2).
     */
    public View getIndicator(int octave) {
        ImageView iv = new ImageView(mFragment.getActivity());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);  
        iv.setImageResource(sOctavePicture[octave]);
        return iv;
    }

    /**
     * Returns the width of each image.
     * @return The width of each image.
     */
    public int indicatorWidth() {
        return mOctaveDim.x;
    }
    
    /**
     * Returns the height of each image.
     * @return The height of each image.
     */
    public int indicatorHeight() {
        return mOctaveDim.y;
    }
    
}
