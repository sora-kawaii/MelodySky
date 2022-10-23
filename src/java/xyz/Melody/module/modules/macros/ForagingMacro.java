package xyz.Melody.module.modules.macros;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.misc.EventKey;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.game.InventoryUtils;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class ForagingMacro extends Module {
   private Option ugm = new Option("UnGrabMouse", true);
   private Option useRod = new Option("Use Rod", false);
   private Numbers treeSlot = new Numbers("Sapling Slot", 0.0, 0.0, 8.0, 1.0);
   private Numbers bonemealSlot = new Numbers("Bonemeal Slot", 1.0, 0.0, 8.0, 1.0);
   private Numbers axeSlot = new Numbers("Axe Slot", 2.0, 0.0, 8.0, 1.0);
   private Numbers rodSlot = new Numbers("Rod Slot", 3.0, 0.0, 8.0, 1.0);
   private Numbers delay = new Numbers("PlaceDelay", 500.0, 0.0, 5000.0, 100.0);
   private Numbers timeBreak = new Numbers("TimeBeforeBreak", 500.0, 100.0, 1000.0, 10.0);
   private Numbers breakDelay = new Numbers("BreakDelay", 2000.0, 1000.0, 3000.0, 50.0);
   private ArrayList dirtPos = new ArrayList();
   private TimerUtil yepTimer = new TimerUtil();
   private TimerUtil failSafeTimer = new TimerUtil();
   private TimerUtil shabTimer = new TimerUtil();
   private ForagingState foragingState;
   private int currentTree = 1;
   private int treeWait;

   public ForagingMacro() {
      super("ForagingMacro", new String[]{"am"}, ModuleType.Macros);
      this.addValues(new Value[]{this.ugm, this.useRod, this.treeSlot, this.bonemealSlot, this.axeSlot, this.rodSlot, this.delay, this.timeBreak, this.breakDelay});
      this.setModInfo("Auto Place -> Grow -> Break Trees.");
   }

   public void onEnable() {
      Helper.sendMessage("[ForagingMacro] Aim a Block And Press ALT to Set Dirt Position.");
      if ((Boolean)this.ugm.getValue()) {
         Client.ungrabMouse();
      }

      this.foragingState = ForagingMacro.ForagingState.TREE;
      this.currentTree = 1;
      this.yepTimer.reset();
      this.failSafeTimer.reset();
      this.shabTimer.reset();
      this.treeWait = ((Double)this.delay.getValue()).intValue();
      this.dirtPos.clear();
      super.onEnable();
   }

   public void onDisable() {
      if ((Boolean)this.ugm.getValue()) {
         Client.regrabMouse();
      }

      super.onDisable();
   }

   @EventHandler
   private void onKey(EventKey event) {
      if (Keyboard.getKeyName(event.getKey()).toLowerCase().contains("lmenu") && this.dirtPos.size() < 4) {
         this.dirtPos.add(this.mc.objectMouseOver.getBlockPos());
      }

   }

   @EventHandler
   private void drawBlocks(EventRender3D event) {
      Iterator var2 = this.dirtPos.iterator();

      while(var2.hasNext()) {
         BlockPos pos = (BlockPos)var2.next();
         RenderUtil.drawFullBlockESP(pos, new Color(Colors.MAGENTA.c), event.getPartialTicks());
      }

   }

   @EventHandler
   private void onTick(EventTick event) {
      if (this.dirtPos.size() >= 4) {
         int saplingCount = InventoryUtils.getAmountInHotbar("Jungle Sapling");
         int boneMealCount = InventoryUtils.getAmountInHotbar("Enchanted Bone Meal");
         if (saplingCount >= 5 && boneMealCount >= 2) {
            switch (this.foragingState) {
               case TREE:
                  if (this.failSafeTimer.hasReached((Double)this.breakDelay.getValue()) && this.yepTimer.hasReached((double)this.treeWait)) {
                     this.swapSlot(((Double)this.treeSlot.getValue()).intValue());
                     BlockPos cur = (BlockPos)this.dirtPos.get(this.currentTree - 1);
                     float[] rots = this.getRotations(cur, EnumFacing.DOWN);
                     this.mc.thePlayer.rotationYaw = rots[0];
                     this.mc.thePlayer.rotationPitch = rots[1];
                     this.yepTimer.reset();
                     this.foragingState = ForagingMacro.ForagingState.LOOKING;
                  }
                  break;
               case BONEMEAL:
                  if (this.yepTimer.hasReached((double)((Double)this.delay.getValue()).intValue())) {
                     this.swapSlot(((Double)this.bonemealSlot.getValue()).intValue());
                     Client.rightClick();
                     this.yepTimer.reset();
                     this.shabTimer.reset();
                     this.foragingState = ForagingMacro.ForagingState.RODSWAP;
                  }
                  break;
               case RODSWAP:
                  if ((Boolean)this.useRod.getValue()) {
                     if (this.yepTimer.hasReached((double)((Double)this.delay.getValue()).intValue())) {
                        this.silentUse(((Double)this.axeSlot.getValue()).intValue(), ((Double)this.rodSlot.getValue()).intValue());
                        Client.rightClick();
                        this.yepTimer.reset();
                        this.failSafeTimer.reset();
                        this.shabTimer.reset();
                        this.foragingState = ForagingMacro.ForagingState.HARVEST;
                        this.swapSlot(((Double)this.axeSlot.getValue()).intValue());
                     }
                  } else {
                     this.yepTimer.reset();
                     this.failSafeTimer.reset();
                     this.shabTimer.reset();
                     this.foragingState = ForagingMacro.ForagingState.HARVEST;
                     this.swapSlot(((Double)this.axeSlot.getValue()).intValue());
                  }
                  break;
               case HARVEST:
                  if (this.failSafeTimer.hasReached((Double)this.breakDelay.getValue())) {
                     this.foragingState = ForagingMacro.ForagingState.TREE;
                     this.currentTree = 1;
                  }

                  IBlockState ibs;
                  if (this.shabTimer.hasReached((Double)this.timeBreak.getValue()) && this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK && (ibs = this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos())) != null && ibs.getBlock() == Blocks.log && this.yepTimer.hasReached((double)((Double)this.timeBreak.getValue()).intValue())) {
                     Client.leftClick();
                     this.yepTimer.reset();
                     this.foragingState = ForagingMacro.ForagingState.TREE;
                     this.currentTree = 1;
                     this.treeWait = 500;
                     this.shabTimer.reset();
                  }
               case LOOKING:
            }

         }
      }
   }

   @EventHandler
   public void onTickWorld(EventTick event) {
      if (this.foragingState == ForagingMacro.ForagingState.LOOKING && this.yepTimer.hasReached((Double)this.delay.getValue())) {
         Client.rightClick();
         this.yepTimer.reset();
         if (this.currentTree < 4) {
            ++this.currentTree;
            this.foragingState = ForagingMacro.ForagingState.TREE;
            this.treeWait = ((Double)this.delay.getValue()).intValue();
         } else {
            this.foragingState = ForagingMacro.ForagingState.BONEMEAL;
         }
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

   private void swapSlot(int slot) {
      if (slot > 0 && slot <= 8) {
         this.mc.thePlayer.inventory.currentItem = slot - 1;
      }

   }

   public void silentUse(int mainSlot, int useSlot) {
      int oldSlot = this.mc.thePlayer.inventory.currentItem;
      if (useSlot > 0 && useSlot <= 8) {
         this.mc.thePlayer.inventory.currentItem = useSlot - 1;
         this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.getHeldItem());
      }

      if (mainSlot > 0 && mainSlot <= 8) {
         this.mc.thePlayer.inventory.currentItem = mainSlot - 1;
      } else if (mainSlot == 0) {
         this.mc.thePlayer.inventory.currentItem = oldSlot;
      }

   }

   static enum ForagingState {
      TREE,
      BONEMEAL,
      RODSWAP,
      HARVEST,
      LOOKING;
   }

   static enum dir {
      NORTH,
      EAST,
      SOUTH,
      WEST;
   }
}
