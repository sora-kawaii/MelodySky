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
    public void onTick(EventTick eventTick) {
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
    public void onTick(EventRender3D eventRender3D) {
        if (this.crop != null) {
            RenderUtil.drawSolidBlockESP(this.crop, Colors.MAGENTA.c, eventRender3D.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    private BlockPos closestCrop() {
        if (this.mc.theWorld == null) {
            return null;
        }
        double d = (Double)this.range.getValue();
        BlockPos blockPos = this.mc.thePlayer.getPosition();
        blockPos = blockPos.add(0, 1, 0);
        Vec3 vec3 = this.mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(d, 2.0, d);
        Vec3i vec3i2 = new Vec3i(d, 0.0, d);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        if (blockPos != null) {
            switch (((Enum)this.mode.getValue()).toString().toLowerCase()) {
                case "all": {
                    for (BlockPos object2 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                        IBlockState iBlockState = this.mc.theWorld.getBlockState(object2);
                        if (iBlockState.getBlock() != Blocks.nether_wart && iBlockState.getBlock() != Blocks.potatoes && iBlockState.getBlock() != Blocks.wheat && iBlockState.getBlock() != Blocks.carrots && iBlockState.getBlock() != Blocks.pumpkin && iBlockState.getBlock() != Blocks.melon_block && iBlockState.getBlock() != Blocks.brown_mushroom && iBlockState.getBlock() != Blocks.red_mushroom && iBlockState.getBlock() != Blocks.cocoa && iBlockState.getBlock() != Blocks.cactus && iBlockState.getBlock() != Blocks.reeds || this.broken.contains(object2)) continue;
                        arrayList.add(new Vec3((double)object2.getX() + 0.5, object2.getY(), (double)object2.getZ() + 0.5));
                    }
                    break;
                }
                case "cane": {
                    for (BlockPos blockPos2 : BlockPos.getAllInBox(blockPos.add(vec3i2), blockPos.subtract(vec3i2))) {
                        IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos2);
                        if (iBlockState.getBlock() != Blocks.reeds || this.broken.contains(blockPos2)) continue;
                        arrayList.add(new Vec3((double)blockPos2.getX() + 0.5, blockPos2.getY(), (double)blockPos2.getZ() + 0.5));
                    }
                    break;
                }
                case "cactus": {
                    for (BlockPos blockPos3 : BlockPos.getAllInBox(blockPos.add(vec3i2), blockPos.subtract(vec3i2))) {
                        IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos3);
                        if (iBlockState.getBlock() != Blocks.cactus || this.broken.contains(blockPos3)) continue;
                        arrayList.add(new Vec3((double)blockPos3.getX() + 0.5, blockPos3.getY(), (double)blockPos3.getZ() + 0.5));
                    }
                    break;
                }
                case "netherwart": {
                    for (BlockPos blockPos4 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                        IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos4);
                        if (iBlockState.getBlock() != Blocks.nether_wart || this.broken.contains(blockPos4)) continue;
                        arrayList.add(new Vec3((double)blockPos4.getX() + 0.5, blockPos4.getY(), (double)blockPos4.getZ() + 0.5));
                    }
                    break;
                }
                case "wheat": {
                    for (BlockPos blockPos5 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                        IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos5);
                        if (iBlockState.getBlock() != Blocks.wheat || this.broken.contains(blockPos5)) continue;
                        arrayList.add(new Vec3((double)blockPos5.getX() + 0.5, blockPos5.getY(), (double)blockPos5.getZ() + 0.5));
                    }
                    break;
                }
                case "carrot": {
                    for (BlockPos blockPos6 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                        IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos6);
                        if (iBlockState.getBlock() != Blocks.carrots || this.broken.contains(blockPos6)) continue;
                        arrayList.add(new Vec3((double)blockPos6.getX() + 0.5, blockPos6.getY(), (double)blockPos6.getZ() + 0.5));
                    }
                    break;
                }
                case "potato": {
                    for (BlockPos blockPos7 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                        IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos7);
                        if (iBlockState.getBlock() != Blocks.potatoes || this.broken.contains(blockPos7)) continue;
                        arrayList.add(new Vec3((double)blockPos7.getX() + 0.5, blockPos7.getY(), (double)blockPos7.getZ() + 0.5));
                    }
                    break;
                }
                case "pumpkin": {
                    for (BlockPos blockPos8 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                        IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos8);
                        if (iBlockState.getBlock() != Blocks.pumpkin || this.broken.contains(blockPos8)) continue;
                        arrayList.add(new Vec3((double)blockPos8.getX() + 0.5, blockPos8.getY(), (double)blockPos8.getZ() + 0.5));
                    }
                    break;
                }
                case "melon": {
                    for (BlockPos blockPos9 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                        IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos9);
                        if (iBlockState.getBlock() != Blocks.melon_block || this.broken.contains(blockPos9)) continue;
                        arrayList.add(new Vec3((double)blockPos9.getX() + 0.5, blockPos9.getY(), (double)blockPos9.getZ() + 0.5));
                    }
                    break;
                }
                case "mushroom": {
                    for (BlockPos blockPos10 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                        IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos10);
                        if (iBlockState.getBlock() != Blocks.brown_mushroom && iBlockState.getBlock() != Blocks.red_mushroom || this.broken.contains(blockPos10)) continue;
                        arrayList.add(new Vec3((double)blockPos10.getX() + 0.5, blockPos10.getY(), (double)blockPos10.getZ() + 0.5));
                    }
                    break;
                }
                case "cocoa": {
                    for (BlockPos blockPos11 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                        IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos11);
                        if (iBlockState.getBlock() != Blocks.cocoa || this.broken.contains(blockPos11)) continue;
                        arrayList.add(new Vec3((double)blockPos11.getX() + 0.5, blockPos11.getY(), (double)blockPos11.getZ() + 0.5));
                    }
                    break;
                }
            }
        }
        double d2 = 9999.0;
        Object object = null;
        for (Vec3 vec32 : arrayList) {
            double d3 = vec32.distanceTo(vec3);
            if (!(d3 < d2)) continue;
            d2 = d3;
            object = vec32;
        }
        if (object != null && d2 < 5.0) {
            return new BlockPos(((Vec3)object).xCoord, ((Vec3)object).yCoord, ((Vec3)object).zCoord);
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

