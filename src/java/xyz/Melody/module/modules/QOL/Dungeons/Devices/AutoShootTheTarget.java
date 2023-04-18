/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.module.modules.QOL.Dungeons.Devices;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPressurePlateWeighted;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3i;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.BlockChangeEvent;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public final class AutoShootTheTarget
extends Module {
    private BlockPos plate = new BlockPos(63, 127, 35);
    private TimerUtil timer = new TimerUtil();
    private BlockPos eme = null;
    private int curPlateState = 0;
    private boolean rotated = false;
    private Numbers<Double> delay = new Numbers<Double>("Delay", 150.0, 100.0, 500.0, 10.0);
    int ticks = 0;

    public AutoShootTheTarget() {
        super("AutoShootTheTarget", ModuleType.Dungeons);
        this.setModInfo("Auto Do Shoot The Target Device.");
        this.addValues(this.delay);
    }

    @EventHandler
    private void onBlockChange(BlockChangeEvent blockChangeEvent) {
        if (!Client.inDungeons) {
            return;
        }
        BlockPos blockPos = blockChangeEvent.getPosition();
        Block block = blockChangeEvent.getNewBlock().func_177230_c();
        this.curPlateState = this.getPlateState();
        if (this.curPlateState == 1 && block == Blocks.field_150475_bE) {
            this.eme = blockPos;
            this.timer.reset();
            this.rotated = false;
        }
    }

    @EventHandler
    private void tick(EventTick eventTick) {
        if (this.ticks < 2) {
            ++this.ticks;
            return;
        }
        if (!Client.inDungeons) {
            return;
        }
        this.curPlateState = this.getPlateState();
        if (this.curPlateState == 1) {
            float[] fArray;
            if (this.eme == null) {
                return;
            }
            for (int i = 0; i < 8; ++i) {
                ItemStack itemStack = this.mc.field_71439_g.field_71071_by.field_70462_a[i];
                if (itemStack == null || itemStack.func_77973_b() == null || !(itemStack.func_77973_b() instanceof ItemBow)) continue;
                this.mc.field_71439_g.field_71071_by.field_70461_c = i;
                break;
            }
            if ((fArray = this.getRotations(this.eme, EnumFacing.UP)).length == 2) {
                this.mc.field_71439_g.field_70177_z = fArray[0];
                this.mc.field_71439_g.field_70125_A = fArray[1] - 2.3f;
                this.rotated = true;
                if (this.timer.hasReached(((Double)this.delay.getValue()).intValue())) {
                    Client.rightClick();
                    this.rotated = false;
                    this.eme = null;
                }
            }
        }
        if (!this.rotated) {
            this.timer.reset();
        }
        this.ticks = 0;
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

    private int getPlateState() {
        int n = 2;
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return 0;
        }
        BlockPos blockPos = this.mc.field_71439_g.func_180425_c();
        blockPos = blockPos.func_177982_a(0, 1, 0);
        Vec3i vec3i = new Vec3i(n, n, n);
        if (blockPos != null) {
            for (BlockPos blockPos2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                IBlockState iBlockState = this.mc.field_71441_e.func_180495_p(blockPos2);
                if (!(iBlockState.func_177230_c() instanceof BlockPressurePlateWeighted)) continue;
                if (blockPos2 == this.plate) {
                    Helper.sendMessage(iBlockState.func_177229_b(BlockPressurePlateWeighted.field_176579_a));
                }
                return (Integer)iBlockState.func_177229_b(BlockPressurePlateWeighted.field_176579_a);
            }
        }
        return 0;
    }

    private BlockPos getEmeraldBlock() {
        int n = 15;
        if (this.mc.field_71439_g == null || this.mc.field_71441_e == null) {
            return null;
        }
        BlockPos blockPos = this.mc.field_71439_g.func_180425_c();
        blockPos = blockPos.func_177982_a(0, 1, 0);
        Vec3i vec3i = new Vec3i(8, 4, n);
        if (blockPos != null) {
            for (BlockPos blockPos2 : BlockPos.func_177980_a((BlockPos)blockPos.func_177971_a(vec3i), (BlockPos)blockPos.func_177973_b(vec3i))) {
                IBlockState iBlockState = this.mc.field_71441_e.func_180495_p(blockPos2);
                if (iBlockState.func_177230_c() != Blocks.field_150475_bE) continue;
                return blockPos2;
            }
        }
        return null;
    }
}

