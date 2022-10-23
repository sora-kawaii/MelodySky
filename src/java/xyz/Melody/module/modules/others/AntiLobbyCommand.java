package xyz.Melody.module.modules.others;

import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.misc.EventChat;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AntiLobbyCommand extends Module {
   private Option od = new Option("DungeonOnly", true);

   public AntiLobbyCommand() {
      super("AntiLobbyCommands", new String[]{"alc", "asc", "lobby"}, ModuleType.Others);
      this.addValues(new Value[]{this.od});
      this.setModInfo("Prevents You using /l or /spawn.");
      this.setEnabled(true);
   }

   @EventHandler
   private void onChat(EventChat e) {
      if (Client.inDungeons || !(Boolean)this.od.getValue()) {
         String msg = e.getMessage();
         if (msg.toLowerCase().contains("/lobby")) {
            Helper.sendMessage(EnumChatFormatting.GREEN + "[AntiLobbyCommand] " + EnumChatFormatting.DARK_GREEN + "Prevented you using " + EnumChatFormatting.RED + "/lobby " + EnumChatFormatting.DARK_GREEN + "Command.");
            e.setCancelled(true);
         } else if (msg.toLowerCase().equals("/l")) {
            Helper.sendMessage(EnumChatFormatting.GREEN + "[AntiLobbyCommand] " + EnumChatFormatting.DARK_GREEN + "Prevented you using " + EnumChatFormatting.RED + "/l " + EnumChatFormatting.DARK_GREEN + "Command.");
            e.setCancelled(true);
         } else if (msg.toLowerCase().contains("/spawn")) {
            Helper.sendMessage(EnumChatFormatting.GREEN + "[AntiLobbyCommand] " + EnumChatFormatting.DARK_GREEN + "Prevented you using " + EnumChatFormatting.RED + "/spawn " + EnumChatFormatting.DARK_GREEN + "Command.");
            e.setCancelled(true);
         }
      }
   }
}
