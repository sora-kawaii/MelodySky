package xyz.Melody.module.modules.QOL.Dungeons.Devices;

import java.util.Iterator;
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
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AutoShootTheTarget extends Module {
   private BlockPos plate = new BlockPos(63, 127, 35);
   private TimerUtil timer = new TimerUtil();
   private BlockPos eme = null;
   private int curPlateState = 0;
   private boolean rotated = false;
   private Numbers delay = new Numbers("Delay", 150.0, 100.0, 500.0, 10.0);
   int ticks = 0;

   public AutoShootTheTarget() {
      super("AutoShootTheTarget", ModuleType.Dungeons);
      this.setModInfo("Auto Do Shoot The Target Device.");
      this.addValues(new Value[]{this.delay});
   }

   @EventHandler
   private void onBlockChange(BlockChangeEvent event) {
      if (Client.inDungeons) {
         BlockPos newPos = event.getPosition();
         Block newBlock = event.getNewBlock().getBlock();
         this.curPlateState = this.getPlateState();
         if (this.curPlateState == 1 && newBlock == Blocks.emerald_block) {
            this.eme = newPos;
            this.timer.reset();
            this.rotated = false;
         }

      }
   }

   @EventHandler
   private void tick(EventTick event) {
      if (this.ticks < 2) {
         ++this.ticks;
      } else if (Client.inDungeons) {
         this.curPlateState = this.getPlateState();
         if (this.curPlateState == 1) {
            if (this.eme == null) {
               return;
            }

            for(int i = 0; i < 8; ++i) {
               ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
               if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemBow) {
                  this.mc.thePlayer.inventory.currentItem = i;
                  break;
               }
            }

            float[] rots = this.getRotations(this.eme, EnumFacing.UP);
            if (rots.length == 2) {
               this.mc.thePlayer.rotationYaw = rots[0];
               this.mc.thePlayer.rotationPitch = rots[1] - 2.3F;
               this.rotated = true;
               if (this.timer.hasReached((double)((Double)this.delay.getValue()).intValue())) {
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
   }

   public float[] getRotations(BlockPos block, EnumFacing face) {
      double x = (double)block.getX() + 0.5 - this.mc.thePlayer.posX + (double)face.getFrontOffsetX() / 2.0;
      double z = (double)block.getZ() + 0.5 - this.mc.thePlayer.posZ + (double)face.getFrontOffsetZ() / 2.0;
      double d1 = this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight() - ((double)block.getY() + 0.5);
      double d3 = (double)MathHelper.sqrt_double(x * x + z * z);
      float yaw = (float)(Math.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
      float pitch = (float)(Math.atan2(d1, d3) * 180.0 / Math.PI);
      if (yaw < 0.0F) {
         yaw += 360.0F;
      }

      return new float[]{yaw, pitch};
   }

   private int getPlateState() {
      int r = 2;
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         BlockPos playerPos = this.mc.thePlayer.getPosition();
         playerPos = playerPos.add(0, 1, 0);
         Vec3i vec3i = new Vec3i(r, r, r);
         if (playerPos != null) {
            Iterator var4 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

            while(var4.hasNext()) {
               BlockPos blockPos = (BlockPos)var4.next();
               IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
               if (blockState.getBlock() instanceof BlockPressurePlateWeighted) {
                  if (blockPos == this.plate) {
                     Helper.sendMessage(blockState.getValue(BlockPressurePlateWeighted.POWER));
                  }

                  return (Integer)blockState.getValue(BlockPressurePlateWeighted.POWER);
               }
            }
         }

         return 0;
      } else {
         return 0;
      }
   }

   private BlockPos getEmeraldBlock() {
      int r = 15;
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         BlockPos playerPos = this.mc.thePlayer.getPosition();
         playerPos = playerPos.add(0, 1, 0);
         Vec3i vec3i = new Vec3i(8, 4, r);
         if (playerPos != null) {
            Iterator var4 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

            while(var4.hasNext()) {
               BlockPos blockPos = (BlockPos)var4.next();
               IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
               if (blockState.getBlock() == Blocks.emerald_block) {
                  return blockPos;
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }
}
