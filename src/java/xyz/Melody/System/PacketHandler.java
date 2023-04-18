/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.System;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;

public final class PacketHandler {
    public ArrayList<Packet<?>> packetsWithoutEvent = new ArrayList();

    public void registerEvent() {
        EventBus.getInstance().register(this);
    }

    public void sendPacketWithoutEvent(Packet<?> packet) {
        this.packetsWithoutEvent.add(packet);
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        block0: {
            int n;
            if (this.packetsWithoutEvent.isEmpty() || (n = 0) >= this.packetsWithoutEvent.size()) break block0;
            Packet<?> packet = this.packetsWithoutEvent.get(n);
            Minecraft.func_71410_x().func_147114_u().func_147298_b().func_179290_a(packet);
            this.packetsWithoutEvent.remove(packet);
        }
    }
}

