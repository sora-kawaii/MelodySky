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

    FriendManager$I(FriendManager friendManager, String string, String[] stringArray, String string2, String string3) {
        super(string, stringArray, string2, string3);
        this.this$0 = friendManager;
        this.fm = friendManager;
    }

    @Override
    public String execute(String[] stringArray) {
        if (stringArray.length >= 3) {
            if (stringArray[0].equalsIgnoreCase("add")) {
                String string = "";
                string = string + String.format("%s:%s%s", stringArray[1], stringArray[2], System.lineSeparator());
                FriendManager.access$0().put(stringArray[1], stringArray[2]);
                Helper.sendMessage("> " + String.format("%s has been added as %s", stringArray[1], stringArray[2]));
                FileManager.save("Friends.txt", string, true);
            } else if (stringArray[0].equalsIgnoreCase("del")) {
                FriendManager.access$0().remove(stringArray[1]);
                Helper.sendMessage("> " + String.format("%s has been removed from your friends list", stringArray[1]));
            } else if (stringArray[0].equalsIgnoreCase("list")) {
                if (FriendManager.access$0().size() > 0) {
                    int n = 1;
                    for (String string : FriendManager.access$0().values()) {
                        Helper.sendMessage("> " + String.format("%s. %s", n, string));
                        ++n;
                    }
                } else {
                    Helper.sendMessage("> get some friends fag lmao");
                }
            }
        } else if (stringArray.length == 2) {
            if (stringArray[0].equalsIgnoreCase("add")) {
                String string = "";
                string = string + String.format("%s%s", stringArray[1], System.lineSeparator());
                FriendManager.access$0().put(stringArray[1], stringArray[1]);
                Helper.sendMessage("> " + String.format("%s has been added as %s", stringArray[1], stringArray[1]));
                FileManager.save("Friends.txt", string, true);
            } else if (stringArray[0].equalsIgnoreCase("del")) {
                FriendManager.access$0().remove(stringArray[1]);
                Helper.sendMessage("> " + String.format("%s has been removed from your friends list", stringArray[1]));
            } else if (stringArray[0].equalsIgnoreCase("list")) {
                if (FriendManager.access$0().size() > 0) {
                    int n = 1;
                    for (String string : FriendManager.access$0().values()) {
                        Helper.sendMessage("> " + String.format("%s. %s", n, string));
                        ++n;
                    }
                } else {
                    Helper.sendMessage("> you dont have any you lonely fuck");
                }
            }
        } else if (stringArray.length == 1) {
            if (stringArray[0].equalsIgnoreCase("list")) {
                if (FriendManager.access$0().size() > 0) {
                    int n = 1;
                    for (String string : FriendManager.access$0().values()) {
                        Helper.sendMessage(String.format("%s. %s", n, string));
                        ++n;
                    }
                } else {
                    Helper.sendMessage("you dont have any you lonely fuck");
                }
            } else if (!stringArray[0].equalsIgnoreCase("add") && !stringArray[0].equalsIgnoreCase("del")) {
                Helper.sendMessage("> Correct usage: " + (Object)((Object)EnumChatFormatting.GRAY) + "Valid .f add/del <player>");
            } else {
                Helper.sendMessage("> " + (Object)((Object)EnumChatFormatting.GRAY) + "Please enter a players name");
            }
        } else if (stringArray.length == 0) {
            Helper.sendMessage("> Correct usage: " + (Object)((Object)EnumChatFormatting.GRAY) + "Valid .f add/del <player>");
        }
        return null;
    }
}

