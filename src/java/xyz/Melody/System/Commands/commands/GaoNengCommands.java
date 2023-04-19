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
    public String execute(String[] args) {
        if (args.length >= 1) {
            String az = args[0];
            az = az.replaceAll("-", "");
            Helper.sendMessage("Checking BlackList For UUID " + az);
            GaoNengManager.GaoNeng gao = GaoNengManager.getIfIsGaoNeng(az);
            if (gao != null) {
                Helper.sendMessage("Player UUID: " + gao.getUuid());
                Helper.sendMessage("Rank: " + gao.getRank() + " [" + gao.getRank().replaceAll("&", "\u00a7") + (Object)((Object)EnumChatFormatting.GRAY) + "]");
                Helper.sendMessage("Reason: " + gao.getReason());
                Helper.sendMessage("Real: " + gao.isRealBlackList());
                Helper.sendMessage("Checker: " + gao.getChecker());
                Helper.sendMessage("QQ: " + gao.getQQ());
                Helper.sendMessage("Phone: " + gao.getPhone());
                Helper.sendMessage("Time: " + gao.getTime());
            } else {
                Helper.sendMessage("No Info Found.");
            }
        } else {
            String az = this.mc.getSession().getSessionID().toString();
            GaoNengManager.GaoNeng gao = GaoNengManager.getIfIsGaoNeng(az = az.replaceAll("-", ""));
            if (gao != null) {
                Helper.sendMessage("Player UUID: " + gao.getUuid());
                Helper.sendMessage("Rank: " + gao.getRank() + " [" + gao.getRank().replaceAll("&", "\u00a7") + (Object)((Object)EnumChatFormatting.GRAY) + "]");
                Helper.sendMessage("Reason: " + gao.getReason());
                Helper.sendMessage("Real: " + gao.isRealBlackList());
                Helper.sendMessage("Checker: " + gao.getChecker());
                Helper.sendMessage("QQ: " + gao.getQQ());
                Helper.sendMessage("Phone: " + gao.getPhone());
                Helper.sendMessage("Time: " + gao.getTime());
            } else {
                Helper.sendMessage("\ufffd\u3cbb\ufffd\ufffd\ufffd\ufffd");
            }
        }
        return null;
    }
}

