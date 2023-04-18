/*
 * Decompiled with CFR 0.152.
 * Deobfuscated by sora-kawaii.
 */
package xyz.Melody.Utils.game;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public final class BlockUtil {
    private static Minecraft mc = Minecraft.func_71410_x();
    public static Vec3 bp = null;

    public static Block getBlock(int n, int n2, int n3) {
        return BlockUtil.mc.field_71441_e.func_180495_p(new BlockPos(n, n2, n3)).func_177230_c();
    }

    public static Block getBlock(BlockPos blockPos) {
        return BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c();
    }

    public static ArrayList<Vec3> whereToMineBlock(BlockPos blockPos) {
        Vec3 vec3 = new Vec3((double)blockPos.func_177958_n() + 0.5, (double)blockPos.func_177956_o() + 0.5, (double)blockPos.func_177952_p() + 0.5);
        ArrayList<Vec3> arrayList = new ArrayList<Vec3>();
        MovingObjectPosition movingObjectPosition = BlockUtil.rayTrace(vec3, 4.5f);
        if (movingObjectPosition != null && movingObjectPosition.func_178782_a().equals(blockPos)) {
            arrayList.add(movingObjectPosition.field_72307_f);
        }
        for (int i = 1; i < 5; ++i) {
            for (int j = 1; j < 5; ++j) {
                for (int k = 1; k < 5; ++k) {
                    Vec3 vec32 = new Vec3((double)blockPos.func_177958_n() + (double)i / 4.0 - 0.125, (double)blockPos.func_177956_o() + (double)j / 4.0 - 0.125, (double)blockPos.func_177952_p() + (double)k / 4.0 - 0.125);
                    movingObjectPosition = BlockUtil.rayTrace(vec32, 4.5f);
                    if (movingObjectPosition == null) continue;
                    bp = movingObjectPosition.field_72307_f;
                    if (!movingObjectPosition.func_178782_a().equals(blockPos)) continue;
                    arrayList.add(movingObjectPosition.field_72307_f);
                }
            }
        }
        return arrayList;
    }

    public static MovingObjectPosition rayTrace(Vec3 vec3, float f) {
        Vec3 vec32 = BlockUtil.mc.field_71439_g.func_174824_e(1.0f);
        Vec3 vec33 = BlockUtil.getLook(vec3);
        return BlockUtil.fastRayTrace(vec32, vec32.func_72441_c(vec33.field_72450_a * (double)f, vec33.field_72448_b * (double)f, vec33.field_72449_c * (double)f));
    }

    public static Vec3 getLook(Vec3 vec3) {
        double d = vec3.field_72450_a - BlockUtil.mc.field_71439_g.field_70165_t;
        double d2 = vec3.field_72448_b - (BlockUtil.mc.field_71439_g.field_70163_u + (double)BlockUtil.mc.field_71439_g.func_70047_e());
        double d3 = vec3.field_72449_c - BlockUtil.mc.field_71439_g.field_70161_v;
        double d4 = Math.sqrt(d * d + d3 * d3);
        return BlockUtil.getVectorForRotation((float)(-(Math.atan2(d2, d4) * 180.0 / Math.PI)), (float)(Math.atan2(d3, d) * 180.0 / Math.PI - 90.0));
    }

    public static Vec3 getVectorForRotation(float f, float f2) {
        double d = -Math.cos(-f * ((float)Math.PI / 180));
        return new Vec3(Math.sin(-f2 * ((float)Math.PI / 180) - (float)Math.PI) * d, Math.sin(-f * ((float)Math.PI / 180)), Math.cos(-f2 * ((float)Math.PI / 180) - (float)Math.PI) * d);
    }

    private static MovingObjectPosition fastRayTrace(Vec3 vec3, Vec3 vec32) {
        MovingObjectPosition movingObjectPosition;
        int n;
        int n2;
        int n3 = (int)Math.floor(vec32.field_72450_a);
        int n4 = (int)Math.floor(vec32.field_72448_b);
        int n5 = (int)Math.floor(vec32.field_72449_c);
        int n6 = (int)Math.floor(vec3.field_72450_a);
        BlockPos blockPos = new BlockPos(n6, n2 = (int)Math.floor(vec3.field_72448_b), n = (int)Math.floor(vec3.field_72449_c));
        IBlockState iBlockState = BlockUtil.mc.field_71441_e.func_180495_p(blockPos);
        Block block = iBlockState.func_177230_c();
        if (block.func_176209_a(iBlockState, false) && (movingObjectPosition = block.func_180636_a(BlockUtil.mc.field_71441_e, blockPos, vec3, vec32)) != null) {
            return movingObjectPosition;
        }
        MovingObjectPosition movingObjectPosition2 = null;
        int n7 = 200;
        while (n7-- >= 0) {
            EnumFacing enumFacing;
            if (n6 == n3 && n2 == n4 && n == n5) {
                return movingObjectPosition2;
            }
            boolean bl = true;
            boolean bl2 = true;
            boolean bl3 = true;
            double d = 999.0;
            double d2 = 999.0;
            double d3 = 999.0;
            if (n3 > n6) {
                d = (double)n6 + 1.0;
            } else if (n3 < n6) {
                d = (double)n6 + 0.0;
            } else {
                bl = false;
            }
            if (n4 > n2) {
                d2 = (double)n2 + 1.0;
            } else if (n4 < n2) {
                d2 = (double)n2 + 0.0;
            } else {
                bl2 = false;
            }
            if (n5 > n) {
                d3 = (double)n + 1.0;
            } else if (n5 < n) {
                d3 = (double)n + 0.0;
            } else {
                bl3 = false;
            }
            double d4 = 999.0;
            double d5 = 999.0;
            double d6 = 999.0;
            double d7 = vec32.field_72450_a - vec3.field_72450_a;
            double d8 = vec32.field_72448_b - vec3.field_72448_b;
            double d9 = vec32.field_72449_c - vec3.field_72449_c;
            if (bl) {
                d4 = (d - vec3.field_72450_a) / d7;
            }
            if (bl2) {
                d5 = (d2 - vec3.field_72448_b) / d8;
            }
            if (bl3) {
                d6 = (d3 - vec3.field_72449_c) / d9;
            }
            if (d4 == -0.0) {
                d4 = -1.0E-4;
            }
            if (d5 == -0.0) {
                d5 = -1.0E-4;
            }
            if (d6 == -0.0) {
                d6 = -1.0E-4;
            }
            if (d4 < d5 && d4 < d6) {
                enumFacing = n3 > n6 ? EnumFacing.WEST : EnumFacing.EAST;
                vec3 = new Vec3(d, vec3.field_72448_b + d8 * d4, vec3.field_72449_c + d9 * d4);
            } else if (d5 < d6) {
                enumFacing = n4 > n2 ? EnumFacing.DOWN : EnumFacing.UP;
                vec3 = new Vec3(vec3.field_72450_a + d7 * d5, d2, vec3.field_72449_c + d9 * d5);
            } else {
                enumFacing = n5 > n ? EnumFacing.NORTH : EnumFacing.SOUTH;
                vec3 = new Vec3(vec3.field_72450_a + d7 * d6, vec3.field_72448_b + d8 * d6, d3);
            }
            n6 = MathHelper.func_76128_c((double)vec3.field_72450_a) - (enumFacing == EnumFacing.EAST ? 1 : 0);
            n2 = MathHelper.func_76128_c((double)vec3.field_72448_b) - (enumFacing == EnumFacing.UP ? 1 : 0);
            n = MathHelper.func_76128_c((double)vec3.field_72449_c) - (enumFacing == EnumFacing.SOUTH ? 1 : 0);
            blockPos = new BlockPos(n6, n2, n);
            IBlockState iBlockState2 = BlockUtil.mc.field_71441_e.func_180495_p(blockPos);
            Block block2 = iBlockState2.func_177230_c();
            if (block2.func_176209_a(iBlockState2, false)) {
                MovingObjectPosition movingObjectPosition3 = block2.func_180636_a(BlockUtil.mc.field_71441_e, blockPos, vec3, vec32);
                if (movingObjectPosition3 == null) continue;
                return movingObjectPosition3;
            }
            movingObjectPosition2 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec3, enumFacing, blockPos);
        }
        return movingObjectPosition2;
    }
}

