package xyz.Melody.module.modules.QOL;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.ScoreboardUtils;
import xyz.Melody.Utils.render.FadeUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class MobTracker extends Module {
   private Option ghost = new Option("Ghost", true);
   private Option bat = new Option("Bat", true);
   private Option starMob = new Option("Starred Mob", true);
   private Option dragon = new Option("Ender Dragon", true);
   private Option arrows = new Option("Arrows", false);
   private Option necron = new Option("Wither", true);

   public MobTracker() {
      super("MobTracker", new String[]{"mt"}, ModuleType.Render);
      this.addValues(new Value[]{this.ghost, this.bat, this.starMob, this.dragon, this.necron, this.arrows});
      this.setModInfo("Entity ESP.");
   }

   public void onEnable() {
      super.onEnable();
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventHandler
   private void onRender3D(EventRender3D event) {
      Iterator var2 = this.mc.theWorld.getLoadedEntityList().iterator();

      while(true) {
         Entity entity;
         do {
            if (!var2.hasNext()) {
               return;
            }

            entity = (Entity)var2.next();
            Color c;
            Color col;
            if (entity instanceof EntityArrow && (Boolean)this.arrows.getValue()) {
               c = new Color(Colors.WHITE.c);
               col = new Color(c.getRed(), c.getGreen(), c.getBlue(), 140);
               RenderUtil.drawFilledESP(entity, col, event);
            }

            if (entity instanceof EntityCreeper && ScoreboardUtils.scoreboardContains("The Mist") && (Boolean)this.ghost.getValue()) {
               c = new Color(Colors.RED.c);
               col = new Color(c.getRed(), c.getGreen(), c.getBlue(), 190);
               RenderUtil.drawFilledESP(entity, col, event);
            }

            if (entity instanceof EntityBat && (Boolean)this.bat.getValue()) {
               c = new Color(Colors.BLUE.c);
               col = new Color(c.getRed(), c.getGreen(), c.getBlue(), 160);
               RenderUtil.drawFilledESP(entity, col, event);
            }

            if (entity instanceof EntityDragon && (Boolean)this.dragon.getValue() && !Client.inDungeons) {
               c = new Color(Colors.YELLOW.c);
               col = new Color(c.getRed(), c.getGreen(), c.getBlue(), 160);
               RenderUtil.drawFilledESP(entity, col, event);
            }

            if (entity instanceof EntityWither && (Boolean)this.necron.getValue() && !entity.isInvisible() && Client.inDungeons) {
               c = FadeUtil.PURPLE.getColor();
               col = new Color(c.getRed(), c.getGreen(), c.getBlue(), 160);
               RenderUtil.drawFilledESP(entity, col, event);
            }
         } while(!(Boolean)this.starMob.getValue());

         if (entity.hasCustomName() && entity.getCustomNameTag().contains("✯")) {
            List mobs = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().expand(0.0, 3.0, 0.0), (e) -> {
               return !(e instanceof EntityArmorStand);
            });
            if (!mobs.isEmpty()) {
               boolean isMiniBoss = entity.getName().toUpperCase().equals("SHADOW ASSASSIN") || entity.getName().toUpperCase().equals("LOST ADVENTURER") || entity.getName().toUpperCase().equals("DIAMOND GUY");
               if (entity != this.mc.thePlayer && !isMiniBoss) {
                  RenderUtil.drawFilledESP((Entity)mobs.get(0), new Color(135, 206, 250, 190), event);
               }
            }
         }

         if (entity instanceof EntityEnderman && entity.isInvisible()) {
            entity.setInvisible(false);
         }

         if (entity instanceof EntityPlayer) {
            switch (entity.getName().toUpperCase()) {
               case "SHADOW ASSASSIN":
                  entity.setInvisible(false);
                  RenderUtil.drawFilledESP(entity, new Color(Colors.RED.c), event);
                  break;
               case "LOST ADVENTURER":
                  RenderUtil.drawFilledESP(entity, new Color(Colors.GREEN.c), event);
                  break;
               case "DIAMOND GUY":
                  RenderUtil.drawFilledESP(entity, new Color(Colors.BLUE.c), event);
            }
         }
      }
   }
}
