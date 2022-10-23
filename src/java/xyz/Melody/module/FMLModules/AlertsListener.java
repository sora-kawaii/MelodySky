package xyz.Melody.module.FMLModules;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.module.modules.QOL.Dungeons.Alerts;

public class AlertsListener {
   public static boolean shouldShowWatcherReady = false;
   public static boolean shouldShowSpiritMaskPoped = false;
   public static boolean shouldShowBonzoMaskPoped = false;
   public static boolean shouldShowBonzoMask2Poped = false;

   @SubscribeEvent(
      receiveCanceled = true
   )
   public void onChat(ClientChatReceivedEvent event) {
      String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
      if (Client.inDungeons) {
         Alerts omg = (Alerts)Client.instance.getModuleManager().getModuleByClass(Alerts.class);
         if (message.equals("[BOSS] The Watcher: That will be enough for now.") && omg.isEnabled() && (Boolean)omg.watcher.getValue()) {
            shouldShowWatcherReady = true;
            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0F, 0.5F);
         }

         if (message.equals("Second Wind Activated! Your Spirit Mask saved your life!") && omg.isEnabled() && (Boolean)omg.spirit.getValue()) {
            shouldShowSpiritMaskPoped = true;
            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0F, 0.5F);
         }

         if (message.equals("Your Bonzo's Mask saved your life!") && omg.isEnabled() && (Boolean)omg.bonzo.getValue()) {
            shouldShowBonzoMaskPoped = true;
            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0F, 0.5F);
         }

         if ((message.equals("Your ⚚ Bonzo's Mask saved your life!") || message.equals("Your ⚚ Bonzo's Mask saved your life!")) && omg.isEnabled() && (Boolean)omg.bonzo.getValue()) {
            shouldShowBonzoMask2Poped = true;
            Minecraft.getMinecraft().thePlayer.playSound("random.orb", 1.0F, 0.5F);
         }

      }
   }
}
