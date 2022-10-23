package xyz.Melody.System.Commands.commands;

import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;

public class NameCommand extends Command {
   public NameCommand() {
      super(".name", new String[]{"name"}, "", "sketit");
   }

   public String execute(String[] args) {
      if (args.length >= 1) {
         if (args[0].toLowerCase().equals("reset")) {
            Client.playerName = this.mc.getSession().getUsername();
            Helper.sendMessage("Reset Custom Name to " + Client.playerName + ".");
            return null;
         }

         String name = args[0].replaceAll("&", "§");
         Client.playerName = name;
         Helper.sendMessage("Set Custom Name to " + Client.playerName + ".");
         Client.instance.saveCustomName();
      } else {
         Helper.sendMessage("Correct Useage: .name [name] / .name reset");
      }

      return null;
   }
}
