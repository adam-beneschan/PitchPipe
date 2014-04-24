package com.adambeneschan.pitchpipe;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.SharedPreferences;

/**
 * The main activity for PitchPipe.  The bulk of the logic is in MainFragment.
 * @author Adam Beneschan
 */
public class MainActivity extends Activity {

    MainFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Retrieve preferences.  "instrument" is the instrument (MIDI
        // program) used to play chords.
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        int instrument = settings.getInt("instrument", -1);
        PitchPipeData.getInstance().setInstrument(instrument);
        
        // Set up the instrument list.  The constructor reads the information
        // from resource files.
        InstrumentList l = new InstrumentList(this);
        PitchPipeData.getInstance().setInstrumentList(l);
        
        mFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.fragment_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items.  
        return mFragment.handleMenuItem(item) || super.onOptionsItemSelected(item);
    }

    @Override 
    protected void onPause() { 
        super.onPause();
        
        // If we're not simply changing configurations, and there's a chord
        // currently playing, terminate the play.
        if (!isChangingConfigurations() && mFragment != null)
            mFragment.stopPitches();
    }

    @Override
    protected void onStop() {
        super.onStop();
        
        // If we're not simply changing configurations, and there's a chord
        // currently playing, terminate the play.
        if (!isChangingConfigurations() && mFragment != null)
            mFragment.stopPitches();
        
        // Save preferences.
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("instrument", PitchPipeData.getInstance().getInstrument());
        editor.commit();
    }
}
