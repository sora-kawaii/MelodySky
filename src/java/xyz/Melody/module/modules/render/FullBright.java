/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.render;

import java.awt.Color;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class FullBright
extends Module {
    private float old;

    public FullBright() {
        super("FullBright", new String[]{"fbright", "brightness", "bright"}, ModuleType.Render);
        this.setColor(new Color(244, 255, 149).getRGB());
        this.setModInfo("Night Vision.");
    }

    @Override
    public void onEnable() {
        this.old = this.mc.field_71474_y.field_74333_Y;
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        this.mc.field_71474_y.field_74333_Y = 1.5999999E7f;
    }

    @Override
    public void onDisable() {
        this.mc.field_71474_y.field_74333_Y = this.old;
    }
}

