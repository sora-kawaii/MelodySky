/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import xyz.Melody.Client;
import xyz.Melody.ClientLib.SkyblockArea;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class WormFishingESP
extends Module {
    private Option<Boolean> debug = new Option<Boolean>("Debug", false);
    private ArrayList<BlockPos> blockPoss = new ArrayList();
    private Thread thread;
    private int ticks = 0;

    public WormFishingESP() {
        super("WormFishingESP", new String[]{"wfe"}, ModuleType.QOL);
        this.setEnabled(false);
        this.addValues(this.debug);
        this.setModInfo("Worm Fishing Lava ESP.");
    }

    @Override
    public void onDisable() {
        this.thread = null;
        this.blockPoss.clear();
        this.thread = null;
        super.onDisable();
    }

    @EventHandler
    private void onUpdate(EventTick eventTick) {
        if (this.ticks < 10) {
            ++this.ticks;
            return;
        }
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            this.thread = null;
            this.blockPoss.clear();
            return;
        }
        if (this.mc.field_71441_e != null && this.mc.field_71439_g != null && this.thread == null) {
            this.thread = new Thread(() -> {
                while (this.isEnabled() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows && this.mc.field_71441_e != null) {
                    this.updateBlocks();
                }
            }, "MelodySky-LavaFishingESP Thread");
            this.thread.start();
        }
        this.ticks = 0;
    }

    @EventHandler
    private void on3D(EventRender3D eventRender3D) {
        BlockPos blockPos;
        int n;
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        for (n = 0; n < this.blockPoss.size(); ++n) {
            blockPos = this.blockPoss.get(n);
            if (!(this.mc.field_71439_g.func_70011_f(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p()) > 250.0)) continue;
            this.blockPoss.remove(blockPos);
            break;
        }
        for (n = 0; n < this.blockPoss.size(); ++n) {
            blockPos = this.blockPoss.get(n);
            Color color = new Color(Colors.ORANGE.c);
            int n2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200).getRGB();
            if (this.mc.field_71439_g.func_70011_f(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p()) < 5.0) continue;
            RenderUtil.drawSolidBlockESP(blockPos, n2, eventRender3D.getPartialTicks());
        }
    }

    private void updateBlocks() {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return;
        }
        for (BlockPos blockPos : BlockPos.func_177980_a((BlockPos)new BlockPos(473, 150, 473), (BlockPos)new BlockPos(823, 64, 823))) {
            if (this.mc.field_71441_e == null) break;
            IBlockState iBlockState = this.mc.field_71441_e.func_180495_p(blockPos);
            if (!Client.instance.getModuleManager().getModuleByClass(WormFishingESP.class).isEnabled() || !this.thread.isAlive()) {
                this.blockPoss.clear();
                return;
            }
            if (this.mc.field_71439_g.func_70011_f(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p()) > 250.0 || iBlockState.func_177230_c() != Blocks.field_150353_l || this.blockPoss.contains(blockPos)) continue;
            if (((Boolean)this.debug.getValue()).booleanValue()) {
                Helper.sendMessage(blockPos);
            }
            this.blockPoss.add(blockPos);
        }
    }
}

