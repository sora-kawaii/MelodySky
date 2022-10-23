package xyz.Melody.System.Managers.Authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.System.Managers.Manager;
import xyz.Melody.Utils.TimerUtil;

public class AuthManager implements Manager {
   private UserObj user;
   public boolean verified = false;
   private TimerUtil timer = new TimerUtil();
   private int ticks = 0;

   public AuthManager(UserObj user) {
      this.user = user;
   }

   public UserObj getUser() {
      return this.user;
   }

   public void init() {
      EventBus.getInstance().register(this);
      this.verify(this.user.getUuid(), this.user.getName());
   }

   @EventHandler
   private void onTick() {
      if (this.ticks < 11) {
         ++this.ticks;
      } else {
         if (this.user != null && this.timer.hasReached(30000.0)) {
            this.verify(this.user.getUuid(), this.user.getName());
            this.timer.reset();
         }

         this.ticks = 0;
      }
   }

   private void verify(String uuid, String name) {
      if (Client.ircNotVerified) {
         this.verified = false;
      } else if (this.isDebugMode(name)) {
         this.verified = true;
      } else {
         try {
            String cloud = this.get("https://gitee.com/AuroraClient/MelodySky-UUID/raw/master/UUIDs");
            if (cloud.contains(uuid)) {
               this.verified = true;
            } else {
               this.verified = false;
            }
         } catch (IOException var4) {
            var4.printStackTrace();
            this.verified = false;
         }

      }
   }

   private boolean isDebugMode(String name) {
      if (name.length() < 6) {
         return false;
      } else {
         String isPlayer = "";

         for(int i = 0; i < 6; ++i) {
            char c = name.charAt(i);
            if (i == 0) {
               isPlayer = isPlayer + String.valueOf(c);
            } else if (i == 1) {
               isPlayer = isPlayer + String.valueOf(c);
            } else if (i == 2) {
               isPlayer = isPlayer + String.valueOf(c);
            } else if (i == 3) {
               isPlayer = isPlayer + String.valueOf(c);
            } else if (i == 4) {
               isPlayer = isPlayer + String.valueOf(c);
            } else if (i == 5) {
               isPlayer = isPlayer + String.valueOf(c);
            }
         }

         if (isPlayer.equals("Player")) {
            return true;
         } else {
            return false;
         }
      }
   }

   public String get(String url) throws IOException {
      String result = "";

      try {
         URL realurl = new URL(url);
         HttpURLConnection httpUrlConn = (HttpURLConnection)realurl.openConnection();
         httpUrlConn.setDoInput(true);
         httpUrlConn.setRequestMethod("GET");
         httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0");
         InputStream input = httpUrlConn.getInputStream();
         InputStreamReader read = new InputStreamReader(input, "utf-8");
         BufferedReader br2 = new BufferedReader(read);

         for(String data = br2.readLine(); data != null; data = br2.readLine()) {
            result = String.valueOf(result) + data + "\n";
         }

         br2.close();
         read.close();
         input.close();
         httpUrlConn.disconnect();
      } catch (MalformedURLException var10) {
      } catch (IOException var11) {
      }

      return result;
   }
}
