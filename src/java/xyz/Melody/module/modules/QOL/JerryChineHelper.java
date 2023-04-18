/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.injection.mixins.packets.S12Accessor;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class JerryChineHelper
extends Module {
    private TimerUtil timer = new TimerUtil();

    public JerryChineHelper() {
        super("JerryChineHelper", new String[]{"jch"}, ModuleType.QOL);
        this.setColor(new Color(158, 205, 125).getRGB());
        this.setModInfo("Help You Using JyrreChineGun.");
    }

    @EventHandler
    private void tick(EventTick eventTick) {
        if (this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            this.timer.reset();
        }
    }

    @EventHandler
    private void OP(EventPacketRecieve eventPacketRecieve) {
        S12PacketEntityVelocity s12PacketEntityVelocity;
        if (this.timer.hasReached(450.0)) {
            return;
        }
        if (eventPacketRecieve.getPacket() instanceof S12PacketEntityVelocity && (s12PacketEntityVelocity = (S12PacketEntityVelocity)eventPacketRecieve.getPacket()).getEntityID() == this.mc.thePlayer.getEntityId() && this.holdingJC()) {
            S12PacketEntityVelocity s12PacketEntityVelocity2 = (S12PacketEntityVelocity)eventPacketRecieve.getPacket();
            ((S12Accessor)((Object)s12PacketEntityVelocity2)).setMotionX(0);
            ((S12Accessor)((Object)s12PacketEntityVelocity2)).setMotionZ(0);
        }
    }

    private boolean holdingJC() {
        String string;
        String string2 = string = this.mc.thePlayer.getHeldItem() != null ? ItemUtils.getSkyBlockID(this.mc.thePlayer.getHeldItem()) : "notHoldingItem";
        return string.equals("JERRY_STAFF");
    }
}

