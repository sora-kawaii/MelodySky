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
import xyz.Melody.System.Commands.commands.CustomNukerCMD;
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
            public String execute(String[] args) {
                for (Command command : CommandManager.this.commands) {
                }
                return null;
            }
        });
        this.commands.add(new CustomNukerCMD());
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

    public Optional<Command> getCommandByName(String name) {
        return this.commands.stream().filter(c2 -> {
            boolean isAlias = false;
            for (String str : c2.getAlias()) {
                if (!str.equalsIgnoreCase(name)) continue;
                isAlias = true;
                break;
            }
            return c2.getName().equalsIgnoreCase(name) || isAlias;
        }).findFirst();
    }

    public void add(Command command) {
        this.commands.add(command);
    }

    @EventHandler
    private void onChat(EventChat e) {
        ClientCommands cmd = (ClientCommands)Client.instance.getModuleManager().getModuleByClass(ClientCommands.class);
        String prefix = ".";
        switch (((Enum)cmd.mode.getValue()).toString()) {
            case "dot": {
                prefix = ".";
                break;
            }
            case "bar": {
                prefix = "-";
                break;
            }
            case "wavy_line": {
                prefix = "~";
            }
        }
        if (!(!Client.clientChat || e.getMessage().length() > 1 && e.getMessage().startsWith(prefix) || e.getMessage().startsWith("/"))) {
            String msg = e.getMessage().replace("&", "\u00a7");
            Client.instance.irc.sendPrefixMsg(msg, true);
            e.setCancelled(true);
            return;
        }
        if (cmd.isEnabled() && e.getMessage().length() > 1 && e.getMessage().startsWith(prefix)) {
            e.setCancelled(true);
            String[] args = e.getMessage().trim().substring(1).split(" ");
            Optional<Command> possibleCmd = this.getCommandByName(args[0]);
            if (possibleCmd.isPresent()) {
                String result = possibleCmd.get().execute(Arrays.copyOfRange(args, 1, args.length));
                if (result != null && !result.isEmpty()) {
                    Helper.sendMessage(result);
                }
            } else {
                Helper.sendMessage(String.format("Command not found Try '%shelp'", "."));
            }
        }
    }
}

