package com.adambeneschan.pitchpipe;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * The fragment that performs the main activity for PitchPipe.  
 * The bulk of the activity's logic is in a Fragment, so that information
 * can be preserved easily over configuration changes (e.g. if the device
 * is rotated).  If the user is building up a chord, the information will
 * be preserved, for instance.
 * @author Adam Beneschan
 */
public class MainFragment extends Fragment {

    private OctaveIndicators mOctaves;
    private NoteButtons mNotes;
    
    private static final int sCENTER = 0x11;
    // defined by FrameLayout.LayoutParams; centers both horizontally and
    // vertically

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        PitchPipeData.getInstance().setChord(null);
        ChordPlayer.getInstance().setFragment(this);
        
        // Don't destroy the fragment if the device is rotated.
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        mOctaves = new OctaveIndicators(this);
        mNotes = new NoteButtons(getActivity(), Utilities.isLandscape(getActivity()));
        setupDisplay(mOctaves, mNotes, v);
        return v;
    }

    /**
     * Halts the play of the chord, if play is in progress.
     */
    public void stopPitches() {
        ChordPlayer.getInstance().stop();
    }
    
    /**
     * Replays the last chord, "rolling" it so that the singers can hear
     * their individual notes.  Does nothing if there is no chord to replay.
     */
    public void replayPitches() {
        PitchPipeData data = PitchPipeData.getInstance();
        ChordBuilder ch = data.getChord();
        if (ch != null) {
            int instr = data.getInstrument();
            int duration = getResources().getInteger(R.integer.chord_duration);
            int rollDelay = getResources().getInteger(R.integer.roll_delay);
            ch.play(instr, Math.min(duration, data.getInstrumentList().instrumentForCode(instr).getFade()), rollDelay);
                // We will "roll" the chord, waiting rollDelay ms after 
                // starting each note before starting the next one.  
        }
    }
    
    /**
     * Brings up the "instrument menu" activity to let the user select
     * an instrument (MIDI program) for playing chords from a menu.
     */
    public void changeInstrument() {
        Intent intent = new Intent(getActivity(), InstrumentActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
    }

    /**
     * Called to handle a menu (action bar) item.  Returns {@code true} if
     * the item is handled, {@code false} if the caller should handle it.
     */
    public boolean handleMenuItem(MenuItem item) {
        // Handle presses on the action bar items; delegated by MainActivity
        switch (item.getItemId()) {
            case R.id.action_stop:
                stopPitches();
                return true;
            case R.id.action_replay:
                replayPitches();
                return true;
            case R.id.action_change_instrument:
                changeInstrument();
                return true;
            default:
                return false;
        }
    }

    /**
     * Displays the main layout, consisting of "note buttons" (the buttons
     * the user presses to add notes to a chord) and "octave indicators" 
     * (graphics that show what octave each row of notes belongs to).
     * (Additional buttons may be added in the future to deal with smaller
     * displays.)
     * @param octaves Supplies the images used to display octave indicators.
     * @param notes Supplies the buttons used to select notes.
     * @param containing The view that contains the image and button views.
     */
    private void setupDisplay(OctaveIndicators octaves, NoteButtons notes, View containing) {
        TableRow row;
        
        if (!Utilities.isLandscape(getActivity())) {
            // Portrait mode.  Table will have 13 rows, 4 columns.  
            // The top item in each column is an image that indicates the
            // octave; below that are buttons for C, C#, ..., B.
        
            TableLayout octaveTable = (TableLayout) containing.findViewById(R.id.octaves);
            TableLayout buttonTable = (TableLayout) containing.findViewById(R.id.buttons);

            // The weight of each row is the height of the thing displayed
            // in the row (octave indicator or button); this will cause the
            // rows to be spaced pretty evenly in the layout.
               
            // Set up the first row.
            row = new TableRow(getActivity());
            for (int c = 0; c < 4; c++) {
                View indicator = octaves.getIndicator(c);
                row.addView(indicator);
                indicator.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 1F));
            }
            row.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, (float)octaves.indicatorHeight()));
            octaveTable.addView(row);

            // Set up rows of buttons.
            for (int r = 0; r < 12; r++) {
                row = new TableRow(getActivity());
                for (int c = 0; c < 4; c++) {
                    // column number = octave number, row number = note number
                    View button = notes.getButton(c, r);
                    setupButtonListener(button, c, r);
                    row.addView(button);
                    button.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, 1F));
                }
                row.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, (float)notes.buttonHeight()));
                buttonTable.addView(row);
            }
        
        } else {
            // Landscape mode.  Table will have 4 rows, 13 columns.
            // The leftmost item in each row is an image that indicates the
            // octave; to the right are buttons for C, C#, ..., B.
        
            TableLayout table = (TableLayout) containing.findViewById(R.id.octaves_and_buttons);
            
            // The weight of each column is the width of the thing displayed
            // in the row (octave indicator or button); this will cause the
            // rows to be spaced pretty evenly in the layout.
            
            for (int r = 0; r < 4; r++) {
                row = new TableRow(getActivity());
                FrameLayout cell = new FrameLayout(getActivity());
                
                // Add octave indicator to row.
                View indicator = octaves.getIndicator(r);
                indicator.setLayoutParams(new FrameLayout.LayoutParams(octaves.indicatorWidth(), octaves.indicatorHeight(), sCENTER));
                cell.addView(indicator);
                cell.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, octaves.indicatorWidth()));
                row.addView(cell);                
                
                // Add note buttons to row.
                for (int c = 0; c < 12; c++) {
                    cell = new FrameLayout(getActivity());
                    // row number = octave number, column number = note number
                    View button = notes.getButton(r, c);
                    setupButtonListener(button, r, c);
                    button.setLayoutParams(new FrameLayout.LayoutParams(notes.buttonWidth(), notes.buttonHeight(), sCENTER));
                    cell.addView(button);
                    cell.setLayoutParams(new TableRow.LayoutParams(0, LayoutParams.MATCH_PARENT, notes.buttonWidth()));
                    row.addView(cell);
                }
                
                // Add the row to the view.
                row.setLayoutParams(new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0, 1F));
                table.addView(row);
            }
        }
    }

    private void setupButtonListener(View button, final int octave, final int note) {
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addNote(octave, note);
            }
        });
    }

    public void addNote(int octave, int note) {
        int n = 36 + 12*octave + note;
        
        final PitchPipeData data = PitchPipeData.getInstance();
        ChordPlayer player = ChordPlayer.getInstance();
        ChordBuilder ch = data.getChord();
        if (ch == null || !player.isPlaying() || data.getPlayingChord() != ch) {
            // Start a new chord, unless we've already started one and that
            // same chord is still playing, in which case we want to add to
            // it.
            ch = new ChordBuilder();
            data.setChord(ch);
        }
        ch.add(n);
        data.setPlayingChord(ch);
        int instr = data.getInstrument();
        int duration = getResources().getInteger(R.integer.chord_duration);
        ch.play(instr, Math.min(duration, data.getInstrumentList().instrumentForCode(instr).getFade()), 0);
    }

}
