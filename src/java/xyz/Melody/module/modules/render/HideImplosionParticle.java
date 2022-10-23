package xyz.Melody.module.modules.render;

import java.awt.Color;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.EnumParticleTypes;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class HideImplosionParticle extends Module {
   public HideImplosionParticle() {
      super("ImplosionParticle", new String[]{"implosionparticle"}, ModuleType.Render);
      this.setModInfo("Hide Implosion(Wither Impact) Particles.");
      this.setColor((new Color(244, 255, 149)).getRGB());
   }

   @EventHandler
   private void onPacketRCV(EventPacketRecieve event) {
      if (event.getPacket() instanceof S2APacketParticles) {
         S2APacketParticles s2a = (S2APacketParticles)event.getPacket();
         boolean dist = s2a.isLongDistance();
         float speed = s2a.getParticleSpeed();
         int count = s2a.getParticleCount();
         float x = s2a.getXOffset();
         float y = s2a.getYOffset();
         float z = s2a.getZOffset();
         EnumParticleTypes type = s2a.getParticleType();
         if (type == EnumParticleTypes.EXPLOSION_LARGE && dist && speed == 8.0F && count == 8 && x == 0.0F && y == 0.0F && z == 0.0F) {
            event.setCancelled(true);
         }
      }

   }
}
