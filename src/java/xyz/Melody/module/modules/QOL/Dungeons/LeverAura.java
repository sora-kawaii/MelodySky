package xyz.Melody.module.modules.QOL.Dungeons;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
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
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class LeverAura extends Module {
   public static BlockPos blockPos;
   private TimerUtil timer = new TimerUtil();
   private Option dungeon = new Option("DungeonOnly", true);
   private Option bossEntry = new Option("BossOnly", true);
   private Option clickedCheck = new Option("ClickedCheck", false);
   private Option poweredCheck = new Option("PoweredCheck", true);
   private Numbers delay = new Numbers("Delay", 50.0, 10.0, 200.0, 1.0);
   private Numbers range = new Numbers("Range", 4.0, 0.0, 4.5, 0.1);
   public static ArrayList allLevers = new ArrayList();
   public static ArrayList clicked = new ArrayList();

   public LeverAura() {
      super("LeverAura", new String[]{"la"}, ModuleType.Dungeons);
      this.addValues(new Value[]{this.dungeon, this.bossEntry, this.clickedCheck, this.poweredCheck, this.range, this.delay});
      this.setModInfo("Auto Pull Levers Around You.");
   }

   @EventHandler
   private void destoryBlock(EventTick event) {
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         if (!(Boolean)this.dungeon.getValue() || Client.inDungeons) {
            if (Client.instance.dungeonUtils.inBoss || !(Boolean)this.bossEntry.getValue()) {
               if (blockPos == null) {
                  blockPos = this.getLever();
                  this.timer.reset();
               } else {
                  if (blockPos != null && this.timer.hasReached((Double)this.delay.getValue())) {
                     clicked.add(blockPos);
                     this.mc.thePlayer.swingItem();
                     this.mc.getNetHandler().getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(blockPos, this.getClosestEnum(blockPos).getIndex(), this.mc.thePlayer.getCurrentEquippedItem(), (float)blockPos.getX(), (float)blockPos.getY(), (float)blockPos.getZ()));
                     blockPos = null;
                     this.timer.reset();
                  }

               }
            }
         }
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

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      blockPos = null;
      clicked.clear();
      allLevers.clear();
      super.onDisable();
   }

   @EventHandler
   public void onTick(EventRender3D event) {
      Iterator var2 = allLevers.iterator();

      while(var2.hasNext()) {
         BlockPos pos = (BlockPos)var2.next();
         RenderUtil.drawSolidBlockESP(pos, Colors.RED.c, event.getPartialTicks());
      }

   }

   @EventHandler
   private void onWorldLoad(EventTick event) {
      if (this.mc.thePlayer == null || this.mc.theWorld == null) {
         blockPos = null;
         clicked.clear();
         allLevers.clear();
      }

   }

   private BlockPos getLever() {
      float r = ((Double)this.range.getValue()).floatValue();
      if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         BlockPos playerPos = this.mc.thePlayer.getPosition();
         playerPos = playerPos.add(0, 1, 0);
         Vec3i vec3i = new Vec3i((double)r, (double)r, (double)r);
         ArrayList levers = new ArrayList();
         if (playerPos != null) {
            allLevers.clear();
            Iterator var5 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

            label41:
            while(true) {
               BlockPos blockPos;
               IBlockState blockState;
               do {
                  do {
                     do {
                        if (!var5.hasNext()) {
                           break label41;
                        }

                        blockPos = (BlockPos)var5.next();
                        blockState = this.mc.theWorld.getBlockState(blockPos);
                     } while(!(blockState.getBlock() instanceof BlockLever));
                  } while(clicked.contains(blockPos) && (Boolean)this.clickedCheck.getValue());

                  BlockLever lever = (BlockLever)blockState.getBlock();
               } while(Boolean.valueOf((Boolean)blockState.getValue(BlockLever.POWERED)) && (Boolean)this.poweredCheck.getValue());

               allLevers.add(blockPos);
               levers.add(new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY(), (double)blockPos.getZ() + 0.5));
            }
         }

         levers.sort(Comparator.comparingDouble((vec) -> {
            return this.mc.thePlayer.getDistance(vec.xCoord, vec.yCoord, vec.zCoord);
         }));
         return !levers.isEmpty() ? new BlockPos((Vec3)levers.get(0)) : null;
      } else {
         return null;
      }
   }
}
