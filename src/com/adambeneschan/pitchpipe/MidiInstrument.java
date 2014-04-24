package com.adambeneschan.pitchpipe;

/**
 * Represents a MIDI instrument (program) that can be used by PitchPipe.
 * @author Adam Beneschan
 */
public class MidiInstrument {
    private int mCode;
    private String mName;
    private int mFade;

    /**
     * Constructs a MidiInstrument.
     * @param code The MIDI instrument (program) code (see http://www.midi.org/techspecs/gm1sound.php).
     * @param name The instrument name (see http://www.midi.org/techspecs/gm1sound.php).
     * @param fade The approximate time, in milliseconds, until a note
     * becomes inaudible after it has started playing.  If the note does not
     * fade all the way out, {@code fade} should be {@code Integer.MAX_VALUE}.
     */
    public MidiInstrument(int code, String name, int fade) {
        mCode = code;
        mName = name;
        mFade = fade;
    }

    /** 
     * Returns the instrument code.
     * @return The MIDI instrument code.
     */
    public int getCode() {
        return mCode;
    }

    /** 
     * Returns the instrument name.
     * @return The instrument name.
     */
    public String getName() {
        return mName;
    }

    /** 
     * Returns the fade-out time.
     * @return The approximate time, in milliseconds, until a note
     * becomes inaudible after it has started playing, or {@code Integer.MAX_VALUE} 
     * if the note does not fade.
     */
    public int getFade() {
        return mFade;
    }

}

