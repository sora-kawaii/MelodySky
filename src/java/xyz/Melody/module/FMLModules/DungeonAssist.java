package xyz.Melody.module.FMLModules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xyz.Melody.Client;
import xyz.Melody.module.modules.QOL.Dungeons.SayMimicKilled;

public class DungeonAssist {
   private List possibleMimic = new ArrayList();
   private Minecraft mc = Minecraft.getMinecraft();
   private int ticks = 0;

   @SubscribeEvent
   public void onTick(TickEvent.ClientTickEvent updateEvent) {
      if (!Client.inDungeons) {
         Client.instance.dungeonUtils.reset();
      }

   }

   @SubscribeEvent
   public void onEntityUpdate(TickEvent.ClientTickEvent updateEvent) {
      if (this.ticks < 6) {
         ++this.ticks;
      } else if (this.mc.thePlayer != null && this.mc.theWorld != null) {
         if (Client.inDungeons) {
            if (!Client.instance.dungeonUtils.foundMimic) {
               Iterator var2 = this.mc.theWorld.loadedEntityList.iterator();

               while(var2.hasNext()) {
                  Entity entity = (Entity)var2.next();
                  if (entity.hasCustomName() && entity.getCustomNameTag().contains("Mimic")) {
                     this.possibleMimic = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(0.0, 1.0, 0.0), (e) -> {
                        return !(e instanceof EntityArmorStand) && e instanceof EntityZombie && ((EntityZombie)e).isChild();
                     });
                  }
               }

               this.ticks = 0;
            }
         }
      }
   }

   @SubscribeEvent
   public void onEntityDeath(LivingDeathEvent event) {
      if (Client.inDungeons && !Client.instance.dungeonUtils.foundMimic && !this.possibleMimic.isEmpty() && event.entity == this.possibleMimic.get(0)) {
         SayMimicKilled smk = (SayMimicKilled)Client.instance.getModuleManager().getModuleByClass(SayMimicKilled.class);
         if (smk.isEnabled()) {
            if (smk.mode.getValue() == SayMimicKilled.Chats.All) {
               this.mc.thePlayer.sendChatMessage("/ac Mimic Killed.");
            }

            if (smk.mode.getValue() == SayMimicKilled.Chats.Party) {
               this.mc.thePlayer.sendChatMessage("/pc Mimic Killed.");
            }
         }

         Client.instance.dungeonUtils.foundMimic = true;
      }

   }

   @SubscribeEvent
   public void onDungeonReset(WorldEvent.Load event) {
      this.ticks = 0;
   }
}
