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
    private void onUpdate(EventTick event) {
        if (this.ticks < 10) {
            ++this.ticks;
            return;
        }
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            this.thread = null;
            this.blockPoss.clear();
            return;
        }
        if (this.mc.theWorld != null && this.mc.thePlayer != null && this.thread == null) {
            this.thread = new Thread(this::lambda$onUpdate$0, "MelodySky-LavaFishingESP Thread");
            this.thread.start();
        }
        this.ticks = 0;
    }

    @EventHandler
    private void on3D(EventRender3D event) {
        BlockPos pos;
        int i;
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        for (i = 0; i < this.blockPoss.size(); ++i) {
            pos = this.blockPoss.get(i);
            if (!(this.mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ()) > 250.0)) continue;
            this.blockPoss.remove(pos);
            break;
        }
        for (i = 0; i < this.blockPoss.size(); ++i) {
            pos = this.blockPoss.get(i);
            Color orange = new Color(Colors.ORANGE.c);
            int c1 = new Color(orange.getRed(), orange.getGreen(), orange.getBlue(), 200).getRGB();
            if (this.mc.thePlayer.getDistance(pos.getX(), pos.getY(), pos.getZ()) < 5.0) continue;
            RenderUtil.drawSolidBlockESP(pos, c1, event.getPartialTicks());
        }
    }

    private void updateBlocks() {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        for (BlockPos blockPos : BlockPos.getAllInBox(new BlockPos(473, 150, 473), new BlockPos(823, 64, 823))) {
            if (this.mc.theWorld == null) break;
            IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
            if (!Client.instance.getModuleManager().getModuleByClass(WormFishingESP.class).isEnabled() || !this.thread.isAlive()) {
                this.blockPoss.clear();
                return;
            }
            if (this.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > 250.0 || blockState.getBlock() != Blocks.lava || this.blockPoss.contains(blockPos)) continue;
            if (((Boolean)this.debug.getValue()).booleanValue()) {
                Helper.sendMessage(blockPos);
            }
            this.blockPoss.add(blockPos);
        }
    }

    private void lambda$onUpdate$0() {
        while (this.isEnabled() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows && this.mc.theWorld != null) {
            this.updateBlocks();
        }
    }
}

