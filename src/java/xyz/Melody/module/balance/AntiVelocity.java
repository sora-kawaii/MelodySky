/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.balance;

import java.awt.Color;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Mouse;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.injection.mixins.packets.S12Accessor;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AntiVelocity
extends Module {
    private TimerUtil timer = new TimerUtil();
    private Mode<Enum> mode = new Mode("Mode", (Enum[])idk.values(), (Enum)idk.Turtle_Shelmet);
    private Option<Boolean> autoMode = new Option<Boolean>("Auto Switch Mode", true);
    private Numbers<Double> explosionTime = new Numbers<Double>("WaitingTime", 500.0, 0.0, 1000.0, 10.0);
    private Option<Boolean> sbonly = new Option<Boolean>("Skyblock Only", true);
    private Option<Boolean> dungeonLava = new Option<Boolean>("Dungeon Lava Check", true);
    private Option<Boolean> jerryChineVC = new Option<Boolean>("JerryChine Vertical", true);

    public AntiVelocity() {
        super("AntiKB", new String[]{"antivelocity", "antiknockback", "antikb"}, ModuleType.Balance);
        this.addValues(this.mode, this.autoMode, this.sbonly, this.dungeonLava, this.jerryChineVC, this.explosionTime);
        this.setColor(new Color(191, 191, 191).getRGB());
    }

    @EventHandler
    private void onPacket(EventPacketRecieve e) {
        S12PacketEntityVelocity vp;
        boolean ignExplosion;
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            return;
        }
        if (((Boolean)this.sbonly.getValue()).booleanValue() && !Client.inSkyblock) {
            return;
        }
        boolean bl = ignExplosion = !this.timer.hasReached((Double)this.explosionTime.getValue()) || this.mc.thePlayer.isInLava() && (Client.inDungeons || (Boolean)this.dungeonLava.getValue() == false);
        if (ignExplosion) {
            S12PacketEntityVelocity velocity;
            if (e.getPacket() instanceof S12PacketEntityVelocity && (velocity = (S12PacketEntityVelocity)e.getPacket()).getEntityID() == this.mc.thePlayer.getEntityId() && ((Boolean)this.jerryChineVC.getValue()).booleanValue() && this.holdingJC()) {
                S12PacketEntityVelocity vp2 = (S12PacketEntityVelocity)e.getPacket();
                ((S12Accessor)((Object)vp2)).setMotionX(0);
                ((S12Accessor)((Object)vp2)).setMotionZ(0);
            }
            return;
        }
        if (e.getPacket() instanceof S27PacketExplosion) {
            e.setCancelled(true);
        }
        if (e.getPacket() instanceof S12PacketEntityVelocity && (vp = (S12PacketEntityVelocity)e.getPacket()).getEntityID() == this.mc.thePlayer.getEntityId()) {
            if (this.mode.getValue() == idk.Reverse) {
                ((S12Accessor)((Object)vp)).setMotionX(-vp.getMotionX());
                ((S12Accessor)((Object)vp)).setMotionZ(-vp.getMotionZ());
                return;
            }
            if (((Boolean)this.autoMode.getValue()).booleanValue()) {
                if (Client.inDungeons) {
                    ((S12Accessor)((Object)vp)).setMotionX(1);
                    ((S12Accessor)((Object)vp)).setMotionZ(1);
                } else {
                    e.setCancelled(true);
                }
            } else {
                if (this.mode.getValue() == idk.Cancel) {
                    e.setCancelled(true);
                }
                if (this.mode.getValue() == idk.Turtle_Shelmet) {
                    ((S12Accessor)((Object)vp)).setMotionX(1);
                    ((S12Accessor)((Object)vp)).setMotionZ(1);
                }
            }
        }
    }

    @EventHandler
    private void onRC(EventTick event) {
        boolean holdingStaff;
        String heldItemID = this.mc.thePlayer.getHeldItem() != null ? ItemUtils.getSkyBlockID(this.mc.thePlayer.getHeldItem()) : "notHoldingItem";
        boolean bl = holdingStaff = heldItemID.contains("BONZO_STAFF") || heldItemID.equals("JERRY_STAFF");
        if ((Double)this.explosionTime.getValue() == 0.0) {
            this.timer.reset();
            return;
        }
        if (holdingStaff && Mouse.isButtonDown(1)) {
            this.timer.reset();
        }
    }

    private boolean holdingJC() {
        String heldItemID;
        String string = heldItemID = this.mc.thePlayer.getHeldItem() != null ? ItemUtils.getSkyBlockID(this.mc.thePlayer.getHeldItem()) : "notHoldingItem";
        return heldItemID.equals("JERRY_STAFF");
    }

    static enum idk {
        Cancel,
        Turtle_Shelmet,
        Reverse;

    }
}

