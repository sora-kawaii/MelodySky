/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.others;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AntiAFK
extends Module {
    private Numbers<Double> delay = new Numbers<Double>("Delay", 5000.0, 1000.0, 20000.0, 500.0);
    private Option<Boolean> move = new Option<Boolean>("Move", true);
    private Option<Boolean> rot = new Option<Boolean>("Rotation", true);
    private TimerUtil timer = new TimerUtil();
    private boolean shouldMove = false;
    private boolean shouldRotate = false;
    private boolean rotated = false;
    private TimerUtil rotationTimer = new TimerUtil();
    private TimerUtil moveTimer = new TimerUtil();
    private boolean moveDone = false;
    private boolean moved = false;
    private boolean moveBack = false;

    public AntiAFK() {
        super("AntiAFK", new String[]{"cc", "ccm", "command"}, ModuleType.Others);
        this.addValues(this.delay, this.move, this.rot);
        this.setModInfo("Anti Moving or Rotation AFK.");
    }

    @EventHandler
    private void tickTimer(EventTick eventTick) {
        this.mc.field_71439_g.func_70095_a(true);
        if (this.timer.hasReached((Double)this.delay.getValue())) {
            this.shouldMove = (Boolean)this.move.getValue() != false;
            this.shouldRotate = (Boolean)this.rot.getValue() != false;
            this.timer.reset();
        }
    }

    @EventHandler
    private void tickRotation(EventTick eventTick) {
        if (this.shouldRotate) {
            if (!this.rotated) {
                this.mc.field_71439_g.field_70177_z += 2.0f;
                this.rotated = true;
            }
            if (this.rotated && this.rotationTimer.hasReached(250.0)) {
                this.mc.field_71439_g.field_70177_z -= 2.0f;
                this.rotated = false;
                this.shouldRotate = false;
                this.rotationTimer.reset();
            }
        } else {
            this.rotationTimer.reset();
        }
    }

    @EventHandler
    private void tickMove(EventTick eventTick) {
        if (this.shouldMove) {
            int n = this.mc.field_71474_y.field_74370_x.func_151463_i();
            int n2 = this.mc.field_71474_y.field_74366_z.func_151463_i();
            if (!this.moveDone) {
                if (!this.moved) {
                    this.moveTimer.reset();
                    KeyBinding.func_74510_a((int)n, (boolean)true);
                    this.moved = true;
                }
                if (this.moved && this.moveTimer.hasReached(50.0)) {
                    KeyBinding.func_74510_a((int)n, (boolean)false);
                    if (this.moveTimer.hasReached(100.0)) {
                        KeyBinding.func_74510_a((int)n2, (boolean)true);
                        if (this.moveTimer.hasReached(150.0)) {
                            KeyBinding.func_74510_a((int)n2, (boolean)false);
                            this.moveDone = true;
                        }
                    }
                }
            } else {
                this.moved = false;
                this.moveTimer.reset();
                this.moveDone = false;
                KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74370_x.func_151463_i(), (boolean)false);
                KeyBinding.func_74510_a((int)this.mc.field_71474_y.field_74366_z.func_151463_i(), (boolean)false);
                this.shouldMove = false;
            }
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        Helper.sendMessage("[Ant1AFK] Auto Disabled due to World Change.");
        this.setEnabled(false);
    }
}

