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
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public class BlockUtil {
   private static Minecraft mc = Minecraft.getMinecraft();
   public static Vec3 bp = null;

   public static Block getBlock(int x2, int y2, int z2) {
      return mc.theWorld.getBlockState(new BlockPos(x2, y2, z2)).getBlock();
   }

   public static Block getBlock(BlockPos pos) {
      return mc.theWorld.getBlockState(pos).getBlock();
   }

   public static ArrayList whereToMineBlock(BlockPos pos) {
      Vec3 center = new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5);
      ArrayList vec3s = new ArrayList();
      MovingObjectPosition position = rayTrace(center, 4.5F);
      if (position != null && position.getBlockPos().equals(pos)) {
         vec3s.add(position.hitVec);
      }

      for(int x = 1; x < 5; ++x) {
         for(int y = 1; y < 5; ++y) {
            for(int z = 1; z < 5; ++z) {
               Vec3 vec = new Vec3((double)pos.getX() + (double)x / 4.0 - 0.125, (double)pos.getY() + (double)y / 4.0 - 0.125, (double)pos.getZ() + (double)z / 4.0 - 0.125);
               position = rayTrace(vec, 4.5F);
               if (position != null) {
                  bp = position.hitVec;
                  if (position.getBlockPos().equals(pos)) {
                     vec3s.add(position.hitVec);
                  }
               }
            }
         }
      }

      return vec3s;
   }

   public static MovingObjectPosition rayTrace(Vec3 target, float range) {
      Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0F);
      Vec3 vec31 = getLook(target);
      return fastRayTrace(vec3, vec3.addVector(vec31.xCoord * (double)range, vec31.yCoord * (double)range, vec31.zCoord * (double)range));
   }

   public static Vec3 getLook(Vec3 vec) {
      double diffX = vec.xCoord - mc.thePlayer.posX;
      double diffY = vec.yCoord - (mc.thePlayer.posY + (double)mc.thePlayer.getEyeHeight());
      double diffZ = vec.zCoord - mc.thePlayer.posZ;
      double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
      return getVectorForRotation((float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI)), (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0));
   }

   public static Vec3 getVectorForRotation(float pitch, float yaw) {
      double f2 = -Math.cos((double)(-pitch * 0.017453292F));
      return new Vec3(Math.sin((double)(-yaw * 0.017453292F - 3.1415927F)) * f2, Math.sin((double)(-pitch * 0.017453292F)), Math.cos((double)(-yaw * 0.017453292F - 3.1415927F)) * f2);
   }

   private static MovingObjectPosition fastRayTrace(Vec3 vec31, Vec3 vec32) {
      int i = (int)Math.floor(vec32.xCoord);
      int j = (int)Math.floor(vec32.yCoord);
      int k = (int)Math.floor(vec32.zCoord);
      int l = (int)Math.floor(vec31.xCoord);
      int j1;
      int i1;
      BlockPos blockpos = new BlockPos(l, i1 = (int)Math.floor(vec31.yCoord), j1 = (int)Math.floor(vec31.zCoord));
      IBlockState iblockstate = mc.theWorld.getBlockState(blockpos);
      Block block = iblockstate.getBlock();
      MovingObjectPosition movingobjectposition;
      if (block.canCollideCheck(iblockstate, false) && (movingobjectposition = block.collisionRayTrace(mc.theWorld, blockpos, vec31, vec32)) != null) {
         return movingobjectposition;
      } else {
         MovingObjectPosition movingobjectposition2 = null;
         int k1 = 200;

         while(k1-- >= 0) {
            if (l == i && i1 == j && j1 == k) {
               return movingobjectposition2;
            }

            boolean flag2 = true;
            boolean flag = true;
            boolean flag1 = true;
            double d0 = 999.0;
            double d1 = 999.0;
            double d2 = 999.0;
            if (i > l) {
               d0 = (double)l + 1.0;
            } else if (i < l) {
               d0 = (double)l + 0.0;
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

            EnumFacing enumfacing;
            if (d3 < d4 && d3 < d5) {
               enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
               vec31 = new Vec3(d0, vec31.yCoord + d7 * d3, vec31.zCoord + d8 * d3);
            } else if (d4 < d5) {
               enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
               vec31 = new Vec3(vec31.xCoord + d6 * d4, d1, vec31.zCoord + d8 * d4);
            } else {
               enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
               vec31 = new Vec3(vec31.xCoord + d6 * d5, vec31.yCoord + d7 * d5, d2);
            }

            l = MathHelper.floor_double(vec31.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
            i1 = MathHelper.floor_double(vec31.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
            j1 = MathHelper.floor_double(vec31.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
            blockpos = new BlockPos(l, i1, j1);
            IBlockState iblockstate1 = mc.theWorld.getBlockState(blockpos);
            Block block1 = iblockstate1.getBlock();
            if (block1.canCollideCheck(iblockstate1, false)) {
               MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(mc.theWorld, blockpos, vec31, vec32);
               if (movingobjectposition1 != null) {
                  return movingobjectposition1;
               }
            } else {
               movingobjectposition2 = new MovingObjectPosition(MovingObjectType.MISS, vec31, enumfacing, blockpos);
            }
         }

         return movingobjectposition2;
      }
   }
}
