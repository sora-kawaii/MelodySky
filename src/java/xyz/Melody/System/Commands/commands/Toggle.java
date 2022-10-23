package xyz.Melody.System.Commands.commands;

import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.Module;

public class Toggle extends Command {
   public Toggle() {
      super("t", new String[]{"toggle", "togl", "turnon", "enable"}, "", "Toggles a specified Module");
   }

   public String execute(String[] args) {
      String modName = "";
      if (args.length > 1) {
         modName = args[1];
      } else if (args.length < 1) {
         Helper.sendMessageWithoutPrefix("§bCorrect usage:§7 .t <module>");
      }

      boolean found = false;
      Module m = Client.instance.getModuleManager().getAlias(args[0]);
      if (m != null) {
         if (!m.isEnabled()) {
            m.setEnabled(true);
         } else {
            m.setEnabled(false);
         }

         found = true;
         if (m.isEnabled()) {
            Helper.sendMessage("> " + m.getName() + EnumChatFormatting.GRAY + " was" + EnumChatFormatting.GREEN + " enabled");
         } else {
            Helper.sendMessage("> " + m.getName() + EnumChatFormatting.GRAY + " was" + EnumChatFormatting.RED + " disabled");
         }
      }

      if (!found) {
         Helper.sendMessage("> Module name " + EnumChatFormatting.RED + args[0] + EnumChatFormatting.GRAY + " is invalid");
      }

      return null;
   }
}
