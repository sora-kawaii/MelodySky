package xyz.Melody.module.FMLModules;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xyz.Melody.Client;
import xyz.Melody.System.IRC.IRC;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;

public class IRCKeepAlive {
   private Minecraft mc = Minecraft.getMinecraft();
   private TimerUtil timer = new TimerUtil();
   private boolean remnided = false;
   private int ticks = 0;

   @SubscribeEvent
   public void onTick(TickEvent event) {
      if (this.ticks < 21) {
         ++this.ticks;
      } else if (Client.instance.irc != null && Client.instance.ircThread != null) {
         if (Client.instance.ircExeption && !this.remnided && this.mc.theWorld != null && this.mc.thePlayer != null) {
            Helper.sendMessage(EnumChatFormatting.RED + "[IMPORTANT] IRC Connection Lost, type " + EnumChatFormatting.GREEN + ".irc reconnect" + EnumChatFormatting.RED + " to Reconnect.");
            this.remnided = true;
         }

         if (Client.instance.ircThread.isAlive()) {
            Client.instance.ircExeption = false;
         } else {
            Client.instance.ircExeption = true;
         }

         if (!Client.instance.ircExeption) {
            this.remnided = false;
         }

         if (this.timer.hasReached(30000.0) && !Client.instance.ircExeption && IRC.pw != null) {
            IRC.pw.println("INGAME_VERIFY: " + Minecraft.getMinecraft().getSession().getUsername() + "@" + Client.instance.authManager.verified + "@" + Client.instance.authManager.getUser().getUuid() + "@" + Client.instance.authManager.getUser().getToken() + "@MelodySky");
            Client.instance.logger.info("[Melody] [IRC] Sent Keep Alive to Server.");
            this.timer.reset();
         }

         this.ticks = 0;
      }
   }
}
