package xyz.Melody.module.balance;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventPostUpdate;
import xyz.Melody.Event.events.world.EventPreUpdate;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Mode;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.GUI.Notification.NotificationPublisher;
import xyz.Melody.GUI.Notification.NotificationType;
import xyz.Melody.System.Managers.FriendManager;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.math.MathUtil;
import xyz.Melody.Utils.render.FadeUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;

public class Aura extends Module {
   public EntityLivingBase curTarget;
   private List targets = new ArrayList();
   private int index;
   private Mode mode;
   private Numbers cps;
   private Numbers range;
   private Numbers sinC;
   private Option rot;
   private Option esp;
   private Option players;
   private Option friend;
   private Option team;
   private Option animals;
   private Option mobs;
   private Option invis;
   private Option death;
   public boolean isBlocking;
   private Comparator angleComparator;
   private TimerUtil attackTimer;
   private TimerUtil switchTimer;
   private TimerUtil singleTimer;
   float anim;

   public Aura() {
      super("KillAura", new String[]{"ka", "aura", "killa"}, ModuleType.Balance);
      this.mode = new Mode("Mode", Aura.AuraMode.values(), Aura.AuraMode.Single);
      this.cps = new Numbers("CPS", 10.0, 1.0, 20.0, 0.5);
      this.range = new Numbers("Range", 4.5, 1.0, 6.0, 0.1);
      this.sinC = new Numbers("Single Switch", 200.0, 1.0, 1000.0, 1.0);
      this.rot = new Option("Rotation", true);
      this.esp = new Option("ESP", false);
      this.players = new Option("Players", true);
      this.friend = new Option("FriendFilter", true);
      this.team = new Option("TeamsFilter", true);
      this.animals = new Option("Animals", true);
      this.mobs = new Option("Mobs", false);
      this.invis = new Option("Invisibles", false);
      this.death = new Option("DeathCheck", true);
      this.angleComparator = Comparator.comparingDouble((e2) -> {
         return (double)e2.getDistanceToEntity(this.mc.thePlayer);
      });
      this.attackTimer = new TimerUtil();
      this.switchTimer = new TimerUtil();
      this.singleTimer = new TimerUtil();
      this.anim = 100.0F;
      this.addValues(new Value[]{this.mode, this.cps, this.range, this.sinC, this.rot, this.esp, this.players, this.friend, this.team, this.animals, this.mobs, this.invis, this.death});
   }

   public void onDisable() {
      this.curTarget = null;
      this.targets.clear();
      if (Client.instance.getModuleManager().getModuleByClass(AutoBlock.class).isEnabled() && this.hasSword() && this.mc.thePlayer.isBlocking()) {
         this.unBlock();
      }

   }

   public void onEnable() {
      this.curTarget = null;
      this.index = 0;
   }

   public static double random(double min, double max) {
      Random random = new Random();
      return min + (double)((int)(random.nextDouble() * (max - min)));
   }

   private boolean shouldAttack() {
      return this.attackTimer.hasReached(1000.0 / ((Double)this.cps.getValue() + MathUtil.randomDouble(-1.0, 1.0)));
   }

   @EventHandler
   public void onRender(EventRender3D event) {
      if (this.curTarget != null) {
         if ((Boolean)this.esp.getValue()) {
            Entity ent;
            Color col;
            if (this.mode.getValue() == Aura.AuraMode.Multi) {
               for(Iterator var2 = this.targets.iterator(); var2.hasNext(); RenderUtil.drawFilledESP(ent, col, event)) {
                  ent = (Entity)var2.next();
                  col = new Color(170, 30, 30, 30);
                  if (ent == this.curTarget) {
                     col = new Color(30, 200, 30, 50);
                  }

                  if (ent.hurtResistantTime > 0) {
                     col = new Color(255, 30, 30, 50);
                     ent.hurtResistantTime = 0;
                  }
               }
            }

            float partialTicks = event.getPartialTicks();
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            RenderUtil.startDrawing();
            GL11.glDisable(2929);
            GL11.glDepthMask(false);
            GL11.glLineWidth(4.0F);
            GL11.glBegin(3);
            double x = this.curTarget.lastTickPosX + (this.curTarget.posX - this.curTarget.lastTickPosX) * (double)partialTicks - this.mc.getRenderManager().viewerPosX;
            double y = this.curTarget.lastTickPosY + (this.curTarget.posY - this.curTarget.lastTickPosY) * (double)partialTicks - this.mc.getRenderManager().viewerPosY;
            double z = this.curTarget.lastTickPosZ + (this.curTarget.posZ - this.curTarget.lastTickPosZ) * (double)partialTicks - this.mc.getRenderManager().viewerPosZ;

            for(int i = 0; i <= 10; ++i) {
               RenderUtil.glColor(FadeUtil.fade(FadeUtil.BLUE.getColor()).getRGB());
               GL11.glVertex3d(x + 1.1 * Math.cos((double)i * 6.283185307179586 / 9.0), y, z + 1.1 * Math.sin((double)i * 6.283185307179586 / 9.0));
            }

            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(2929);
            RenderUtil.stopDrawing();
            GL11.glEnable(3553);
            GL11.glPopMatrix();
         }

      }
   }

