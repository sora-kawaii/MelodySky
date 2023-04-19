/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.injection.mixins.packets;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Client;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.world.EventPacketSend;
import xyz.Melody.module.modules.QOL.AutoEnchantTable;

@Mixin(value={NetHandlerPlayClient.class})
public class MixinNetHandlerPlayClient {
    @Inject(method="handleSetSlot", at={@At(value="RETURN")})
    public void handleSetSlot(S2FPacketSetSlot packetIn, CallbackInfo ci) {
        AutoEnchantTable aet = (AutoEnchantTable)Client.instance.getModuleManager().getModuleByClass(AutoEnchantTable.class);
        aet.processInventoryContents();
    }

    @Inject(method="addToSendQueue", at={@At(value="HEAD")}, cancellable=true)
    public void sendPacket(Packet<?> packet, CallbackInfo callback) {
        EventPacketSend event = new EventPacketSend(packet);
        EventBus.getInstance().call(event);
        if (event.isCancelled()) {
            callback.cancel();
        }
    }
}

