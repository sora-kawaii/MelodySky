/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public final class Music {
    public static synchronized void playSound(final Class<?> clazz, final String string) {
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(clazz.getResourceAsStream("/assets/minecraft/Melody/Sounds/" + string));
                    clip.open(audioInputStream);
                    FloatControl floatControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                    floatControl.setValue(-10.0f);
                    clip.start();
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }).start();
    }
}

