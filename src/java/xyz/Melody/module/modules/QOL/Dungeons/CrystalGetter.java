package xyz.Melody.module.modules.QOL.Dungeons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityEnderCrystal;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPreUpdate;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class CrystalGetter extends Module {
   private TimerUtil timer = new TimerUtil();
   private EntityEnderCrystal crystal;
   private ArrayList crystalList = new ArrayList();
   private Numbers range = new Numbers("Range", 6.0, 1.0, 6.0, 1.0);

   public CrystalGetter() {
      super("CrystalGetter", new String[]{"cr"}, ModuleType.Dungeons);
      this.addValues(new Value[]{this.range});
      this.setModInfo("Auto Get Crystal in Range in F7/M7.");
   }

   @EventHandler
   public void onTick(EventTick event) {
      this.crystal = this.getClosestCrystal();
   }

   @EventHandler
   public void onRightClick(EventPreUpdate event) {
      if (this.crystal != null) {
         if (!((double)this.mc.thePlayer.getDistanceToEntity(this.crystal) > (Double)this.range.getValue())) {
            List armorStand;
            if (this.crystal != null && this.timer.hasReached(200.0) && !(armorStand = this.mc.theWorld.getEntitiesInAABBexcluding(this.crystal, this.crystal.getEntityBoundingBox(), (entity) -> {
               return entity instanceof EntityArmorStand && entity.getCustomNameTag().contains("CLICK HERE");
            })).isEmpty() && armorStand.get(0) != null) {
               this.mc.playerController.interactWithEntitySendPacket(this.mc.thePlayer, (Entity)armorStand.get(0));
               Helper.sendMessage("Interact With Entity ID: " + ((EntityArmorStand)armorStand.get(0)).getEntityId());
               this.timer.reset();
            }

         }
      }
   }

   private EntityEnderCrystal getClosestCrystal() {
      this.crystalList.clear();
      Iterator var1 = this.mc.theWorld.loadedEntityList.iterator();

      while(var1.hasNext()) {
         Entity ent = (Entity)var1.next();
         if (ent instanceof EntityEnderCrystal) {
            this.crystalList.add((EntityEnderCrystal)ent);
         }
      }

      this.crystalList.sort((o1, o2) -> {
         return (int)(o1.getDistanceToEntity(this.mc.thePlayer) - o2.getDistanceToEntity(this.mc.thePlayer));
      });
      if (this.crystalList.size() > 0) {
         return (EntityEnderCrystal)this.crystalList.get(0);
      } else {
         return null;
      }
   }
}
