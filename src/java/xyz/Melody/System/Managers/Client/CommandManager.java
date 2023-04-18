/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Managers.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.misc.EventChat;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.System.Commands.commands.AuthMe;
import xyz.Melody.System.Commands.commands.AutoRubyCMD;
import xyz.Melody.System.Commands.commands.Bind;
import xyz.Melody.System.Commands.commands.CustomItemSwitch;
import xyz.Melody.System.Commands.commands.CustomLbinColor;
import xyz.Melody.System.Commands.commands.GaoNengCommands;
import xyz.Melody.System.Commands.commands.Help;
import xyz.Melody.System.Commands.commands.IRCCommands;
import xyz.Melody.System.Commands.commands.NameCommand;
import xyz.Melody.System.Commands.commands.RankCommand;
import xyz.Melody.System.Commands.commands.ShowItemSBID;
import xyz.Melody.System.Commands.commands.Toggle;
import xyz.Melody.System.Managers.Manager;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.modules.others.ClientCommands;

public final class CommandManager
implements Manager {
    private List<Command> commands;

    @Override
    public void init() {
        this.commands = new ArrayList<Command>();
        this.commands.add(new Command("test", new String[]{"test"}, "", "testing"){

            @Override
            public String execute(String[] stringArray) {
                for (Command command : CommandManager.this.commands) {
                }
                return null;
            }
        });
        this.commands.add(new AutoRubyCMD());
        this.commands.add(new RankCommand());
        this.commands.add(new GaoNengCommands());
        this.commands.add(new AuthMe());
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

    public List<Command> getCommands() {
        return this.commands;
    }

    public Optional<Command> getCommandByName(String string) {
        return this.commands.stream().filter(command -> {
            boolean bl = false;
            for (String string2 : command.getAlias()) {
                if (!string2.equalsIgnoreCase(string)) continue;
                bl = true;
                break;
            }
            return command.getName().equalsIgnoreCase(string) || bl;
        }).findFirst();
    }

    public void add(Command command) {
        this.commands.add(command);
    }

    @EventHandler
    private void onChat(EventChat eventChat) {
        String[] stringArray;
        ClientCommands clientCommands = (ClientCommands)Client.instance.getModuleManager().getModuleByClass(ClientCommands.class);
        String string = ".";
        switch (((Enum)clientCommands.mode.getValue()).toString()) {
            case "dot": {
                string = ".";
                break;
            }
            case "bar": {
                string = "-";
                break;
            }
            case "wavy_line": {
                string = "~";
            }
        }
        if (!(!Client.clientChat || eventChat.getMessage().length() > 1 && eventChat.getMessage().startsWith(string) || eventChat.getMessage().startsWith("/"))) {
            stringArray = eventChat.getMessage().replace("&", "\u00a7");
            Client.instance.irc.sendPrefixMsg((String)stringArray, true);
            eventChat.setCancelled(true);
            return;
        }
        if (clientCommands.isEnabled() && eventChat.getMessage().length() > 1 && eventChat.getMessage().startsWith(string)) {
            eventChat.setCancelled(true);
            stringArray = eventChat.getMessage().trim().substring(1).split(" ");
            Optional<Command> optional = this.getCommandByName(stringArray[0]);
            if (optional.isPresent()) {
                String string2 = optional.get().execute(Arrays.copyOfRange(stringArray, 1, stringArray.length));
                if (string2 != null && !string2.isEmpty()) {
                    Helper.sendMessage(string2);
                }
            } else {
                Helper.sendMessage(String.format("Command not found Try '%shelp'", "."));
            }
        }
    }
}

