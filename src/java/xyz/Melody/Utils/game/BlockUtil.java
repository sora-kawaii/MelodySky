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
    private static Minecraft mc = Minecraft.getMinecraft();
    public static Vec3 bp = null;

    public static Block getBlock(int x2, int y2, int z2) {
        return BlockUtil.mc.theWorld.getBlockState(new BlockPos(x2, y2, z2)).getBlock();
    }

    public static Block getBlock(BlockPos pos) {
        return BlockUtil.mc.theWorld.getBlockState(pos).getBlock();
    }

    public static ArrayList<Vec3> whereToMineBlock(BlockPos pos) {
        Vec3 center = new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
        ArrayList<Vec3> vec3s = new ArrayList<Vec3>();
        MovingObjectPosition position = BlockUtil.rayTrace(center, 4.5f);
        if (position != null && position.getBlockPos().equals(pos)) {
            vec3s.add(position.hitVec);
        }
        for (int x = 1; x < 5; ++x) {
            for (int y = 1; y < 5; ++y) {
                for (int z = 1; z < 5; ++z) {
                    Vec3 vec = new Vec3((double)pos.getX() + (double)x / 4.0 - 0.125, (double)pos.getY() + (double)y / 4.0 - 0.125, (double)pos.getZ() + (double)z / 4.0 - 0.125);
                    position = BlockUtil.rayTrace(vec, 4.5f);
                    if (position == null) continue;
                    bp = position.hitVec;
                    if (!position.getBlockPos().equals(pos)) continue;
                    vec3s.add(position.hitVec);
                }
            }
        }
        return vec3s;
    }

    public static MovingObjectPosition rayTrace(Vec3 target, float range) {
        Vec3 vec3 = BlockUtil.mc.thePlayer.getPositionEyes(1.0f);
        Vec3 vec31 = BlockUtil.getLook(target);
        return BlockUtil.fastRayTrace(vec3, vec3.addVector(vec31.xCoord * (double)range, vec31.yCoord * (double)range, vec31.zCoord * (double)range));
    }

    public static Vec3 getLook(Vec3 vec) {
        double diffX = vec.xCoord - BlockUtil.mc.thePlayer.posX;
        double diffY = vec.yCoord - (BlockUtil.mc.thePlayer.posY + (double)BlockUtil.mc.thePlayer.getEyeHeight());
        double diffZ = vec.zCoord - BlockUtil.mc.thePlayer.posZ;
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        return BlockUtil.getVectorForRotation((float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI)), (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0));
    }

    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        double f2 = -Math.cos(-pitch * ((float)Math.PI / 180));
        return new Vec3(Math.sin(-yaw * ((float)Math.PI / 180) - (float)Math.PI) * f2, Math.sin(-pitch * ((float)Math.PI / 180)), Math.cos(-yaw * ((float)Math.PI / 180) - (float)Math.PI) * f2);
    }

    private static MovingObjectPosition fastRayTrace(Vec3 vec31, Vec3 vec32) {
        MovingObjectPosition movingobjectposition;
        int j1;
        int i1;
        int i = (int)Math.floor(vec32.xCoord);
        int j = (int)Math.floor(vec32.yCoord);
        int k = (int)Math.floor(vec32.zCoord);
        int n = (int)Math.floor(vec31.xCoord);
        BlockPos blockpos = new BlockPos(n, i1 = (int)Math.floor(vec31.yCoord), j1 = (int)Math.floor(vec31.zCoord));
        IBlockState iblockstate = BlockUtil.mc.theWorld.getBlockState(blockpos);
        Block block = iblockstate.getBlock();
        if (block.canCollideCheck(iblockstate, false) && (movingobjectposition = block.collisionRayTrace(BlockUtil.mc.theWorld, blockpos, vec31, vec32)) != null) {
            return movingobjectposition;
        }
        MovingObjectPosition movingobjectposition2 = null;
        int k1 = 200;
        while (k1-- >= 0) {
            EnumFacing enumfacing;
            if (n == i && i1 == j && j1 == k) {
                return movingobjectposition2;
            }
            boolean flag2 = true;
            boolean flag = true;
            boolean flag1 = true;
            double d0 = 999.0;
            double d1 = 999.0;
            double d2 = 999.0;
            if (i > n) {
                d0 = (double)n + 1.0;
            } else if (i < n) {
                d0 = (double)n + 0.0;
            } else {
                flag2 = false;
            }
            if (j > i1) {
                d1 = (double)i1 + 1.0;
            } else if (j < i1) {
                d1 = (double)i1 + 0.0;
            } else {
                flag = false;
            }
            if (k > j1) {
                d2 = (double)j1 + 1.0;
            } else if (k < j1) {
                d2 = (double)j1 + 0.0;
            } else {
                flag1 = false;
            }
            double d3 = 999.0;
            double d4 = 999.0;
            double d5 = 999.0;
            double d6 = vec32.xCoord - vec31.xCoord;
            double d7 = vec32.yCoord - vec31.yCoord;
            double d8 = vec32.zCoord - vec31.zCoord;
            if (flag2) {
                d3 = (d0 - vec31.xCoord) / d6;
            }
            if (flag) {
                d4 = (d1 - vec31.yCoord) / d7;
            }
            if (flag1) {
                d5 = (d2 - vec31.zCoord) / d8;
            }
            if (d3 == -0.0) {
                d3 = -1.0E-4;
            }
            if (d4 == -0.0) {
                d4 = -1.0E-4;
            }
            if (d5 == -0.0) {
                d5 = -1.0E-4;
            }
            if (d3 < d4 && d3 < d5) {
                enumfacing = i > n ? EnumFacing.WEST : EnumFacing.EAST;
                vec31 = new Vec3(d0, vec31.yCoord + d7 * d3, vec31.zCoord + d8 * d3);
            } else if (d4 < d5) {
                enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                vec31 = new Vec3(vec31.xCoord + d6 * d4, d1, vec31.zCoord + d8 * d4);
            } else {
                enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                vec31 = new Vec3(vec31.xCoord + d6 * d5, vec31.yCoord + d7 * d5, d2);
            }
            n = MathHelper.floor_double(vec31.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
            i1 = MathHelper.floor_double(vec31.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
            j1 = MathHelper.floor_double(vec31.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
            blockpos = new BlockPos(n, i1, j1);
            IBlockState iblockstate1 = BlockUtil.mc.theWorld.getBlockState(blockpos);
            Block block1 = iblockstate1.getBlock();
            if (block1.canCollideCheck(iblockstate1, false)) {
                MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(BlockUtil.mc.theWorld, blockpos, vec31, vec32);
                if (movingobjectposition1 == null) continue;
                return movingobjectposition1;
            }
            movingobjectposition2 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec31, enumfacing, blockpos);
        }
        return movingobjectposition2;
    }
}

