/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.client;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.FML.PacketEvent;
import xyz.Melody.Event.events.world.EventPacketRecieve;

@Mixin(value={NetworkManager.class})
public abstract class MixinNetworkManager {
    @Inject(method="channelRead0", at={@At(value="HEAD")}, cancellable=true)
    private void read(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callback) {
        if (MinecraftForge.EVENT_BUS.post(new PacketEvent.ReceiveEvent(packet))) {
            callback.cancel();
        }
        EventPacketRecieve event = new EventPacketRecieve(packet);
        EventBus.getInstance().call(event);
        if (event.isCancelled()) {
            callback.cancel();
        }
    }

    @Inject(method="sendPacket(Lnet/minecraft/network/Packet;)V", at={@At(value="HEAD")}, cancellable=true)
    private void send(Packet<?> packet, CallbackInfo callback) {
        if (MinecraftForge.EVENT_BUS.post(new PacketEvent.SendEvent(packet))) {
            callback.cancel();
        }
    }
}

