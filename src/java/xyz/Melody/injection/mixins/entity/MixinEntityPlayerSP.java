package xyz.Melody.injection.mixins.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.Melody.Event.EventBus;
import xyz.Melody.Event.events.misc.EventChat;
import xyz.Melody.Event.events.world.EventPostUpdate;
import xyz.Melody.Event.events.world.EventPreUpdate;
import xyz.Melody.injection.mixins.gui.MixinAbstractClientPlayer;

@SideOnly(Side.CLIENT)
@Mixin({EntityPlayerSP.class})
public abstract class MixinEntityPlayerSP extends MixinAbstractClientPlayer {
   private double cachedX;
   private double cachedY;
   private double cachedZ;
   private float cachedRotationPitch;
   private float cachedRotationYaw;
   @Shadow
   private boolean serverSneakState;
   @Shadow
   public boolean serverSprintState;
   @Shadow
   public int sprintingTicksLeft;
   @Shadow
   protected int sprintToggleTimer;
   @Shadow
   public float timeInPortal;
   @Shadow
   public float prevTimeInPortal;
   @Shadow
   protected Minecraft mc;
   @Shadow
   public MovementInput movementInput;
   @Shadow
   public float horseJumpPower;
   @Shadow
   public int horseJumpPowerCounter;
   @Shadow
   @Final
   public NetHandlerPlayClient sendQueue;
   @Shadow
   private double lastReportedPosX;
   @Shadow
   private int positionUpdateTicks;
   @Shadow
   private double lastReportedPosY;
   @Shadow
   private double lastReportedPosZ;
   @Shadow
   private float lastReportedYaw;
   @Shadow
   private float lastReportedPitch;

   @Shadow
   public abstract void playSound(String var1, float var2, float var3);

   @Shadow
   public abstract void setSprinting(boolean var1);

   @Shadow
   protected abstract boolean pushOutOfBlocks(double var1, double var3, double var5);

   @Shadow
   public abstract void sendPlayerAbilities();

   @Shadow
   protected abstract void sendHorseJump();

   @Shadow
   public abstract boolean isRidingHorse();

   @Shadow
   public abstract boolean isSneaking();

   @Shadow
   protected abstract boolean isCurrentViewEntity();

   @Overwrite
   public void sendChatMessage(String message) {
      EventChat event = new EventChat(message);
      EventBus.getInstance().call(event);
      if (!event.isCancelled()) {
         this.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
      }
   }

   @Inject(
      method = "onUpdateWalkingPlayer",
      at = {@At("HEAD")}
   )
   private void onUpdateWalkingPlayerPre(CallbackInfo ci) {
      EventPreUpdate event = new EventPreUpdate(this.rotationYaw, this.rotationPitch, this.posX, this.posY, this.posZ, this.onGround);
      EventBus.getInstance().call(event);
      this.cachedX = this.posX;
      this.cachedY = this.posY;
      this.cachedZ = this.posZ;
      this.cachedRotationYaw = this.rotationYaw;
      this.cachedRotationPitch = this.rotationPitch;
      this.posX = event.getX();
      this.posY = event.getY();
      this.posZ = event.getZ();
      this.rotationYaw = event.getYaw();
      this.rotationPitch = event.getPitch();
   }

   @Inject(
      method = "onUpdateWalkingPlayer",
      at = {@At("RETURN")}
   )
   private void onUpdateWalkingPlayerPost(CallbackInfo ci) {
      this.posX = this.cachedX;
      this.posY = this.cachedY;
      this.posZ = this.cachedZ;
      this.rotationYaw = this.cachedRotationYaw;
      this.rotationPitch = this.cachedRotationPitch;
      EventBus.getInstance().call(new EventPostUpdate(this.rotationYaw, this.rotationPitch));
   }
}
