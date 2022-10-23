package xyz.Melody.module.modules.QOL.Dungeons;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.FMLModules.AlertsListener;

public class Alerts extends Module {
   private TimerUtil titleTimer = new TimerUtil();
   private TimerUtil bonzoTimer = new TimerUtil();
   private TimerUtil bonzo2Timer = new TimerUtil();
   private TimerUtil spiritTimer = new TimerUtil();
   public boolean spiritMaskPoped = false;
   public boolean bonzoMaskPoped = false;
   public boolean bonzoMask2Poped = false;
   public boolean spiritMaskReady = true;
   public boolean bonzoMaskReady = true;
   public boolean bonzoMask2Ready = true;
   public boolean shouldShowSpiritMaskReady = false;
   public boolean shouldShowBonzoMaskReady = false;
   public boolean shouldShowBonzoMask2Ready = false;
   public boolean reminded = false;
   public Option watcher = new Option("WatcherReady", true);
   public Option bonzo = new Option("BonzoMask", true);
   public Option spirit = new Option("SpiritMask", true);

   public Alerts() {
      super("Alerts", ModuleType.Dungeons);
      this.addValues(new Value[]{this.watcher, this.bonzo, this.spirit});
      this.setModInfo("Create Alert Titles.");
   }

   public void onEnable() {
      Helper.sendMessage("Please set your Gui Scale to Normal to use this Module.");
      super.onEnable();
   }

   public void onDisable() {
      this.titleTimer.reset();
      this.bonzoTimer.reset();
      this.bonzo2Timer.reset();
      this.spiritTimer.reset();
      this.spiritMaskPoped = false;
      this.bonzoMaskPoped = false;
      this.bonzoMask2Poped = false;
      this.spiritMaskReady = true;
      this.bonzoMaskReady = true;
      this.bonzoMask2Ready = true;
      this.shouldShowSpiritMaskReady = false;
      this.shouldShowBonzoMaskReady = false;
      this.shouldShowBonzoMask2Ready = false;
      super.onDisable();
   }

   @EventHandler
   private void onPop2D(EventRender2D event) {
      if (AlertsListener.shouldShowWatcherReady) {
         if (!this.reminded) {
            this.titleTimer.reset();
            this.reminded = true;
         }

         Client.drawTitle("Watcher Ready", EnumChatFormatting.RED);
         if (this.titleTimer.hasReached(100.0)) {
            AlertsListener.shouldShowWatcherReady = false;
            this.reminded = false;
            this.titleTimer.reset();
         }
      }

      if (AlertsListener.shouldShowSpiritMaskPoped) {
         if (!this.spiritMaskPoped) {
            this.titleTimer.reset();
            this.spiritMaskPoped = true;
         }

         Client.drawTitle("Spirit Mask Poped!", EnumChatFormatting.RED);
         this.spiritMaskReady = false;
         this.shouldShowSpiritMaskReady = false;
         if (this.titleTimer.hasReached(100.0)) {
            AlertsListener.shouldShowSpiritMaskPoped = false;
            this.titleTimer.reset();
         }
      }

      if (AlertsListener.shouldShowBonzoMaskPoped) {
         if (!this.bonzoMaskPoped) {
            this.titleTimer.reset();
            this.bonzoMaskPoped = true;
         }

         Client.drawTitle("Bonzo Mask Poped!", EnumChatFormatting.RED);
         this.bonzoMaskReady = false;
         this.shouldShowBonzoMaskReady = false;
         if (this.titleTimer.hasReached(100.0)) {
            AlertsListener.shouldShowBonzoMaskPoped = false;
            this.titleTimer.reset();
         }
      }

      if (AlertsListener.shouldShowBonzoMask2Poped) {
         if (!this.bonzoMask2Poped) {
            this.titleTimer.reset();
            this.bonzoMask2Poped = true;
         }

         Client.drawTitle("⚚ Bonzo Mask Poped!", EnumChatFormatting.RED);
         this.bonzoMask2Ready = false;
         this.shouldShowBonzoMask2Ready = false;
         if (this.titleTimer.hasReached(100.0)) {
            AlertsListener.shouldShowBonzoMask2Poped = false;
            this.titleTimer.reset();
         }
      }

   }

   @EventHandler
   private void onTick(EventTick event) {
      if (this.spiritMaskPoped && this.spiritTimer.hasReached(30000.0)) {
         this.shouldShowSpiritMaskReady = true;
         this.spiritTimer.reset();
      }

      if (this.bonzoMaskPoped && this.bonzoTimer.hasReached(208000.0)) {
         this.shouldShowBonzoMaskReady = true;
         this.bonzoTimer.reset();
      }

      if (this.bonzoMask2Poped && this.bonzo2Timer.hasReached(208000.0)) {
         this.shouldShowBonzoMask2Ready = true;
         this.bonzo2Timer.reset();
      }

   }

   @EventHandler
   private void onReady2D(EventRender2D event) {
      if (this.shouldShowSpiritMaskReady) {
         if (!this.spiritMaskReady) {
            this.titleTimer.reset();
            this.spiritMaskReady = true;
         }

         Client.drawTitle("Spirit Mask Ready!", EnumChatFormatting.GREEN);
         this.spiritMaskPoped = false;
         if (this.titleTimer.hasReached(3000.0)) {
            Helper.sendMessage("Spirit Mask Ready!");
            this.shouldShowSpiritMaskReady = false;
            this.titleTimer.reset();
         }
      }

      if (this.shouldShowBonzoMaskReady) {
         if (!this.bonzoMaskReady) {
            this.titleTimer.reset();
            this.bonzoMaskReady = true;
         }

         Client.drawTitle("Bonzo Mask Ready!", EnumChatFormatting.GREEN);
         this.bonzoMaskPoped = false;
         if (this.titleTimer.hasReached(3000.0)) {
            Helper.sendMessage("Bonzo Mask Ready!");
            this.shouldShowBonzoMaskReady = false;
            this.titleTimer.reset();
         }
      }

      if (this.shouldShowBonzoMask2Ready) {
         if (!this.bonzoMask2Ready) {
            this.titleTimer.reset();
            this.bonzoMask2Ready = true;
         }

         Client.drawTitle("⚚ Bonzo Mask Ready!", EnumChatFormatting.GREEN);
         this.bonzoMask2Poped = false;
         if (this.titleTimer.hasReached(3000.0)) {
            Helper.sendMessage("⚚ Bonzo Mask Ready!");
            this.shouldShowBonzoMask2Ready = false;
            this.titleTimer.reset();
         }
      }

   }

   @SubscribeEvent
   public void clear(WorldEvent.Load event) {
      this.spiritMaskPoped = false;
      this.bonzoMaskPoped = false;
      this.bonzoMask2Poped = false;
      this.spiritMaskReady = true;
      this.bonzoMaskReady = true;
      this.bonzoMask2Ready = true;
      this.shouldShowSpiritMaskReady = false;
      this.shouldShowBonzoMaskReady = false;
      this.shouldShowBonzoMask2Ready = false;
      this.reminded = false;
      this.titleTimer.reset();
      this.bonzoTimer.reset();
      this.bonzo2Timer.reset();
      this.spiritTimer.reset();
   }
}
