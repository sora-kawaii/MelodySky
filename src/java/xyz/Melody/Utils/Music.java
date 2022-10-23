package xyz.Melody.Utils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;

public class Music {
   public static synchronized void playSound(final Class clz, final String url) {
      (new Thread(new Runnable() {
         public void run() {
            try {
               Clip clip = AudioSystem.getClip();
               AudioInputStream inputStream = AudioSystem.getAudioInputStream(clz.getResourceAsStream("/assets/minecraft/Melody/Sounds/" + url));
               clip.open(inputStream);
               FloatControl gainControl = (FloatControl)clip.getControl(Type.MASTER_GAIN);
               gainControl.setValue(-17.0F);
               clip.start();
            } catch (Exception var4) {
               System.err.println(var4.getMessage());
            }

         }
      })).start();
   }
}
