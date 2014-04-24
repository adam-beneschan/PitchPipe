package com.adambeneschan.pitchpipe;

public class PitchPipeData {

    private static PitchPipeData sData = null;
    
    private ChordBuilder mChord = null;
    private ChordBuilder mPlaying = null;
    private boolean mStopped = false;
    private int mInstrument = 53;
    private InstrumentList mInstrList = null;
    
    private PitchPipeData() { 
    }

    public static PitchPipeData getInstance() {
        if (sData == null)  {
            sData = new PitchPipeData();
        }
        return sData;
    }

    public void setChord(ChordBuilder chord) {
        mChord = chord;
    }

    public ChordBuilder getChord() {
        return mChord;
    }

    public void setPlayingChord(ChordBuilder chord) {
        mPlaying = chord;
    }

    public ChordBuilder getPlayingChord() {
        return mPlaying;
    }

    public void setInstrument(int instrument) {
        if (instrument >= 0)
            mInstrument = instrument;
    }

    public int getInstrument() {
        return mInstrument;
    }

    public void setInstrumentList(InstrumentList list) {
        mInstrList = list;
    }

    public InstrumentList getInstrumentList() {
        return mInstrList;
    }

    public void setIsStopped(boolean stopped) {
        mStopped = stopped;
    }

    public boolean isStopped() {
        return mStopped;
    }

}
