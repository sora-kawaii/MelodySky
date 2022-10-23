package xyz.Melody.System.Commands.commands;

import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.modules.others.FetchLBinData;

public class CustomLbinColor extends Command {
   public CustomLbinColor() {
      super("CustomLbinColor", new String[]{"clbc"}, "", "FUCK YOU!");
   }

   public String execute(String[] args) {
      if (args.length >= 1) {
         FetchLBinData.colorPrefix = args[0];
      } else {
         Helper.sendMessageWithoutPrefix("§bCorrect usage:§7 .clbc [ColorPrefix]");
      }

      return null;
   }
}
