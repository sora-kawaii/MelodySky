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
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
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
            this.mc.field_71439_g.func_71038_i();
            this.mc.func_147114_u().func_147298_b().func_179290_a(new C08PacketPlayerBlockPlacement(blockPos, this.getClosestEnum(blockPos).func_176745_a(), this.mc.field_71439_g.func_71045_bC(), blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p()));
            blockPos = null;
            this.timer.reset();
        }
    }

    public float[] getRotations(BlockPos blockPos, EnumFacing enumFacing) {
        double d = (double)blockPos.func_177958_n() + 0.5 - this.mc.field_71439_g.field_70165_t + (double)enumFacing.func_82601_c() / 2.0;
        double d2 = (double)blockPos.func_177952_p() + 0.5 - this.mc.field_71439_g.field_70161_v + (double)enumFacing.func_82599_e() / 2.0;
        double d3 = this.mc.field_71439_g.field_70163_u + (double)this.mc.field_71439_g.func_70047_e() - ((double)blockPos.func_177956_o() + 0.5);
        double d4 = MathHelper.func_76133_a((double)(d * d + d2 * d2));
        float f = (float)(Math.atan2(d2, d) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(Math.atan2(d3, d4) * 180.0 / Math.PI);
        if (f < 0.0f) {
            f += 360.0f;
        }
        return new float[]{f, f2};
    }

    private EnumFacing getClosestEnum(BlockPos blockPos) {
        EnumFacing enumFacing = EnumFacing.UP;
        float f = MathHelper.func_76142_g((float)this.getRotations(blockPos, EnumFacing.UP)[0]);
        if (f >= 45.0f && f <= 135.0f) {
            enumFacing = EnumFacing.EAST;
        } else if (f >= 135.0f && f <= 180.0f || f <= -135.0f && f >= -180.0f) {
            enumFacing = EnumFacing.SOUTH;
        } else if (f <= -45.0f && f >= -135.0f) {
            enumFacing = EnumFacing.WEST;
        } else if (f >= -45.0f && f <= 0.0f || f <= 45.0f && f >= 0.0f) {
            enumFacing = EnumFacing.NORTH;
        }
        if (MathHelper.func_76142_g((float)this.getRotations(blockPos, EnumFacing.UP)[1]) > 75.0f || MathHelper.func_76142_g((float)this.getRotations(blockPos, EnumFacing.UP)[1]) < -75.0f) {
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
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            blockPos = null;
            clicked.clear();
            allLevers.clear();
        }
    }

    private BlockPos getLever() {
        float f = ((Double)this.range.getValue()).floatValue();
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return null;
        }
        BlockPos blockPos = this.mc.field_71439_g.func_180425_c();
        blockPos = blockPos.func_177982_a(0, 1, 0);
        Vec3i vec3i = new Vec3i(f, f, f);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        if (blockPos != null) {
            allLevers.clear();
            for (BlockPos blockPos2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                IBlockState iBlockState = this.mc.field_71441_e.func_180495_p(blockPos2);
                if (!(iBlockState.func_177230_c() instanceof BlockLever)) continue;
                if (clicked.contains(blockPos2) && ((Boolean)this.clickedCheck.getValue()).booleanValue()) continue;
                BlockLever blockLever = (BlockLever)iBlockState.func_177230_c();
                if (Boolean.valueOf((Boolean)iBlockState.func_177229_b(BlockLever.field_176359_b)).booleanValue() && ((Boolean)this.poweredCheck.getValue()).booleanValue()) continue;
                allLevers.add(blockPos2);
                arrayList.add(new Vec3((double)blockPos2.func_177958_n() + 0.5, blockPos2.func_177956_o(), (double)blockPos2.func_177952_p() + 0.5));
            }
        }
        arrayList.sort(Comparator.comparingDouble(vec3 -> this.mc.field_71439_g.func_70011_f(vec3.field_72450_a, vec3.field_72448_b, vec3.field_72449_c)));
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

