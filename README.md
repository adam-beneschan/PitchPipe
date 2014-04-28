PitchPipe is an Android app I wrote for practice; it's the first app I
wrote that doesn't involve displaying "Hello, world".  

The idea is that if you're out somewhere with a group of choral
singers but don't have a piano handy, you can use this to give the
singers their pitches.  

## License

Some of the code is adapted from code written by Kevin Boone; see
http://kevinboone.net/javamidi.html.  Please see
http://kevinboone.net/download_policy.html for his policy on using his
work. 

## Building

This repo is set up as an Eclipse project.  It should be possible to
import this directly into Eclipse and build it, if you've configured
your Eclipse to work with Android projects.  If not, my apologies; I'm
new at this.  You may have to configure the build path.  Minimum
Android API is level 11.

## Features

Well, not too many yet.  But there's a Stop button to stop a chord
that's currently playing, and a Replay to replay the last chord.  The
menu has "Select instrument" so that you can change the instrument
(MIDI program) used to play the notes.

## Deficiencies

I've gotten this to work on a Samsung Galaxy Note 3, which has a large
display, for a phone.  I have not tested it on smaller displays.  It
is likely that a smaller display will not support the number of
buttons the app currently displays, and the UI will need to be
different.  This is something I'm planning to address.
