package xyz.Melody.module.modules.others;

import net.minecraft.network.play.server.S02PacketChat;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AutoGG extends Module {
   public AutoGG() {
      super("AutoGG", ModuleType.Others);
      this.setModInfo("Auto Say GG When Game Over.");
   }

   @EventHandler
   public void onPacket(EventPacketRecieve event) {
      if (event.getPacket() instanceof S02PacketChat) {
         S02PacketChat packet = (S02PacketChat)event.getPacket();
         String game = this.getSubString(packet.getChatComponent().toString(), "style=Style{hasParent=true, color=§b, bold=true, italic=null, underlined=null, obfuscated=null, clickEvent=ClickEvent{action=RUN_COMMAND, value='/play ", "'},");
         if (!game.contains("TextComponent") && !game.equalsIgnoreCase("")) {
            this.mc.thePlayer.sendChatMessage("GG");
         }
      }

   }

   public String getSubString(String text, String left, String right) {
      String result = "";
      int zLen;
      if (left != null && !left.isEmpty()) {
         zLen = text.indexOf(left);
         if (zLen > -1) {
            zLen += left.length();
         } else {
            zLen = 0;
         }
      } else {
         zLen = 0;
      }

      int yLen = text.indexOf(right, zLen);
      if (yLen < 0 || right == null || right.isEmpty()) {
         yLen = text.length();
      }

      result = text.substring(zLen, yLen);
      return result;
   }
}
