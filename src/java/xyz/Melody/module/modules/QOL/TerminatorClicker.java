/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import org.lwjgl.input.Mouse;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.math.MathUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class TerminatorClicker
extends Module {
    private TimerUtil timer = new TimerUtil();
    private Option<Boolean> juju = new Option<Boolean>("Juju", false);
    private Numbers<Double> cps = new Numbers<Double>("CPS", 14.0, 10.0, 20.0, 0.1);

    public TerminatorClicker() {
        super("TerminatorClicker", new String[]{"tc"}, ModuleType.QOL);
        this.addValues(this.juju, this.cps);
        this.setColor(new Color(191, 191, 191).getRGB());
        this.setModInfo("Auto Right Click Terminator.");
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        if (this.mc.thePlayer.getHeldItem() == null) {
            return;
        }
        String string = ItemUtils.getSkyBlockID(this.mc.thePlayer.getHeldItem());
        if (string.equals("TERMINATOR") || ((Boolean)this.juju.getValue()).booleanValue() && string.equals("JUJU_SHORTBOW")) {
            float f = (float)(1000.0 / ((double)((Double)this.cps.getValue()).floatValue() + MathUtil.randomDouble(-2.0, 2.0)));
            if (Mouse.isButtonDown(1) && this.timer.hasReached(f) && this.mc.currentScreen == null) {
                Client.rightClick();
                this.timer.reset();
            }
        }
    }
}

