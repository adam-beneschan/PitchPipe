package com.adambeneschan.pitchpipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

public class InstrumentList {

    private List<MidiInstrument> sInstrList;

    // Pattern used for both instrument_list and instrument_names strings
    // in resource files.  The pattern is a numeric MIDI program code, 
    // followed by ";", followed by data.
    private static final Pattern sInstrPattern = Pattern.compile("^([0-9]+);(.*)$");


    /** 
     * Constructor for InstrumentList.
     * @param c The Context in which the InstrumentList should retrieve any resources.
     */
    public InstrumentList(Context c) { 
        
        Resources res = c.getResources();
        Map<Integer,String> names = new HashMap<Integer,String>();
        
        // Retrieve instrument names from resource files.  These are 
        // kept in a map, by program code.  The actual list of instruments
        // that appear in the menu is in the instrument_list resource; 
        // codes that appear here but not in the instrument_list will not
        // be used.
        
        TypedArray instrNames = res.obtainTypedArray(R.array.instrument_names);
        for (int i = 0; i < instrNames.length(); i++) {
            String s = instrNames.getString(i);
            if (s != null) {
                Matcher m = sInstrPattern.matcher(s);
                if (m.find()) {
                    int code = Integer.valueOf(m.group(1));
                    names.put(code, m.group(2));
                }
            }
        }
    
        // Retrieve the list of instruments, along with other data 
        // (currently, the approximate fade-out time), and create 
        // sInstrList from the data (using the name from the map created
        // above).
        
        TypedArray instrList = res.obtainTypedArray(R.array.instrument_list);
        sInstrList = new ArrayList<MidiInstrument>();
        for (int i = 0; i < instrList.length(); i++) {
            String s = instrList.getString(i);
            if (s != null) {
                Matcher m = sInstrPattern.matcher(s);
                if (m.find()) {
                    int code = Integer.valueOf(m.group(1));
                    String fadeString = m.group(2);
                    int fade;
                    if ("none".equals(fadeString)) {
                        fade = Integer.MAX_VALUE;
                    } else {
                        fade = Integer.valueOf(fadeString);
                    }
                    String name = names.get(code);
                    if (name == null) {
                        // If no name, just use a dummy name.  The
                        // alternative would be not to add it to sInstrList.
                        name = "Program " + code;
                    }
                    sInstrList.add(new MidiInstrument(code, name, fade));
                }
            }
        }
    
    }

    /** 
     * Returns a list of all instruments that can be used for chords.
     * @return A list of all available instruments.
     */
    public List<MidiInstrument> list() {
        return sInstrList;
    }

    /** 
     * Returns the MidiInstrument in the list with a specified code.
     * @param code The MIDI instrument (program) code (see http://www.midi.org/techspecs/gm1sound.php).
     * @return The MidiInstrument whose code is {@code code}, or 
     * null if there is no such instrument in the list.
     */
    public MidiInstrument instrumentForCode(int code) {
        for (MidiInstrument instr : sInstrList) {
            if (instr.getCode() == code)
                return instr;
        }
        return null;
    }

}
