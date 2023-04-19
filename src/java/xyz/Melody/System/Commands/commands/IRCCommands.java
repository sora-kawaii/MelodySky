/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.commands;

import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.Utils.Helper;

public final class IRCCommands
extends Command {
    public IRCCommands() {
        super(".irc", new String[]{"irc"}, "", "sketit");
    }

    @Override
    public String execute(String[] args) {
        if (args.length >= 1) {
            if (args[0].toLowerCase().contains("reconnect")) {
                Client.instance.irc.disconnect();
                Helper.sendMessage("Reconnecting to IRC Server.");
                Client.instance.irc.start(true);
            } else if (args[0].toLowerCase().contains("disconnect")) {
                Helper.sendMessage("Disconnecting to IRC Server.");
                Client.instance.irc.disconnect();
            } else if (args[0].toLowerCase().contains("connect") && args.length == 2) {
                int port = 11451;
                try {
                    port = Integer.parseInt(args[1]);
                }
                catch (NumberFormatException e) {
                    Helper.sendMessage("Port can only be a NON-Negative Integer.");
                    Helper.sendMessage(e.getStackTrace());
                    e.printStackTrace();
                }
                Client.instance.irc.disconnect();
                Client.instance.irc.connect(port, true);
                Client.instance.ircExeption = false;
            } else if (args[0].toLowerCase().contains("online")) {
                Client.instance.irc.sendMessage("WHO_ONLINE");
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

