/**
 * 
 */
package com.adambeneschan.pitchpipe;

import java.util.List;

/**
 * Represents information about a chord to be played.
 * @author Adam Beneschan
 */
public class Chord {

    private List<Integer> mNotes;
    private int mInstrument;
    private int mDuration;
    private int mDelay;

    /**
     * Constructs a Chord.
     * @param notes The notes to be played, in MIDI form (60=middle C).
     * @param instrument The MIDI instrument code (see http://www.midi.org/techspecs/gm1sound.php).
     * @param duration The time in milliseconds to play the chord (if not 
     * stopped early).  If the chord is "rolled", the time starts when the
     * last note is played.
     * @param delay The time in milliseconds to wait after starting each
     * note.  If 0, all notes will be played at once; if > 0, the chord will
     * be "rolled".
     */
    public Chord(List<Integer> notes, int instrument, int duration, int delay) {
        mNotes = notes;
        mInstrument = instrument;
        mDuration = duration;
        mDelay = delay;
    }

    /** 
     * Returns the list of notes.
     * @return The list of notes.
     */
    public List<Integer> getNotes() {
        return mNotes;
    }   
    

    /** 
     * Returns the instrument code.
     * @return The MIDI instrument code.
     */
    public int getInstrument() {
        return mInstrument;
    }   
    

    /** 
     * Returns the time the chord should play.
     * @return The time the chord should play, in milliseconds, starting
     * with the start of the last note if the chord is rolled.
     */
    public int getDuration() {
        return mDuration;
    }   
    

    /** 
     * Returns the delay between notes.
     * @return The delay between notes, in milliseconds.
     */
    public int getDelay() {
        return mDelay;
    }   
}
