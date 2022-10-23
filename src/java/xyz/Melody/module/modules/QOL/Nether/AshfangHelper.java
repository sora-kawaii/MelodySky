package xyz.Melody.module.modules.QOL.Nether;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.StringUtils;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.world.EventPreUpdate;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.math.RotationUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class AshfangHelper extends Module {
   private TimerUtil timer = new TimerUtil();

   public AshfangHelper() {
      super("AshfangHelper", new String[]{"mbe"}, ModuleType.Nether);
      this.setColor((new Color(158, 205, 125)).getRGB());
      this.setModInfo("Auto Shoot Orbs to Ashfang.");
   }

   @EventHandler
   public void onRenderEntity(EventPreUpdate event) {
      Iterator var2 = this.mc.theWorld.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Entity ent = (Entity)var2.next();
         if (ent instanceof EntityArmorStand) {
            EntityArmorStand entity = (EntityArmorStand)ent;
            if (!entity.hasCustomName()) {
               return;
            }

            String entityName = StringUtils.stripControlCodes(entity.getCustomNameTag());
            if (entityName.equals("Blazing Soul")) {
               this.onRenderOrb(ent, event);
               return;
            }
         }
      }

   }

   public boolean onRenderOrb(Entity entityToInteract, EventPreUpdate event) {
      Entity ashfang = null;
      Iterator var4 = this.mc.theWorld.loadedEntityList.iterator();

      while(var4.hasNext()) {
         Entity entity = (Entity)var4.next();
         if (entity instanceof EntityArmorStand && entity.hasCustomName() && StringUtils.stripControlCodes(entity.getCustomNameTag()).contains("Lv200] Ashfang")) {
            ashfang = entity;
            break;
         }
      }

      if (ashfang != null) {
         float[] rotations = RotationUtil.getRotations(ashfang);
         event.setYaw(rotations[0]);
         event.setPitch(rotations[1]);
         if (this.timer.hasReached(100.0)) {
            this.mc.playerController.attackEntity(this.mc.thePlayer, entityToInteract);
            this.timer.reset();
            return true;
         }
      }

      return false;
   }
}
