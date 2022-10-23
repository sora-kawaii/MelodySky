package xyz.Melody.System.IRC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import xyz.Melody.Client;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;

public class IRC {
   private static Minecraft mc = Minecraft.getMinecraft();
   public BufferedReader reader;
   public static Socket socket;
   public static PrintWriter pw;
   static InputStream in;
   private TimerUtil timer = new TimerUtil();
   public boolean shouldThreadStop = false;

   public void exit() {
      if (pw != null) {
         this.sendMessage("CLOSE");
      }

   }

   public void start() {
      this.shouldThreadStop = false;
      Client.instance.ircThread = new IRCThread();
      Client.instance.ircThread.setName("MelodyChat");
      Client.instance.ircThread.start();
   }

   public void handleInput() {
      byte[] data = new byte[1024];
      if (pw != null && in != null) {
         if (!Client.stopping && !this.shouldThreadStop) {
            try {
               int len = in.read(data);
               String ircmessage = new String(data, 0, len);
               ircmessage = ircmessage.replaceAll("\n", "");
               ircmessage = ircmessage.replaceAll("\r", "");
               ircmessage = ircmessage.replaceAll("\t", "");
               if (ircmessage.equals("CLOSE")) {
                  return;
               }

               if (ircmessage.equals("RECEIVED_ALIVE")) {
                  Client.instance.ircExeption = false;
                  Client.instance.logger.info("[Melody] [IRC] Server Sent RECEIVED_ALIVE to You.");
                  return;
               }

               if (!Client.instance.authManager.verified) {
                  Client.ircNotVerified = true;
                  Client.instance.logger.error("[Melody] [IRC] DETECTED NONE-VERIFIED USER, CONNECTION DENIED.");
                  this.sendMessage("NONE_VERIFIED_USER_TOKEN");
                  this.sendMessage("CLOSE");
                  if (mc.theWorld != null && mc.thePlayer != null) {
                     Helper.sendMessage("[IRC] Detected That You are a NONE-Verified User, so You Will not Connect to IRC Server.");
                  }

                  this.disconnect();
                  return;
               }

               if (mc.thePlayer != null && mc.theWorld != null) {
                  mc.thePlayer.addChatMessage(new ChatComponentText(ircmessage));
               }

               Client.instance.logger.info("[Melody] [IRC] " + ircmessage);
            } catch (IOException var4) {
               Client.instance.ircExeption = true;
               Client.instance.logger.info("[Melody] [CONSOL] IRC Ran into an Exception.");
               var4.printStackTrace();
            } catch (StringIndexOutOfBoundsException var5) {
               Client.instance.ircExeption = true;
               Client.instance.logger.info("[Melody] [CONSOL] IRC Ran into an Exception.");
               var5.printStackTrace();
            }

         }
      } else {
         Client.instance.logger.error("[Melody] [IRC] Unexpected Error.");
         Client.instance.ircExeption = true;
      }
   }

   public void connect() {
      if (!Client.instance.authManager.verified) {
         if (mc.theWorld != null && mc.thePlayer != null) {
            Helper.sendMessage("[IRC] Failed to Verify Client User.");
            Helper.sendMessage("[AUTHENTICATION] Blocked Your Connection to the IRC Server.");
         }

         Client.instance.logger.error("[Melody] [IRC] Failed to Verify Client User.");
         Client.instance.logger.error("[Melody] [AUTHENTICATION] Blocked Your Connection to the IRC Server.");
      } else {
         try {
            socket = new Socket("127.0.0.1", 25565);
            in = socket.getInputStream();
            pw = new PrintWriter(socket.getOutputStream(), true);
            pw.println(Minecraft.getMinecraft().getSession().getUsername() + "@" + Client.instance.authManager.verified + "@" + Client.instance.authManager.getUser().getUuid() + "@" + Client.instance.authManager.getUser().getToken() + "@MelodySky");
            if (mc.thePlayer != null && mc.theWorld != null) {
               Helper.sendMessage("IRC Connected!");
            }

            Client.instance.logger.info("[Melody] [CONSOLE] IRC Connected!");
            Client.instance.ircExeption = false;
         } catch (IOException var2) {
            if (mc.thePlayer != null && mc.theWorld != null) {
               Helper.sendMessage("Failed to Connect IRC Server.");
            }

            Client.instance.ircExeption = true;
            var2.printStackTrace();
         }

      }
   }