   private boolean hasSword() {
      if (this.mc.thePlayer.getCurrentEquippedItem() != null) {
         return this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword;
      } else {
         return false;
      }
   }

   @EventHandler
   private void onTick(EventTick event) {
      if ((Boolean)this.death.getValue() && this.mc.thePlayer != null) {
         if (!this.mc.thePlayer.isEntityAlive() || this.mc.currentScreen != null && this.mc.currentScreen instanceof GuiGameOver) {
            this.setEnabled(false);
            NotificationPublisher.queue("Auto Disable", "Aura Disabled For Death.", NotificationType.INFO, 2000);
            return;
         }

         if (this.mc.thePlayer.ticksExisted <= 1) {
            this.setEnabled(false);
            NotificationPublisher.queue("Auto Disable", "Aura Disabled For Death.", NotificationType.INFO, 2000);
            return;
         }

         if (this.mc.thePlayer.getHealth() == 0.0F) {
            this.setEnabled(false);
            NotificationPublisher.queue("Auto Disable", "Aura Disabled For Death.", NotificationType.INFO, 2000);
            return;
         }
      }

   }

   private void block() {
      if (this.hasSword()) {
         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
         if (this.mc.playerController.sendUseItem(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem())) {
            this.mc.getItemRenderer().resetEquippedProgress2();
         }

         this.isBlocking = true;
      }

   }

   private void unBlock() {
      if (this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && this.isBlocking) {
         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
         this.mc.playerController.onStoppedUsingItem(this.mc.thePlayer);
         this.isBlocking = false;
      }

   }

   @EventHandler
   private void onUpdate(EventPreUpdate event) {
      this.setSuffix(this.mode.getValue());
      if (this.curTarget == null && Client.instance.getModuleManager().getModuleByClass(AutoBlock.class).isEnabled() && this.hasSword()) {
         this.unBlock();
      }

      if (this.hasSword() && this.curTarget != null && Client.instance.getModuleManager().getModuleByClass(AutoBlock.class).isEnabled() && !this.isBlocking) {
         this.block();
      }

      this.targets = this.getTargets((Double)this.range.getValue());
      this.targets.sort(this.angleComparator);
      if (this.curTarget != null && (this.curTarget.getHealth() == 0.0F || !this.curTarget.isEntityAlive() || (double)this.mc.thePlayer.getDistanceToEntity(this.curTarget) > (Double)this.range.getValue())) {
         this.curTarget = null;
         ++this.index;
      }

      if (this.targets.size() > 1 && this.mode.getValue() == Aura.AuraMode.Multi && this.mc.thePlayer.ticksExisted % 2 == 1) {
         if (this.curTarget == null) {
            this.curTarget = (EntityLivingBase)this.targets.get(0);
         }

         if (this.curTarget.hurtTime > 0 || this.switchTimer.hasReached(10.0)) {
            this.curTarget.hurtTime = 0;
            ++this.index;
            this.switchTimer.reset();
         }
      }

      if (this.mc.thePlayer.ticksExisted % 2 == 0 && this.targets.size() > 1 && this.mode.getValue() == Aura.AuraMode.Single && this.singleTimer.hasReached((double)((Double)this.sinC.getValue()).longValue())) {
         if ((double)this.curTarget.getDistanceToEntity(this.mc.thePlayer) > (Double)this.range.getValue()) {
            ++this.index;
         } else if (this.curTarget.isDead) {
            ++this.index;
         }

         this.singleTimer.reset();
      }

      if (!this.targets.isEmpty()) {
         if (this.index >= this.targets.size()) {
            this.index = 0;
         }

         this.curTarget = (EntityLivingBase)this.targets.get(this.index);
      }

   }

