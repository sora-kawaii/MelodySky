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
    private void onTick(EventTick event) {
        if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
            return;
        }
        if (!ScoreboardUtils.scoreboardLowerContains("mines of divan")) {
            return;
        }
        if (this.calculationThread == null || !this.calculationThread.isAlive()) {
            this.calculationThread = new Thread(this::lambda$onTick$0, "Calculation");
            this.calculationThread.start();
        }
    }

    @SubscribeEvent(priority=EventPriority.LOW, receiveCanceled=true)
    public void onGuiChat(ClientChatReceivedEvent e) {
        double dist;
        String msg = e.message.getUnformattedText();
        if (msg.contains("TREASURE: ") && this.dist != (dist = Double.parseDouble(msg.split("TREASURE: ")[1].split("m")[0].replaceAll("(?!\\.)\\D", "")))) {
            this.dist = dist;
        }
    }

    @EventHandler
    private void on3D(EventRender3D event) {
        for (BlockPos pos : this.possibles) {
            RenderUtil.drawSolidBlockESP(pos, Colors.GREEN.c, event.getPartialTicks());
        }
        if (this.result == null) {
            return;
        }
        RenderUtil.drawSolidBlockESP(this.result, Colors.MAGENTA.c, event.getPartialTicks());
        double posX = (double)this.result.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
        double posY = (double)this.result.getY() - this.mc.getRenderManager().viewerPosY + 0.5;
        double posZ = (double)this.result.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
        RenderUtil.startDrawing();
        this.trace(this.result, Colors.BLUE.c, posX, posY, posZ);
        RenderUtil.stopDrawing();
        for (TileEntity entity : this.mc.theWorld.loadedTileEntityList) {
            BlockPos chestPos;
            if (!(entity instanceof TileEntityChest) || entity.getPos().getY() > 88 || Math.abs(entity.getPos().getX() - this.result.getX()) >= 25 || Math.abs(entity.getPos().getZ() - this.result.getZ()) >= 25) continue;
            TileEntityChest chest = (TileEntityChest)entity;
            this.result = chestPos = chest.getPos();
            RenderUtil.drawSolidBlockESP(chestPos, Colors.RED.c, event.getPartialTicks());
            double px = (double)chestPos.getX() - this.mc.getRenderManager().viewerPosX + 0.5;
            double py = (double)chestPos.getY() - this.mc.getRenderManager().viewerPosY + 0.5;
            double pz = (double)chestPos.getZ() - this.mc.getRenderManager().viewerPosZ + 0.5;
            RenderUtil.startDrawing();
            this.trace(chestPos, Colors.RED.c, px, py, pz);
            RenderUtil.stopDrawing();
        }
    }

    private boolean isValidBlock(BlockPos pos) {
        Block block = this.mc.theWorld.getBlockState(pos).getBlock();
        boolean valid = block == Blocks.gold_block || block == Blocks.prismarine || block == Blocks.chest || block == Blocks.stained_glass || block == Blocks.stained_glass_pane || block == Blocks.wool || block == Blocks.stained_hardened_clay || block == Blocks.air;
        return valid;
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

    private Vec3 getPlayerPos() {
        return new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)(this.mc.thePlayer.getEyeHeight() - this.mc.thePlayer.getDefaultEyeHeight()), this.mc.thePlayer.posZ);
    }

    private double round(double value, int precision) {
        int scale = (int)Math.pow(10.0, precision);
        return (double)Math.round(value * (double)scale) / (double)scale;
    }

    private void lambda$onTick$0() {
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
                    int zOffset = (int)Math.floor(-this.dist);
                    while ((double)zOffset <= Math.ceil(this.dist)) {
                        for (int y = 65; y <= 75; ++y) {
                            BlockPos pos;
                            double calculatedDist = 0.0;
                            int xOffset = 0;
                            while (calculatedDist < this.dist) {
                                pos = new BlockPos(Math.floor(this.mc.thePlayer.posX) + (double)xOffset, (double)y, Math.floor(this.mc.thePlayer.posZ) + (double)zOffset);
                                calculatedDist = this.getPlayerPos().distanceTo(new Vec3(pos).addVector(0.0, 1.0, 0.0));
                                if (this.round(calculatedDist, 1) == this.dist && !this.possibles.contains(pos) && this.isValidBlock(pos) && this.mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.air) {
                                    this.possibles.add(pos);
                                }
                                ++xOffset;
                            }
                            xOffset = 0;
                            calculatedDist = 0.0;
                            while (calculatedDist < this.dist) {
                                pos = new BlockPos(Math.floor(this.mc.thePlayer.posX) - (double)xOffset, (double)y, Math.floor(this.mc.thePlayer.posZ) + (double)zOffset);
                                calculatedDist = this.getPlayerPos().distanceTo(new Vec3(pos).addVector(0.0, 1.0, 0.0));
                                if (this.round(calculatedDist, 1) == this.dist && !this.possibles.contains(pos) && this.isValidBlock(pos) && this.mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock() == Blocks.air) {
                                    this.possibles.add(pos);
                                }
                                ++xOffset;
                            }
                        }
                        ++zOffset;
                    }
                    if (this.possibles.size() == 0) {
                        this.possibles.clear();
                        this.group.clear();
                    }
                } else if (this.possibles.size() != 1) {
                    this.group.add(this.mc.thePlayer.getPosition());
                    ArrayList<BlockPos> temp = new ArrayList<BlockPos>();
                    for (BlockPos pos : this.possibles) {
                        if (this.round(this.getPlayerPos().distanceTo(new Vec3(pos).addVector(0.0, 1.0, 0.0)), 1) != this.dist) continue;
                        temp.add(pos);
                    }
                    this.possibles = temp;
                    if (this.possibles.size() == 0) {
                        this.possibles.clear();
                        this.group.clear();
                    }
                } else {
                    BlockPos pos = this.possibles.get(0);
                    Vec3 vec3 = new Vec3(pos);
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
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

