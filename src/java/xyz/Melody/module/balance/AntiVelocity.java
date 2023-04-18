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
    private void onPacket(EventPacketRecieve eventPacketRecieve) {
        S12PacketEntityVelocity s12PacketEntityVelocity;
        boolean bl;
        if (this.mc.field_71441_e == null || this.mc.field_71439_g == null) {
            return;
        }
        if (((Boolean)this.sbonly.getValue()).booleanValue() && !Client.inSkyblock) {
            return;
        }
        boolean bl2 = bl = !this.timer.hasReached((Double)this.explosionTime.getValue()) || this.mc.field_71439_g.func_180799_ab() && (Client.inDungeons || (Boolean)this.dungeonLava.getValue() == false);
        if (bl) {
            S12PacketEntityVelocity s12PacketEntityVelocity2;
            if (eventPacketRecieve.getPacket() instanceof S12PacketEntityVelocity && (s12PacketEntityVelocity2 = (S12PacketEntityVelocity)eventPacketRecieve.getPacket()).func_149412_c() == this.mc.field_71439_g.func_145782_y() && ((Boolean)this.jerryChineVC.getValue()).booleanValue() && this.holdingJC()) {
                S12PacketEntityVelocity s12PacketEntityVelocity3 = (S12PacketEntityVelocity)eventPacketRecieve.getPacket();
                ((S12Accessor)((Object)s12PacketEntityVelocity3)).setMotionX(0);
                ((S12Accessor)((Object)s12PacketEntityVelocity3)).setMotionZ(0);
            }
            return;
        }
        if (eventPacketRecieve.getPacket() instanceof S27PacketExplosion) {
            eventPacketRecieve.setCancelled(true);
        }
        if (eventPacketRecieve.getPacket() instanceof S12PacketEntityVelocity && (s12PacketEntityVelocity = (S12PacketEntityVelocity)eventPacketRecieve.getPacket()).func_149412_c() == this.mc.field_71439_g.func_145782_y()) {
            if (this.mode.getValue() == idk.Reverse) {
                ((S12Accessor)((Object)s12PacketEntityVelocity)).setMotionX(-s12PacketEntityVelocity.func_149411_d());
                ((S12Accessor)((Object)s12PacketEntityVelocity)).setMotionZ(-s12PacketEntityVelocity.func_149409_f());
                return;
            }
            if (((Boolean)this.autoMode.getValue()).booleanValue()) {
                if (Client.inDungeons) {
                    ((S12Accessor)((Object)s12PacketEntityVelocity)).setMotionX(1);
                    ((S12Accessor)((Object)s12PacketEntityVelocity)).setMotionZ(1);
                } else {
                    eventPacketRecieve.setCancelled(true);
                }
            } else {
                if (this.mode.getValue() == idk.Cancel) {
                    eventPacketRecieve.setCancelled(true);
                }
                if (this.mode.getValue() == idk.Turtle_Shelmet) {
                    ((S12Accessor)((Object)s12PacketEntityVelocity)).setMotionX(1);
                    ((S12Accessor)((Object)s12PacketEntityVelocity)).setMotionZ(1);
                }
            }
        }
    }

    @EventHandler
    private void onRC(EventTick eventTick) {
        boolean bl;
        String string = this.mc.field_71439_g.func_70694_bm() != null ? ItemUtils.getSkyBlockID(this.mc.field_71439_g.func_70694_bm()) : "notHoldingItem";
        boolean bl2 = bl = string.contains("BONZO_STAFF") || string.equals("JERRY_STAFF");
        if ((Double)this.explosionTime.getValue() == 0.0) {
            this.timer.reset();
            return;
        }
        if (bl && Mouse.isButtonDown(1)) {
            this.timer.reset();
        }
    }

    private boolean holdingJC() {
        String string;
        String string2 = string = this.mc.field_71439_g.func_70694_bm() != null ? ItemUtils.getSkyBlockID(this.mc.field_71439_g.func_70694_bm()) : "notHoldingItem";
        return string.equals("JERRY_STAFF");
    }

    static enum idk {
        Cancel,
        Turtle_Shelmet,
        Reverse;

    }
}