   private float smoothRotation(float current, float target, float maxIncrement) {
      float deltaAngle = MathHelper.wrapAngleTo180_float(target - current);
      if (deltaAngle > maxIncrement) {
         deltaAngle = maxIncrement;
      }

      if (deltaAngle < -maxIncrement) {
         deltaAngle = -maxIncrement;
      }

      return current + deltaAngle / 2.0F;
   }

   @EventHandler
   private void onUpdatePost(EventPostUpdate e) {
      if (this.curTarget != null) {
         if (this.shouldAttack()) {
            if (this.hasSword() && this.mc.thePlayer.isBlocking() && this.isValidEntity(this.curTarget)) {
               this.unBlock();
            }

            this.attack();
            this.attackTimer.reset();
         }

         if (!this.mc.thePlayer.isBlocking() && this.hasSword() && Client.instance.getModuleManager().getModuleByClass(AutoBlock.class).isEnabled()) {
            this.block();
         }
      }

   }

   private void attack() {
      this.mc.thePlayer.swingItem();
      this.mc.thePlayer.onEnchantmentCritical(this.curTarget);
      this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(this.curTarget, Action.ATTACK));
   }

   public List getTargets(Double value) {
      return this.mode.getValue() != Aura.AuraMode.Multi ? (List)this.mc.theWorld.loadedEntityList.stream().filter((e) -> {
         return (double)this.mc.thePlayer.getDistanceToEntity(e) <= value && this.isValidEntity(e);
      }).collect(Collectors.toList()) : (List)this.mc.theWorld.loadedEntityList.stream().filter((e) -> {
         return this.isValidEntity(e);
      }).collect(Collectors.toList());
   }

   private boolean isValidEntity(Entity ent) {
      if (ent == this.mc.thePlayer) {
         return false;
      } else if ((double)this.mc.thePlayer.getDistanceToEntity(ent) > (Double)this.range.getValue()) {
         return false;
      } else if (ent.isInvisible() && !(Boolean)this.invis.getValue()) {
         return false;
      } else if (!ent.isEntityAlive()) {
         return false;
      } else if (FriendManager.isFriend(ent.getName())) {
         return false;
      } else if (ent instanceof EntityPlayer && FriendManager.isFriend(ent.getDisplayName().getUnformattedText()) && (Boolean)this.friend.getValue()) {
         return false;
      } else if ((ent instanceof EntityMob || ent instanceof EntityGhast || ent instanceof EntityGolem || ent instanceof EntityDragon || ent instanceof EntitySlime) && (Boolean)this.mobs.getValue()) {
         return true;
      } else if ((ent instanceof EntitySquid || ent instanceof EntityBat || ent instanceof EntityVillager) && (Boolean)this.animals.getValue()) {
         return true;
      } else if (ent instanceof EntityAnimal && (Boolean)this.animals.getValue()) {
         return true;
      } else {
         AntiBot ab = (AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
         if (ab.isEntityBot(ent)) {
            return false;
         } else {
            return ent instanceof EntityPlayer && (Boolean)this.players.getValue() && !this.isOnSameTeam(ent);
         }
      }
   }

   public boolean isOnSameTeam(Entity entity) {
      if (!(Boolean)this.team.getValue()) {
         return false;
      } else {
         if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().startsWith("§")) {
            if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().length() <= 2 || entity.getDisplayName().getUnformattedText().length() <= 2) {
               return false;
            }

            if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().substring(0, 2).equals(entity.getDisplayName().getUnformattedText().substring(0, 2))) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isTeam(EntityPlayer e, EntityPlayer e2) {
      return e.getDisplayName().getFormattedText().contains("ยง" + this.isOnSameTeam(e)) && e2.getDisplayName().getFormattedText().contains("ยง" + this.isOnSameTeam(e));
   }

   static enum AuraMode {
      Multi,
      Single;
   }
}
