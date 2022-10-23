package xyz.Melody.System.Managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.misc.EventChat;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.System.Commands.commands.Bind;
import xyz.Melody.System.Commands.commands.CustomItemSwitch;
import xyz.Melody.System.Commands.commands.CustomLbinColor;
import xyz.Melody.System.Commands.commands.Help;
import xyz.Melody.System.Commands.commands.IRCCommands;
import xyz.Melody.System.Commands.commands.NameCommand;
import xyz.Melody.System.Commands.commands.ShowItemSBID;
import xyz.Melody.System.Commands.commands.Toggle;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.modules.others.ClientCommands;

public class CommandManager implements Manager {
   private List commands;

   public void init() {
      this.commands = new ArrayList();
      this.commands.add(new Command("test", new String[]{"test"}, "", "testing") {
         public String execute(String[] args) {
            Command var3;
            for(Iterator var2 = CommandManager.this.commands.iterator(); var2.hasNext(); var3 = (Command)var2.next()) {
            }

            return null;
         }
      });
      this.commands.add(new NameCommand());
      this.commands.add(new CustomLbinColor());
      this.commands.add(new ShowItemSBID());
      this.commands.add(new IRCCommands());
      this.commands.add(new CustomItemSwitch());
      this.commands.add(new Help());
      this.commands.add(new Toggle());
      this.commands.add(new Bind());
      EventBus.getInstance().register(this);
   }

   public List getCommands() {
      return this.commands;
   }

   public Optional getCommandByName(String name) {
      return this.commands.stream().filter((c2) -> {
         boolean isAlias = false;
         String[] arrstring = c2.getAlias();
         int n = arrstring.length;

         for(int n2 = 0; n2 < n; ++n2) {
            String str = arrstring[n2];
            if (str.equalsIgnoreCase(name)) {
               isAlias = true;
               break;
            }
         }

         return c2.getName().equalsIgnoreCase(name) || isAlias;
      }).findFirst();
   }

   public void add(Command command) {
      this.commands.add(command);
   }

   @EventHandler
   private void onChat(EventChat e) {
      if (Client.clientChat && (e.getMessage().length() <= 1 || !e.getMessage().startsWith(".")) && !e.getMessage().startsWith("/")) {
         String msg = e.getMessage().replace("&", "§");
         Client.instance.irc.sendPrefixMsg(msg, true);
         e.setCancelled(true);
      } else {
         if (Client.instance.getModuleManager().getModuleByClass(ClientCommands.class).isEnabled() && e.getMessage().length() > 1 && e.getMessage().startsWith(".")) {
            e.setCancelled(true);
            String[] args = e.getMessage().trim().substring(1).split(" ");
            Optional possibleCmd = this.getCommandByName(args[0]);
            if (possibleCmd.isPresent()) {
               String result = ((Command)possibleCmd.get()).execute((String[])Arrays.copyOfRange(args, 1, args.length));
               if (result != null && !result.isEmpty()) {
                  Helper.sendMessage(result);
               }
            } else {
               Helper.sendMessage(String.format("Command not found Try '%shelp'", "."));
            }
         }

      }
   }
}