   public void disconnect() {
      if (socket != null && in != null && pw != null) {
         this.shouldThreadStop = true;

         try {
            this.sendMessage("CLOSE");
            socket.close();
            in.close();
            pw.close();
            socket = null;
            in = null;
            pw = null;
            if (mc.theWorld != null && mc.thePlayer != null) {
               Helper.sendMessage("Disconnected to IRC Server.");
            }
         } catch (IOException var2) {
            var2.printStackTrace();
         }
      } else {
         Helper.sendMessage("Already Disconnected From IRC Server.");
      }

   }

   public void sendMessage(String msg) {
      if (!Client.instance.authManager.verified) {
         if (mc.theWorld != null && mc.thePlayer != null) {
            Helper.sendMessage("[IRC] Failed to Verify Client User.");
            Helper.sendMessage("[AUTHENTICATION] Blocked Your Connection to the IRC Server.");
         }

         Client.instance.logger.error("[Melody] [IRC] Failed to Verify Client User.");
         Client.instance.logger.error("[Melody] [AUTHENTICATION] Blocked Your Connection to the IRC Server.");
      } else if (pw == null) {
         Client.instance.logger.error("[Melody] [IRC] Unexpected Error.");
         Client.instance.ircExeption = true;
      } else {
         pw.println(msg);
      }
   }

   public void sendPrefixMsg(String message, boolean prefix) {
      if (!Client.instance.authManager.verified) {
         if (mc.theWorld != null && mc.thePlayer != null) {
            Helper.sendMessage("[IRC] Failed to Verify Client User.");
            Helper.sendMessage("[AUTHENTICATION] Blocked Your Connection to the IRC Server.");
         }

         Client.instance.logger.error("[Melody] [IRC] Failed to Verify Client User.");
         Client.instance.logger.error("[Melody] [AUTHENTICATION] Blocked Your Connection to the IRC Server.");
      } else if (pw == null) {
         Client.instance.logger.error("[Melody] [IRC] Unexpected Error.");
         Client.instance.ircExeption = true;
      } else {
         String pref = "";
         String uuid = mc.getSession().getProfile().getId().toString();
         if (uuid.contains("6ceb8943-c0cf-49e8-b416-ac7d8b60261e")) {
            pref = "§d[Melody]§b" + mc.thePlayer.getName();
            Client.devMode = true;
         } else if (uuid.contains("293e94c6-53b6-4876-bd82-771611b549a9")) {
            pref = "§6[纸]§b" + mc.thePlayer.getName();
         } else if (uuid.contains("222f316f-5ec8-4298-b98e-5ffe2f98a228")) {
            pref = "§e[SMCP]§b" + mc.thePlayer.getName();
         } else if (uuid.contains("3d92223f-319a-4cbb-8eae-559a809d7598")) {
            pref = "§b[琪亚娜]§b" + mc.thePlayer.getName();
         } else if (uuid.contains("ea1e1b9f-b3fb-4585-a623-abc147411b58")) {
            pref = "§2[狗]§b" + mc.thePlayer.getName();
         } else if (uuid.contains("35bec0ee-5b7b-4e0e-8190-cda5eaed6456")) {
            pref = "§z[我不是虫神]§b" + mc.thePlayer.getName();
         } else if (uuid.contains("d90df5f2-1d55-40e2-b765-28afd2c5fb0c")) {
            pref = "§3[user]§zMeow_Emilia";
         } else {
            pref = "§3[user]§b" + mc.thePlayer.getName();
         }

         pw.println("§7Melody > " + pref + "§r: " + message);
      }
   }
}
