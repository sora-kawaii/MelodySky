/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.commands;

import net.minecraft.util.EnumChatFormatting;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.System.Managers.GaoNeng.GaoNengManager;
import xyz.Melody.Utils.Helper;

public final class GaoNengCommands
extends Command {
    public GaoNengCommands() {
        super(".checkgaoneng", new String[]{"cgn", "gn"}, "", "sketit");
    }

    @Override
    public String execute(String[] stringArray) {
        if (stringArray.length >= 1) {
            String string = stringArray[0];
            string = string.replaceAll("-", "");
            Helper.sendMessage("Checking BlackList For UUID " + string);
            GaoNengManager.GaoNeng gaoNeng = GaoNengManager.getIfIsGaoNeng(string);
            if (gaoNeng != null) {
                Helper.sendMessage("Player UUID: " + gaoNeng.getUuid());
                Helper.sendMessage("Rank: " + gaoNeng.getRank() + " [" + gaoNeng.getRank().replaceAll("&", "\u00a7") + (Object)((Object)EnumChatFormatting.GRAY) + "]");
                Helper.sendMessage("Reason: " + gaoNeng.getReason());
                Helper.sendMessage("Real: " + gaoNeng.isRealBlackList());
                Helper.sendMessage("Checker: " + gaoNeng.getChecker());
                Helper.sendMessage("QQ: " + gaoNeng.getQQ());
                Helper.sendMessage("Phone: " + gaoNeng.getPhone());
                Helper.sendMessage("Time: " + gaoNeng.getTime());
            } else {
                Helper.sendMessage("No Info Found.");
            }
        } else {
            String string = this.mc.func_110432_I().func_111286_b().toString();
            GaoNengManager.GaoNeng gaoNeng = GaoNengManager.getIfIsGaoNeng(string = string.replaceAll("-", ""));
            if (gaoNeng != null) {
                Helper.sendMessage("Player UUID: " + gaoNeng.getUuid());
                Helper.sendMessage("Rank: " + gaoNeng.getRank() + " [" + gaoNeng.getRank().replaceAll("&", "\u00a7") + (Object)((Object)EnumChatFormatting.GRAY) + "]");
                Helper.sendMessage("Reason: " + gaoNeng.getReason());
                Helper.sendMessage("Real: " + gaoNeng.isRealBlackList());
                Helper.sendMessage("Checker: " + gaoNeng.getChecker());
                Helper.sendMessage("QQ: " + gaoNeng.getQQ());
                Helper.sendMessage("Phone: " + gaoNeng.getPhone());
                Helper.sendMessage("Time: " + gaoNeng.getTime());
            } else {
                Helper.sendMessage("\ufffd\u3cbb\ufffd\ufffd\ufffd\ufffd");
            }
        }
        return null;
    }
}

