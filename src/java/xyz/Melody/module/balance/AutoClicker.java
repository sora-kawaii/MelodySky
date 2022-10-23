package xyz.Melody.module.balance;

import java.awt.Color;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import org.lwjgl.input.Mouse;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AutoClicker extends Module {
   private TimerUtil timer = new TimerUtil();
   private Option left = new Option("LMB", true);
   private Option right = new Option("RMB", false);
   private Numbers lcps = new Numbers("LCps", 14.0, 1.0, 20.0, 0.1);
   private Numbers rcps = new Numbers("RCps", 14.0, 1.0, 20.0, 0.1);

   public AutoClicker() {
      super("AutoClicker", new String[]{"ac"}, ModuleType.Balance);
      this.addValues(new Value[]{this.left, this.right, this.lcps, this.rcps});
      this.setColor((new Color(244, 255, 149)).getRGB());
   }

   public void onEnable() {
      this.timer.reset();
   }

   @EventHandler
   private void onLMB(EventTick e) {
      if (this.mc.objectMouseOver != null) {
         if (this.mc.objectMouseOver.entityHit != null || this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() == Blocks.air.getDefaultState().getBlock() || this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() == Blocks.flowing_water.getDefaultState().getBlock() || this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() == Blocks.water.getDefaultState().getBlock() || this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() == Blocks.flowing_lava.getDefaultState().getBlock() || this.mc.theWorld.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock() == Blocks.lava.getDefaultState().getBlock()) {
            float ldelay = 1000.0F / ((Double)this.lcps.getValue()).floatValue();
            if (Mouse.isButtonDown(0) && this.timer.delay(ldelay) && this.mc.currentScreen == null && (Boolean)this.left.getValue()) {
               this.mc.thePlayer.swingItem();
               if (this.mc.objectMouseOver.entityHit != null) {
                  this.mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(this.mc.objectMouseOver.entityHit, Action.ATTACK));
               }

               this.timer.reset();
            }

         }
      }
   }

   @EventHandler
   private void onRMB(EventTick event) {
      float rdelay = 1000.0F / ((Double)this.rcps.getValue()).floatValue();
      if (Mouse.isButtonDown(1) && this.timer.delay(rdelay) && this.mc.currentScreen == null && (Boolean)this.right.getValue()) {
         Client.rightClick();
         this.timer.reset();
      }

   }

   public void onDisable() {
      this.timer.reset();
   }
}
