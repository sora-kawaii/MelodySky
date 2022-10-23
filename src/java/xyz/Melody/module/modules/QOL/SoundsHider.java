package xyz.Melody.module.modules.QOL;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class SoundsHider extends Module {
   private Option emanHurt = new Option("Enderman Hurt", false);
   private Option emanDie = new Option("Enderman Die", true);
   private Option emanAnger = new Option("Enderman Anger", true);
   private Option abcd = new Option("Ability Cooldown", true);
   private Option jerryChine = new Option("Jerry-ChineGun", false);
   private Option explosions = new Option("Explosions", false);

   public SoundsHider() {
      super("SoundsHider", new String[]{"nbt"}, ModuleType.QOL);
      this.addValues(new Value[]{this.emanHurt, this.emanAnger, this.emanDie, this.abcd, this.jerryChine, this.explosions});
      this.setModInfo("Anti NBT Updates on Drill/Gauntlets.");
   }

   @EventHandler
   private void onPacketRCV(EventPacketRecieve e) {
      Packet packet = e.getPacket();
      if (packet instanceof S29PacketSoundEffect) {
         S29PacketSoundEffect sound = (S29PacketSoundEffect)packet;
         if ((Boolean)this.emanHurt.getValue() && sound.getSoundName().contains("mob.endermen.hit")) {
            e.setCancelled(true);
         }

         if ((Boolean)this.emanDie.getValue() && sound.getSoundName().contains("mob.endermen.death")) {
            e.setCancelled(true);
         }

         if ((Boolean)this.emanAnger.getValue() && (sound.getSoundName().contains("mob.endermen.scream") || sound.getSoundName().contains("mob.endermen.stare"))) {
            e.setCancelled(true);
         }

         if ((Boolean)this.explosions.getValue() && sound.getSoundName().contains("random.explode")) {
            e.setCancelled(true);
         }

         if ((Boolean)this.jerryChine.getValue()) {
            if (sound.getSoundName().contains("mob.villager.yes") && sound.getVolume() == 0.35F) {
               e.setCancelled(true);
            }

            if (sound.getSoundName().contains("mob.villager.haggle") && sound.getVolume() == 0.5F) {
               e.setCancelled(true);
            }
         }

         if ((Boolean)this.abcd.getValue() && sound.getSoundName().contains("mob.endermen.portal") && sound.getPitch() == 0.0F && sound.getVolume() == 8.0F) {
            e.setCancelled(true);
         }
      }

   }
}
