/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.Player.EventPreUpdate;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class GoldNuker
extends Module {
    private BlockPos blockPos;
    private BlockPos lastBlockPos = null;
    private Numbers<Double> range = new Numbers<Double>("Range", 5.0, 1.0, 6.0, 0.1);
    private Option<Boolean> rot = new Option<Boolean>("Rotation", false);
    private float currentDamage = 0.0f;
    private int blockHitDelay = 0;

    public GoldNuker() {
        super("GoldNuker", new String[]{"am"}, ModuleType.Macros);
        this.addValues(this.range, this.rot);
        this.setModInfo("Auto Mine Gold Block Around You.");
    }

    @EventHandler
    private void destoryBlock(EventPreUpdate eventPreUpdate) {
        IBlockState iBlockState;
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (Client.pickaxeAbilityReady && this.mc.playerController != null && this.mc.thePlayer.inventory.getStackInSlot(this.mc.thePlayer.inventory.currentItem) != null) {
            this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getStackInSlot(this.mc.thePlayer.inventory.currentItem));
            Client.pickaxeAbilityReady = false;
        }
        if (this.currentDamage > 100.0f) {
            this.currentDamage = 0.0f;
        }
        if (this.blockPos != null && this.mc.theWorld != null && ((iBlockState = this.mc.theWorld.getBlockState(this.blockPos)).getBlock() == Blocks.bedrock || iBlockState.getBlock() == Blocks.air)) {
            this.currentDamage = 0.0f;
        }
        if (this.currentDamage == 0.0f) {
            this.lastBlockPos = this.blockPos;
            this.blockPos = this.getBlock();
        }
        if (this.blockPos != null) {
            if (this.blockHitDelay > 0) {
                --this.blockHitDelay;
                return;
            }
            if (this.currentDamage == 0.0f) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.blockPos, EnumFacing.DOWN));
            }
            this.mc.thePlayer.swingItem();
            this.currentDamage += 1.0f;
        }
        if (((Boolean)this.rot.getValue()).booleanValue()) {
            float f = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos))[0];
            float f2 = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos))[1];
            this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, f, 70.0f);
            eventPreUpdate.setYaw(this.mc.thePlayer.rotationYaw);
            this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, f2, 70.0f);
            eventPreUpdate.setPitch(this.mc.thePlayer.rotationPitch);
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.currentDamage = 0.0f;
        super.onDisable();
    }

    @EventHandler
    public void onTick(EventRender3D eventRender3D) {
        if (this.getBlock() != null) {
            RenderUtil.drawSolidBlockESP(this.getBlock(), new Color(198, 139, 255, 190).getRGB(), eventRender3D.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load load) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    public float[] getRotations(BlockPos blockPos, EnumFacing enumFacing) {
        double d = (double)blockPos.getX() + 0.5 - this.mc.thePlayer.posX + (double)enumFacing.getFrontOffsetX() / 2.0;
        double d2 = (double)blockPos.getZ() + 0.5 - this.mc.thePlayer.posZ + (double)enumFacing.getFrontOffsetZ() / 2.0;
        double d3 = this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight() - ((double)blockPos.getY() + 0.5);
        double d4 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f = (float)(Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(Math.atan2(d3, d4) * 180.0 / Math.PI);
        if (f < 0.0f) {
            f += 360.0f;
        }
        return new float[]{f, f2};
    }

    private EnumFacing getClosestEnum(BlockPos blockPos) {
        EnumFacing enumFacing = EnumFacing.UP;
        float f = MathHelper.wrapAngleTo180_float(this.getRotations(blockPos, EnumFacing.UP)[0]);
        if (f >= 45.0f && f <= 135.0f) {
            enumFacing = EnumFacing.EAST;
        } else if (f >= 135.0f && f <= 180.0f || f <= -135.0f && f >= -180.0f) {
            enumFacing = EnumFacing.SOUTH;
        } else if (f <= -45.0f && f >= -135.0f) {
            enumFacing = EnumFacing.WEST;
        } else if (f >= -45.0f && f <= 0.0f || f <= 45.0f && f >= 0.0f) {
            enumFacing = EnumFacing.NORTH;
        }
        if (MathHelper.wrapAngleTo180_float(this.getRotations(blockPos, EnumFacing.UP)[1]) > 75.0f || MathHelper.wrapAngleTo180_float(this.getRotations(blockPos, EnumFacing.UP)[1]) < -75.0f) {
            enumFacing = EnumFacing.UP;
        }
        return enumFacing;
    }

    private BlockPos getBlock() {
        int n = ((Double)this.range.getValue()).intValue();
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return null;
        }
        BlockPos blockPos = this.mc.thePlayer.getPosition();
        blockPos = blockPos.add(0, 1, 0);
        Vec3i vec3i = new Vec3i(n, n, n);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        if (blockPos != null) {
            for (BlockPos blockPos2 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos2);
                if (!this.isGold(iBlockState)) continue;
                arrayList.add(new Vec3((double)blockPos2.getX() + 0.5, blockPos2.getY(), (double)blockPos2.getZ() + 0.5));
            }
        }
        arrayList.sort(Comparator.comparingDouble(vec3 -> this.mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord)));
        if (!arrayList.isEmpty()) {
            return new BlockPos(((Vec3)arrayList.get((int)0)).xCoord, ((Vec3)arrayList.get((int)0)).yCoord, ((Vec3)arrayList.get((int)0)).zCoord);
        }
        return null;
    }

    private boolean isGold(IBlockState iBlockState) {
        return iBlockState.getBlock() == Blocks.gold_block;
    }

    private float smoothRotation(float f, float f2, float f3) {
        float f4 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f4 > f3) {
            f4 = f3;
        }
        if (f4 < -f3) {
            f4 = -f3;
        }
        return f + f4 / 2.0f;
    }
}

