package xyz.Melody.module.modules.macros;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
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
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventPreUpdate;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class GoldNuker extends Module {
   private BlockPos blockPos;
   private BlockPos lastBlockPos = null;
   private Option rot = new Option("Rotation", false);
   private float currentDamage = 0.0F;
   private int blockHitDelay = 0;

   public GoldNuker() {
      super("GoldNuker", new String[]{"am"}, ModuleType.Macros);
      this.addValues(new Value[]{this.rot});
      this.setModInfo("Auto Mine Gold Block Around You.");
   }

   @EventHandler
   private void destoryBlock(EventPreUpdate event) {
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         if (Client.pickaxeAbilityReady && this.mc.playerController != null && this.mc.thePlayer.inventory.getStackInSlot(this.mc.thePlayer.inventory.currentItem) != null) {
            this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getStackInSlot(this.mc.thePlayer.inventory.currentItem));
            Client.pickaxeAbilityReady = false;
         }

         if (this.currentDamage > 100.0F) {
            this.currentDamage = 0.0F;
         }

         if (this.blockPos != null && this.mc.theWorld != null) {
            IBlockState blockState = this.mc.theWorld.getBlockState(this.blockPos);
            if (blockState.getBlock() == Blocks.bedrock || blockState.getBlock() == Blocks.air) {
               this.currentDamage = 0.0F;
            }
         }

         if (this.currentDamage == 0.0F) {
            this.lastBlockPos = this.blockPos;
            this.blockPos = this.getBlock();
         }

         if (this.blockPos != null) {
            if (this.blockHitDelay > 0) {
               --this.blockHitDelay;
               return;
            }

            if (this.currentDamage == 0.0F) {
               this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, this.blockPos, EnumFacing.DOWN));
            }

            this.mc.thePlayer.swingItem();
            ++this.currentDamage;
         }

         if ((Boolean)this.rot.getValue()) {
            float yaw = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos))[0];
            float pitch = this.getRotations(this.blockPos, this.getClosestEnum(this.blockPos))[1];
            event.setYaw(this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, yaw, 70.0F));
            event.setPitch(this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, pitch, 70.0F));
         }

      }
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      this.currentDamage = 0.0F;
      super.onDisable();
   }

   @EventHandler
   public void onTick(EventRender3D event) {
      if (this.getBlock() != null) {
         RenderUtil.drawSolidBlockESP(this.getBlock(), (new Color(198, 139, 255, 190)).getRGB(), event.getPartialTicks());
      }

   }

   @SubscribeEvent
   public void clear(WorldEvent.Load event) {
      Helper.sendMessage("[MacroProtection] Auto Disabled " + EnumChatFormatting.GREEN + this.getName() + EnumChatFormatting.GRAY + " due to World Change.");
      this.setEnabled(false);
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

   private EnumFacing getClosestEnum(BlockPos pos) {
      EnumFacing closestEnum = EnumFacing.UP;
      float rotations = MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[0]);
      if (rotations >= 45.0F && rotations <= 135.0F) {
         closestEnum = EnumFacing.EAST;
      } else if ((!(rotations >= 135.0F) || !(rotations <= 180.0F)) && (!(rotations <= -135.0F) || !(rotations >= -180.0F))) {
         if (rotations <= -45.0F && rotations >= -135.0F) {
            closestEnum = EnumFacing.WEST;
         } else if (rotations >= -45.0F && rotations <= 0.0F || rotations <= 45.0F && rotations >= 0.0F) {
            closestEnum = EnumFacing.NORTH;
         }
      } else {
         closestEnum = EnumFacing.SOUTH;
      }

      if (MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) > 75.0F || MathHelper.wrapAngleTo180_float(this.getRotations(pos, EnumFacing.UP)[1]) < -75.0F) {
         closestEnum = EnumFacing.UP;
      }

      return closestEnum;
   }

   private BlockPos getBlock() {
      int r = 6;
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         BlockPos playerPos = this.mc.thePlayer.getPosition();
         playerPos = playerPos.add(0, 1, 0);
         Vec3 playerVec = this.mc.thePlayer.getPositionVector();
         Vec3i vec3i = new Vec3i(r, r, r);
         ArrayList chests = new ArrayList();
         if (playerPos != null) {
            Iterator var6 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

            while(var6.hasNext()) {
               BlockPos blockPos = (BlockPos)var6.next();
               IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
               if (this.isGold(blockState)) {
                  chests.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
               }
            }
         }

         double smallest = 9999.0;
         Vec3 closest = null;

         for(int i = 0; i < chests.size(); ++i) {
            double dist = ((Vec3)chests.get(i)).distanceTo(playerVec);
            if (dist < smallest) {
               smallest = dist;
               closest = (Vec3)chests.get(i);
            }
         }

         if (closest != null && smallest < 5.0) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private boolean isGold(IBlockState blockState) {
      return blockState.getBlock() == Blocks.gold_block;
   }

   private float smoothRotation(float current, float target, float maxIncrement) {
      float deltaAngle = MathHelper.wrapAngleTo180_float(target - current);
      if (deltaAngle > maxIncrement) {
         deltaAngle = maxIncrement;
      }

      if (deltaAngle < -maxIncrement) {
         deltaAngle = -maxIncrement;
      }

      return current + deltaAngle / 2.0F;
   }
}
