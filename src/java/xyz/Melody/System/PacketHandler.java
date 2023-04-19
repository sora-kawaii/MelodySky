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
    private void onTick(EventTick event) {
        block0: {
            int i;
            if (this.packetsWithoutEvent.isEmpty() || (i = 0) >= this.packetsWithoutEvent.size()) break block0;
            Packet<?> pack = this.packetsWithoutEvent.get(i);
            Minecraft.getMinecraft().getNetHandler().getNetworkManager().sendPacket(pack);
            this.packetsWithoutEvent.remove(pack);
        }
    }
}

