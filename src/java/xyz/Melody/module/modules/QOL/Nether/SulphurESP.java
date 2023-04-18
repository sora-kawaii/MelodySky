/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Nether;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.ClientLib.SkyblockArea;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.BlockChangeEvent;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class SulphurESP
extends Module {
    private Numbers<Double> range = new Numbers<Double>("Range", 200.0, 100.0, 500.0, 10.0);
    private Option<Boolean> debug = new Option<Boolean>("Debug", false);
    private ArrayList<BlockPos> blockPoss = new ArrayList();
    private Thread thread;
    private int ticks = 0;

    public SulphurESP() {
        super("SulphurESP", new String[]{"sulesp"}, ModuleType.Nether);
        this.addValues(this.range, this.debug);
        this.setModInfo("Sulphur ESP (Crimson Island).");
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        this.blockPoss.clear();
    }

    @Override
    public void onDisable() {
        this.thread = null;
        this.blockPoss.clear();
        super.onDisable();
    }

    @EventHandler
    private void onUpdate(EventTick eventTick) {
        if (this.ticks < 10) {
            ++this.ticks;
            return;
        }
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crimson_Island) {
            this.thread = null;
            this.blockPoss.clear();
            return;
        }
        if (this.mc.field_71441_e != null && this.mc.field_71439_g != null && this.thread == null) {
            this.thread = new Thread(() -> {
                while (this.isEnabled() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crimson_Island && this.mc.field_71441_e != null) {
                    this.updateBlocks();
                }
            }, "MelodySky-SulphurESP Thread");
            this.thread.start();
        }
        this.ticks = 0;
    }

    @EventHandler
    private void on3D(EventRender3D eventRender3D) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crimson_Island) {
            return;
        }
        for (int i = 0; i < this.blockPoss.size(); ++i) {
            BlockPos blockPos = this.blockPoss.get(i);
            Color color = new Color(Colors.YELLOW.c);
            int n = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200).getRGB();
            RenderUtil.drawSolidBlockESP(blockPos, n, eventRender3D.getPartialTicks());
        }
    }

    @EventHandler
    private void onBlockChange(BlockChangeEvent blockChangeEvent) {
        if (this.blockPoss.contains(blockChangeEvent.getPosition())) {
            this.blockPoss.remove(blockChangeEvent.getPosition());
        }
    }

    private void updateBlocks() {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crimson_Island) {
            return;
        }
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return;
        }
        Vec3i vec3i = new Vec3i((Double)this.range.getValue(), (double)((int)(this.mc.field_71439_g.field_70163_u - (this.mc.field_71439_g.field_70163_u - 32.0))), (Double)this.range.getValue());
        Vec3i vec3i2 = new Vec3i((Double)this.range.getValue(), this.mc.field_71439_g.field_70163_u - (this.mc.field_71439_g.field_70163_u - 200.0), (Double)this.range.getValue());
        for (BlockPos blockPos : BlockPos.func_177980_a((BlockPos)this.mc.field_71439_g.func_180425_c().func_177971_a(vec3i2), (BlockPos)this.mc.field_71439_g.func_180425_c().func_177973_b(vec3i))) {
            if (this.mc.field_71441_e == null) break;
            IBlockState iBlockState = this.mc.field_71441_e.func_180495_p(blockPos);
            if (!Client.instance.getModuleManager().getModuleByClass(SulphurESP.class).isEnabled() || !this.thread.isAlive()) {
                this.blockPoss.clear();
                return;
            }
            if (this.mc.field_71439_g.func_70011_f(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p()) > 250.0 || iBlockState.func_177230_c() != Blocks.field_150360_v || this.blockPoss.contains(blockPos)) continue;
            if (((Boolean)this.debug.getValue()).booleanValue()) {
                Helper.sendMessage(blockPos);
            }
            this.blockPoss.add(blockPos);
        }
    }
}

