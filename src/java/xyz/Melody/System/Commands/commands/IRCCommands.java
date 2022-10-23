package xyz.Melody.System.Commands.commands;

import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;

public class IRCCommands extends Command {
   public IRCCommands() {
      super(".irc", new String[]{"irc"}, "", "sketit");
   }

   public String execute(String[] args) {
      if (args.length >= 1) {
         if (args[0].toLowerCase().contains("reconnect")) {
            if (Client.instance.ircExeption) {
               Helper.sendMessage("Trying to Reconnect to IRC Server.");
               Client.instance.irc.start();
               Client.instance.ircExeption = false;
            } else {
               Helper.sendMessage("Disconnecting to IRC Server.");
               Client.instance.irc.disconnect();
               Helper.sendMessage("Reconnecting to IRC Server.");
               Client.instance.irc.start();
            }
         } else if (args[0].toLowerCase().contains("disconnect")) {
            Helper.sendMessage("Disconnecting to IRC Server.");
            Client.instance.irc.disconnect();
         } else {
            Client.clientChat = !Client.clientChat;
            Helper.sendMessage("Client Chat Enabled: " + Client.clientChat);
         }
      } else {
         Client.clientChat = !Client.clientChat;
         Helper.sendMessage("Client Chat Enabled: " + Client.clientChat);
      }

      return null;
   }
}
