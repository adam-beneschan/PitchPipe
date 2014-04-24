package com.adambeneschan.pitchpipe;

import java.io.File;

import android.app.Fragment;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Class to play chords using MIDI files.  There should be only one
 * instance of this class in an app.
 * @author Adam Beneschan
 */
public class ChordPlayer {

    private Play mPlayThread = null;
    private int mFileCounter = 0;
    private Fragment mFragment = null;
    private boolean mPlaying = false;
    
    private static ChordPlayer sPlayer = null;

    private static final String TAG = "ChordPlayer";

    private ChordPlayer() { }

    /**
     * Returns the single instance of this class.
     * @return The single instance of this class.
     */
    public static ChordPlayer getInstance() {
        if (sPlayer == null) {
            sPlayer = new ChordPlayer();
        }        
        return sPlayer;
    }
    
    /**
     * Specifies the {@code Fragment} that is using this {@code ChordPlayer}.
     * @param f The fragment.
     */
    public void setFragment(Fragment f) {
        mFragment = f;
    }

    /**
     * Starts playing a chord by creating a MIDI file and then playing it.  If
     * another chord is already playing, stops the play of that chord and
     * starts the new one.  
     * @param chord The chord to be played.
     * @param l If non-null, called back when the play of this chord has
     * stopped.
     */
    public void play(Chord chord, PlayStoppedListener l) {
        mPlaying = true;
        if (mPlayThread != null)  {
            mPlayThread.stopPlaying(true);
        }
        mPlayThread = new Play(chord, l);
        mPlayThread.start();
    }

    /**
     * Stops playing the current chord, if it is playing one.
     */
    public void stop() {
        if (mPlayThread != null)  
            mPlayThread.stopPlaying(false);
        mPlaying = false;
    }
    
    /**
     * Tests whether the player is currently playing a chord.
     * @return {@code true} if a chord is currently playing.  If {@code play()}
     * causes a chord to stop before a new one is started, the result will
     * still be {@code true} during any small gap between the chords.
     */
    public boolean isPlaying() {
        return mPlaying;
    }
    
    private class Play extends Thread {
        
        private Object mPlayDone = new Object();
        private volatile boolean mIsDone = false;
        private File mMidiFile;
        private MediaPlayer mPlayer = null;
        private Chord mChord;
        private PlayStoppedListener mCallback;
        private PlayStoppedListener.Reason mReason = null;

        public Play(Chord chord, PlayStoppedListener l) {
            mChord = chord;
            mCallback = l;
        }

        @Override
        public void run() {
            if (mFragment == null)
                return;
            
            // Create a temporary file name to hold the MIDI file
            mFileCounter++;
            mMidiFile = new File(mFragment.getActivity().getCacheDir(), "pp-mid" + mFileCounter + ".mid");
            
            try {
                // Create the MIDI file to play the chord
                MidiFileCreator.create(mMidiFile, mChord);
                
                // Set up the MediaPlayer to play the MIDI file
                mPlayer = new MediaPlayer();
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.setDataSource(mMidiFile.getAbsolutePath());
                mPlayer.prepare();
                mPlayer.setLooping(false);
                
                // Set up listener that runs when MIDI file is done playing.
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        synchronized(mPlayDone) {
                            mPlaying = false;
                            mIsDone = true;
                            mReason = PlayStoppedListener.Reason.COMPLETED;
                            mPlayDone.notify();
                        }
                    }
                });
                
                // Start the MediaPlayer.
                mPlayer.start();                
                
                // Wait until "done" notification, either by completion
                // listener or stopPlaying.
                synchronized(mPlayDone) {
                    while (!mIsDone)  // prevent spurious wakeup
                        mPlayDone.wait();
                }
                
                mPlayer.release();   // Release MediaPlayer resources
                mPlayer = null;
                mMidiFile.delete();  // Delete the temporary file
                if (mReason == null)
                    mReason = PlayStoppedListener.Reason.EXCEPTION;
                        // this shouldn't happen
                
                // Call PlayStoppedListener callback if any
                callListener(mReason);
            } catch (Exception e) {
                Log.e(TAG, "Exception creating/playing MIDI file", e);
                callListener(PlayStoppedListener.Reason.EXCEPTION);
            }
        }

        /**
         * If a chord is playing, tells the current MediaPlayer to stop.
         * @param newPlay True if the reason for stopping is in order to
         * immediately start a new chord play.
         */
        public void stopPlaying(boolean newPlay) {
            mReason = newPlay ? PlayStoppedListener.Reason.NEW_PLAY
                              : PlayStoppedListener.Reason.STOPPED;
            if (mPlayer != null) {
                try {
                    mPlayer.stop();
                    mPlayer.release();
                    synchronized(mPlayDone) {
                        mIsDone = true;
                        mPlayDone.notify();  // wake up run()
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Exception stopping MIDI file", e);
                }
            }
        }
    
        /**
         * Called when MediaPlayer completes.
         */
        private void callListener(PlayStoppedListener.Reason r) {
            if (mCallback != null)
                mCallback.onPlayStopped(r);
        }
    }

}
