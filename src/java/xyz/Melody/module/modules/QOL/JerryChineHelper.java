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
    private void tick(EventTick event) {
        if (this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            this.timer.reset();
        }
    }

    @EventHandler
    private void OP(EventPacketRecieve e) {
        S12PacketEntityVelocity velocity;
        if (this.timer.hasReached(450.0)) {
            return;
        }
        if (e.getPacket() instanceof S12PacketEntityVelocity && (velocity = (S12PacketEntityVelocity)e.getPacket()).getEntityID() == this.mc.thePlayer.getEntityId() && this.holdingJC()) {
            S12PacketEntityVelocity vp = (S12PacketEntityVelocity)e.getPacket();
            ((S12Accessor)((Object)vp)).setMotionX(0);
            ((S12Accessor)((Object)vp)).setMotionZ(0);
        }
    }

    private boolean holdingJC() {
        String heldItemID;
        String string = heldItemID = this.mc.thePlayer.getHeldItem() != null ? ItemUtils.getSkyBlockID(this.mc.thePlayer.getHeldItem()) : "notHoldingItem";
        return heldItemID.equals("JERRY_STAFF");
    }
}

