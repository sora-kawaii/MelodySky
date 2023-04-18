/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import net.minecraft.util.BlockPos;
import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.System.Managers.Client.FileManager;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.modules.macros.AutoRuby;

public final class AutoRubyCMD
extends Command {
    public AutoRubyCMD() {
        super(".autoruby", new String[]{"ar"}, "", "sketit");
    }

    @Override
    public String execute(String[] stringArray) {
        if (stringArray.length >= 1) {
            AutoRuby autoRuby = (AutoRuby)Client.instance.getModuleManager().getModuleByClass(AutoRuby.class);
            if (stringArray[0].toLowerCase().contains("start")) {
                autoRuby.started = true;
                Helper.sendMessage("AutoRuby: Started.");
            } else if (stringArray[0].toLowerCase().contains("stop")) {
                autoRuby.started = false;
                Helper.sendMessage("AutoRuby: Stopped.");
            } else if (stringArray[0].toLowerCase().contains("remove")) {
                try {
                    autoRuby.wps.remove(Integer.parseInt(stringArray[1]) - 1);
                }
                catch (Exception exception) {
                    Helper.sendMessage(exception.getMessage());
                }
            } else if (stringArray[0].toLowerCase().contains("add")) {
                try {
                    int n = Integer.parseInt(stringArray[2]);
                    int n2 = Integer.parseInt(stringArray[3]);
                    int n3 = Integer.parseInt(stringArray[4]);
                    BlockPos blockPos = new BlockPos(n, n2, n3);
                    autoRuby.wps.add(Integer.parseInt(stringArray[1]), blockPos);
                }
                catch (Exception exception) {
                    Helper.sendMessage(exception.getMessage());
                }
            } else if (stringArray[0].toLowerCase().contains("clear")) {
                autoRuby.wps.clear();
                Helper.sendMessage("AutoRuby: Waypoints Cleared.");
            } else if (stringArray[0].toLowerCase().contains("save")) {
                if (stringArray[1] == null) {
                    Helper.sendMessage("Correct Useage: .ar save [configName]");
                    return null;
                }
                if (stringArray[1].contains("/") || stringArray[1].contains(":") || stringArray[1].contains("*") || stringArray[1].contains("?") || stringArray[1].contains(String.valueOf('\"')) || stringArray[1].contains("<") || stringArray[1].contains(">") || stringArray[1].contains("|")) {
                    Helper.sendMessage("Config Name can not be '/', ':', '*', '?', " + String.valueOf('\"') + ", '<', '>', '|'.");
                    return null;
                }
                String string = "";
                int n = 0;
                for (BlockPos blockPos : autoRuby.wps) {
                    String arrayList = blockPos.getX() + ":" + blockPos.getY() + ":" + blockPos.getZ() + "%";
                    string = string + arrayList;
                    ++n;
                }
                FileManager.save(stringArray[1] + ".txt", string, false);
                Helper.sendMessage("[AutoRuby] Saved " + n + " Waypoints.");
            } else if (stringArray[0].toLowerCase().contains("load")) {
                if (stringArray[1] == null) {
                    Helper.sendMessage("Correct Useage: .ar load [configName]");
                    return null;
                }
                List<String> list = FileManager.read(stringArray[1] + ".txt");
                String string = "";
                for (String stringArray22 : list) {
                    string = string + stringArray22;
                }
                int n = 0;
                String[] stringArray2 = string.split("%");
                ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
                for (String string2 : stringArray2) {
                    StringTokenizer stringTokenizer = new StringTokenizer(string2, ":");
                    int n2 = Integer.parseInt(stringTokenizer.nextToken());
                    int n3 = Integer.parseInt(stringTokenizer.nextToken());
                    int n4 = Integer.parseInt(stringTokenizer.nextToken());
                    arrayList.add(new BlockPos(n2, n3, n4));
                    ++n;
                }
                autoRuby.wps.clear();
                autoRuby.wps.addAll(arrayList);
                Helper.sendMessage("[AutoRuby] Loaded " + n + " Waypoints.");
            } else if (stringArray[0].toLowerCase().contains("help")) {
                Helper.sendMessageWithoutPrefix("====================== AutoRuby ======================");
                Helper.sendMessageWithoutPrefix("AutoGemstone:> .ar start - Start Mining.");
                Helper.sendMessageWithoutPrefix("AutoGemstone:> .ar stop - Stop Mining.");
                Helper.sendMessageWithoutPrefix("AutoGemstone:> .ar add [index] [x] [y] [z] - Add a Waypoint After 'index'.");
                Helper.sendMessageWithoutPrefix("AutoGemstone:> .ar remove [index] - Remove a waypoint.");
                Helper.sendMessageWithoutPrefix("AutoGemstone:> .ar save [name] - Save Current Waypoints as 'name'.");
                Helper.sendMessageWithoutPrefix("AutoGemstone:> .ar load [name] - Load saved Waypoints as 'name'.");
            }
        } else {
            Helper.sendMessage("useage: .AutoRuby start/stop");
        }
        return null;
    }
}

