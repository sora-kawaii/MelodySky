/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Managers.Client;

import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.System.Managers.Client.FileManager;
import xyz.Melody.System.Managers.Client.FriendManager;
import xyz.Melody.Utils.Helper;

class FriendManager$I
extends Command {
    private final FriendManager fm;
    final FriendManager this$0;

    FriendManager$I(FriendManager var1, String $anonymous0, String[] $anonymous1, String $anonymous2, String $anonymous3) {
        super($anonymous0, $anonymous1, $anonymous2, $anonymous3);
        this.this$0 = var1;
        this.fm = var1;
    }

    @Override
    public String execute(String[] args) {
        if (args.length >= 3) {
            if (args[0].equalsIgnoreCase("add")) {
                String friends = "";
                friends = friends + String.format("%s:%s%s", args[1], args[2], System.lineSeparator());
                FriendManager.access$0().put(args[1], args[2]);
                Helper.sendMessage("> " + String.format("%s has been added as %s", args[1], args[2]));
                FileManager.save("Friends.txt", friends, true);
            } else if (args[0].equalsIgnoreCase("del")) {
                FriendManager.access$0().remove(args[1]);
                Helper.sendMessage("> " + String.format("%s has been removed from your friends list", args[1]));
            } else if (args[0].equalsIgnoreCase("list")) {
                if (FriendManager.access$0().size() > 0) {
                    int var5 = 1;
                    for (String fr : FriendManager.access$0().values()) {
                        Helper.sendMessage("> " + String.format("%s. %s", var5, fr));
                        ++var5;
                    }
                } else {
                    Helper.sendMessage("> get some friends fag lmao");
                }
            }
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                String friends = "";
                friends = friends + String.format("%s%s", args[1], System.lineSeparator());
                FriendManager.access$0().put(args[1], args[1]);
                Helper.sendMessage("> " + String.format("%s has been added as %s", args[1], args[1]));
                FileManager.save("Friends.txt", friends, true);
            } else if (args[0].equalsIgnoreCase("del")) {
                FriendManager.access$0().remove(args[1]);
                Helper.sendMessage("> " + String.format("%s has been removed from your friends list", args[1]));
            } else if (args[0].equalsIgnoreCase("list")) {
                if (FriendManager.access$0().size() > 0) {
                    int var5 = 1;
                    for (String fr : FriendManager.access$0().values()) {
                        Helper.sendMessage("> " + String.format("%s. %s", var5, fr));
                        ++var5;
                    }
                } else {
                    Helper.sendMessage("> you dont have any you lonely fuck");
                }
            }
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                if (FriendManager.access$0().size() > 0) {
                    int var5 = 1;
                    for (String fr : FriendManager.access$0().values()) {
                        Helper.sendMessage(String.format("%s. %s", var5, fr));
                        ++var5;
                    }
                } else {
                    Helper.sendMessage("you dont have any you lonely fuck");
                }
            } else if (!args[0].equalsIgnoreCase("add") && !args[0].equalsIgnoreCase("del")) {
                Helper.sendMessage("> Correct usage: " + (Object)((Object)EnumChatFormatting.GRAY) + "Valid .f add/del <player>");
            } else {
                Helper.sendMessage("> " + (Object)((Object)EnumChatFormatting.GRAY) + "Please enter a players name");
            }
        } else if (args.length == 0) {
            Helper.sendMessage("> Correct usage: " + (Object)((Object)EnumChatFormatting.GRAY) + "Valid .f add/del <player>");
        }
        return null;
    }
}

