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
    public String execute(String[] stringArray) {
        if (stringArray.length >= 1) {
            if (stringArray[0].toLowerCase().contains("reconnect")) {
                Client.instance.irc.disconnect();
                Helper.sendMessage("Reconnecting to IRC Server.");
                Client.instance.irc.start(true);
            } else if (stringArray[0].toLowerCase().contains("disconnect")) {
                Helper.sendMessage("Disconnecting to IRC Server.");
                Client.instance.irc.disconnect();
            } else if (stringArray[0].toLowerCase().contains("connect") && stringArray.length == 2) {
                int n = 11451;
                try {
                    n = Integer.parseInt(stringArray[1]);
                }
                catch (NumberFormatException numberFormatException) {
                    Helper.sendMessage("Port can only be a NON-Negative Integer.");
                    Helper.sendMessage(numberFormatException.getStackTrace());
                    numberFormatException.printStackTrace();
                }
                Client.instance.irc.disconnect();
                Client.instance.irc.connect(n, true);
                Client.instance.ircExeption = false;
            } else if (stringArray[0].toLowerCase().contains("online")) {
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

