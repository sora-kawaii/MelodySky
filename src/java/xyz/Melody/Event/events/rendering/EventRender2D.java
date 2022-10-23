package xyz.Melody.Event.events.rendering;

import xyz.Melody.Event.Event;

public class EventRender2D extends Event {
   private float partialTicks;

   public EventRender2D(float partialTicks) {
      this.partialTicks = partialTicks;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public void setPartialTicks(float partialTicks) {
      this.partialTicks = partialTicks;
   }
}
