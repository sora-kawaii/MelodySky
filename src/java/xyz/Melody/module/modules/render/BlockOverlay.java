/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.render;

import java.awt.Color;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.render.ColorUtils;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class BlockOverlay
extends Module {
    private Numbers<Double> r = new Numbers<Double>("Red", 255.0, 0.0, 255.0, 1.0);
    private Numbers<Double> g = new Numbers<Double>("Green", 255.0, 0.0, 255.0, 1.0);
    private Numbers<Double> b = new Numbers<Double>("Blue", 255.0, 0.0, 255.0, 1.0);
    private Numbers<Double> a = new Numbers<Double>("Alpha", 255.0, 0.0, 255.0, 1.0);
    private Option<Boolean> rb = new Option<Boolean>("Rainbow", false);
    private Numbers<Double> width = new Numbers<Double>("Width", 3.0, 1.0, 8.0, 0.5);

    public BlockOverlay() {
        super("BlockOverlay", new String[]{"boverlay", "overlay"}, ModuleType.Render);
        this.setModInfo("Aimming Block Overlay.");
        this.addValues(this.r, this.g, this.b, this.a, this.rb, this.width);
    }

    @EventHandler
    private void on3D(EventRender3D eventRender3D) {
        if (this.mc.field_71476_x == null || this.mc.field_71476_x.func_178782_a() == null || this.mc.field_71476_x.field_72308_g != null) {
            return;
        }
        BlockPos blockPos = this.mc.field_71476_x.func_178782_a();
        if (this.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150350_a) {
            return;
        }
        Color color = (Boolean)this.rb.getValue() != false ? this.addAlpha(ColorUtils.rainbow(0L, 1.0f), ((Double)this.a.getValue()).intValue()) : new Color(((Double)this.r.getValue()).intValue(), ((Double)this.g.getValue()).intValue(), ((Double)this.b.getValue()).intValue(), ((Double)this.a.getValue()).intValue());
        RenderUtil.drawSolidBlockESP(blockPos, color.getRGB(), ((Double)this.width.getValue()).floatValue(), eventRender3D.getPartialTicks());
    }

    private Color addAlpha(Color color, int n) {
        int n2 = color.getRed();
        int n3 = color.getGreen();
        int n4 = color.getBlue();
        return new Color(n2, n3, n4, n);
    }
}

