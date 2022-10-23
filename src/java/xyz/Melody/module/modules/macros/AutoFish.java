package xyz.Melody.module.modules.macros;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.Melody.Client;
import xyz.Melody.Event.EventHandler;
import xyz.Melody.Event.events.rendering.EventRender2D;
import xyz.Melody.Event.events.rendering.EventRender3D;
import xyz.Melody.Event.events.world.EventPacketRecieve;
import xyz.Melody.Event.events.world.EventPreUpdate;
import xyz.Melody.Event.events.world.EventTick;
import xyz.Melody.Event.value.Numbers;
import xyz.Melody.Event.value.Option;
import xyz.Melody.Event.value.Value;
import xyz.Melody.System.Managers.FriendManager;
import xyz.Melody.Utils.Colors;
import xyz.Melody.Utils.Helper;
import xyz.Melody.Utils.PlayerListUtils;
import xyz.Melody.Utils.TimerUtil;
import xyz.Melody.Utils.math.MathUtil;
import xyz.Melody.Utils.math.RotationUtil;
import xyz.Melody.Utils.render.RenderUtil;
import xyz.Melody.module.Module;
import xyz.Melody.module.ModuleType;
import xyz.Melody.module.balance.AntiBot;

public class AutoFish extends Module {
   private int tickTimer = 0;
   private int tickTimer1 = 0;
   private int dickTimer = 0;
   private Vec3 soundVec = null;
   private Option unGrab = new Option("UnGrabMouse", false);
   private Option lockRod = new Option("LockRod", false);
   private Option admin = new Option("AntiAdmin", false);
   private Option lockView = new Option("LockView", false);
   private Option dead = new Option("DeathCheck", true);
   private Option waterCheck = new Option("Water/Lava Check", true);
   private Option escape = new Option("Escape", false);
   private Numbers escapeRange = new Numbers("Escape Range", 5.0, 0.0, 20.0, 1.0);
   private Option kill = new Option("AutoKill", false);
   private Option rckill = new Option("RightClickKill", false);
   private Numbers killRange = new Numbers("KillRange", 3.0, 0.0, 4.2, 0.1);
   private Numbers rccd = new Numbers("RcDelay(ms)", 2500.0, 100.0, 5000.0, 100.0);
   private Numbers angleDiff = new Numbers("AngleDiff", 1.0E-4, 0.0, 0.1, 1.0E-4);
   private Numbers angleSize = new Numbers("AngleSize", 60.0, 10.0, 100.0, 5.0);
   private Numbers killSize = new Numbers("ScKillSize", 1.0, 1.0, 20.0, 1.0);
   private Option autoThrow = new Option("AutoThrow", false);
   private Option showDebug = new Option("Show Debug", false);
   private Option packetDebug = new Option("PacketDebug", false);
   private Option rotation = new Option("NoRotationAFK", true);
   private Option move = new Option("NoMovingAFK", true);
   private Option holdShift = new Option("Sneaking", false);
   private Option randomDelay = new Option("RandomDelay", true);
   private Numbers angle = new Numbers("RotationAngle", 1.0, 1.0, 5.0, 1.0);
   private Numbers tickTimerVale = new Numbers("TickTimer", 80.0, 20.0, 200.0, 10.0);
   private Option soundBB = new Option("SoundBox", false);
   private Numbers soundRadius = new Numbers("SoundRadius", 0.5, 0.1, 5.0, 0.1);
   private Option squid = new Option("KillSquids", true);
   private Option guard = new Option("KillGuardians", true);
   private Option skeleton = new Option("KillSkeletons", true);
   private Option zombie = new Option("KillZombies", true);
   private Option witch = new Option("KillWitches", true);
   private Option cat = new Option("KillOcelots", true);
   private Option silverfish = new Option("KillSilverFishes", true);
   private Option golem = new Option("KillGolems", true);
   private Option rabbit = new Option("KillRabbits", true);
   private Option sheep = new Option("KillSheeps", true);
   private Option endermite = new Option("KillEnderMites", true);
   private Option blaze = new Option("KillBlazes", true);
   private Option pigman = new Option("KillPigmans", true);
   private Option horse = new Option("KillHorses", true);
   private Option player = new Option("KillOthers", true);
   private Enum currentStage;
   private boolean backRotaion;
   private boolean soundReady;
   private boolean soundCDReady;
   private boolean motionReady;
   private int extraDelay;
   private boolean delaySet;
   private List allSCNear;
   private EntityLivingBase currentSC;
   private TimerUtil attackTimer;
   private TimerUtil rightClickTimer;
   private boolean reachedSize;
   private int lcIndex;
   private boolean yawRecorded;
   private boolean pitchRecorded;
   private float lastRotationYaw;
   private float lastRotationPitch;
   private boolean yawRestored;
   private boolean pitchRestored;
   private float yawDiff;
   private float pitchDiff;
   private boolean shouldSwitchToWeapon;
   private boolean shouldSwitchToRod;
   private boolean switchedToRod;
   private Vec3 lockedVec;
   private TimerUtil reThrowTimer;
   private TimerUtil moveTimer;
   private boolean moveDone;
   private boolean moved;
   private boolean moveBack;
   private boolean needToEscape;
   private TimerUtil escapeDelay;
   private boolean escaped;

