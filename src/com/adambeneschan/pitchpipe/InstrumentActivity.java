package com.adambeneschan.pitchpipe;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.app.Activity;
import android.app.ListActivity;

/**
 * Activity that lets user select instrument (MIDI program) from a menu.
 * @author Adam Beneschan
 */
public class InstrumentActivity extends ListActivity implements OnItemClickListener {

    private int mCurrInstr;
    private boolean mFirst;
    
    // Class representing one instrument in the menu, used for ArrayAdapter.
    
    private static class MidiInstrumentForList {
        private MidiInstrument mInstr;
        
        public MidiInstrumentForList(MidiInstrument instr) {
            mInstr = instr;
        }
        
        public MidiInstrument getInstr() {
            return mInstr;
        }

        public int getCode() {
            return mInstr.getCode();
        }

        @Override
        public String toString() {
            return mInstr.getCode() + ". " + mInstr.getName();
        }
    }

    private static List<MidiInstrumentForList> sList;
    
    // Customize the ArrayAdapter so that each menu item has a string and
    // possibly a check mark (indicating that the item is the currently
    // selected one).  getView arranges them according to the layout
    // resource instrument_item.xml.

    private class InstrumentAdapter extends ArrayAdapter<MidiInstrumentForList> {
        List<MidiInstrumentForList> mValues;
        Activity mActivity;

        public InstrumentAdapter(Activity a, List<MidiInstrumentForList> values) {
            super(a, R.layout.instrument_item, values);
            this.mActivity = a;
            this.mValues = values;
        }
    
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = mActivity.getLayoutInflater();
            View itemView = inflater.inflate(R.layout.instrument_item, parent, false);
            
            // Set up the text field
            TextView text = (TextView)itemView.findViewById(R.id.instrument_text);
            text.setText(mValues.get(position).toString());
            
            // Set up the "check mark" field 
            ImageView image = (ImageView)itemView.findViewById(R.id.instrument_current);
            if (mValues.get(position).getCode() == mCurrInstr) {
                // Display a check mark
                image.setImageResource(R.drawable.btn_check_buttonless_on);
            } else {
                // Clear the check mark field
                image.setImageDrawable(null);
            }
            return itemView;
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrument);
    
        // The instrument currently selected
        mCurrInstr = PitchPipeData.getInstance().getInstrument();

        ListAdapter adapter = new InstrumentAdapter(this, instrumentList());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
        mFirst = true;
    }

    // Display the menu so that the currently selected item is a little 
    // above the center of the display.  getHeight() will not work right 
    // away when the activity is created, so we need to wait until it 
    // returns a valid result, i.e. when onWindowFocusChanged is called.
    // This will adjust the display only the *first* time 
    // onWindowFocusChanged is called with hasFocus==true.
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mFirst) {
            getListView().setSelectionFromTop(positionOfInstrument(mCurrInstr), (int) (getListView().getHeight() * 0.45));
            mFirst = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.instrument, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_stop:
                ChordPlayer.getInstance().stop();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Called when a menu item is selected.  Displays a menu with "Listen" and
    // "Select" options.  "Listen" plays a note using the instrument;
    // Select selects it as the instrument to use for playing chords, and
    // terminates the activity.
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        // Determine the instrument that was clicked
        final MidiInstrument instr = PitchPipeData.getInstance().getInstrumentList().list().get(position);
        
        // Set up popup menu with Listen, Select
        final PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.instrument_popup, popup.getMenu());
        
        // Popup menu actions
        popup.setOnMenuItemClickListener (new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_listen:
                        listenInstrument(instr);  // Play a note using the instrument
                        popup.show();             // Keep the popup menu displayed
                        return true;
                    case R.id.action_select:
                        selectInstrument(instr);  
                        finish();  // completes Activity and goes back to previous activity
                        return true;
                    default:
                        return false;
                }
            }
        });
        
        // Display popup menu
        popup.show();
    }

    /**
     * Select the instrument for playing chords.
     */
    private void selectInstrument(MidiInstrument instr) {
        PitchPipeData.getInstance().setInstrument(instr.getCode());
    }

    /**
     * Let the user hear what the instrument sounds like.  Plays a middle C
     * using that instrument for a short period.
     */
    private void listenInstrument(MidiInstrument instr) {
        PitchPipeData data = PitchPipeData.getInstance();
        
        // Create a "chord" of just one note.
        ChordBuilder listenNote = new ChordBuilder();
        listenNote.add(60);  // middle C
        data.setPlayingChord(listenNote);
        // do not use data.setChord; that keeps the "main" chord built up
        // by selecting the notes
        
        // Maximum time to play the note.
        int duration = getResources().getInteger(R.integer.listen_duration);
        
        // Play it.
        listenNote.play(instr.getCode(), duration, 0);
    }

    /**
     * Determines where on the menu an instrument will be.
     * @param instrument A MIDI instrument code.
     * @return The index in the menu where the instrument is located, or
     * 0 (i.e. return a valid index) if the instrument isn't on the menu.
     */
    private int positionOfInstrument(int instrument) {
        for (int i = 0; i < sList.size(); i++) {
            if (sList.get(i).getCode() == instrument)
                return i;
        }
        return 0;
    }

    /**
     * Returns the instruments that will appear on the menu.
     * @return The list of instruments.  The first time this is called,
     * the list is created from PitchPipeData's getInstrumentList, which
     * is built from resource files.
     */
    private List<MidiInstrumentForList> instrumentList() {
        if (sList == null) {
            sList = new ArrayList<MidiInstrumentForList>();
            for (MidiInstrument instr : PitchPipeData.getInstance().getInstrumentList().list()) 
                sList.add(new MidiInstrumentForList(instr));
        }
        return sList;
    }
}
