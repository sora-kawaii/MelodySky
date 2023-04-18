/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.ClientLib;

import net.minecraft.client.Minecraft;
import xyz.Melody.Client;
import xyz.Melody.Utils.PlayerListUtils;

public final class SkyblockArea {
    private Areas currentArea;

    public SkyblockArea() {
        this.currentArea = Areas.NULL;
    }

    public void updateCurrentArea() {
        if (Minecraft.func_71410_x().field_71441_e == null || Minecraft.func_71410_x().field_71439_g == null) {
            return;
        }
        if (!Client.inSkyblock) {
            this.currentArea = Areas.NULL;
            return;
        }
        if (PlayerListUtils.tabContains("Area: Hub")) {
            this.currentArea = Areas.HUB;
            return;
        }
        if (PlayerListUtils.tabContains("Area: Jerry's Workshop")) {
            this.currentArea = Areas.Jerrys_WorkShop;
            return;
        }
        if (PlayerListUtils.tabContains("Area: Private Island")) {
            this.currentArea = Areas.Private_Island;
            return;
        }
        if (PlayerListUtils.tabContains("Area: Spider's Den")) {
            this.currentArea = Areas.Spider_Den;
            return;
        }
        if (PlayerListUtils.tabContains("Area: The Park")) {
            this.currentArea = Areas.The_Park;
            return;
        }
        if (PlayerListUtils.tabContains("Area: The End")) {
            this.currentArea = Areas.The_End;
            return;
        }
        if (PlayerListUtils.tabContains("Area: Crimson Isle")) {
            this.currentArea = Areas.Crimson_Island;
            return;
        }
        if (PlayerListUtils.tabContains("Area: Deep Caverns")) {
            this.currentArea = Areas.Deep_Caverns;
            return;
        }
        if (PlayerListUtils.tabContains("Area: Dwarven Mines")) {
            this.currentArea = Areas.Dwarven_Mines;
            return;
        }
        if (PlayerListUtils.tabContains("Area: Gold Mine")) {
            this.currentArea = Areas.Gold_Mine;
            return;
        }
        if (PlayerListUtils.tabContains("Area: The Farming Islands")) {
            this.currentArea = Areas.The_Farming_Island;
            return;
        }
        if (PlayerListUtils.tabContains("Area: Dungeon Hub")) {
            this.currentArea = Areas.Dungeon_HUB;
            return;
        }
        if (PlayerListUtils.tabContains("Area: Crystal Hollows")) {
            this.currentArea = Areas.Crystal_Hollows;
            return;
        }
        if (Client.inDungeons) {
            this.currentArea = Areas.In_Dungeon;
            return;
        }
        this.currentArea = Areas.NULL;
    }

    public Areas[] getAllAreas() {
        return Areas.values();
    }

    public Areas getCurrentArea() {
        return this.currentArea;
    }

    public void setCurrentArea(Areas areas) {
        this.currentArea = areas;
    }

    public static enum Areas {
        NULL,
        Jerrys_WorkShop,
        Private_Island,
        HUB,
        Spider_Den,
        The_End,
        Dungeon_HUB,
        The_Park,
        Deep_Caverns,
        Dwarven_Mines,
        Gold_Mine,
        The_Farming_Island,
        Crimson_Island,
        Crystal_Hollows,
        In_Dungeon;

    }
}

