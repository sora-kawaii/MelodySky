package xyz.Melody.System.Commands.commands;

import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;

public class Help extends Command {
   public Help() {
      super("Help", new String[]{"list"}, "", "sketit");
   }

   public String execute(String[] args) {
      if (args.length == 0) {
         Helper.sendMessageWithoutPrefix("§7§m§l----------------------------------");
         Helper.sendMessageWithoutPrefix("                    §b§lMelodySky");
         Helper.sendMessageWithoutPrefix("§b.help >§7 list commands");
         Helper.sendMessageWithoutPrefix("§b.bind >§7 bind a module to a key");
         Helper.sendMessageWithoutPrefix("§b.t >§7 toggle a module on/off");
         Helper.sendMessageWithoutPrefix("§b.friend >§7 friend a player");
         Helper.sendMessageWithoutPrefix("§b.is ITEM_ID >§7 set Custom Item Swapping(ItemSwitcher Custom Mode)");
         Helper.sendMessageWithoutPrefix("§b.irc >§7 Toggle Client Chat Feature.");
         Helper.sendMessageWithoutPrefix("§b.CustomLbinColor <number_value> >§7 Set Lbin Color in Tooltips.");
         Helper.sendMessageWithoutPrefix("§b.sbid >§7 Show Holding Item's Skyblock ID.");
         Helper.sendMessageWithoutPrefix("§7§m§l----------------------------------");
      } else {
         Helper.sendMessage("invalid syntax Valid .help");
      }

      return null;
   }
}
