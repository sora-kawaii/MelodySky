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
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.ClientLib.SkyblockArea;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Option;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class SapphireMiningPit
extends Module {
    private Option<Boolean> debug = new Option<Boolean>("Debug", false);
    private ArrayList<BlockPos> blockPoss = new ArrayList();
    private Thread thread;
    private Thread scanThread0;
    private Thread scanThread1;
    private Thread scanThread2;
    private Thread scanThread3;
    private int ticks = 0;

    public SapphireMiningPit() {
        super("SapphirePitESP", new String[]{"spe"}, ModuleType.QOL);
        this.setEnabled(false);
        this.addValues(this.debug);
        this.setModInfo("Sapphire Mining Pit ESP.");
    }

    @Override
    public void onDisable() {
        Helper.sendMessage("[SapphirePitESP] Scanning Abort.");
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
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            this.thread = null;
            this.blockPoss.clear();
            return;
        }
        if (this.mc.theWorld != null && this.mc.thePlayer != null && this.thread == null) {
            this.thread = new Thread(this::lambda$onUpdate$4, "MelodySky-SapphireFinder Call");
            this.thread.start();
        }
        this.ticks = 0;
    }

    @EventHandler
    private void on3D(EventRender3D event) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        for (int i = 0; i < this.blockPoss.size(); ++i) {
            BlockPos pos = this.blockPoss.get(i);
            Color orange = new Color(Colors.BLUE.c);
            int c1 = new Color(orange.getRed(), orange.getGreen(), orange.getBlue(), 200).getRGB();
            RenderUtil.drawSolidBlockESP(pos, c1, event.getPartialTicks());
            double posX = (double)pos.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
            double posY = (double)pos.getY() - this.mc.getRenderManager().viewerPosY + 0.5;
            double posZ = (double)pos.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
            RenderUtil.startDrawing();
            this.trace(pos, Colors.BLUE.c, posX, posY, posZ);
            RenderUtil.stopDrawing();
        }
    }

    private void updateBlocks0() {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        int dick = 0;
        for (BlockPos blockPos : BlockPos.getAllInBox(new BlockPos(473, 31, 473), new BlockPos(648, 188, 648))) {
            if (this.mc.theWorld == null || !this.isEnabled()) break;
            IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
            if (!Client.instance.getModuleManager().getModuleByClass(SapphireMiningPit.class).isEnabled()) {
                this.blockPoss.clear();
                return;
            }
            ++dick;
            try {
                if (!this.mc.theWorld.isBlockLoaded(blockPos) || blockState.getBlock() != Blocks.double_stone_slab) continue;
                IBlockState bsup2 = this.mc.theWorld.getBlockState(blockPos.up(6));
                IBlockState bsup1 = this.mc.theWorld.getBlockState(blockPos.up(2));
                IBlockState bs = this.mc.theWorld.getBlockState(blockPos.up(1));
                IBlockState bs0 = this.mc.theWorld.getBlockState(blockPos.down(1));
                IBlockState bs1 = this.mc.theWorld.getBlockState(blockPos.down(2));
                if (bsup2.getBlock() == Blocks.stone_slab && bsup1.getBlock() == Blocks.double_stone_slab && bs.getBlock() == Blocks.stone_slab && bs1.getBlock() == Blocks.stone_brick_stairs && bs0.getBlock() == Blocks.double_stone_slab) {
                    if (((Boolean)this.debug.getValue()).booleanValue()) {
                        Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.GREEN) + "TRUE");
                    }
                    if (this.blockPoss.contains(blockPos)) continue;
                    Helper.sendMessage("[SapphirePitESP] Sapphire Pit Found.");
                    Helper.sendMessage("[SapphirePitESP] Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ());
                    NotificationPublisher.queue("Sapphire Pit ESP", "Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ(), NotificationType.INFO, 7500);
                    this.blockPoss.add(blockPos);
                    continue;
                }
                if (!((Boolean)this.debug.getValue()).booleanValue()) continue;
                Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.RED) + "FALSE");
                Helper.sendMessage(bs1.getBlock().getRegistryName());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.blockPoss.isEmpty() && ((Boolean)this.debug.getValue()).booleanValue()) {
            Helper.sendMessage("[SapphirePitESP] Scanned " + dick + " Blocks.");
            Helper.sendMessage("[SapphirePitESP] " + this.blockPoss.size() + " Coords Possible.");
        }
    }

    private void updateBlocks1() {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        int dick = 0;
        for (BlockPos blockPos : BlockPos.getAllInBox(new BlockPos(648, 31, 648), new BlockPos(823, 188, 823))) {
            if (this.mc.theWorld == null || !this.isEnabled()) break;
            IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
            if (!Client.instance.getModuleManager().getModuleByClass(SapphireMiningPit.class).isEnabled()) {
                this.blockPoss.clear();
                return;
            }
            ++dick;
            try {
                if (!this.mc.theWorld.isBlockLoaded(blockPos) || blockState.getBlock() != Blocks.double_stone_slab) continue;
                IBlockState bsup2 = this.mc.theWorld.getBlockState(blockPos.up(6));
                IBlockState bsup1 = this.mc.theWorld.getBlockState(blockPos.up(2));
                IBlockState bs = this.mc.theWorld.getBlockState(blockPos.up(1));
                IBlockState bs0 = this.mc.theWorld.getBlockState(blockPos.down(1));
                IBlockState bs1 = this.mc.theWorld.getBlockState(blockPos.down(2));
                if (bsup2.getBlock() == Blocks.stone_slab && bsup1.getBlock() == Blocks.double_stone_slab && bs.getBlock() == Blocks.stone_slab && bs1.getBlock() == Blocks.stone_brick_stairs && bs0.getBlock() == Blocks.double_stone_slab) {
                    if (((Boolean)this.debug.getValue()).booleanValue()) {
                        Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.GREEN) + "TRUE");
                    }
                    if (this.blockPoss.contains(blockPos)) continue;
                    Helper.sendMessage("[SapphirePitESP] Sapphire Pit Found.");
                    Helper.sendMessage("[SapphirePitESP] Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ());
                    NotificationPublisher.queue("Sapphire Pit ESP", "Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ(), NotificationType.INFO, 7500);
                    this.blockPoss.add(blockPos);
                    continue;
                }
                if (!((Boolean)this.debug.getValue()).booleanValue()) continue;
                Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.RED) + "FALSE");
                Helper.sendMessage(bs1.getBlock().getRegistryName());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.blockPoss.isEmpty() && ((Boolean)this.debug.getValue()).booleanValue()) {
            Helper.sendMessage("[SapphirePitESP] Scanned " + dick + " Blocks.");
            Helper.sendMessage("[SapphirePitESP] " + this.blockPoss.size() + " Coords Possible.");
        }
    }

    private void updateBlocks2() {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        int dick = 0;
        for (BlockPos blockPos : BlockPos.getAllInBox(new BlockPos(473, 31, 823), new BlockPos(648, 188, 648))) {
            if (this.mc.theWorld == null || !this.isEnabled()) break;
            IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
            if (!Client.instance.getModuleManager().getModuleByClass(SapphireMiningPit.class).isEnabled()) {
                this.blockPoss.clear();
                return;
            }
            ++dick;
            try {
                if (!this.mc.theWorld.isBlockLoaded(blockPos) || blockState.getBlock() != Blocks.double_stone_slab) continue;
                IBlockState bsup2 = this.mc.theWorld.getBlockState(blockPos.up(6));
                IBlockState bsup1 = this.mc.theWorld.getBlockState(blockPos.up(2));
                IBlockState bs = this.mc.theWorld.getBlockState(blockPos.up(1));
                IBlockState bs0 = this.mc.theWorld.getBlockState(blockPos.down(1));
                IBlockState bs1 = this.mc.theWorld.getBlockState(blockPos.down(2));
                if (bsup2.getBlock() == Blocks.stone_slab && bsup1.getBlock() == Blocks.double_stone_slab && bs.getBlock() == Blocks.stone_slab && bs1.getBlock() == Blocks.stone_brick_stairs && bs0.getBlock() == Blocks.double_stone_slab) {
                    if (((Boolean)this.debug.getValue()).booleanValue()) {
                        Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.GREEN) + "TRUE");
                    }
                    if (this.blockPoss.contains(blockPos)) continue;
                    Helper.sendMessage("[SapphirePitESP] Sapphire Pit Found.");
                    Helper.sendMessage("[SapphirePitESP] Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ());
                    NotificationPublisher.queue("Sapphire Pit ESP", "Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ(), NotificationType.INFO, 7500);
                    this.blockPoss.add(blockPos);
                    continue;
                }
                if (!((Boolean)this.debug.getValue()).booleanValue()) continue;
                Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.RED) + "FALSE");
                Helper.sendMessage(bs1.getBlock().getRegistryName());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.blockPoss.isEmpty() && ((Boolean)this.debug.getValue()).booleanValue()) {
            Helper.sendMessage("[SapphirePitESP] Scanned " + dick + " Blocks.");
            Helper.sendMessage("[SapphirePitESP] " + this.blockPoss.size() + " Coords Possible.");
        }
    }

    private void updateBlocks3() {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        int dick = 0;
        for (BlockPos blockPos : BlockPos.getAllInBox(new BlockPos(648, 31, 648), new BlockPos(823, 188, 473))) {
            if (this.mc.theWorld == null || !this.isEnabled()) break;
            IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
            if (!Client.instance.getModuleManager().getModuleByClass(SapphireMiningPit.class).isEnabled()) {
                this.blockPoss.clear();
                return;
            }
            ++dick;
            try {
                if (!this.mc.theWorld.isBlockLoaded(blockPos) || blockState.getBlock() != Blocks.double_stone_slab) continue;
                IBlockState bsup2 = this.mc.theWorld.getBlockState(blockPos.up(6));
                IBlockState bsup1 = this.mc.theWorld.getBlockState(blockPos.up(2));
                IBlockState bs = this.mc.theWorld.getBlockState(blockPos.up(1));
                IBlockState bs0 = this.mc.theWorld.getBlockState(blockPos.down(1));
                IBlockState bs1 = this.mc.theWorld.getBlockState(blockPos.down(2));
                if (bsup2.getBlock() == Blocks.stone_slab && bsup1.getBlock() == Blocks.double_stone_slab && bs.getBlock() == Blocks.stone_slab && bs1.getBlock() == Blocks.stone_brick_stairs && bs0.getBlock() == Blocks.double_stone_slab) {
                    if (((Boolean)this.debug.getValue()).booleanValue()) {
                        Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.GREEN) + "TRUE");
                    }
                    if (this.blockPoss.contains(blockPos)) continue;
                    Helper.sendMessage("[SapphirePitESP] Sapphire Pit Found.");
                    Helper.sendMessage("[SapphirePitESP] Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ());
                    NotificationPublisher.queue("Sapphire Pit ESP", "Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.getX() + ", " + blockPos.getY() + ", " + blockPos.getZ(), NotificationType.INFO, 7500);
                    this.blockPoss.add(blockPos);
                    continue;
                }
                if (!((Boolean)this.debug.getValue()).booleanValue()) continue;
                Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.RED) + "FALSE");
                Helper.sendMessage(bs1.getBlock().getRegistryName());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (this.blockPoss.isEmpty() && ((Boolean)this.debug.getValue()).booleanValue()) {
            Helper.sendMessage("[SapphirePitESP] Scanned " + dick + " Blocks.");
            Helper.sendMessage("[SapphirePitESP] " + this.blockPoss.size() + " Coords Possible.");
        }
    }

    private void trace(BlockPos pos, int color, double x, double y, double z) {
        GL11.glEnable(2848);
        RenderUtil.setColor(color);
        GL11.glLineWidth(3.0f);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, this.mc.thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(2848);
    }

    private void lambda$onUpdate$4() {
        Helper.sendMessage("[SapphirePitESP] This May Take Some Time.");
        if (!this.isEnabled() || Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (this.mc.theWorld == null) {
            return;
        }
        this.scanThread0 = new Thread(this::lambda$null$0, "MS-SE-Scan 0");
        this.scanThread0.start();
        this.scanThread1 = new Thread(this::lambda$null$1, "MS-SE-Scan 1");
        this.scanThread1.start();
        this.scanThread2 = new Thread(this::lambda$null$2, "MS-SE-Scan 2");
        this.scanThread2.start();
        this.scanThread3 = new Thread(this::lambda$null$3, "MS-SE-Scan 3");
        this.scanThread3.start();
    }

    private void lambda$null$3() {
        while (this.mc.theWorld != null && this.isEnabled() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
            this.updateBlocks3();
        }
    }

    private void lambda$null$2() {
        while (this.mc.theWorld != null && this.isEnabled() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
            this.updateBlocks2();
        }
    }

    private void lambda$null$1() {
        while (this.mc.theWorld != null && this.isEnabled() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
            this.updateBlocks1();
        }
    }

    private void lambda$null$0() {
        while (this.mc.theWorld != null && this.isEnabled() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
            this.updateBlocks0();
        }
    }
}

