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
    public static synchronized void playSound(final Class<?> clz, final String url) {
        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(clz.getResourceAsStream("/assets/minecraft/Melody/Sounds/" + url));
                    clip.open(inputStream);
                    FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-10.0f);
                    clip.start();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