   public AutoFish() {
      super("AutoFish", new String[]{"af", "fishing", "fish"}, ModuleType.Macros);
      this.currentStage = AutoFish.stage.NONE;
      this.backRotaion = false;
      this.soundReady = false;
      this.soundCDReady = false;
      this.motionReady = false;
      this.extraDelay = 0;
      this.delaySet = false;
      this.allSCNear = new ArrayList();
      this.currentSC = null;
      this.attackTimer = new TimerUtil();
      this.rightClickTimer = new TimerUtil();
      this.reachedSize = false;
      this.lcIndex = 0;
      this.yawRecorded = false;
      this.pitchRecorded = false;
      this.lastRotationYaw = 0.0F;
      this.lastRotationPitch = 0.0F;
      this.yawRestored = true;
      this.pitchRestored = true;
      this.yawDiff = 0.0F;
      this.pitchDiff = 0.0F;
      this.shouldSwitchToWeapon = false;
      this.shouldSwitchToRod = false;
      this.switchedToRod = false;
      this.lockedVec = new Vec3(0.0, 0.0, 0.0);
      this.reThrowTimer = new TimerUtil();
      this.moveTimer = new TimerUtil();
      this.moveDone = false;
      this.moved = false;
      this.moveBack = false;
      this.needToEscape = false;
      this.escapeDelay = new TimerUtil();
      this.escaped = false;
      this.addValues(new Value[]{this.unGrab, this.lockRod, this.lockView, this.dead, this.admin, this.waterCheck, this.escape, this.escapeRange, this.autoThrow, this.showDebug, this.packetDebug, this.rotation, this.move, this.holdShift, this.randomDelay, this.angle, this.tickTimerVale, this.soundBB, this.soundRadius, this.angleDiff, this.angleSize, this.kill, this.rckill, this.rccd, this.killSize, this.killRange, this.squid, this.guard, this.skeleton, this.zombie, this.witch, this.cat, this.silverfish, this.golem, this.rabbit, this.sheep, this.endermite, this.blaze, this.pigman, this.horse, this.player});
      this.setColor((new Color(191, 191, 191)).getRGB());
      this.setModInfo("Just Auto Fish.");
   }

   public void onEnable() {
      if ((Boolean)this.unGrab.getValue()) {
         Client.ungrabMouse();
      }

      if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit == null) {
         this.lockedVec = this.mc.objectMouseOver.hitVec;
      }

