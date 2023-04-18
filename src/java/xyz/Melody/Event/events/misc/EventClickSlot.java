/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.events.misc;

import net.minecraft.entity.player.EntityPlayer;
import xyz.Melody.Event.Event;

public class EventClickSlot
extends Event {
    private int windowID;
    private int slotNumber;
    private int button;
    private int mode;
    private EntityPlayer player;

    public EventClickSlot(int n, int n2, int n3, int n4, EntityPlayer entityPlayer) {
        this.windowID = n;
        this.slotNumber = n2;
        this.button = n3;
        this.mode = n4;
        this.player = entityPlayer;
    }

    public int getWindowID() {
        return this.windowID;
    }

    public int getSlotNumber() {
        return this.slotNumber;
    }

    public int getButton() {
        return this.button;
    }

    public int getMode() {
        return this.mode;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }
}

