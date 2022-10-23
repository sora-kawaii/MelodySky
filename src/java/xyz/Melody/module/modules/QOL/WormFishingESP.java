package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import xyz.Melody.Client;
import xyz.Melody.SkyblockArea;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.math.RotationUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class WormFishingESP extends Module {
   private Numbers range = new Numbers("Range", 300.0, 100.0, 1000.0, 10.0);
   private Option debug = new Option("Debug", false);
   private ArrayList blockPoss = new ArrayList();
   private Thread thread;
   private boolean shouldBreak = false;
   private boolean loading = false;
   private int ticks = 0;

   public WormFishingESP() {
      super("WormFishingESP", new String[]{"wfe"}, ModuleType.QOL);
      this.setEnabled(false);
      this.addValues(new Value[]{this.range, this.debug});
      this.setModInfo("Worm Fishing Lava ESP.");
   }

   public void onDisable() {
      this.loading = false;
      this.shouldBreak = true;
      this.blockPoss.clear();
      this.thread = null;
      super.onDisable();
   }

   @EventHandler
   private void onUpdate(EventTick event) {
      if (this.ticks < 11) {
         ++this.ticks;
      } else if (Client.instance.sbArea.getCurrentArea() != SkyblockArea.Areas.Crystal_Hollows) {
         this.blockPoss.clear();
      } else {
         if (this.mc.theWorld != null && this.mc.thePlayer != null && !this.loading) {
            this.shouldBreak = false;
            this.thread = new Thread(() -> {
               this.loading = true;
               this.updateBlocks();
            }, "MelodySky-LavaFishingESP");
            this.thread.start();
         }

         if (this.thread == null || this.thread != null && !this.thread.isAlive()) {
            this.loading = false;
         }

         this.ticks = 0;
      }
   }

   @EventHandler
   private void on3D(EventRender3D event) {
      if (Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
         int i;
         BlockPos pos;
         for(i = 0; i < this.blockPoss.size(); ++i) {
            pos = (BlockPos)this.blockPoss.get(i);
            if (this.mc.thePlayer.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) > (double)(((Double)this.range.getValue()).intValue() + 10)) {
               this.blockPoss.remove(pos);
               break;
            }
         }

         for(i = 0; i < this.blockPoss.size(); ++i) {
            pos = (BlockPos)this.blockPoss.get(i);
            Color orange = new Color(Colors.ORANGE.c);
            int c1 = (new Color(orange.getRed(), orange.getGreen(), orange.getBlue(), 200)).getRGB();
            if (!(this.mc.thePlayer.getDistance((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) < 5.0) && !(RotationUtil.isInFov(this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) > 360.0)) {
               RenderUtil.drawSolidBlockESP(pos, c1, event.getPartialTicks());
            }
         }

      }
   }

   private synchronized void updateBlocks() {
      int r = ((Double)this.range.getValue()).intValue();
      if (Client.instance.sbArea.getCurrentArea() == SkyblockArea.Areas.Crystal_Hollows) {
         if (this.mc.thePlayer != null && this.mc.theWorld != null) {
            BlockPos playerPos = this.mc.thePlayer.getPosition();
            playerPos = playerPos.add(0, 1, 0);
            Vec3i vec3i = new Vec3i(r, r, r);
            if (playerPos != null) {
               Iterator var4 = BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i)).iterator();

               while(var4.hasNext()) {
                  BlockPos blockPos = (BlockPos)var4.next();
                  IBlockState blockState = this.mc.theWorld.getBlockState(blockPos);
                  if (!Client.instance.getModuleManager().getModuleByClass(WormFishingESP.class).isEnabled() || this.shouldBreak) {
                     this.blockPoss.clear();
                     return;
                  }

                  if (blockPos.getY() > 64 && blockState.getBlock() == Blocks.lava && !this.blockPoss.contains(blockPos)) {
                     if ((Boolean)this.debug.getValue()) {
                        Helper.sendMessage(blockPos);
                     }

                     this.blockPoss.add(blockPos);
                  }
               }
            }

         }
      }
   }
}
