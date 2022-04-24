package com.robsutar.Engine.Audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class OnePlayAudio {
    final File file;
    public OnePlayAudio(File file){
        this.file=file;
    }
    public void play(){
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            Clip c = AudioSystem.getClip();
            c.open(stream);
            c.start();
            c.addLineListener(event -> {
                if(event.getType() == LineEvent.Type.STOP) {
                    c.close();
                }
            });
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
}
