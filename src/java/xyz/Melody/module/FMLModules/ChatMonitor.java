package xyz.Melody.module.FMLModules;

import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StringUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.module.modules.QOL.GhostBlock;
import xyz.Melody.module.modules.QOL.Dungeons.LeverAura;
import xyz.Melody.module.modules.macros.PowderChestMacro;

public class ChatMonitor {
   public static boolean shouldShow = false;

   @SubscribeEvent(
      receiveCanceled = true
   )
   public void onNecron(ClientChatReceivedEvent event) {
      String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
      if (message.equals("[BOSS] Necron: Goodbye.")) {
         event.message = new ChatComponentText(event.message.getFormattedText().replaceAll("Goodbye.", "Goldor, Fuck You!"));
      }

      if (message.equals("[BOSS] Necron: ARGH!")) {
         event.message = new ChatComponentText(event.message.getFormattedText().replaceAll("ARGH!", "NMSL!"));
      }

      if (message.equals("[BOSS] Necron: All this, for nothing...")) {
         event.message = new ChatComponentText(event.message.getFormattedText().replaceAll("All this, for nothing...", "No handle for you..."));
      }

   }

   @SubscribeEvent(
      receiveCanceled = true
   )
   public void onChat(ClientChatReceivedEvent event) {
      String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
      if (message.contains("Mining Speed Boost is now available!")) {
         Client.pickaxeAbilityReady = true;
      }

      if (message.contains("You used your Mining Speed Boost Pickaxe Ability!")) {
         Client.pickaxeAbilityReady = false;
      }

      if (message.contains("You have successfully picked the lock on this chest!")) {
         PowderChestMacro.nextRotation = null;
         PowderChestMacro.done.add(PowderChestMacro.chestPos);
         PowderChestMacro.chest = null;
      }

   }

   @SubscribeEvent(
      receiveCanceled = true
   )
   public void onOMG(ClientChatReceivedEvent event) {
      String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
      String msg = event.message.getFormattedText();
      if (!message.contains("XJC") && (message.startsWith("PUZZLE FAIL") || message.startsWith("RARE REWARD") || message.contains("You were killed by") || message.contains("You were crushed") || message.contains("You fell into the void") || message.startsWith("PET DROP") || message.contains("You suffocated") || message.contains("You burnt to death") || message.contains("You died"))) {
         Client.instance.irc.sendPrefixMsg(msg, true);
      }

   }

   @SubscribeEvent(
      receiveCanceled = true
   )
   public void onBossSay(ClientChatReceivedEvent event) {
      String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
      if (message.contains("[BOSS]") && message.contains(":") && !message.contains("The Watcher")) {
         Client.instance.dungeonUtils.inBoss = true;
      }

      if (message.contains("[BOSS]") && message.contains(":") && message.contains("The Watcher")) {
         Client.instance.dungeonUtils.inBoss = false;
      }

   }

   @SubscribeEvent
   public void clear(WorldEvent.Load event) {
      if ((Boolean)PowderChestMacro.autoClear.getValue()) {
         PowderChestMacro.done.clear();
      }

      LeverAura.allLevers.clear();
      LeverAura.clicked.clear();
      LeverAura.blockPos = null;
      GhostBlock.blockposs.clear();
      Client.pickaxeAbilityReady = false;
   }
}
