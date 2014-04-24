/**
 * 
 */
package com.adambeneschan.pitchpipe;

/**
 * Interface definition for a Callback invoked when a chord's play has stopped.
 * @author Adam Beneschan
 *
 */
public interface PlayStoppedListener {

    /**
     * The reason a chord has stopped playing.
     */
    public enum Reason { 
        /** 
         * The chord played for the specified duration.
         */
        COMPLETED, 
        /** 
         * The chord's play was stopped by a stop() call.
         */
        STOPPED, 
        /** 
         * The chord's play was stopped by a play() call to play a 
         * different chord.
         */
        NEW_PLAY,
        /**
         * The chord's play was stopped (or could not be started) due
         * to an exception.
         */
        EXCEPTION
    }

    /**
     * Called when a chord's play has stopped;
     */
    public void onPlayStopped(Reason reason);

}    
