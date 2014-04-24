/**
 * 
 */
package com.adambeneschan.pitchpipe;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

/**
 * Supplies the note buttons that will be 
 * @author Adam Beneschan
 *
 */
public class NoteButtons {

    private Context mContext;

    // Table listing the note names for the notes of the chromatic scale.
    // If the button should display two notes (e.g. C#/Db), they are 
    // separated by a slash in this table, although they may not appear
    // that way on the actual button.
    // U+266F is sharp symbol, U+266D is flat symbol
    private static final String[] sNoteName = new String[] {
        "C", "C\u266f/D\u266d", "D", "D\u266f/E\u266d", "E", "F",
        "F\u266f/G\u266d", "G", "G\u266f/A\u266d", "A", "A\u266f/B\u266d", "B"
    };

    /**
     * Constructs a NoteButtons object.
     * @param c The context (e.g. activity) in which the buttons will be used.
     * @param isLandscape Specifies the orientation, which affects the
     *   appearance of the buttons.
     */
    public NoteButtons(Context c, boolean isLandscape) {
        this.mContext = c;
    }

    /**
     * Creates a button to be displayed for a given note.
     * @param octave The octave number, an integer from 0 to 3 inclusive
     * (0 = octave starting at low C, 1 = the octave above #0, 
     * 2 = octave starting at middle C, 3 = the octave above #2).
     * @param note The note in the scale, an integer from 0 to 11 inclusive
     * (0 = C, 1 = C#, ..., 2 = B)
     */
    public View getButton(final int octave, final int note) {
        Button button = new Button(mContext);
        String s = sNoteName[note];
        
        int textSize;
        if (s.contains("/")) {
            // If s contains a slash, the note is an accidental with two
            // names, e.g. C#/Db, D#/Eb, etc.  The accidental_separator 
            // resource gives us the string that will appear between two 
            // note names on a button; this could be different in portrait
            // and landscape mode, for instance.
            String sep = mContext.getResources().getString(R.string.accidental_separator);
            s = s.replaceAll("/", sep);
            textSize = mContext.getResources().getInteger(R.integer.accidental_text_size);
        } else {
            textSize = mContext.getResources().getInteger(R.integer.normal_text_size);
        }
        
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
        button.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        button.setText(s);
        button.setBackgroundResource(R.drawable.note_button);
        return button;
    }

    /**
     * Returns the width of each note button.
     * @return The width of each note button (which may depend on the
     * orientation and other configuration aspects).
     */
    public int buttonWidth() {
        return mContext.getResources().getInteger(R.integer.button_width);
    }

    /**
     * Returns the height of each note button.
     * @return The height of each note button (which may depend on the
     * orientation and other configuration aspects).
     */
    public int buttonHeight() {
        return mContext.getResources().getInteger(R.integer.button_height);
    }

}
