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
    public void onTick(EventTick event) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            this.gemstones.clear();
            this.isScanning = false;
            this.lastChecked = null;
            return;
        }
        if (!(this.isScanning || this.lastChecked != null && this.lastChecked.equals(this.mc.thePlayer.playerLocation))) {
            this.isScanning = true;
            int gemstoneRadius = ((Double)this.radius.getValue()).intValue();
            this.thread = new Thread(() -> this.lambda$onTick$0(gemstoneRadius), "MelodySky-GemStoneESP");
            this.thread.start();
        }
    }

    @EventHandler
    public void onBlockChange(BlockChangeEvent event) {
        if (event.getNewBlock().getBlock() == Blocks.air) {
            this.gemstones.remove(event.getPosition());
        }
    }

    @EventHandler
    public void onRenderWorld(EventRender3D event) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        for (Map.Entry<BlockPos, Gemstone> gemstone : this.gemstones.entrySet()) {
            double d;
            if (!this.isGemstoneEnabled(gemstone.getValue())) continue;
            double distance = Math.sqrt(gemstone.getKey().distanceSq(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ));
            if (d > (double)(((Double)this.radius.getValue()).intValue() + 2)) continue;
            int alpha = (int)Math.abs(100.0 - distance / (double)((Double)this.radius.getValue()).intValue() * 100.0);
            Color color = ColorUtils.addAlpha(gemstone.getValue().color, alpha);
            RenderUtil.drawSolidBlockESP(gemstone.getKey(), color.getRGB(), event.getPartialTicks());
        }
    }

    private Gemstone getGemstone(IBlockState block) {
        if (block.getBlock() != Blocks.stained_glass) {
            return null;
        }
        if (((Boolean)this.pane.getValue()).booleanValue() && block.getBlock() != Blocks.stained_glass_pane) {
            return null;
        }
        EnumDyeColor color = this.firstNotNull(block.getValue(BlockStainedGlass.COLOR), block.getValue(BlockStainedGlassPane.COLOR));
        if (color == Gemstone.RUBY.dyeColor) {
            return Gemstone.RUBY;
        }
        if (color == Gemstone.AMETHYST.dyeColor) {
            return Gemstone.AMETHYST;
        }
        if (color == Gemstone.JADE.dyeColor) {
            return Gemstone.JADE;
        }
        if (color == Gemstone.SAPPHIRE.dyeColor) {
            return Gemstone.SAPPHIRE;
        }
        if (color == Gemstone.AMBER.dyeColor) {
            return Gemstone.AMBER;
        }
        if (color == Gemstone.TOPAZ.dyeColor) {
            return Gemstone.TOPAZ;
        }
        if (color == Gemstone.JASPER.dyeColor) {
            return Gemstone.JASPER;
        }
        if (color == Gemstone.OPAL.dyeColor) {
            return Gemstone.OPAL;
        }
        return null;
    }

    private boolean isGemstoneEnabled(Gemstone gemstone) {
        switch (I.$SwitchMap$xyz$Melody$module$modules$QOL$GemStoneESP$Gemstone[gemstone.ordinal()]) {
            case 1: {
                return (Boolean)this.ruby.getValue();
            }
            case 2: {
                return (Boolean)this.amethyst.getValue();
            }
            case 3: {
                return (Boolean)this.jade.getValue();
            }
            case 4: {
                return (Boolean)this.sapphire.getValue();
            }
            case 5: {
                return (Boolean)this.amber.getValue();
            }
            case 6: {
                return (Boolean)this.topaz.getValue();
            }
            case 7: {
                return (Boolean)this.jasper.getValue();
            }
            case 8: {
                return (Boolean)this.opal.getValue();
            }
        }
        return false;
    }

    @EventHandler
    public void onWorldChange(EventTick event) {
        if (this.mc.theWorld == null || this.mc.thePlayer == null) {
            this.gemstones.clear();
            this.lastChecked = null;
        }
    }

    public <T> T firstNotNull(T ... args) {
        for (T arg : args) {
            if (arg == null) continue;
            return arg;
        }
        return null;
    }

    private void lambda$onTick$0(int gemstoneRadius) {
        BlockPos playerPosition;
        this.lastChecked = playerPosition = this.mc.thePlayer.getPosition();
        for (int x = playerPosition.getX() - gemstoneRadius; x < playerPosition.getX() + gemstoneRadius; ++x) {
            for (int y = playerPosition.getY() - gemstoneRadius; y < playerPosition.getY() + gemstoneRadius; ++y) {
                for (int z = playerPosition.getZ() - gemstoneRadius; z < playerPosition.getZ() + gemstoneRadius; ++z) {
                    Gemstone gemstone;
                    BlockPos position = new BlockPos(x, y, z);
                    if (this.mc.theWorld.isAirBlock(position) || (gemstone = this.getGemstone(this.mc.theWorld.getBlockState(position))) == null) continue;
                    this.gemstones.put(position, gemstone);
                }
            }
        }
        this.isScanning = false;
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

        private Gemstone(Color color, EnumDyeColor dyeColor) {
            this.color = color;
            this.dyeColor = dyeColor;
        }
    }
}

