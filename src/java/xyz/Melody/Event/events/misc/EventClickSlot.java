package xyz.Melody.Event.events.misc;

import net.minecraft.entity.player.EntityPlayer;
import xyz.Melody.Event.Event;

public class EventClickSlot extends Event {
   private int windowID;
   private int slotNumber;
   private int button;
   private int mode;
   private EntityPlayer player;

   public EventClickSlot(int windowID, int slotNumber, int button, int mode, EntityPlayer player) {
      this.windowID = windowID;
      this.slotNumber = slotNumber;
      this.button = button;
      this.mode = mode;
      this.player = player;
   }

   public int getWindowID() {
      return this.windowID;
   }

   public int getSlotNumber() {
      return this.slotNumber;
   }

   public int getButton() {
      return this.button;
   }

   public int getMode() {
      return this.mode;
   }

   public EntityPlayer getPlayer() {
      return this.player;
   }
}
