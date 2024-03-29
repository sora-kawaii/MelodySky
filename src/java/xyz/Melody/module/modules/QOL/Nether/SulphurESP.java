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
    public void clear(WorldEvent.Load event) {
        this.blockPoss.clear();
    }

    @Override
    public void onDisable() {
        this.thread = null;
        this.blockPoss.clear();
        super.onDisable();
    }

    @EventHandler
    private void onUpdate(EventTick event) {
        if (this.ticks < 10) {
            ++this.ticks;
            return;
        }
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crimson_Island) {
            this.thread = null;
            this.blockPoss.clear();
            return;
        }
        if (this.mc.theWorld != null && this.mc.thePlayer != null && this.thread == null) {
            this.thread = new Thread(this::lambda$onUpdate$0, "MelodySky-SulphurESP Thread");
            this.thread.start();
        }
        this.ticks = 0;
    }

    @EventHandler
    private void on3D(EventRender3D event) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crimson_Island) {
            return;
        }
        for (int i = 0; i < this.blockPoss.size(); ++i) {
            BlockPos pos = this.blockPoss.get(i);
            Color orange = new Color(Colors.YELLOW.c);
            int c1 = new Color(orange.getRed(), orange.getGreen(), orange.getBlue(), 200).getRGB();
            RenderUtil.drawSolidBlockESP(pos, c1, event.getPartialTicks());
        }
    }

    @EventHandler
    private void onBlockChange(BlockChangeEvent event) {
        if (this.blockPoss.contains(event.getPosition())) {
            this.blockPoss.remove(event.getPosition());
        }
    }

    private void updateBlocks() {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crimson_Island) {
            return;
        }
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        Vec3i sub = new Vec3i((Double)this.range.getValue(), (double)((int)(this.mc.thePlayer.posY - (this.mc.thePlayer.posY - 32.0))), (Double)this.range.getValue());
        Vec3i add = new Vec3i((Double)this.range.getValue(), this.mc.thePlayer.posY - (this.mc.thePlayer.posY - 200.0), (Double)this.range.getValue());
        for (BlockPos blockPos : BlockPos.getAllInBox(this.mc.thePlayer.getPosition().add(add), this.mc.thePlayer.getPosition().subtract(sub))) {
            if (this.mc.theWorld == null) break;
            IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
            if (!Client.instance.getModuleManager().getModuleByClass(SulphurESP.class).isEnabled() || !this.thread.isAlive()) {
                this.blockPoss.clear();
                return;
            }
            if (this.mc.thePlayer.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > 250.0 || blockState.getBlock() != Blocks.sponge || this.blockPoss.contains(blockPos)) continue;
            if (((Boolean)this.debug.getValue()).booleanValue()) {
                Helper.sendMessage(blockPos);
            }
            this.blockPoss.add(blockPos);
        }
    }

    private void lambda$onUpdate$0() {
        while (this.isEnabled() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crimson_Island && this.mc.theWorld != null) {
            this.updateBlocks();
        }
    }
}

