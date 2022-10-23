package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.BlockChest;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.BlockPos;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class GhostBlock extends Module {
   private boolean shouldSet = false;
   public static ArrayList blockposs = new ArrayList();
   private TimerUtil timer = new TimerUtil();
   private Option pickaxe = new Option("rcPickaxe", true);

   public GhostBlock() {
      super("GhostBlock", new String[]{"gb"}, ModuleType.QOL);
      this.addValues(new Value[]{this.pickaxe});
      this.setColor((new Color(244, 255, 149)).getRGB());
      this.setModInfo("Create Ghost Block Where You are Looking.");
   }

   @EventHandler
   private void onTick(EventTick e) {
      if (this.mc.objectMouseOver != null) {
         if (this.mc.objectMouseOver.entityHit == null) {
            if (!Client.inDungeons || !(this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockChest)) {
               if (this.shouldSet) {
                  if (this.timer.hasReached(50.0)) {
                     if (this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() != Blocks.air.getDefaultState().getBlock()) {
                        BlockPos pos = this.mc.objectMouseOver.getBlockPos();
                        this.mc.theWorld.setBlockToAir(pos);
                        blockposs.add(pos);
                        this.timer.reset();
                     }

                     this.shouldSet = false;
                  }
               }
            }
         }
      }
   }

   @EventHandler
   private void tickBlock(EventTick e) {
      Iterator var2 = blockposs.iterator();

      while(var2.hasNext()) {
         BlockPos pos = (BlockPos)var2.next();
         this.mc.theWorld.setBlockToAir(pos);
      }

      if (this.mc.objectMouseOver != null) {
         if (this.mc.objectMouseOver.entityHit == null) {
            if (Mouse.isButtonDown(1)) {
               BlockPos p = this.mc.objectMouseOver.getBlockPos();
               if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemPickaxe) {
                  return;
               }

               Iterator var6 = blockposs.iterator();

               while(var6.hasNext()) {
                  BlockPos pos = (BlockPos)var6.next();
                  if (p.getX() == pos.getX() && p.getY() == pos.getY() && p.getZ() == pos.getZ()) {
                     blockposs.remove(pos);
                     break;
                  }
               }
            }

         }
      }
   }

   @EventHandler
   private void tickUpdate(EventTick e) {
      if (this.mc.objectMouseOver != null) {
         if (this.mc.objectMouseOver.entityHit == null) {
            if (this.mc.currentScreen == null) {
               if (!Client.inDungeons || !(this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockChest)) {
                  if (this.timer.hasReached(50.0)) {
                     if ((Boolean)this.pickaxe.getValue() && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemPickaxe && Mouse.isButtonDown(1)) {
                        this.mc.theWorld.setBlockToAir(this.mc.objectMouseOver.getBlockPos());
                        blockposs.add(this.mc.objectMouseOver.getBlockPos());
                        this.timer.reset();
                     }

                  }
               }
            }
         }
      }
   }

   @EventHandler
   private void onKey(EventTick event) {
      if (this.mc.currentScreen == null) {
         if (this.timer.hasReached(50.0)) {
            if (Keyboard.isKeyDown(this.getKey())) {
               this.shouldSet = true;
            }

         }
      }
   }

   public void onDisable() {
      this.setEnabled(true);
      super.onDisable();
   }
}
