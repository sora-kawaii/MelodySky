/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.macros;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.block.Block;
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
import xyz.Melody.System.Managers.Client.FileManager;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class CustomNuker
extends Module {
    public String blockName = "stone";
    private BlockPos blockPos;
    private BlockPos lastBlockPos = null;
    private Numbers<Double> range = new Numbers<Double>("Range", 5.0, 1.0, 6.0, 0.1);
    private Option<Boolean> rot = new Option<Boolean>("Rotation", false);
    private float currentDamage = 0.0f;
    private int blockHitDelay = 0;

    public CustomNuker() {
        super("CustomNuker", new String[]{"cosn"}, ModuleType.Macros);
        this.addValues(this.range, this.rot);
        this.setModInfo("'.cusn help'");
        this.setEnabled(false);
    }

    @EventHandler
    private void destoryBlock(EventPreUpdate event) {
        IBlockState blockState;
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
        if (this.blockPos != null && this.mc.theWorld != null && ((blockState = this.mc.theWorld.getBlockState(this.blockPos)).getBlock() == Blocks.bedrock || blockState.getBlock() == Blocks.air)) {
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
            float yaw = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos))[0];
            float pitch = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos))[1];
            this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, yaw, 70.0f);
            event.setYaw(this.mc.thePlayer.rotationYaw);
            this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, pitch, 70.0f);
            event.setPitch(this.mc.thePlayer.rotationPitch);
        }
    }

    public void readCustomNuker() {
        List<String> sets = FileManager.read("CustomNuker.txt");
        for (String sb : sets) {
            if (sb == "" || sb == null) continue;
            this.blockName = sb;
        }
    }

    public void saveCustomNuker() {
        String enabled = this.blockName == null ? "" : this.blockName;
        FileManager.save("CustomNuker.txt", enabled, false);
    }

    @Override
    public void onEnable() {
        this.readCustomNuker();
        if (this.blockName == null || this.blockName.equals("")) {
            this.blockName = "stone";
            Helper.sendMessage("[CustomNuker] An Error Occured, Set to Default Block(stone).");
        }
        this.setModInfo("'.cusn help' | cur: " + this.blockName);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        this.currentDamage = 0.0f;
        super.onDisable();
    }

    @EventHandler
    public void onTick(EventRender3D event) {
        if (this.getBlock() != null) {
            RenderUtil.drawSolidBlockESP(this.getBlock(), new Color(198, 139, 255, 190).getRGB(), event.getPartialTicks());
        }
    }

    @SubscribeEvent
    public void clear(WorldEvent.Load event) {
        Helper.sendMessage("[MacroProtection] Auto Disabled " + (Object)((Object)EnumChatFormatting.GREEN) + this.getName() + (Object)((Object)EnumChatFormatting.GRAY) + " due to World Change.");
        this.setEnabled(false);
    }

    public float[] getRotations(BlockPos block, EnumFacing face) {
        double x = (double)block.getX() + 0.5 - this.mc.thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
        double z = (double)block.getZ() + 0.5 - this.mc.thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
        double d1 = this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight() - ((double)block.getY() + 0.5);
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI);
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        return new float[]{yaw, pitch};
    }

    private EnumFacing getClosestEnum(BlockPos pos) {
        EnumFacing closestEnum = EnumFacing.UP;
        float rotations2 = MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[0]);
        if (rotations2 >= 45.0f && rotations2 <= 135.0f) {
            closestEnum = EnumFacing.EAST;
        } else if (rotations2 >= 135.0f && rotations2 <= 180.0f || rotations2 <= -135.0f && rotations2 >= -180.0f) {
            closestEnum = EnumFacing.SOUTH;
        } else if (rotations2 <= -45.0f && rotations2 >= -135.0f) {
            closestEnum = EnumFacing.WEST;
        } else if (rotations2 >= -45.0f && rotations2 <= 0.0f || rotations2 <= 45.0f && rotations2 >= 0.0f) {
            closestEnum = EnumFacing.NORTH;
        }
        if (MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) > 75.0f || MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) < -75.0f) {
            closestEnum = EnumFacing.UP;
        }
        return closestEnum;
    }

    private BlockPos getBlock() {
        int r = ((Double)this.range.getValue()).intValue();
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return null;
        }
        BlockPos playerPos = this.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> blocks = new ArrayList<Vec3>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
                if (!this.isObs(blockState)) continue;
                blocks.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
            }
        }
        blocks.sort(Comparator.comparingDouble(this::lambda$getBlock$0));
        if (!blocks.isEmpty()) {
            return new BlockPos(((Vec3)blocks.get((int)0)).xCoord, ((Vec3)blocks.get((int)0)).yCoord, ((Vec3)blocks.get((int)0)).zCoord);
        }
        return null;
    }

    private boolean isObs(IBlockState blockState) {
        Block block = Block.getBlockFromName(this.blockName);
        return blockState.getBlock() == block;
    }

    private float smoothRotation(float current, float target, float maxIncrement) {
        float deltaAngle = MathHelper.wrapAngleTo180_float(target - current);
        if (deltaAngle > maxIncrement) {
            deltaAngle = maxIncrement;
        }
        if (deltaAngle < -maxIncrement) {
            deltaAngle = -maxIncrement;
        }
        return current + deltaAngle / 2.0f;
    }

    private double lambda$getBlock$0(Vec3 vec) {
        return this.mc.thePlayer.getDistance(vec.xCoord, vec.yCoord, vec.zCoord);
    }
}

