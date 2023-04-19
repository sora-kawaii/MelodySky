/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons;

import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.block.BlockLever;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class LeverAura
extends Module {
    public static BlockPos blockPos;
    private TimerUtil timer = new TimerUtil();
    private Option<Boolean> dungeon = new Option<Boolean>("DungeonOnly", true);
    private Option<Boolean> bossEntry = new Option<Boolean>("BossOnly", true);
    private Option<Boolean> clickedCheck = new Option<Boolean>("ClickedCheck", false);
    private Option<Boolean> poweredCheck = new Option<Boolean>("PoweredCheck", true);
    private Numbers<Double> delay = new Numbers<Double>("Delay", 50.0, 10.0, 200.0, 1.0);
    private Numbers<Double> range = new Numbers<Double>("Range", 4.0, 0.0, 4.5, 0.1);
    public static ArrayList<BlockPos> allLevers;
    public static ArrayList<BlockPos> clicked;

    public LeverAura() {
        super("LeverAura", new String[]{"la"}, ModuleType.Dungeons);
        this.addValues(this.dungeon, this.bossEntry, this.clickedCheck, this.poweredCheck, this.range, this.delay);
        this.setModInfo("Auto Pull Levers Around You.");
    }

    @EventHandler
    private void destoryBlock(EventTick event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return;
        }
        if (((Boolean)this.dungeon.getValue()).booleanValue() && !Client.inDungeons) {
            return;
        }
        if (!Client.instance.dungeonUtils.inBoss && ((Boolean)this.bossEntry.getValue()).booleanValue()) {
            return;
        }
        if (blockPos == null) {
            blockPos = this.getLever();
            this.timer.reset();
            return;
        }
        if (blockPos != null && this.timer.hasReached((Double)this.delay.getValue())) {
            clicked.add(blockPos);
            this.mc.thePlayer.swingItem();
            this.mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(blockPos, this.getClosestEnum(blockPos).getIndex(), this.mc.thePlayer.getCurrentEquippedItem(), blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            blockPos = null;
            this.timer.reset();
        }
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

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void onDisable() {
        blockPos = null;
        clicked.clear();
        allLevers.clear();
        super.onDisable();
    }

    @EventHandler
    public void onTick(EventRender3D event) {
        for (BlockPos pos : allLevers) {
            RenderUtil.drawSolidBlockESP(pos, Colors.RED.c, event.getPartialTicks());
        }
    }

    @EventHandler
    private void onWorldLoad(EventTick event) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            blockPos = null;
            clicked.clear();
            allLevers.clear();
        }
    }

    private BlockPos getLever() {
        float r = ((Double)this.range.getValue()).floatValue();
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return null;
        }
        BlockPos playerPos = this.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> levers = new ArrayList<Vec3>();
        if (playerPos != null) {
            allLevers.clear();
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
                if (!(blockState.getBlock() instanceof BlockLever)) continue;
                if (clicked.contains(blockPos) && ((Boolean)this.clickedCheck.getValue()).booleanValue()) continue;
                BlockLever lever = (BlockLever)blockState.getBlock();
                if (Boolean.valueOf(blockState.getValue(BlockLever.POWERED)).booleanValue() && ((Boolean)this.poweredCheck.getValue()).booleanValue()) continue;
                allLevers.add(blockPos);
                levers.add(new Vec3((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5));
            }
        }
        levers.sort(Comparator.comparingDouble(this::lambda$getLever$0));
        if (!levers.isEmpty()) {
            return new BlockPos((Vec3)levers.get(0));
        }
        return null;
    }

    private double lambda$getLever$0(Vec3 vec) {
        return this.mc.thePlayer.getDistance(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    static {
        allLevers = new ArrayList();
        clicked = new ArrayList();
    }
}

