/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoCloseChest
extends Module {
    public AutoCloseChest() {
        super("AutoCloseChest", new String[]{"acc"}, ModuleType.Dungeons);
        this.setModInfo("Auto Close Secret Chests.");
    }

    @EventHandler
    public void onGuiDraw(EventTick eventTick) {
        GuiScreen guiScreen = this.mc.field_71462_r;
        if (guiScreen instanceof GuiChest && Client.inSkyblock && Client.inDungeons && this.getGuiName(guiScreen).equals("Chest")) {
            this.mc.field_71439_g.func_71053_j();
        }
    }

    public String getGuiName(GuiScreen guiScreen) {
        if (guiScreen instanceof GuiChest) {
            return ((ContainerChest)((GuiChest)guiScreen).field_147002_h).func_85151_d().func_145748_c_().func_150260_c();
        }
        return "";
    }
}

