package xyz.Melody.module.balance;

import java.awt.Color;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import org.lwjgl.input.Mouse;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.Item.ItemUtils;
import xyz.Melody.injection.mixins.packets.S12Accessor;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AntiVelocity extends Module {
   private TimerUtil timer = new TimerUtil();
   private Numbers explosionTime = new Numbers("WaitingTime", 500.0, 0.0, 1000.0, 10.0);
   private Option sbonly = new Option("Skyblock Only", true);
   private Option dungeonLava = new Option("Dungeon Lava Check", true);
   private Option jerryChineVC = new Option("JerryChine Vertical", true);

   public AntiVelocity() {
      super("AntiKB", new String[]{"antivelocity", "antiknockback", "antikb"}, ModuleType.Balance);
      this.addValues(new Value[]{this.sbonly, this.dungeonLava, this.jerryChineVC, this.explosionTime});
      this.setColor((new Color(191, 191, 191)).getRGB());
   }

   @EventHandler
   private void onPacket(EventPacketRecieve e) {
      if (this.mc.theWorld != null && this.mc.thePlayer != null) {
         if (!(Boolean)this.sbonly.getValue() || Client.inSkyblock) {
            boolean ignExplosion = !this.timer.hasReached((Double)this.explosionTime.getValue()) || this.mc.thePlayer.isInLava() && (Client.inDungeons || !(Boolean)this.dungeonLava.getValue());
            S12PacketEntityVelocity velocity;
            if (ignExplosion) {
               if (e.getPacket() instanceof S12PacketEntityVelocity) {
                  velocity = (S12PacketEntityVelocity)e.getPacket();
                  if (velocity.getEntityID() == this.mc.thePlayer.getEntityId() && (Boolean)this.jerryChineVC.getValue() && this.holdingJC()) {
                     S12PacketEntityVelocity vp = (S12PacketEntityVelocity)e.getPacket();
                     ((S12Accessor)vp).setMotionX(0);
                     ((S12Accessor)vp).setMotionZ(0);
                  }
               }

            } else {
               if (e.getPacket() instanceof S12PacketEntityVelocity) {
                  velocity = (S12PacketEntityVelocity)e.getPacket();
                  if (velocity.getEntityID() == this.mc.thePlayer.getEntityId()) {
                     e.setCancelled(true);
                  }
               }

               if (e.getPacket() instanceof S27PacketExplosion) {
                  e.setCancelled(true);
               }

            }
         }
      }
   }

   @EventHandler
   private void onRC(EventTick event) {
      String heldItemID = this.mc.thePlayer.getHeldItem() != null ? ItemUtils.getSkyBlockID(this.mc.thePlayer.getHeldItem()) : "notHoldingItem";
      boolean holdingStaff = heldItemID.contains("BONZO_STAFF") || heldItemID.equals("JERRY_STAFF");
      if ((Double)this.explosionTime.getValue() == 0.0) {
         this.timer.reset();
      } else {
         if (holdingStaff && Mouse.isButtonDown(1)) {
            this.timer.reset();
         }

      }
   }

   private boolean holdingJC() {
      String heldItemID = this.mc.thePlayer.getHeldItem() != null ? ItemUtils.getSkyBlockID(this.mc.thePlayer.getHeldItem()) : "notHoldingItem";
      return heldItemID.equals("JERRY_STAFF");
   }
}
