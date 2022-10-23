package xyz.Melody.module.balance;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Mouse;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPreUpdate;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AutoHead extends Module {
   private boolean eatingApple;
   private int switched = -1;
   public static boolean doingStuff;
   private final TimerUtil timer = new TimerUtil();
   private final Option eatHeads = new Option("Eatheads", true);
   private final Option eatApples = new Option("Eatapples", true);
   private final Numbers health = new Numbers("HpPercentage", 50.0, 10.0, 100.0, 1.0);
   private final Numbers delay = new Numbers("Delay", 750.0, 10.0, 2000.0, 25.0);

   public AutoHead() {
      super("AutoHead", new String[]{"AutoHead", "EH", "eathead"}, ModuleType.Balance);
      this.addValues(new Value[]{this.health, this.delay, this.eatApples, this.eatHeads});
   }

   public void onEnable() {
      doingStuff = false;
      this.eatingApple = false;
      this.switched = -1;
      this.timer.reset();
      super.onEnable();
   }

   public void onDisable() {
      doingStuff = false;
      if (this.eatingApple) {
         this.repairItemPress();
         this.repairItemSwitch();
      }

      super.onDisable();
   }

   private void repairItemPress() {
      if (this.mc.gameSettings != null) {
         KeyBinding keyBindUseItem = this.mc.gameSettings.keyBindUseItem;
         if (keyBindUseItem != null) {
            KeyBinding.setKeyBindState(keyBindUseItem.getKeyCode(), false);
         }
      }

   }

   @EventHandler
   public void onUpdate(EventPreUpdate event) {
      if (this.mc.thePlayer != null) {
         InventoryPlayer inventory = this.mc.thePlayer.inventory;
         if (inventory != null) {
            doingStuff = false;
            if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
               KeyBinding useItem = this.mc.gameSettings.keyBindUseItem;
               if (!this.timer.hasReached((Double)this.delay.getValue())) {
                  this.eatingApple = false;
                  this.repairItemPress();
                  this.repairItemSwitch();
                  return;
               }

               if (this.mc.thePlayer.capabilities.isCreativeMode || this.mc.thePlayer.isPotionActive(Potion.regeneration) || (double)this.mc.thePlayer.getHealth() >= (double)this.mc.thePlayer.getMaxHealth() * ((Double)this.health.getValue() / 100.0)) {
                  this.timer.reset();
                  if (this.eatingApple) {
                     this.eatingApple = false;
                     this.repairItemPress();
                     this.repairItemSwitch();
                  }

                  return;
               }

               for(int i = 0; i < 2; ++i) {
                  boolean doEatHeads = i != 0;
                  if (doEatHeads) {
                     if (!(Boolean)this.eatHeads.getValue()) {
                        continue;
                     }
                  } else if (!(Boolean)this.eatApples.getValue()) {
                     this.eatingApple = false;
                     this.repairItemPress();
                     this.repairItemSwitch();
                     continue;
                  }

                  int slot;
                  if (doEatHeads) {
                     slot = this.getItemFromHotbar(397);
                  } else {
                     slot = this.getItemFromHotbar(322);
                  }

                  if (slot != -1) {
                     int tempSlot = inventory.currentItem;
                     doingStuff = true;
                     if (doEatHeads) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
                        this.timer.reset();
                     } else {
                        inventory.currentItem = slot;
                        KeyBinding.setKeyBindState(useItem.getKeyCode(), true);
                        if (!this.eatingApple) {
                           this.eatingApple = true;
                           this.switched = tempSlot;
                        }
                     }
                  }
               }
            }

         }
      }
   }

   private void repairItemSwitch() {
      EntityPlayerSP p = this.mc.thePlayer;
      if (p != null) {
         InventoryPlayer inventory = p.inventory;
         if (inventory != null) {
            int switched = this.switched;
            if (switched != -1) {
               inventory.currentItem = switched;
               int switched = -1;
               this.switched = switched;
            }
         }
      }
   }

   private int getItemFromHotbar(int id) {
      for(int i = 0; i < 9; ++i) {
         if (this.mc.thePlayer.inventory.mainInventory[i] != null) {
            ItemStack is = this.mc.thePlayer.inventory.mainInventory[i];
            Item item = is.getItem();
            if (Item.getIdFromItem(item) == id) {
               return i;
            }
         }
      }

      return -1;
   }
}
