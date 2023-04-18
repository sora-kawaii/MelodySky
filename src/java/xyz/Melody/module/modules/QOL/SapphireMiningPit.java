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
                Helper.sendMessage("[SapphirePitESP] This May Take Some Time.");
                if (!this.isEnabled() || Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
                    return;
                }
                if (this.mc.field_71441_e == null) {
                    return;
                }
                this.scanThread0 = new Thread(() -> {
                    while (this.mc.field_71441_e != null && this.isEnabled() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
                        this.updateBlocks0();
                    }
                }, "MS-SE-Scan 0");
                this.scanThread0.start();
                this.scanThread1 = new Thread(() -> {
                    while (this.mc.field_71441_e != null && this.isEnabled() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
                        this.updateBlocks1();
                    }
                }, "MS-SE-Scan 1");
                this.scanThread1.start();
                this.scanThread2 = new Thread(() -> {
                    while (this.mc.field_71441_e != null && this.isEnabled() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
                        this.updateBlocks2();
                    }
                }, "MS-SE-Scan 2");
                this.scanThread2.start();
                this.scanThread3 = new Thread(() -> {
                    while (this.mc.field_71441_e != null && this.isEnabled() && Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
                        this.updateBlocks3();
                    }
                }, "MS-SE-Scan 3");
                this.scanThread3.start();
            }, "MelodySky-SapphireFinder Call");
            this.thread.start();
        }
        this.ticks = 0;
    }

    @EventHandler
    private void on3D(EventRender3D eventRender3D) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        for (int i = 0; i < this.blockPoss.size(); ++i) {
            BlockPos blockPos = this.blockPoss.get(i);
            Color color = new Color(Colors.BLUE.c);
            int n = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200).getRGB();
            RenderUtil.drawSolidBlockESP(blockPos, n, eventRender3D.getPartialTicks());
            double d = (double)blockPos.func_177958_n() - this.mc.func_175598_ae().field_78730_l + 0.5;
            double d2 = (double)blockPos.func_177956_o() - this.mc.func_175598_ae().field_78731_m + 0.5;
            double d3 = (double)blockPos.func_177952_p() - this.mc.func_175598_ae().field_78728_n + 0.5;
            RenderUtil.startDrawing();
            this.trace(blockPos, Colors.BLUE.c, d, d2, d3);
            RenderUtil.stopDrawing();
        }
    }

    private void updateBlocks0() {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return;
        }
        int n = 0;
        for (BlockPos blockPos : BlockPos.func_177980_a((BlockPos)new BlockPos(473, 31, 473), (BlockPos)new BlockPos(648, 188, 648))) {
            if (this.mc.field_71441_e == null || !this.isEnabled()) break;
            IBlockState iBlockState = this.mc.field_71441_e.func_180495_p(blockPos);
            if (!Client.instance.getModuleManager().getModuleByClass(SapphireMiningPit.class).isEnabled()) {
                this.blockPoss.clear();
                return;
            }
            ++n;
            try {
                if (!this.mc.field_71441_e.func_175667_e(blockPos) || iBlockState.func_177230_c() != Blocks.field_150334_T) continue;
                IBlockState iBlockState2 = this.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(6));
                IBlockState iBlockState3 = this.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2));
                IBlockState iBlockState4 = this.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(1));
                IBlockState iBlockState5 = this.mc.field_71441_e.func_180495_p(blockPos.func_177979_c(1));
                IBlockState iBlockState6 = this.mc.field_71441_e.func_180495_p(blockPos.func_177979_c(2));
                if (iBlockState2.func_177230_c() == Blocks.field_150333_U && iBlockState3.func_177230_c() == Blocks.field_150334_T && iBlockState4.func_177230_c() == Blocks.field_150333_U && iBlockState6.func_177230_c() == Blocks.field_150390_bg && iBlockState5.func_177230_c() == Blocks.field_150334_T) {
                    if (((Boolean)this.debug.getValue()).booleanValue()) {
                        Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.GREEN) + "TRUE");
                    }
                    if (this.blockPoss.contains(blockPos)) continue;
                    Helper.sendMessage("[SapphirePitESP] Sapphire Pit Found.");
                    Helper.sendMessage("[SapphirePitESP] Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.func_177958_n() + ", " + blockPos.func_177956_o() + ", " + blockPos.func_177952_p());
                    NotificationPublisher.queue("Sapphire Pit ESP", "Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.func_177958_n() + ", " + blockPos.func_177956_o() + ", " + blockPos.func_177952_p(), NotificationType.INFO, 7500);
                    this.blockPoss.add(blockPos);
                    continue;
                }
                if (!((Boolean)this.debug.getValue()).booleanValue()) continue;
                Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.RED) + "FALSE");
                Helper.sendMessage(iBlockState6.func_177230_c().getRegistryName());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (this.blockPoss.isEmpty() && ((Boolean)this.debug.getValue()).booleanValue()) {
            Helper.sendMessage("[SapphirePitESP] Scanned " + n + " Blocks.");
            Helper.sendMessage("[SapphirePitESP] " + this.blockPoss.size() + " Coords Possible.");
        }
    }

    private void updateBlocks1() {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return;
        }
        int n = 0;
        for (BlockPos blockPos : BlockPos.func_177980_a((BlockPos)new BlockPos(648, 31, 648), (BlockPos)new BlockPos(823, 188, 823))) {
            if (this.mc.field_71441_e == null || !this.isEnabled()) break;
            IBlockState iBlockState = this.mc.field_71441_e.func_180495_p(blockPos);
            if (!Client.instance.getModuleManager().getModuleByClass(SapphireMiningPit.class).isEnabled()) {
                this.blockPoss.clear();
                return;
            }
            ++n;
            try {
                if (!this.mc.field_71441_e.func_175667_e(blockPos) || iBlockState.func_177230_c() != Blocks.field_150334_T) continue;
                IBlockState iBlockState2 = this.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(6));
                IBlockState iBlockState3 = this.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2));
                IBlockState iBlockState4 = this.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(1));
                IBlockState iBlockState5 = this.mc.field_71441_e.func_180495_p(blockPos.func_177979_c(1));
                IBlockState iBlockState6 = this.mc.field_71441_e.func_180495_p(blockPos.func_177979_c(2));
                if (iBlockState2.func_177230_c() == Blocks.field_150333_U && iBlockState3.func_177230_c() == Blocks.field_150334_T && iBlockState4.func_177230_c() == Blocks.field_150333_U && iBlockState6.func_177230_c() == Blocks.field_150390_bg && iBlockState5.func_177230_c() == Blocks.field_150334_T) {
                    if (((Boolean)this.debug.getValue()).booleanValue()) {
                        Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.GREEN) + "TRUE");
                    }
                    if (this.blockPoss.contains(blockPos)) continue;
                    Helper.sendMessage("[SapphirePitESP] Sapphire Pit Found.");
                    Helper.sendMessage("[SapphirePitESP] Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.func_177958_n() + ", " + blockPos.func_177956_o() + ", " + blockPos.func_177952_p());
                    NotificationPublisher.queue("Sapphire Pit ESP", "Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.func_177958_n() + ", " + blockPos.func_177956_o() + ", " + blockPos.func_177952_p(), NotificationType.INFO, 7500);
                    this.blockPoss.add(blockPos);
                    continue;
                }
                if (!((Boolean)this.debug.getValue()).booleanValue()) continue;
                Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.RED) + "FALSE");
                Helper.sendMessage(iBlockState6.func_177230_c().getRegistryName());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (this.blockPoss.isEmpty() && ((Boolean)this.debug.getValue()).booleanValue()) {
            Helper.sendMessage("[SapphirePitESP] Scanned " + n + " Blocks.");
            Helper.sendMessage("[SapphirePitESP] " + this.blockPoss.size() + " Coords Possible.");
        }
    }

    private void updateBlocks2() {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return;
        }
        int n = 0;
        for (BlockPos blockPos : BlockPos.func_177980_a((BlockPos)new BlockPos(473, 31, 823), (BlockPos)new BlockPos(648, 188, 648))) {
            if (this.mc.field_71441_e == null || !this.isEnabled()) break;
            IBlockState iBlockState = this.mc.field_71441_e.func_180495_p(blockPos);
            if (!Client.instance.getModuleManager().getModuleByClass(SapphireMiningPit.class).isEnabled()) {
                this.blockPoss.clear();
                return;
            }
            ++n;
            try {
                if (!this.mc.field_71441_e.func_175667_e(blockPos) || iBlockState.func_177230_c() != Blocks.field_150334_T) continue;
                IBlockState iBlockState2 = this.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(6));
                IBlockState iBlockState3 = this.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2));
                IBlockState iBlockState4 = this.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(1));
                IBlockState iBlockState5 = this.mc.field_71441_e.func_180495_p(blockPos.func_177979_c(1));
                IBlockState iBlockState6 = this.mc.field_71441_e.func_180495_p(blockPos.func_177979_c(2));
                if (iBlockState2.func_177230_c() == Blocks.field_150333_U && iBlockState3.func_177230_c() == Blocks.field_150334_T && iBlockState4.func_177230_c() == Blocks.field_150333_U && iBlockState6.func_177230_c() == Blocks.field_150390_bg && iBlockState5.func_177230_c() == Blocks.field_150334_T) {
                    if (((Boolean)this.debug.getValue()).booleanValue()) {
                        Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.GREEN) + "TRUE");
                    }
                    if (this.blockPoss.contains(blockPos)) continue;
                    Helper.sendMessage("[SapphirePitESP] Sapphire Pit Found.");
                    Helper.sendMessage("[SapphirePitESP] Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.func_177958_n() + ", " + blockPos.func_177956_o() + ", " + blockPos.func_177952_p());
                    NotificationPublisher.queue("Sapphire Pit ESP", "Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.func_177958_n() + ", " + blockPos.func_177956_o() + ", " + blockPos.func_177952_p(), NotificationType.INFO, 7500);
                    this.blockPoss.add(blockPos);
                    continue;
                }
                if (!((Boolean)this.debug.getValue()).booleanValue()) continue;
                Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.RED) + "FALSE");
                Helper.sendMessage(iBlockState6.func_177230_c().getRegistryName());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (this.blockPoss.isEmpty() && ((Boolean)this.debug.getValue()).booleanValue()) {
            Helper.sendMessage("[SapphirePitESP] Scanned " + n + " Blocks.");
            Helper.sendMessage("[SapphirePitESP] " + this.blockPoss.size() + " Coords Possible.");
        }
    }

    private void updateBlocks3() {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return;
        }
        int n = 0;
        for (BlockPos blockPos : BlockPos.func_177980_a((BlockPos)new BlockPos(648, 31, 648), (BlockPos)new BlockPos(823, 188, 473))) {
            if (this.mc.field_71441_e == null || !this.isEnabled()) break;
            IBlockState iBlockState = this.mc.field_71441_e.func_180495_p(blockPos);
            if (!Client.instance.getModuleManager().getModuleByClass(SapphireMiningPit.class).isEnabled()) {
                this.blockPoss.clear();
                return;
            }
            ++n;
            try {
                if (!this.mc.field_71441_e.func_175667_e(blockPos) || iBlockState.func_177230_c() != Blocks.field_150334_T) continue;
                IBlockState iBlockState2 = this.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(6));
                IBlockState iBlockState3 = this.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(2));
                IBlockState iBlockState4 = this.mc.field_71441_e.func_180495_p(blockPos.func_177981_b(1));
                IBlockState iBlockState5 = this.mc.field_71441_e.func_180495_p(blockPos.func_177979_c(1));
                IBlockState iBlockState6 = this.mc.field_71441_e.func_180495_p(blockPos.func_177979_c(2));
                if (iBlockState2.func_177230_c() == Blocks.field_150333_U && iBlockState3.func_177230_c() == Blocks.field_150334_T && iBlockState4.func_177230_c() == Blocks.field_150333_U && iBlockState6.func_177230_c() == Blocks.field_150390_bg && iBlockState5.func_177230_c() == Blocks.field_150334_T) {
                    if (((Boolean)this.debug.getValue()).booleanValue()) {
                        Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.GREEN) + "TRUE");
                    }
                    if (this.blockPoss.contains(blockPos)) continue;
                    Helper.sendMessage("[SapphirePitESP] Sapphire Pit Found.");
                    Helper.sendMessage("[SapphirePitESP] Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.func_177958_n() + ", " + blockPos.func_177956_o() + ", " + blockPos.func_177952_p());
                    NotificationPublisher.queue("Sapphire Pit ESP", "Coords: " + (Object)((Object)EnumChatFormatting.GREEN) + blockPos.func_177958_n() + ", " + blockPos.func_177956_o() + ", " + blockPos.func_177952_p(), NotificationType.INFO, 7500);
                    this.blockPoss.add(blockPos);
                    continue;
                }
                if (!((Boolean)this.debug.getValue()).booleanValue()) continue;
                Helper.sendMessage(blockPos + " " + (Object)((Object)EnumChatFormatting.RED) + "FALSE");
                Helper.sendMessage(iBlockState6.func_177230_c().getRegistryName());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (this.blockPoss.isEmpty() && ((Boolean)this.debug.getValue()).booleanValue()) {
            Helper.sendMessage("[SapphirePitESP] Scanned " + n + " Blocks.");
            Helper.sendMessage("[SapphirePitESP] " + this.blockPoss.size() + " Coords Possible.");
        }
    }

    private void trace(BlockPos blockPos, int n, double d, double d2, double d3) {
        GL11.glEnable(2848);
        RenderUtil.setColor(n);
        GL11.glLineWidth(3.0f);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, this.mc.field_71439_g.func_70047_e(), 0.0);
        GL11.glVertex3d(d, d2, d3);
        GL11.glEnd();
        GL11.glDisable(2848);
    }
}

