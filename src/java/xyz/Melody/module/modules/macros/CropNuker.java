/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.util.ArrayList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class CropNuker
extends Module {
    private BlockPos crop = null;
    private final ArrayList<BlockPos> broken = new ArrayList();
    private TimerUtil timer = new TimerUtil();
    private Mode<Enum> mode = new Mode("Mode", (Enum[])crops.values(), (Enum)crops.Cane);
    private Numbers<Double> range = new Numbers<Double>("Range", 5.0, 1.0, 6.0, 0.1);

    public CropNuker() {
        super("CropNuker", new String[]{"gn"}, ModuleType.Macros);
        this.addValues(this.mode, this.range);
        this.setModInfo("Auto Break Crops Around You.");
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.crop = null;
        this.broken.clear();
        super.onDisable();
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (this.mc.thePlayer == null) {
            this.broken.clear();
            return;
        }
        if (this.timer.hasReached(30000.0)) {
            this.broken.clear();
            this.timer.reset();
        }
        this.crop = this.closestCrop();
        if (this.crop != null) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.crop, EnumFacing.DOWN));
            this.mc.thePlayer.swingItem();
            this.broken.add(this.crop);
        }
    }

    @EventHandler
    public void onTick(EventRender3D event) {
        if (this.crop != null) {
            RenderUtil.drawSolidBlockESP(this.crop, Colors.MAGENTA.c, event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    private BlockPos closestCrop() {
        if (this.mc.theWorld == null) {
            return null;
        }
        double r = (Double)this.range.getValue();
        BlockPos playerPos = this.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = this.mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, 2.0, r);
        Vec3i vec3iCane = new Vec3i(r, 0.0, r);
        ArrayList<Vec3> warts = new ArrayList<Vec3>();
        if (playerPos != null) {
            switch (((Enum)this.mode.getValue()).toString().toLowerCase()) {
                case "all": {
                    IBlockState blockState;
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() != Blocks.nether_wart && blockState.getBlock() != Blocks.potatoes && blockState.getBlock() != Blocks.wheat && blockState.getBlock() != Blocks.carrots && blockState.getBlock() != Blocks.pumpkin && blockState.getBlock() != Blocks.melon_block && blockState.getBlock() != Blocks.brown_mushroom && blockState.getBlock() != Blocks.red_mushroom && blockState.getBlock() != Blocks.cocoa && blockState.getBlock() != Blocks.cactus && blockState.getBlock() != Blocks.reeds || this.broken.contains(blockPos)) continue;
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
                    }
                    break;
                }
                case "cane": {
                    IBlockState blockState;
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3iCane), playerPos.subtract(vec3iCane))) {
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() != Blocks.reeds || this.broken.contains(blockPos)) continue;
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
                    }
                    break;
                }
                case "cactus": {
                    IBlockState blockState;
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3iCane), playerPos.subtract(vec3iCane))) {
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() != Blocks.cactus || this.broken.contains(blockPos)) continue;
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
                    }
                    break;
                }
                case "netherwart": {
                    IBlockState blockState;
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() != Blocks.nether_wart || this.broken.contains(blockPos)) continue;
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
                    }
                    break;
                }
                case "wheat": {
                    IBlockState blockState;
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() != Blocks.wheat || this.broken.contains(blockPos)) continue;
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
                    }
                    break;
                }
                case "carrot": {
                    IBlockState blockState;
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() != Blocks.carrots || this.broken.contains(blockPos)) continue;
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
                    }
                    break;
                }
                case "potato": {
                    IBlockState blockState;
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() != Blocks.potatoes || this.broken.contains(blockPos)) continue;
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
                    }
                    break;
                }
                case "pumpkin": {
                    IBlockState blockState;
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() != Blocks.pumpkin || this.broken.contains(blockPos)) continue;
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
                    }
                    break;
                }
                case "melon": {
                    IBlockState blockState;
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() != Blocks.melon_block || this.broken.contains(blockPos)) continue;
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
                    }
                    break;
                }
                case "mushroom": {
                    IBlockState blockState;
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() != Blocks.brown_mushroom && blockState.getBlock() != Blocks.red_mushroom || this.broken.contains(blockPos)) continue;
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
                    }
                    break;
                }
                case "cocoa": {
                    IBlockState blockState;
                    for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                        if (blockState.getBlock() != Blocks.cocoa || this.broken.contains(blockPos)) continue;
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
                    }
                    break;
                }
            }
        }
        double smallest = 9999.0;
        Vec3 closest = null;
        for (Vec3 wart : warts) {
            double dist = wart.distanceTo(playerVec);
            if (!(dist < smallest)) continue;
            smallest = dist;
            closest = wart;
        }
        if (closest != null && smallest < 5.0) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }

    private static enum crops {
        ALL,
        Cane,
        Cactus,
        NetherWart,
        Wheat,
        Carrot,
        Potato,
        Pumpkin,
        Melon,
        Mushroom,
        Cocoa;

    }
}

