package xyz.Melody.module.modules.macros;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class CropNuker extends Module {
   private BlockPos crop = null;
   private final ArrayList broken = new ArrayList();
   private Mode mode;

   public CropNuker() {
      super("CropNuker", new String[]{"gn"}, ModuleType.Macros);
      this.mode = new Mode("Mode", CropNuker.crops.values(), CropNuker.crops.Cane);
      this.addValues(new Value[]{this.mode});
      this.setModInfo("Auto Break Crops Around You.");
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      this.crop = null;
      this.broken.clear();
      super.onDisable();
   }

   @EventHandler
   public void onTick(EventTick event) {
      if (this.mc.thePlayer == null) {
         this.broken.clear();
      } else {
         if (this.broken.size() > 40) {
            this.broken.clear();
         }

         this.crop = this.closestCrop();
         if (this.crop != null) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, this.crop, EnumFacing.DOWN));
            this.mc.thePlayer.swingItem();
            this.broken.add(this.crop);
         }

      }
   }

   @EventHandler
   public void onTick(EventRender3D event) {
      if (this.crop != null) {
         RenderUtil.drawSolidBlockESP(this.crop, Colors.MAGENTA.c, event.getPartialTicks());
      }

   }

   @SubscribeEvent
   public void clear(WorldEvent.Load event) {
      Helper.sendMessage("[MacroProtection] Auto Disabled " + EnumChatFormatting.GREEN + this.getName() + EnumChatFormatting.GRAY + " due to World Change.");
      this.setEnabled(false);
   }

   private BlockPos closestCrop() {
      if (this.mc.theWorld == null) {
         return null;
      } else {
         double r = 6.0;
         BlockPos playerPos = this.mc.thePlayer.getPosition();
         playerPos = playerPos.add(0, 1, 0);
         Vec3 playerVec = this.mc.thePlayer.getPositionVector();
         Vec3i vec3i = new Vec3i(r, 2.0, r);
         Vec3i vec3iCane = new Vec3i(r, 0.0, r);
         ArrayList warts = new ArrayList();
         if (playerPos != null) {
            Iterator var10;
            BlockPos blockPos;
            IBlockState blockState;
            label255:
            switch (((Enum)this.mode.getValue()).toString().toLowerCase()) {
               case "all":
                  var10 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

                  while(true) {
                     do {
                        if (!var10.hasNext()) {
                           break label255;
                        }

                        blockPos = (BlockPos)var10.next();
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                     } while(blockState.getBlock() != Blocks.nether_wart && blockState.getBlock() != Blocks.potatoes && blockState.getBlock() != Blocks.wheat && blockState.getBlock() != Blocks.carrots && blockState.getBlock() != Blocks.pumpkin && blockState.getBlock() != Blocks.melon_block && blockState.getBlock() != Blocks.brown_mushroom && blockState.getBlock() != Blocks.red_mushroom && blockState.getBlock() != Blocks.cocoa && blockState.getBlock() != Blocks.cactus && blockState.getBlock() != Blocks.reeds);

                     if (!this.broken.contains(blockPos)) {
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
                     }
                  }
               case "cane":
                  var10 = BlockPos.getAllInBox(playerPos.add(vec3iCane), playerPos.subtract(vec3iCane)).iterator();

                  while(true) {
                     if (!var10.hasNext()) {
                        break label255;
                     }

                     blockPos = (BlockPos)var10.next();
                     blockState = this.mc.theWorld.getBlockState(blockPos);
                     if (blockState.getBlock() == Blocks.reeds && !this.broken.contains(blockPos)) {
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
                     }
                  }
               case "cactus":
                  var10 = BlockPos.getAllInBox(playerPos.add(vec3iCane), playerPos.subtract(vec3iCane)).iterator();

                  while(true) {
                     if (!var10.hasNext()) {
                        break label255;
                     }

                     blockPos = (BlockPos)var10.next();
                     blockState = this.mc.theWorld.getBlockState(blockPos);
                     if (blockState.getBlock() == Blocks.cactus && !this.broken.contains(blockPos)) {
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
                     }
                  }
               case "netherwart":
                  var10 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

                  while(true) {
                     if (!var10.hasNext()) {
                        break label255;
                     }

                     blockPos = (BlockPos)var10.next();
                     blockState = this.mc.theWorld.getBlockState(blockPos);
                     if (blockState.getBlock() == Blocks.nether_wart && !this.broken.contains(blockPos)) {
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
                     }
                  }
               case "wheat":
                  var10 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

                  while(true) {
                     if (!var10.hasNext()) {
                        break label255;
                     }

                     blockPos = (BlockPos)var10.next();
                     blockState = this.mc.theWorld.getBlockState(blockPos);
                     if (blockState.getBlock() == Blocks.wheat && !this.broken.contains(blockPos)) {
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
                     }
                  }
               case "carrot":
                  var10 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

                  while(true) {
                     if (!var10.hasNext()) {
                        break label255;
                     }

                     blockPos = (BlockPos)var10.next();
                     blockState = this.mc.theWorld.getBlockState(blockPos);
                     if (blockState.getBlock() == Blocks.carrots && !this.broken.contains(blockPos)) {
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
                     }
                  }
               case "potato":
                  var10 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

                  while(true) {
                     if (!var10.hasNext()) {
                        break label255;
                     }

                     blockPos = (BlockPos)var10.next();
                     blockState = this.mc.theWorld.getBlockState(blockPos);
                     if (blockState.getBlock() == Blocks.potatoes && !this.broken.contains(blockPos)) {
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
                     }
                  }
               case "pumpkin":
                  var10 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

                  while(true) {
                     if (!var10.hasNext()) {
                        break label255;
                     }

                     blockPos = (BlockPos)var10.next();
                     blockState = this.mc.theWorld.getBlockState(blockPos);
                     if (blockState.getBlock() == Blocks.pumpkin && !this.broken.contains(blockPos)) {
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
                     }
                  }
               case "melon":
                  var10 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

                  while(true) {
                     if (!var10.hasNext()) {
                        break label255;
                     }

                     blockPos = (BlockPos)var10.next();
                     blockState = this.mc.theWorld.getBlockState(blockPos);
                     if (blockState.getBlock() == Blocks.melon_block && !this.broken.contains(blockPos)) {
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
                     }
                  }
               case "mushroom":
                  var10 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

                  while(true) {
                     do {
                        if (!var10.hasNext()) {
                           break label255;
                        }

                        blockPos = (BlockPos)var10.next();
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                     } while(blockState.getBlock() != Blocks.brown_mushroom && blockState.getBlock() != Blocks.red_mushroom);

                     if (!this.broken.contains(blockPos)) {
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
                     }
                  }
               case "cocoa":
                  var10 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

                  while(var10.hasNext()) {
                     blockPos = (BlockPos)var10.next();
                     blockState = this.mc.theWorld.getBlockState(blockPos);
                     if (blockState.getBlock() == Blocks.cocoa && !this.broken.contains(blockPos)) {
                        warts.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
                     }
                  }
            }
         }

         double smallest = 9999.0;
         Vec3 closest = null;
         Iterator var17 = warts.iterator();

         while(var17.hasNext()) {
            Vec3 wart = (Vec3)var17.next();
            double dist = wart.distanceTo(playerVec);
            if (dist < smallest) {
               smallest = dist;
               closest = wart;
            }
         }

         if (closest != null && smallest < 5.0) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
         } else {
            return null;
         }
      }
   }

   private static enum crops {
      ALL,
      Cane,
      Cactus,
      NetherWart,
      Wheat,
      Carrot,
      Potato,
      Pumpkin,
      Melon,
      Mushroom,
      Cocoa;
   }
}
