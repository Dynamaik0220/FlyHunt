package game;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.*;

public class MusikPlayer
{

    Clip    background_Clip;            // Clip for looping background music
    boolean mute            = false;    // needed to be able to mute during ingame

    public MusikPlayer()
    {
        try
        {
            InputStream         resource_Path       = getClass().getResourceAsStream("music/background.wav");                   // get background music as RessourceStream from inside the jar
            AudioInputStream    background_Stream   = AudioSystem.getAudioInputStream(new BufferedInputStream(resource_Path));  // get and AudioInputStream from the resource
            AudioFormat         background_Format   = background_Stream.getFormat();                                            // check the audioformat from the audiostream
            DataLine.Info       background_Info     = new DataLine.Info(Clip.class, background_Format);                         // get a LineInfo for the format

            background_Clip = (Clip) AudioSystem.getLine(background_Info);  // get a new Line for the background loop
            background_Clip.open(background_Stream);                        // open the Line

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void startBackgroundMusik()
    {
        if (!mute)  // check if sounds are muted right now
        {
            try
            {
                background_Clip.loop(Clip.LOOP_CONTINUOUSLY);   // start looping background music
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void stopBackgroundMusik()
    {
        if (!mute)  // check if sounds are muted right now
        {
            try
            {
                background_Clip.stop(); // stop background music
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void playSuccess()
    {
        if (!mute)  // check if sounds are muted right now
        {
            Clip success_Clip = null;   // new Clip for the success sound
            try
            {
                InputStream         resource_Path   = getClass().getResourceAsStream("music/success.wav");                // get success sound from inside jar
                AudioInputStream    success_Stream  = AudioSystem.getAudioInputStream(new BufferedInputStream(resource_Path));  // get the resourceStream as AdioInputStream
                AudioFormat         success_Format  = success_Stream.getFormat();                                               // check audioformat
                DataLine.Info       success_Info    = new DataLine.Info(Clip.class, success_Format);                            // get LineInfo

                success_Clip = (Clip) AudioSystem.getLine(success_Info);    // get a new Line for play the success sound
                success_Clip.open(success_Stream);                          // open the new Line
                success_Clip.start();                                       // start the sound

            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void unMute()
    {
        if (!mute)  // check if sounds are muted right now
        {
            stopBackgroundMusik();  // stop background music
            mute = true;            // set sounds muted
            return;                 // leave method
        }

        if (mute)  // check if sounds are muted right now
        {
            mute = false;           // set sounds unmuted
            startBackgroundMusik(); // start background music again
        }
    }
}
