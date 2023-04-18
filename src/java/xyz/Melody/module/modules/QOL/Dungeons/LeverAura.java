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
    private void destoryBlock(EventTick eventTick) {
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
    public void onTick(EventRender3D eventRender3D) {
        for (BlockPos blockPos : allLevers) {
            RenderUtil.drawSolidBlockESP(blockPos, Colors.RED.c, eventRender3D.getPartialTicks());
        }
    }

    @EventHandler
    private void onWorldLoad(EventTick eventTick) {
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            blockPos = null;
            clicked.clear();
            allLevers.clear();
        }
    }

    private BlockPos getLever() {
        float f = ((Double)this.range.getValue()).floatValue();
        if (this.mc.thePlayer == null || this.mc.theWorld == null) {
            return null;
        }
        BlockPos blockPos = this.mc.thePlayer.getPosition();
        blockPos = blockPos.add(0, 1, 0);
        Vec3i vec3i = new Vec3i(f, f, f);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        if (blockPos != null) {
            allLevers.clear();
            for (BlockPos blockPos2 : BlockPos.getAllInBox(blockPos.add(vec3i), blockPos.subtract(vec3i))) {
                IBlockState iBlockState = this.mc.theWorld.getBlockState(blockPos2);
                if (!(iBlockState.getBlock() instanceof BlockLever)) continue;
                if (clicked.contains(blockPos2) && ((Boolean)this.clickedCheck.getValue()).booleanValue()) continue;
                BlockLever blockLever = (BlockLever)iBlockState.getBlock();
                if (Boolean.valueOf(iBlockState.getValue(BlockLever.POWERED)).booleanValue() && ((Boolean)this.poweredCheck.getValue()).booleanValue()) continue;
                allLevers.add(blockPos2);
                arrayList.add(new Vec3((double)blockPos2.getX() + 0.5, blockPos2.getY(), (double)blockPos2.getZ() + 0.5));
            }
        }
        arrayList.sort(Comparator.comparingDouble(vec3 -> this.mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord)));
        if (!arrayList.isEmpty()) {
            return new BlockPos((Vec3)arrayList.get(0));
        }
        return null;
    }

    static {
        allLevers = new ArrayList();
        clicked = new ArrayList();
    }
}