      super.onEnable();
   }

   public void onDisable() {
      if ((Boolean)this.holdShift.getValue()) {
         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
      }

      if ((Boolean)this.unGrab.getValue()) {
         Client.regrabMouse();
      }

      this.needToEscape = false;
      this.lockedVec = new Vec3(0.0, 0.0, 0.0);
      this.reThrowTimer.reset();
      this.moveDone = false;
      this.escaped = false;
      this.tickTimer = 0;
      this.tickTimer1 = 0;
      this.dickTimer = 0;
      this.soundVec = null;
      this.currentStage = AutoFish.stage.NONE;
      this.backRotaion = false;
      this.soundReady = false;
      this.soundCDReady = false;
      this.motionReady = false;
      this.extraDelay = 0;
      this.delaySet = false;
      this.currentSC = null;
      this.yawRecorded = false;
      this.pitchRecorded = false;
      this.lastRotationYaw = 0.0F;
      this.lastRotationPitch = 0.0F;
      this.shouldSwitchToWeapon = false;
      this.shouldSwitchToRod = false;
      this.yawRestored = true;
      this.pitchRestored = true;
      this.moveBack = false;
      this.switchedToRod = false;
      this.reachedSize = false;
      this.attackTimer.reset();
      this.rightClickTimer.reset();
      this.escapeDelay.reset();
      this.escaped = false;
      super.onDisable();
   }

   private boolean shouldAttack() {
      return this.attackTimer.hasReached(1000.0 / (8.0 + MathUtil.randomDouble(-1.0, 1.0)));
   }

   @EventHandler
   private void onPlayerDetected(EventTick event) {
      if (this.playerInRange()) {
         this.needToEscape = true;
      }

      if ((Boolean)this.escape.getValue() && !this.escaped) {
         if (this.needToEscape && this.escapeDelay.hasReached(5000.0)) {
            Helper.sendMessage("[AutoFish] Player Detected, Warping to Private Island.");
            this.mc.thePlayer.sendChatMessage("/l");
            this.escaped = true;
            this.setEnabled(false);
            this.escapeDelay.reset();
         }

         if (!this.needToEscape) {
            this.escapeDelay.reset();
         }
      }

      if ((Boolean)this.admin.getValue() && PlayerListUtils.tabContains("[ADMIN]") && this.allSCNear.isEmpty()) {
         Helper.sendMessage("[AutoFish] Admin Detected, Warping to Private Island.");
         this.mc.thePlayer.sendChatMessage("/l");
      }

   }

   @SubscribeEvent(
      receiveCanceled = true
   )
   public void onChat(ClientChatReceivedEvent event) {
      String message = StringUtils.stripControlCodes(event.message.getUnformattedText());
      if (message.startsWith("From [ADMIN]") || message.startsWith("From [GM]") || message.startsWith("From [YOUTUBE]")) {
         Helper.sendMessage("[AutoFish] Admin Detected, Quitting Server.");
         boolean flag = this.mc.isIntegratedServerRunning();
         this.mc.theWorld.sendQuittingDisconnectingPacket();
         this.mc.loadWorld((WorldClient)null);
         if (flag) {
            this.mc.displayGuiScreen(new GuiMainMenu());
         } else {
            this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
         }
      }

   }

   @EventHandler
   private void onDead(EventTick event) {
      if ((Boolean)this.dead.getValue()) {
         if (this.mc.thePlayer.isEntityAlive() && !this.mc.thePlayer.isDead) {
            if (this.mc.thePlayer.ticksExisted <= 1) {
               Helper.sendMessage("[AutoFish] Detected mc.thePlayer.tickExisted <= 1, Disabled AutoFish.");
               this.setEnabled(false);
            } else if (this.mc.thePlayer.getHealth() == 0.0F) {
               Helper.sendMessage("[AutoFish] Detected mc.thePlayer.getHealth() == 0, Disabled AutoFish.");
               this.setEnabled(false);
            }
         } else {
            Helper.sendMessage("[AutoFish] Detected mc.thePlayer.isDead, Disabled AutoFish.");
            this.setEnabled(false);
         }
      }
   }

   @EventHandler
   private void onReThrow(EventTick event) {
      if ((Boolean)this.waterCheck.getValue()) {
         if (!this.reachedSize && this.pitchRestored && this.yawRestored) {
            if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod && this.mc.thePlayer.fishEntity != null) {
               if (!this.mc.thePlayer.fishEntity.isInWater() && !this.mc.thePlayer.fishEntity.isInLava()) {
                  if (this.reThrowTimer.hasReached(10000.0)) {
                     Client.rightClick();
                     this.currentStage = AutoFish.stage.NONE;
                     this.reThrowTimer.reset();
                  }
               } else {
                  this.reThrowTimer.reset();
               }
            }

         }
      }
   }

   @EventHandler
   private void onLockRod(EventTick event) {
      if ((Boolean)this.lockRod.getValue()) {
         if (!this.reachedSize && this.pitchRestored && this.yawRestored) {
            for(int i = 0; i < 9; ++i) {
               ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
               if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemFishingRod && this.yawRestored && this.pitchRestored) {
                  this.mc.thePlayer.inventory.currentItem = i;
                  break;
               }
            }

         }
      }
   }

   @EventHandler
   private void lockView(EventRender2D event) {
      if ((Boolean)this.lockView.getValue()) {
         if (!this.reachedSize && this.pitchRestored && this.yawRestored) {
            if (this.currentStage != AutoFish.stage.NONE) {
               Rotation r = this.vec3ToRotation(this.lockedVec);
               this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, r.yaw, 30.0F);
               this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, r.pitch, 30.0F);
            }

         }
      }
   }

   @EventHandler
   private void onKillSC(EventPreUpdate event) {
      if ((Boolean)this.kill.getValue()) {
         this.loadSCs();
         if (!this.needToEscape) {
            if (((Double)this.killSize.getValue()).intValue() > 1) {
               if (this.allSCNear.size() >= ((Double)this.killSize.getValue()).intValue()) {
                  this.reachedSize = true;
               }
            } else {
               this.reachedSize = true;
            }
         } else {
            this.reachedSize = true;
         }

         if (this.currentSC == null && !this.allSCNear.isEmpty()) {
            this.currentSC = (EntityLivingBase)this.allSCNear.get(0);
         }

         if (this.currentSC != null && this.shouldSwitchToWeapon && this.reachedSize) {
            this.mc.thePlayer.inventory.currentItem = 0;
            this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(0));
            this.rightClickTimer.reset();
            this.attackTimer.reset();
            this.shouldSwitchToWeapon = false;
         }

         float rotationSpeed = ((Double)this.angleSize.getValue()).floatValue() * 3.0F;
         float targetYaw;
         float targetPitch;
         if (this.currentSC != null && this.shouldAttack() && this.reachedSize) {
            if (this.mc.thePlayer.inventory.currentItem != 0) {
               this.shouldSwitchToWeapon = true;
            } else if (this.mc.thePlayer.inventory.currentItem == 0) {
               this.switchedToRod = false;
               if (!(Boolean)this.rckill.getValue()) {
                  this.attack(this.currentSC);
               }

               if ((Boolean)this.rckill.getValue()) {
                  if (this.rightClickTimer.hasReached((double)((Double)this.rccd.getValue()).longValue()) && this.mc.thePlayer.inventory.currentItem == 0) {
                     Client.rightClick();
                     this.rightClickTimer.reset();
                  }

                  if (!this.yawRecorded) {
                     this.lastRotationYaw = this.mc.thePlayer.rotationYaw;
                     this.yawRestored = false;
                     this.yawRecorded = true;
                  }

                  if (!this.pitchRecorded) {
                     this.lastRotationPitch = this.mc.thePlayer.rotationPitch;
                     this.pitchRestored = false;
                     this.pitchRecorded = true;
                  }

                  if (this.yawRecorded && this.pitchRecorded) {
                     targetYaw = (float)RotationUtil.getRotationToEntity(this.currentSC)[0];
                     targetPitch = (float)RotationUtil.getRotationToEntity(this.currentSC)[1];
                     this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, targetYaw, rotationSpeed);
                     this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, targetPitch, rotationSpeed);
                  }
               }
            }
         }

         targetYaw = ((Double)this.angleDiff.getValue()).floatValue() * 10.0F;
         if (this.currentSC == null && this.allSCNear.isEmpty()) {
            if (this.yawRecorded) {
               this.yawDiff = Math.abs(this.mc.thePlayer.rotationYaw - this.lastRotationYaw);
               if (Math.abs(this.mc.thePlayer.rotationYaw - this.lastRotationYaw) > 360.0F - targetYaw) {
                  this.mc.thePlayer.rotationYaw = this.lastRotationYaw;
                  this.mc.thePlayer.rotationPitch = this.lastRotationPitch;
                  this.yawRestored = true;
                  this.yawRecorded = false;
               }

               if (Math.abs(this.mc.thePlayer.rotationYaw - this.lastRotationYaw) > targetYaw) {
                  targetPitch = this.lastRotationYaw;
                  this.mc.thePlayer.rotationYaw = this.smoothRotation(this.mc.thePlayer.rotationYaw, targetPitch, rotationSpeed);
               } else {
                  this.mc.thePlayer.rotationYaw = this.lastRotationYaw;
                  this.yawRestored = true;
                  this.yawRecorded = false;
               }
            }

            if (this.pitchRecorded) {
               this.pitchDiff = Math.abs(this.mc.thePlayer.rotationPitch - this.lastRotationPitch);
               if (Math.abs(this.mc.thePlayer.rotationPitch - this.lastRotationPitch) > targetYaw) {
                  targetPitch = this.lastRotationPitch;
                  this.mc.thePlayer.rotationPitch = this.smoothRotation(this.mc.thePlayer.rotationPitch, targetPitch, rotationSpeed);
               } else {
                  this.mc.thePlayer.rotationPitch = this.lastRotationPitch;
                  this.pitchRestored = true;
                  this.pitchRecorded = false;
               }
            }

            this.reachedSize = false;
         }

         if (this.currentSC != null && (double)this.mc.thePlayer.getDistanceToEntity(this.currentSC) > (Double)this.killRange.getValue()) {
            if (!this.switchedToRod) {
               this.shouldSwitchToRod = true;
            }

            this.currentSC = null;
         }

         if (this.allSCNear.isEmpty() && !this.switchedToRod) {
            this.shouldSwitchToRod = true;
         }

         if (this.currentSC != null && !this.currentSC.isEntityAlive()) {
            if (!this.switchedToRod) {
               this.shouldSwitchToRod = true;
            }

            this.currentSC = null;
         }

         if (this.currentSC == null && this.shouldSwitchToRod && !this.reachedSize) {
            for(int i = 0; i < 9; ++i) {
               ItemStack itemStack = this.mc.thePlayer.inventory.mainInventory[i];
               if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemFishingRod && this.yawRestored && this.pitchRestored) {
                  this.dickTimer = 40;
                  this.lastRotationYaw = 0.0F;
                  this.lastRotationPitch = 0.0F;
                  this.mc.thePlayer.inventory.currentItem = i;
                  this.shouldSwitchToRod = false;
                  this.switchedToRod = true;
                  break;
               }
            }
         }

         if (this.currentSC != null && this.reachedSize) {
            this.reset();
            this.tickTimer = 0;
            this.tickTimer1 = 0;
            this.dickTimer = 0;
            this.soundVec = null;
         }

      }
   }

   @EventHandler
   private void onESPSC(EventRender3D event) {
      if ((Boolean)this.soundBB.getValue()) {
         if (this.currentSC != null && this.reachedSize) {
            RenderUtil.entityOutlineAXIS(this.currentSC, Colors.AQUA.c, event);
         }

      }
   }

   @EventHandler
   private void onDebugDraw(EventRender2D event) {
      if ((Boolean)this.showDebug.getValue()) {
         ScaledResolution scale = new ScaledResolution(this.mc);
         this.mc.fontRendererObj.drawString("Current Stage: " + this.currentStage, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 6, -1);
         this.mc.fontRendererObj.drawString("TickTimer: " + this.tickTimer, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 18, -1);
         this.mc.fontRendererObj.drawString("SoundCDTimer: " + this.tickTimer1 + (this.tickTimer1 == 50 ? " (Ready)" : ""), scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 30, -1);
         this.mc.fontRendererObj.drawString("AutoThrowTimer: " + this.dickTimer, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 42, -1);
         this.mc.fontRendererObj.drawString("SoundMonitor: " + this.soundReady, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 54, -1);
         this.mc.fontRendererObj.drawString("SoundReady: " + this.soundCDReady, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 66, -1);
         this.mc.fontRendererObj.drawString("MotionReady: " + this.motionReady, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 78, -1);
         if ((Boolean)this.randomDelay.getValue()) {
            this.mc.fontRendererObj.drawString("ExtraDelay: " + this.extraDelay, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 90, -1);
         }

         this.mc.fontRendererObj.drawString("YawReady: " + this.yawRestored, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 102, -1);
         this.mc.fontRendererObj.drawString("YawDiff: " + this.yawDiff, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 114, -1);
         this.mc.fontRendererObj.drawString("PitchReady: " + this.pitchRestored, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 126, -1);
         this.mc.fontRendererObj.drawString("PitchDiff: " + this.pitchDiff, scale.getScaledWidth() / 2 + 6, scale.getScaledHeight() / 2 + 138, -1);
      }
   }

   @EventHandler
   private void onSneak(EventPreUpdate event) {
      if ((Boolean)this.holdShift.getValue()) {
         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
      }

   }

   @EventHandler
   private void onTick(EventTick event) {
      if (this.mc.thePlayer.fishEntity != null && this.currentStage != AutoFish.stage.FINISH) {
         this.currentStage = AutoFish.stage.WAITING;
      }

      if (this.mc.thePlayer.getHeldItem() == null || !(this.mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod)) {
         this.currentStage = AutoFish.stage.NONE;
      }

      if (this.currentStage == AutoFish.stage.NONE) {
         this.reset();
         this.tickTimer = 0;
         this.tickTimer1 = 0;
         this.soundVec = null;
      }

      if (this.currentStage == AutoFish.stage.WAITING && this.mc.thePlayer.fishEntity == null) {
         this.currentStage = AutoFish.stage.NONE;
      }

      if (this.mc.thePlayer.getHeldItem() != null && (Boolean)this.autoThrow.getValue() && this.currentStage == AutoFish.stage.NONE && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemFishingRod) {
         if (this.dickTimer < 20) {
            ++this.dickTimer;
            return;
         }

         if ((Boolean)this.rotation.getValue()) {
            EntityPlayerSP var10000;
            if (this.backRotaion) {
               var10000 = this.mc.thePlayer;
               var10000.rotationYaw -= ((Double)this.angle.getValue()).floatValue();
               this.backRotaion = !this.backRotaion;
            } else {
               var10000 = this.mc.thePlayer;
               var10000.rotationYaw += ((Double)this.angle.getValue()).floatValue();
               this.backRotaion = !this.backRotaion;
            }
         }

         Client.rightClick();
      }

      this.dickTimer = 0;
   }

   @EventHandler
   public void onPacket(EventPacketRecieve e) {
      if (this.currentStage == AutoFish.stage.WAITING) {
         Packet packet = e.getPacket();
         if (packet instanceof S29PacketSoundEffect) {
            S29PacketSoundEffect sound = (S29PacketSoundEffect)packet;
            if ((Boolean)this.packetDebug.getValue()) {
               Helper.sendMessage("Current Sound: " + sound.getSoundName());
            }

            if (sound.getSoundName().contains("game.player.swim.splash") || sound.getSoundName().contains("random.splash")) {
               float radius = ((Double)this.soundRadius.getValue()).floatValue();
               if (Math.abs(sound.getX() - this.mc.thePlayer.fishEntity.posX) <= (double)radius && Math.abs(sound.getZ() - this.mc.thePlayer.fishEntity.posZ) <= (double)radius) {
                  this.soundReady = true;
                  this.soundVec = new Vec3(sound.getX(), sound.getY(), sound.getZ());
               }
            }
         }

         if (packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity velocity = (S12PacketEntityVelocity)packet;
            if (velocity.getMotionX() == 0 && velocity.getMotionY() != 0 && velocity.getMotionZ() == 0) {
               this.motionReady = true;
            }
         }

      }
   }

   @EventHandler
   private void onMove(EventTick event) {
      if ((Boolean)this.move.getValue()) {
         if (!this.moveDone) {
            if (this.currentStage == AutoFish.stage.FINISH && !this.moved) {
               this.moveTimer.reset();
               KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindLeft.getKeyCode(), true);
               this.moved = true;
            }

            if (this.moved && this.moveTimer.hasReached(50.0)) {
               KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindLeft.getKeyCode(), false);
               if (this.moveTimer.hasReached(100.0)) {
                  KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindRight.getKeyCode(), true);
                  if (this.moveTimer.hasReached(150.0)) {
                     KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindRight.getKeyCode(), false);
                     this.moveDone = true;
                  }
               }
            }
         } else if (this.currentStage != AutoFish.stage.FINISH) {
            this.moved = false;
            this.moveTimer.reset();
            this.moveDone = false;
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindLeft.getKeyCode(), false);
            KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindRight.getKeyCode(), false);
         }
      }

   }

   @EventHandler
   private void onReady(EventTick event) {
      if (this.soundCDReady && this.motionReady && this.currentStage != AutoFish.stage.FINISH) {
         this.currentStage = AutoFish.stage.FINISH;
         Client.rightClick();
         this.reset();
      }

   }

   @EventHandler
   private void onCDReady(EventTick event) {
      if (this.currentStage == AutoFish.stage.WAITING) {
         if (this.tickTimer1 < 50) {
            this.soundCDReady = false;
            this.soundReady = false;
            ++this.tickTimer1;
         } else {
            this.soundCDReady = this.soundReady;
         }
      }

   }

   @EventHandler
   private void onThrow(EventTick event) {
      if (this.currentStage == AutoFish.stage.FINISH) {
         if (!this.delaySet) {
            this.extraDelay = (Boolean)this.randomDelay.getValue() ? Math.abs((int)(Math.random() * 50.0)) : 0;
            this.delaySet = true;
         }

         if (this.tickTimer < ((Double)this.tickTimerVale.getValue()).intValue() + this.extraDelay) {
            ++this.tickTimer;
            return;
         }

         Client.rightClick();
         if ((Boolean)this.rotation.getValue()) {
            EntityPlayerSP var10000;
            if (this.backRotaion) {
               var10000 = this.mc.thePlayer;
               var10000.rotationYaw -= ((Double)this.angle.getValue()).floatValue();
               this.backRotaion = !this.backRotaion;
            } else {
               var10000 = this.mc.thePlayer;
               var10000.rotationYaw += ((Double)this.angle.getValue()).floatValue();
               this.backRotaion = !this.backRotaion;
            }
         }

         this.currentStage = AutoFish.stage.WAITING;
         this.tickTimer1 = 0;
         this.tickTimer = 0;
         this.delaySet = false;
      }

   }

   @EventHandler
   private void onR3D(EventRender3D event) {
      if ((Boolean)this.soundBB.getValue()) {
         AxisAlignedBB soundVecBB;
         if (this.mc.thePlayer.fishEntity != null) {
            soundVecBB = this.mc.thePlayer.fishEntity.getEntityBoundingBox().expand((Double)this.soundRadius.getValue(), 0.0, (Double)this.soundRadius.getValue());
            RenderUtil.drawOutlinedBoundingBox(soundVecBB, Colors.AQUA.c, event.getPartialTicks());
         }

         if (this.soundVec != null) {
            soundVecBB = new AxisAlignedBB(this.soundVec.xCoord + 0.05, this.soundVec.yCoord + 0.05, this.soundVec.zCoord + 0.05, this.soundVec.xCoord - 0.05, this.soundVec.yCoord - 0.05, this.soundVec.zCoord - 0.05);
            RenderUtil.drawOutlinedBoundingBox(soundVecBB, Colors.RED.c, event.getPartialTicks());
         }

      }
   }

   @SubscribeEvent
   public void clear(WorldEvent.Load event) {
      Helper.sendMessage("[MacroProtection] Auto Disabled " + EnumChatFormatting.GREEN + this.getName() + EnumChatFormatting.GRAY + " due to World Change.");
      this.setEnabled(false);
   }

   private void reset() {
      this.soundReady = false;
      this.soundCDReady = false;
      this.motionReady = false;
   }

   private void loadSCs() {
      this.allSCNear.clear();
      this.allSCNear = this.getTargets((Double)this.killRange.getValue());
      this.allSCNear.sort(Comparator.comparingDouble((e) -> {
         return this.mc.thePlayer.getDistanceSqToEntity(e);
      }));
   }

   public List getTargets(Double value) {
      return (List)this.mc.theWorld.loadedEntityList.stream().filter((e) -> {
         return this.isSC(e) && (double)this.mc.thePlayer.getDistanceToEntity(e) < value;
      }).collect(Collectors.toList());
   }

   private void attack(EntityLivingBase entity) {
      this.mc.thePlayer.swingItem();
      this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, Action.ATTACK));
      this.attackTimer.reset();
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

   private boolean playerInRange() {
      if (!(Boolean)this.escape.getValue()) {
         return false;
      } else {
         Iterator var1 = this.mc.theWorld.playerEntities.iterator();

         EntityPlayer player;
         do {
            if (!var1.hasNext()) {
               return false;
            }

            player = (EntityPlayer)var1.next();
         } while(((AntiBot)Client.instance.getModuleManager().getModuleByClass(AntiBot.class)).isEntityBot(player) || FriendManager.isFriend(player.getName()) || !(this.mc.thePlayer.getDistanceToEntity(player) < (float)((Double)this.escapeRange.getValue()).intValue()) || player == this.mc.thePlayer);

         return true;
      }
   }

   public Rotation vec3ToRotation(Vec3 vec) {
      double diffX = vec.xCoord - this.mc.thePlayer.posX;
      double diffY = vec.yCoord - this.mc.thePlayer.posY - (double)this.mc.thePlayer.getEyeHeight();
      double diffZ = vec.zCoord - this.mc.thePlayer.posZ;
      double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float pitch = (float)(-Math.atan2(dist, diffY));
      float yaw = (float)Math.atan2(diffZ, diffX);
      pitch = (float)wrapAngleTo180(((double)(pitch * 180.0F) / Math.PI + 90.0) * -1.0);
      yaw = (float)wrapAngleTo180((double)(yaw * 180.0F) / Math.PI - 90.0);
      return new Rotation(pitch, yaw);
   }

   private static double wrapAngleTo180(double angle) {
      return angle - Math.floor(angle / 360.0 + 0.5) * 360.0;
   }

   private boolean isSC(Entity e) {
      if (e == this.mc.thePlayer) {
         return false;
      } else if ((double)this.mc.thePlayer.getDistanceToEntity(e) > (Double)this.killRange.getValue()) {
         return false;
      } else if (!e.isDead && e.isEntityAlive()) {
         if (e.getDisplayName() != null && PlayerListUtils.tabContains(e.getName())) {
            return false;
         } else if (e instanceof EntitySquid && (Boolean)this.squid.getValue()) {
            return true;
         } else if (e instanceof EntityGuardian && (Boolean)this.guard.getValue()) {
            return true;
         } else if (e instanceof EntityZombie && (Boolean)this.zombie.getValue()) {
            return true;
         } else if (e instanceof EntitySkeleton && (Boolean)this.skeleton.getValue()) {
            return true;
         } else if (e instanceof EntityWitch && (Boolean)this.witch.getValue()) {
            return true;
         } else if (e instanceof EntityOcelot && (Boolean)this.cat.getValue()) {
            return true;
         } else if (e instanceof EntitySilverfish && (Boolean)this.silverfish.getValue()) {
            return true;
         } else if (e instanceof EntityGolem && (Boolean)this.golem.getValue()) {
            return true;
         } else if (e instanceof EntityRabbit && (Boolean)this.rabbit.getValue()) {
            return true;
         } else if (e instanceof EntitySheep && (Boolean)this.sheep.getValue()) {
            return true;
         } else if (e instanceof EntityEndermite && (Boolean)this.endermite.getValue()) {
            return true;
         } else if (e instanceof EntityBlaze && (Boolean)this.blaze.getValue()) {
            return true;
         } else if (e instanceof EntityPigZombie && (Boolean)this.pigman.getValue()) {
            return true;
         } else if (e instanceof EntityPlayer && (Boolean)this.player.getValue()) {
            return true;
         } else {
            return e instanceof EntityHorse && (Boolean)this.horse.getValue();
         }
      } else {
         return false;
      }
   }

   private static class Rotation {
      public float pitch;
      public float yaw;

      public Rotation(float pitch, float yaw) {
         this.pitch = pitch;
         this.yaw = yaw;
      }
   }

   static enum stage {
      NONE,
      WAITING,
      FINISH;
   }
}
