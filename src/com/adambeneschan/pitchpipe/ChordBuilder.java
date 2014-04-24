/**
 * 
 */
package com.adambeneschan.pitchpipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds a chord that can be added to, one note at a time.
 * @author Adam Beneschan
 *
 */
public class ChordBuilder {

    private ArrayList<Integer> mNotes;
    private PlayStoppedListener mListener = null;
    
    /**
     * Constructs a new Chord.
     */
    public ChordBuilder() {
        mNotes = new ArrayList<Integer>();
    }
    
    /**
     * Removes all notes from the chord.
     */
    public void clear() {
        mNotes.clear();
    }
    
    /**
     * Adds a note to the chord.
     * @param note The note, as a MIDI note (60=middle C).
     */
    public void add(int note) {
        mNotes.add(note);
    }
    
    /**
     * Changes the last note added to the chord.
     * @param note The new note, as a MIDI note (60=middle C).
     */
    public void changeLast(int note) {
        if (mNotes.size() == 0)
            mNotes.add(note);
        else
            mNotes.set(mNotes.size()-1, note);
    }
    
    /**
     * Registers a listener to be called when this chord stops playing.
     * Only one listener can be registered for a chord.  The listener in
     * effect when {@code play} is called will be used; calling
     * {@code setPlayStoppedListener} after {@code play} will not affect
     * the previous {@code play}.
     * @param listener The listener to register ({@code null} means no
     * listener will be called).
     */
    public void setPlayStoppedListener(PlayStoppedListener listener) {
        mListener = listener;
    } 
    
    /**
     * Plays a chord that contains the current notes of the chord.  (The
     * playing will not be affected if changes are made to this Chord while
     * the chord is still being played.)
     * @param instrument The MIDI instrument code (see http://www.midi.org/techspecs/gm1sound.php).
     * @param duration The time in milliseconds to play the chord (if not 
     * stopped early).  If the chord is "rolled", the time starts when the
     * last note is played.
     * @param delay The time in milliseconds to wait after starting each
     * note.  If 0, all notes will be played at once; if > 0, the chord will
     * be "rolled".
     */
    public void play(int instrument, int duration, int delay) {
        List<Integer> notesCopy = new ArrayList<Integer>(mNotes);
        ChordPlayer.getInstance().play(new Chord(notesCopy, instrument, duration, delay), mListener);
    }
    
}
