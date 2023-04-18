/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.render;

import java.awt.Color;
import xyz.Melody.GUI.ClickNew.NewClickGui;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class ClickGui
extends Module {
    private NewClickGui clickGui = new NewClickGui();

    public ClickGui() {
        super("ClickGui", new String[]{"click"}, ModuleType.Render);
        this.setColor(new Color(244, 255, 149).getRGB());
    }

    @Override
    public void onEnable() {
        this.mc.func_147108_a(this.clickGui);
        this.setEnabled(false);
    }
}

