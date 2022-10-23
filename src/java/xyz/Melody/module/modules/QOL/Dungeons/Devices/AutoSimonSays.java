package xyz.Melody.module.modules.QOL.Dungeons.Devices;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.BlockChangeEvent;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Value;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AutoSimonSays extends Module {
   private final List simonSaysQueue = new ArrayList();
   public static final BlockPos simonSaysStart = new BlockPos(110, 121, 91);
   public static boolean clickedSimonSays;
   private long lastInteractTime;
   private Numbers delay = new Numbers("Delay", 350.0, 200.0, 1000.0, 10.0);

   public AutoSimonSays() {
      super("AutoSimonSays", new String[]{"ss"}, ModuleType.Dungeons);
      this.addValues(new Value[]{this.delay});
      this.setModInfo("Auto Do Simon Says Device.");
   }

   public void onDisable() {
      this.simonSaysQueue.clear();
      clickedSimonSays = false;
      super.onDisable();
   }

   @EventHandler
   public void onTick(EventTick event) {
      if (Client.inDungeons) {
         if (this.simonSaysQueue.size() != 0 && System.currentTimeMillis() - this.lastInteractTime >= ((Double)this.delay.getValue()).longValue() && this.mc.theWorld.getBlockState(new BlockPos(110, 121, 92)).getBlock() == Blocks.stone_button) {
            Iterator var2 = (new ArrayList(this.simonSaysQueue)).iterator();

            while(var2.hasNext()) {
               BlockPos pos = (BlockPos)var2.next();
               MovingObjectPosition intercept = this.calculateInterceptLook(pos, 5.5F);
               if (intercept != null && this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem(), pos, intercept.sideHit, intercept.hitVec)) {
                  this.mc.thePlayer.swingItem();
                  this.simonSaysQueue.remove(pos);
                  this.lastInteractTime = System.currentTimeMillis();
                  break;
               }
            }
         }

      }
   }

   @EventHandler
   public void onPacket(BlockChangeEvent event) {
      if (Client.inDungeons) {
         if (event.getPosition().getX() == 111 && event.getNewBlock().getBlock() == Blocks.sea_lantern && (this.simonSaysQueue.size() == 0 || !((BlockPos)this.simonSaysQueue.get(this.simonSaysQueue.size() - 1)).equals(event.getPosition()))) {
            this.simonSaysQueue.add(new BlockPos(110, event.getPosition().getY(), event.getPosition().getZ()));
            clickedSimonSays = true;
         }

      }
   }

   @SubscribeEvent
   public void onWorldChange(WorldEvent.Load event) {
      this.simonSaysQueue.clear();
      clickedSimonSays = false;
   }

   public MovingObjectPosition calculateInterceptLook(BlockPos pos, float range) {
      AxisAlignedBB aabb = this.getBlockAABB(pos);
      Vec3 vec3 = this.getPositionEyes();
      Vec3 look = getMiddleOfAABB(aabb);
      return vec3.squareDistanceTo(look) > (double)(range * range) ? null : aabb.calculateIntercept(vec3, look);
   }

   public Vec3 getPositionEyes() {
      return new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.fastEyeHeight(), this.mc.thePlayer.posZ);
   }

   public AxisAlignedBB getBlockAABB(BlockPos pos) {
      Block block = this.mc.theWorld.getBlockState(pos).getBlock();
      block.setBlockBoundsBasedOnState(this.mc.theWorld, pos);
      return block.getSelectedBoundingBox(this.mc.theWorld, pos);
   }

   public float fastEyeHeight() {
      return this.mc.thePlayer.isSneaking() ? 1.54F : 1.62F;
   }

   public static Vec3 getMiddleOfAABB(AxisAlignedBB aabb) {
      return new Vec3((aabb.maxX + aabb.minX) / 2.0, (aabb.maxY + aabb.minY) / 2.0, (aabb.maxZ + aabb.minZ) / 2.0);
   }
}
