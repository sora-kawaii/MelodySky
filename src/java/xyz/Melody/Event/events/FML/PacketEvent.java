/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Event.events.FML;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PacketEvent
extends Event {
    public Direction direction;
    public Packet<?> packet;

    public PacketEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    static enum Direction {
        INBOUND,
        OUTBOUND;

    }

    public static class SendEvent
    extends PacketEvent {
        public SendEvent(Packet<?> packet) {
            super(packet);
            this.direction = Direction.OUTBOUND;
        }
    }

    public static class ReceiveEvent
    extends PacketEvent {
        public ReceiveEvent(Packet<?> packet) {
            super(packet);
            this.direction = Direction.INBOUND;
        }
    }
}

