/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System.Commands.commands;

import xyz.Melody.Client;
import xyz.Melody.System.Commands.Command;
import xyz.Melody.System.Managers.Client.FileManager;
import xyz.Melody.Utils.Helper;
import xyz.Melody.module.modules.QOL.Swappings.ItemSwitcher;

public final class CustomItemSwitch
extends Command {
    public CustomItemSwitch() {
        super("is", new String[]{"itemswitcher"}, "", "FUCK YOU!");
    }

    @Override
    public String execute(String[] stringArray) {
        if (stringArray.length >= 1) {
            ItemSwitcher itemSwitcher = (ItemSwitcher)Client.instance.getModuleManager().getModuleByClass(ItemSwitcher.class);
            if (itemSwitcher != null) {
                itemSwitcher.setCustomItemID(stringArray[0].toUpperCase());
                Helper.sendMessage("Set Custom ItemSwitcher Item to " + stringArray[0].toUpperCase());
                String string = stringArray[0].toUpperCase();
                FileManager.save("CustomIS.txt", string, false);
                Client.instance.logger.info("[Melody] [Config] CustomItemSwitcher ItemID Saved!");
            } else {
                Helper.sendMessage("Unexpected Error.");
            }
        } else {
            Helper.sendMessageWithoutPrefix("\u00a7bCorrect usage:\u00a77 .is [ItemID]");
        }
        return null;
    }
}

