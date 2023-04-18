/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.ClientLib.SkyblockArea;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.ScoreboardUtils;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class DivanTreasure
extends Module {
    private TimerUtil timer = new TimerUtil();
    private String msg = null;
    private double dist = 0.0;
    private BlockPos result = null;
    private ArrayList<BlockPos> group = new ArrayList();
    private ArrayList<BlockPos> possibles = new ArrayList();
    private ArrayList<BlockPos> results = new ArrayList();
    private Thread calculationThread;

    public DivanTreasure() {
        super("DivanHelper", new String[]{"divantreasure", "divanhelper"}, ModuleType.QOL);
        this.setModInfo("Calculate Chest Position in Divan's Mine.");
    }

    @EventHandler
    private void onTick(EventTick eventTick) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (!ScoreboardUtils.scoreboardLowerContains("mines of divan")) {
            return;
        }
        if (this.calculationThread == null || !this.calculationThread.isAlive()) {
            this.calculationThread = new Thread(() -> {
                try {
                    while (true) {
                        if (!this.isEnabled()) {
                            this.possibles.clear();
                            this.group.clear();
                            break;
                        }
                        if (this.msg == null) {
                            this.msg = this.dist + "";
                        }
                        if (Double.parseDouble(this.msg) == this.dist) {
                            return;
                        }
                        this.msg = this.dist + "";
                        if (this.possibles.size() == 0) {
                            this.group.add(this.mc.thePlayer.getPosition());
                            int n = (int)Math.floor(-this.dist);
                            while ((double)n <= Math.ceil(this.dist)) {
                                for (int i = 65; i <= 75; ++i) {
                                    BlockPos blockPos;
                                    double d = 0.0;
                                    int n2 = 0;
                                    while (d < this.dist) {
                                        blockPos = new BlockPos(Math.floor(this.mc.thePlayer.posX) + (double)n2, (double)i, Math.floor(this.mc.thePlayer.posZ) + (double)n);
                                        d = this.getPlayerPos().distanceTo(new Vec3(blockPos).addVector(0.0, 1.0, 0.0));
                                        if (this.round(d, 1) == this.dist && !this.possibles.contains(blockPos) && this.isValidBlock(blockPos) && this.mc.theWorld.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.air) {
                                            this.possibles.add(blockPos);
                                        }
                                        ++n2;
                                    }
                                    n2 = 0;
                                    d = 0.0;
                                    while (d < this.dist) {
                                        blockPos = new BlockPos(Math.floor(this.mc.thePlayer.posX) - (double)n2, (double)i, Math.floor(this.mc.thePlayer.posZ) + (double)n);
                                        d = this.getPlayerPos().distanceTo(new Vec3(blockPos).addVector(0.0, 1.0, 0.0));
                                        if (this.round(d, 1) == this.dist && !this.possibles.contains(blockPos) && this.isValidBlock(blockPos) && this.mc.theWorld.getBlockState(blockPos.add(0, 1, 0)).getBlock() == Blocks.air) {
                                            this.possibles.add(blockPos);
                                        }
                                        ++n2;
                                    }
                                }
                                ++n;
                            }
                            if (this.possibles.size() == 0) {
                                this.possibles.clear();
                                this.group.clear();
                            }
                        } else if (this.possibles.size() != 1) {
                            this.group.add(this.mc.thePlayer.getPosition());
                            ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
                            for (BlockPos blockPos : this.possibles) {
                                if (this.round(this.getPlayerPos().distanceTo(new Vec3(blockPos).addVector(0.0, 1.0, 0.0)), 1) != this.dist) continue;
                                arrayList.add(blockPos);
                            }
                            this.possibles = arrayList;
                            if (this.possibles.size() == 0) {
                                this.possibles.clear();
                                this.group.clear();
                            }
                        } else {
                            BlockPos blockPos = this.possibles.get(0);
                            Vec3 vec3 = new Vec3(blockPos);
                            if (Math.abs(this.dist - this.getPlayerPos().distanceTo(vec3)) > 5.0) {
                                this.possibles.clear();
                                this.group.clear();
                            }
                        }
                        if (this.possibles.size() != 1) continue;
                        this.result = this.possibles.get(0);
                        this.possibles.clear();
                        this.group.clear();
                    }
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }, "Calculation");
            this.calculationThread.start();
        }
    }

    @SubscribeEvent(priority=EventPriority.LOW, receiveCanceled=true)
    public void onGuiChat(ClientChatReceivedEvent clientChatReceivedEvent) {
        double d;
        String string = clientChatReceivedEvent.message.getUnformattedText();
        if (string.contains("TREASURE: ") && this.dist != (d = Double.parseDouble(string.split("TREASURE: ")[1].split("m")[0].replaceAll("(?!\\.)\\D", "")))) {
            this.dist = d;
        }
    }

    @EventHandler
    private void on3D(EventRender3D eventRender3D) {
        for (BlockPos blockPos : this.possibles) {
            RenderUtil.drawSolidBlockESP(blockPos, Colors.GREEN.c, eventRender3D.getPartialTicks());
        }
        if (this.result == null) {
            return;
        }
        RenderUtil.drawSolidBlockESP(this.result, Colors.MAGENTA.c, eventRender3D.getPartialTicks());
        double d = (double)this.result.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
        double d2 = (double)this.result.getY() - this.mc.getRenderManager().viewerPosY + 0.5;
        double d3 = (double)this.result.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
        RenderUtil.startDrawing();
        this.trace(this.result, Colors.BLUE.c, d, d2, d3);
        RenderUtil.stopDrawing();
        for (TileEntity tileEntity : this.mc.theWorld.loadedTileEntityList) {
            BlockPos blockPos;
            if (!(tileEntity instanceof TileEntityChest) || tileEntity.getPos().getY() > 88 || Math.abs(tileEntity.getPos().getX() - this.result.getX()) >= 25 || Math.abs(tileEntity.getPos().getZ() - this.result.getZ()) >= 25) continue;
            TileEntityChest tileEntityChest = (TileEntityChest)tileEntity;
            this.result = blockPos = tileEntityChest.getPos();
            RenderUtil.drawSolidBlockESP(blockPos, Colors.RED.c, eventRender3D.getPartialTicks());
            double d4 = (double)blockPos.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
            double d5 = (double)blockPos.getY() - this.mc.getRenderManager().viewerPosY + 0.5;
            double d6 = (double)blockPos.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
            RenderUtil.startDrawing();
            this.trace(blockPos, Colors.RED.c, d4, d5, d6);
            RenderUtil.stopDrawing();
        }
    }

    private boolean isValidBlock(BlockPos blockPos) {
        Block block = this.mc.theWorld.getBlockState(blockPos).getBlock();
        boolean bl = block == Blocks.gold_block || block == Blocks.prismarine || block == Blocks.chest || block == Blocks.stained_glass || block == Blocks.stained_glass_pane || block == Blocks.wool || block == Blocks.stained_hardened_clay || block == Blocks.air;
        return bl;
    }

    private void trace(BlockPos blockPos, int n, double d, double d2, double d3) {
        GL11.glEnable(2848);
        RenderUtil.setColor(n);
        GL11.glLineWidth(3.0f);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0, this.mc.thePlayer.getEyeHeight(), 0.0);
        GL11.glVertex3d(d, d2, d3);
        GL11.glEnd();
        GL11.glDisable(2848);
    }

    private Vec3 getPlayerPos() {
        return new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)(this.mc.thePlayer.getEyeHeight() - this.mc.thePlayer.getDefaultEyeHeight()), this.mc.thePlayer.posZ);
    }

    private double round(double d, int n) {
        int n2 = (int)Math.pow(10.0, n);
        return (double)Math.round(d * (double)n2) / (double)n2;
    }
}

