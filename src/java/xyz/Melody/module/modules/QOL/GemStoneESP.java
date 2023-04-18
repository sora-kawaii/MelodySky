/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import xyz.Melody.Client;
import xyz.Melody.ClientLib.SkyblockArea;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.BlockChangeEvent;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.render.ColorUtils;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class GemStoneESP
extends Module {
    private Numbers<Double> radius = new Numbers<Double>("Radius", 100.0, 1.0, 500.0, 1.0);
    private Option<Boolean> jade = new Option<Boolean>("Jade", true);
    private Option<Boolean> amber = new Option<Boolean>("Amber", true);
    private Option<Boolean> topaz = new Option<Boolean>("Topaz", true);
    private Option<Boolean> sapphire = new Option<Boolean>("Sapphire", true);
    private Option<Boolean> amethyst = new Option<Boolean>("Amethyst", true);
    private Option<Boolean> jasper = new Option<Boolean>("Jasper", true);
    private Option<Boolean> ruby = new Option<Boolean>("Ruby", true);
    private Option<Boolean> opal = new Option<Boolean>("Opal", true);
    private Option<Boolean> pane = new Option<Boolean>("GlassPane", false);
    private ConcurrentHashMap<BlockPos, Gemstone> gemstones = new ConcurrentHashMap();
    private BlockPos lastChecked = null;
    private boolean isScanning = false;
    private Thread thread;

    public GemStoneESP() {
        super("GemStoneESP", new String[]{"tags"}, ModuleType.Render);
        this.addValues(this.radius, this.jade, this.amber, this.topaz, this.sapphire, this.amethyst, this.jasper, this.ruby, this.opal, this.pane);
        this.setModInfo("Just Gemstone ESP.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    public void onTick(EventTick eventTick) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            this.gemstones.clear();
            this.isScanning = false;
            this.lastChecked = null;
            return;
        }
        if (!(this.isScanning || this.lastChecked != null && this.lastChecked.equals(this.mc.field_71439_g.field_71081_bT))) {
            this.isScanning = true;
            int n = ((Double)this.radius.getValue()).intValue();
            this.thread = new Thread(() -> {
                BlockPos blockPos;
                this.lastChecked = blockPos = this.mc.field_71439_g.func_180425_c();
                for (int i = blockPos.func_177958_n() - n; i < blockPos.func_177958_n() + n; ++i) {
                    for (int j = blockPos.func_177956_o() - n; j < blockPos.func_177956_o() + n; ++j) {
                        for (int k = blockPos.func_177952_p() - n; k < blockPos.func_177952_p() + n; ++k) {
                            Gemstone gemstone;
                            BlockPos blockPos2 = new BlockPos(i, j, k);
                            if (this.mc.field_71441_e.func_175623_d(blockPos2) || (gemstone = this.getGemstone(this.mc.field_71441_e.func_180495_p(blockPos2))) == null) continue;
                            this.gemstones.put(blockPos2, gemstone);
                        }
                    }
                }
                this.isScanning = false;
            }, "MelodySky-GemStoneESP");
            this.thread.start();
        }
    }

    @EventHandler
    public void onBlockChange(BlockChangeEvent blockChangeEvent) {
        if (blockChangeEvent.getNewBlock().func_177230_c() == Blocks.field_150350_a) {
            this.gemstones.remove(blockChangeEvent.getPosition());
        }
    }

    @EventHandler
    public void onRenderWorld(EventRender3D eventRender3D) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        for (Map.Entry<BlockPos, Gemstone> entry : this.gemstones.entrySet()) {
            double d;
            if (!this.isGemstoneEnabled(entry.getValue())) continue;
            double d2 = Math.sqrt(entry.getKey().func_177954_c(this.mc.field_71439_g.field_70165_t, this.mc.field_71439_g.field_70163_u, this.mc.field_71439_g.field_70161_v));
            if (d > (double)(((Double)this.radius.getValue()).intValue() + 2)) continue;
            int n = (int)Math.abs(100.0 - d2 / (double)((Double)this.radius.getValue()).intValue() * 100.0);
            Color color = ColorUtils.addAlpha(entry.getValue().color, n);
            RenderUtil.drawSolidBlockESP(entry.getKey(), color.getRGB(), eventRender3D.getPartialTicks());
        }
    }

    private Gemstone getGemstone(IBlockState iBlockState) {
        if (iBlockState.func_177230_c() != Blocks.field_150399_cn) {
            return null;
        }
        if (((Boolean)this.pane.getValue()).booleanValue() && iBlockState.func_177230_c() != Blocks.field_150397_co) {
            return null;
        }
        EnumDyeColor enumDyeColor = this.firstNotNull((EnumDyeColor)((Object)iBlockState.func_177229_b(BlockStainedGlass.field_176547_a)), (EnumDyeColor)((Object)iBlockState.func_177229_b(BlockStainedGlassPane.field_176245_a)));
        if (enumDyeColor == Gemstone.RUBY.dyeColor) {
            return Gemstone.RUBY;
        }
        if (enumDyeColor == Gemstone.AMETHYST.dyeColor) {
            return Gemstone.AMETHYST;
        }
        if (enumDyeColor == Gemstone.JADE.dyeColor) {
            return Gemstone.JADE;
        }
        if (enumDyeColor == Gemstone.SAPPHIRE.dyeColor) {
            return Gemstone.SAPPHIRE;
        }
        if (enumDyeColor == Gemstone.AMBER.dyeColor) {
            return Gemstone.AMBER;
        }
        if (enumDyeColor == Gemstone.TOPAZ.dyeColor) {
            return Gemstone.TOPAZ;
        }
        if (enumDyeColor == Gemstone.JASPER.dyeColor) {
            return Gemstone.JASPER;
        }
        if (enumDyeColor == Gemstone.OPAL.dyeColor) {
            return Gemstone.OPAL;
        }
        return null;
    }

    private boolean isGemstoneEnabled(Gemstone gemstone) {
        switch (gemstone) {
            case RUBY: {
                return (Boolean)this.ruby.getValue();
            }
            case AMETHYST: {
                return (Boolean)this.amethyst.getValue();
            }
            case JADE: {
                return (Boolean)this.jade.getValue();
            }
            case SAPPHIRE: {
                return (Boolean)this.sapphire.getValue();
            }
            case AMBER: {
                return (Boolean)this.amber.getValue();
            }
            case TOPAZ: {
                return (Boolean)this.topaz.getValue();
            }
            case JASPER: {
                return (Boolean)this.jasper.getValue();
            }
            case OPAL: {
                return (Boolean)this.opal.getValue();
            }
        }
        return false;
    }

    @EventHandler
    public void onWorldChange(EventTick eventTick) {
        if (this.mc.field_71441_e == null || this.mc.field_71439_g == null) {
            this.gemstones.clear();
            this.lastChecked = null;
        }
    }

    public <T> T firstNotNull(T ... TArray) {
        for (T t : TArray) {
            if (t == null) continue;
            return t;
        }
        return null;
    }

    static enum Gemstone {
        RUBY(new Color(188, 3, 29), EnumDyeColor.RED),
        AMETHYST(new Color(137, 0, 201), EnumDyeColor.PURPLE),
        JADE(new Color(157, 249, 32), EnumDyeColor.LIME),
        SAPPHIRE(new Color(60, 121, 224), EnumDyeColor.LIGHT_BLUE),
        AMBER(new Color(237, 139, 35), EnumDyeColor.ORANGE),
        TOPAZ(new Color(249, 215, 36), EnumDyeColor.YELLOW),
        JASPER(new Color(214, 15, 150), EnumDyeColor.MAGENTA),
        OPAL(new Color(245, 245, 240), EnumDyeColor.WHITE);

        public Color color;
        public EnumDyeColor dyeColor;

        private Gemstone(Color color, EnumDyeColor enumDyeColor) {
            this.color = color;
            this.dyeColor = enumDyeColor;
        }
    }
}

