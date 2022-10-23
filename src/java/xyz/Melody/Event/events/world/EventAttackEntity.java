package xyz.Melody.Event.events.world;

import net.minecraft.entity.Entity;
import xyz.Melody.Event.Event;

public class EventAttackEntity extends Event {
   private Entity entity;

   public EventAttackEntity(Entity entity) {
      this.entity = entity;
   }

   public Entity getEntity() {
      return this.entity;
   }
}
