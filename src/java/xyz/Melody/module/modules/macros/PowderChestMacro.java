package xyz.Melody.module.modules.macros;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import xyz.Melody.Client;
import xyz.Melody.SkyblockArea;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.events.world.EventPreUpdate;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class PowderChestMacro extends Module {
   public static Vec3 nextRotation = null;
   public static ArrayList done = new ArrayList();
   public static Option autoClear = new Option("AutoClear", true);
   public Option silent = new Option("Silent", false);
   private Numbers velocity = new Numbers("Speed", 50.0, 30.0, 100.0, 1.0);
   public static Vec3 chest;
   public static BlockPos chestPos;
   private float silentYaw;
   private float silentPitch;

   public PowderChestMacro() {
      super("PowderChest", new String[]{"chest"}, ModuleType.Macros);
      this.addValues(new Value[]{autoClear, this.silent, this.velocity});
      this.setModInfo("Auto Unlock Powder Chests.");
   }

   @EventHandler
   private void onUpdate(EventPreUpdate event) {
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         if (Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
            chest = this.getChest();
            chestPos = this.getChestPos();
         }
      }
   }

   @EventHandler
   private void onRotation(EventPreUpdate event) {
      if (Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
         if (chest != null && nextRotation != null) {
            float yaw = this.vec3ToRotation(nextRotation).yaw;
            float pitch = this.vec3ToRotation(nextRotation).pitch;
            float speed = ((Double)this.velocity.getValue()).floatValue();
            if ((Boolean)this.silent.getValue()) {
               event.setYaw(this.silentYaw = this.smoothRotation(this.silentYaw, yaw, speed));
               event.setPitch(this.silentPitch = this.smoothRotation(this.silentPitch, pitch, speed));
            } else {
               this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, yaw, speed);
               this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, pitch, speed);
            }
         } else {
            this.silentYaw = this.mc.thePlayer.rotationYaw;
            this.silentPitch = this.mc.thePlayer.rotationPitch;
         }

      }
   }

   @EventHandler
   public void receivePacket(EventPacketRecieve event) {
      if (Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
         if (event.getPacket() instanceof S2APacketParticles) {
            S2APacketParticles packet = (S2APacketParticles)event.getPacket();
            if (packet.getParticleType().equals(EnumParticleTypes.CRIT)) {
               Vec3 particlePos = new Vec3(packet.getXCoordinate(), packet.getYCoordinate(), packet.getZCoordinate());
               if (chest != null) {
                  double dist = this.getChest().distanceTo(particlePos);
                  if (dist < 1.0) {
                     nextRotation = particlePos;
                  }
               }
            }
         }

      }
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      done.clear();
      super.onDisable();
   }

   @EventHandler
   public void onR3D(EventRender3D event) {
      if (Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
         Iterator var2 = this.mc.theWorld.loadedTileEntityList.iterator();

         while(var2.hasNext()) {
            TileEntity entity = (TileEntity)var2.next();
            if (entity instanceof TileEntityChest) {
               TileEntityChest chouShaBi = (TileEntityChest)entity;
               RenderUtil.drawSolidBlockESP(chouShaBi.getPos(), Colors.BLUE.c, event.getPartialTicks());
            }
         }

      }
   }

   @EventHandler
   public void on3D(EventRender3D event) {
      if (Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
         if (chestPos != null) {
            RenderUtil.drawSolidBlockESP(new BlockPos(chest), Colors.ORANGE.c, event.getPartialTicks());
         }

      }
   }

   private Vec3 getChest() {
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         ArrayList chests = new ArrayList();
         if (!chests.isEmpty()) {
            chests.clear();
         }

         Iterator var2 = this.mc.theWorld.loadedTileEntityList.iterator();

         while(var2.hasNext()) {
            TileEntity entity = (TileEntity)var2.next();
            if (entity instanceof TileEntityChest) {
               TileEntityChest chest = (TileEntityChest)entity;
               Vec3 chestVec = new Vec3((double)((float)chest.getPos().getX() + 0.5F), (double)chest.getPos().getY(), (double)((float)chest.getPos().getZ() + 0.5F));
               BlockPos chestPos = chest.getPos();
               if (!done.contains(chestPos) && this.mc.thePlayer.getDistance((double)chest.getPos().getX(), (double)chest.getPos().getY(), (double)chest.getPos().getZ()) < 4.0) {
                  chests.add(chestVec);
               }
            }
         }

         chests.sort(Comparator.comparingDouble((vec) -> {
            return this.mc.thePlayer.getDistance(vec.xCoord, vec.yCoord, vec.zCoord);
         }));
         if (!chests.isEmpty()) {
            return (Vec3)chests.get(0);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private BlockPos getChestPos() {
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         BlockPos chest = null;
         if (PowderChestMacro.chest != null) {
            chest = new BlockPos(PowderChestMacro.chest);
         }

         return chest;
      } else {
         return null;
      }
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

   public Rotation vec3ToRotation(Vec3 vec) {
      double diffX = vec.xCoord - this.mc.thePlayer.posX;
      double diffY = vec.yCoord - this.mc.thePlayer.posY - (double)this.mc.thePlayer.getEyeHeight();
      double diffZ = vec.zCoord - this.mc.thePlayer.posZ;
      double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float pitch = (float)(-Math.atan2(dist, diffY));
      float yaw = (float)Math.atan2(diffZ, diffX);
      pitch = (float)wrapAngleTo180(((double)(pitch * 180.0F) / Math.PI + 90.0) * -1.0);
      yaw = (float)wrapAngleTo180((double)(yaw * 180.0F) / Math.PI - 90.0);
      return new Rotation(pitch, yaw);
   }

   private static double wrapAngleTo180(double angle) {
      return angle - Math.floor(angle / 360.0 + 0.5) * 360.0;
   }

   private static class Rotation {
      public float pitch;
      public float yaw;

      public Rotation(float pitch, float yaw) {
         this.pitch = pitch;
         this.yaw = yaw;
      }
   }
}
