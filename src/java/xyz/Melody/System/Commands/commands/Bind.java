package xyz.Melody.System.Commands.commands;

import org.lwjgl.input.Keyboard;
import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.Module;

public class Bind extends Command {
   public Bind() {
      super("Bind", new String[]{"b"}, "", "sketit");
   }

   public String execute(String[] args) {
      if (args.length >= 2) {
         Module m = Client.instance.getModuleManager().getAlias(args[0]);
         if (m != null) {
            int k = Keyboard.getKeyIndex(args[1].toUpperCase());
            m.setKey(k);
            Object[] arrobject = new Object[]{m.getName(), k == 0 ? "none" : args[1].toUpperCase()};
            Helper.sendMessage(String.format("> Bound %s to %s", arrobject));
         } else {
            Helper.sendMessage("> Invalid module name, double check spelling.");
         }
      } else {
         Helper.sendMessageWithoutPrefix("§bCorrect usage:§7 .bind <module> <key>");
      }

      return null;
   }
}
