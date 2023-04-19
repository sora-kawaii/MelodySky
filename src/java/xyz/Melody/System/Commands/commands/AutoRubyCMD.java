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
    public String execute(String[] args) {
        if (args.length >= 1) {
            AutoRuby ar = (AutoRuby)Client.instance.getModuleManager().getModuleByClass(AutoRuby.class);
            if (args[0].toLowerCase().contains("start")) {
                ar.started = true;
                Helper.sendMessage("AutoRuby: Started.");
            } else if (args[0].toLowerCase().contains("stop")) {
                ar.started = false;
                Helper.sendMessage("AutoRuby: Stopped.");
            } else if (args[0].toLowerCase().contains("remove")) {
                try {
                    ar.wps.remove(Integer.parseInt(args[1]) - 1);
                }
                catch (Exception e) {
                    Helper.sendMessage(e.getMessage());
                }
            } else if (args[0].toLowerCase().contains("add")) {
                try {
                    int x = Integer.parseInt(args[2]);
                    int y = Integer.parseInt(args[3]);
                    int z = Integer.parseInt(args[4]);
                    BlockPos append = new BlockPos(x, y, z);
                    ar.wps.add(Integer.parseInt(args[1]), append);
                }
                catch (Exception e) {
                    Helper.sendMessage(e.getMessage());
                }
            } else if (args[0].toLowerCase().contains("clear")) {
                ar.wps.clear();
                Helper.sendMessage("AutoRuby: Waypoints Cleared.");
            } else if (args[0].toLowerCase().contains("save")) {
                if (args[1] == null) {
                    Helper.sendMessage("Correct Useage: .ar save [configName]");
                    return null;
                }
                if (args[1].contains("/") || args[1].contains(":") || args[1].contains("*") || args[1].contains("?") || args[1].contains(String.valueOf('\"')) || args[1].contains("<") || args[1].contains(">") || args[1].contains("|")) {
                    Helper.sendMessage("Config Name can not be '/', ':', '*', '?', " + String.valueOf('\"') + ", '<', '>', '|'.");
                    return null;
                }
                String all = "";
                int count = 0;
                for (BlockPos pos : ar.wps) {
                    String sus = pos.getX() + ":" + pos.getY() + ":" + pos.getZ() + "%";
                    all = all + sus;
                    ++count;
                }
                FileManager.save(args[1] + ".txt", all, false);
                Helper.sendMessage("[AutoRuby] Saved " + count + " Waypoints.");
            } else if (args[0].toLowerCase().contains("load")) {
                if (args[1] == null) {
                    Helper.sendMessage("Correct Useage: .ar load [configName]");
                    return null;
                }
                List<String> poses = FileManager.read(args[1] + ".txt");
                String all = "";
                for (String str : poses) {
                    all = all + str;
                }
                int count = 0;
                String[] ps = all.split("%");
                ArrayList<BlockPos> pornhub = new ArrayList<BlockPos>();
                for (String s : ps) {
                    StringTokenizer st = new StringTokenizer(s, ":");
                    int x = Integer.parseInt(st.nextToken());
                    int y = Integer.parseInt(st.nextToken());
                    int z = Integer.parseInt(st.nextToken());
                    pornhub.add(new BlockPos(x, y, z));
                    ++count;
                }
                ar.wps.clear();
                ar.wps.addAll(pornhub);
                Helper.sendMessage("[AutoRuby] Loaded " + count + " Waypoints.");
            } else if (args[0].toLowerCase().contains("help")) {
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

